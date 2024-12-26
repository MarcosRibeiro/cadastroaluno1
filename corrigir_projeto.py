import os
import re
import json
import shutil
import subprocess
from pathlib import Path

################################################################################
# CONFIGURAÇÕES GERAIS
################################################################################

# Ajuste este caminho para a pasta raiz do seu projeto
BASE_DIR = r"C:\cadastroaluno"

ERROR_LOG_FILE = os.path.join(BASE_DIR, "error.txt")

# Esse bloco será injetado no pom.xml para corrigir jaxb-api e commons-text
DEPENDENCY_MANAGEMENT_BLOCK = r"""
<dependencyManagement>
  <dependencies>
    <!-- Forçando jaxb-api 2.3.1 para resolver conflito com liquibase/jackson -->
    <dependency>
      <groupId>javax.xml.bind</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>2.3.1</version>
      <scope>compile</scope>
    </dependency>
    <!-- Forçando commons-text 1.12.0 -->
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-text</artifactId>
      <version>1.12.0</version>
    </dependency>
  </dependencies>
</dependencyManagement>
"""

################################################################################
# FUNÇÕES DE LOG E EXECUÇÃO DE COMANDOS
################################################################################

def log(msg):
  print(f"[INFO] {msg}")


def run_cmd(cmd, cwd=None, capture_to_error_log=False):
  """
  Executa um comando no shell.
  Se capture_to_error_log=True, redireciona stdout+stderr para ERROR_LOG_FILE (modo append).
  """
  if cwd is None:
    cwd = BASE_DIR

  log(f"==> Executando: {cmd}")
  mode = 'a' if capture_to_error_log else 'w+'

  # Se for capturar, abre o error.txt em modo append
  if capture_to_error_log:
    with open(ERROR_LOG_FILE, mode, encoding='utf-8') as ef:
      proc = subprocess.Popen(cmd, shell=True, cwd=cwd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
      out, err = proc.communicate()
      if out:
        out_decoded = out.decode("utf-8", errors="replace")
        ef.write(out_decoded)
        print(out_decoded)
      if err:
        err_decoded = err.decode("utf-8", errors="replace")
        ef.write(err_decoded)
        print(err_decoded)
      return proc.returncode
  else:
    # não redireciona para o arquivo
    proc = subprocess.Popen(cmd, shell=True, cwd=cwd)
    proc.wait()
    return proc.returncode


################################################################################
# 1) CORRIGIR POM.XML (DEPENDENCYMANAGEMENT)
################################################################################

def corrigir_pom_xml():
  """
  Adiciona (ou injeta) dependencyManagement no pom.xml, se não existir,
  forçando jaxb-api:2.3.1 e commons-text:1.12.0
  """
  pom_path = os.path.join(BASE_DIR, "pom.xml")
  if not os.path.isfile(pom_path):
    log("pom.xml não encontrado. Pulando correção de Maven.")
    return

  with open(pom_path, "r", encoding="utf-8") as fr:
    content = fr.read()

  if "<dependencyManagement>" in content:
    log("dependencyManagement já existe no pom.xml. Ajuste manual se houver conflitos.")
  else:
    # Insere o bloco antes de </project>, usando regex.
    match = re.search(r"(</project>)", content)
    if match:
      index_insert = match.start()
      new_content = (
        content[:index_insert]
        + DEPENDENCY_MANAGEMENT_BLOCK
        + "\n"
        + content[index_insert:]
      )
      with open(pom_path, "w", encoding="utf-8") as fw:
        fw.write(new_content)
      log("dependencyManagement adicionado ao pom.xml com jaxb-api e commons-text.")
    else:
      log("Não foi possível localizar </project> no pom.xml. Ajuste manualmente, se necessário.")


################################################################################
# 2) CORREÇÕES NOS ARQUIVOS TYPESCRIPT
################################################################################

def corrigir_import_dayjs_ts(content):
  """
  Substitui `import { dayjs } from 'dayjs/esm'` por `import dayjs from 'dayjs'`
  e casos similares.
  Também se encontrar `import { customParseFormat } from 'dayjs/esm/plugin/customParseFormat'`
  troca por `import customParseFormat from 'dayjs/plugin/customParseFormat'`, etc.
  """
  # 1) Substituir import { dayjs } from 'dayjs/esm';
  pattern_dayjs = r"import\s*\{\s*dayjs\s*\}\s*from\s*['\"]dayjs/esm['\"];?"
  content = re.sub(pattern_dayjs, "import dayjs from 'dayjs';", content)

  # 2) Substituir import { customParseFormat } from 'dayjs/esm/plugin/customParseFormat';
  pattern_parseformat = r"import\s*\{\s*customParseFormat\s*\}\s*from\s*['\"]dayjs/esm/plugin/customParseFormat['\"];?"
  content = re.sub(pattern_parseformat, "import customParseFormat from 'dayjs/plugin/customParseFormat';", content)

  # 3) Substituir import { duration } from 'dayjs/esm/plugin/duration';
  pattern_duration = r"import\s*\{\s*duration\s*\}\s*from\s*['\"]dayjs/esm/plugin/duration['\"];?"
  content = re.sub(pattern_duration, "import duration from 'dayjs/plugin/duration';", content)

  # 4) Substituir import { relativeTime } from 'dayjs/esm/plugin/relativeTime';
  pattern_reltime = r"import\s*\{\s*relativeTime\s*\}\s*from\s*['\"]dayjs/esm/plugin/relativeTime['\"];?"
  content = re.sub(pattern_reltime, "import relativeTime from 'dayjs/plugin/relativeTime';", content)

  # Pode haver outros plugins, se necessário
  return content


def remover_standalone_declaracao(content):
  """
  Remove 'standalone: true' do decorator @Component(...).
  Pois em geral o projeto está usando NgModule e não standalone.
  """
  pattern = r"(standalone\s*:\s*true\s*,?)"
  new_content = re.sub(pattern, "", content)
  return new_content


def converter_import_default_para_named(content):
  """
  Converte 'import X from "..."' em 'import { X } from "..."' se encontrarmos
  que não existe 'export default X' no arquivo importado (heurística simplificada).
  Aqui é simples: forçamos a trocar todo 'import X from "..."' -> 'import { X } from "..."'.
  Ajuste se quiser algo mais inteligente.
  """
  # Regex para capturar: import Something from './blah';
  pattern = r"import\s+(\w+)\s+from\s+(['\"].+?['\"])\s*;"
  def replacer(m):
    symbol = m.group(1)
    path_ = m.group(2)
    # Ex: import { MySymbol } from './my-file';
    return f"import {{ {symbol} }} from {path_};"

  new_content = re.sub(pattern, replacer, content)
  return new_content


def ensure_commonmodule_in_imports(ts_content):
  """
  Se for @Component com "imports: [...]", verifica se "CommonModule" está presente.
  Se não estiver, adiciona. Parecido com:
    @Component({
      ...
      imports: [RouterModule, FormsModule],
      ...
    })
  E também adicionar BrowserAnimationsModule, ReactiveFormsModule, etc. se achar <mat- ou formGroup.
  """
  # 1) Detectar se tem *ngIf, *ngFor, etc. Se sim, garante "CommonModule"
  needs_common = False
  if re.search(r"\*ngIf\s*=", ts_content) or re.search(r"\*ngFor\s*=", ts_content):
    needs_common = True

  # 2) Detectar se tem <mat-  => Angular Material
  needs_material = False
  if re.search(r"<mat-", ts_content) or re.search(r"matSuffix", ts_content):
    needs_material = True

  # 3) Detectar se tem [formGroup], formControlName, etc => ReactiveFormsModule
  needs_forms = False
  if re.search(r"\[formGroup\]", ts_content) or re.search(r"formControlName\s*=", ts_content):
    needs_forms = True

  # Regex para achar `@Component({ ... imports: [...] })`
  # Vamos usar uma regex parcial e “meio frágil”: (\@Component\s*\(\{\s*(?:.|\n)*?imports\s*:\s*\[)(.*?)\](.*?\))
  # Mas isso é arriscado. Ajuste se necessário.
  pattern_comp = r"(@Component\s*\(\{\s*(?:[^}]|\n)*?\bimports\s*:\s*\[)([^\]]*)(\]\s*,?\s*(?:[^}]|\n)*?\))"
  m = re.search(pattern_comp, ts_content, flags=re.DOTALL)
  if not m:
    # Se não encontrar, não faz nada
    return ts_content

  # Sub-bloco: localiza a lista
  before = m.group(1)  # até a [
  inside = m.group(2)  # conteúdo dentro de [ ... ]
  after = m.group(3)   # até fechar )
  # Exemplo: inside = "RouterModule, FormsModule"

  # Precisamos transformar em uma lista, remover espaços etc.
  imports_list = [x.strip() for x in inside.split(",") if x.strip()]

  # Se needs_common e "CommonModule" não está na imports_list => add
  if needs_common and "CommonModule" not in imports_list:
    imports_list.append("CommonModule")

  # Se needs_forms e "FormsModule" não está na imports_list => add
  # e "ReactiveFormsModule" também
  if needs_forms:
    if "FormsModule" not in imports_list:
      imports_list.append("FormsModule")
    if "ReactiveFormsModule" not in imports_list:
      imports_list.append("ReactiveFormsModule")

  # Se needs_material => também adiciona "BrowserAnimationsModule"
  # (poderia ser splitted em outro lugar, mas é um exemplo)
  if needs_material and "BrowserAnimationsModule" not in imports_list:
    imports_list.append("BrowserAnimationsModule")

  # Reconstrói a string
  new_inside = ", ".join(imports_list)
  new_content = before + new_inside + after
  ts_content = (
    ts_content[: m.start()]
    + new_content
    + ts_content[m.end() :]
  )

  # Agora precisamos garantir que exista import { CommonModule } from '@angular/common';
  # e etc.
  lines = ts_content.split("\n")
  has_common_import = any("from '@angular/common'" in ln for ln in lines)
  if needs_common and not has_common_import:
    lines.insert(0, "import { CommonModule } from '@angular/common';")

  # BrowserAnimationsModule de '@angular/platform-browser/animations'
  if needs_material:
    has_browser_anim = any("BrowserAnimationsModule" in ln for ln in lines)
    if not has_browser_anim:
      lines.insert(0, "import { BrowserAnimationsModule } from '@angular/platform-browser/animations';")

  # FormsModule e ReactiveFormsModule
  if needs_forms:
    if not any("FormsModule" in ln for ln in lines):
      lines.insert(0, "import { FormsModule } from '@angular/forms';")
    if not any("ReactiveFormsModule" in ln for ln in lines):
      lines.insert(0, "import { ReactiveFormsModule } from '@angular/forms';")

  ts_content = "\n".join(lines)
  return ts_content


def processar_arquivos_ts():
  """
  Percorre todo src/main/webapp/app e faz:
    - Ajustes dayjs (troca 'dayjs/esm' por 'dayjs', etc.)
    - remove 'standalone: true'
    - converte import default -> named (heurístico)
    - Se encontrar @Component(... imports: []), injeta CommonModule, etc. se usar *ngIf, <mat- etc.
  """
  app_dir = os.path.join(BASE_DIR, "src", "main", "webapp", "app")
  if not os.path.isdir(app_dir):
    log(f"Diretório não encontrado: {app_dir}")
    return

  for root, dirs, files in os.walk(app_dir):
    for file in files:
      if file.endswith(".ts"):
        fpath = os.path.join(root, file)
        with open(fpath, "r", encoding="utf-8") as fr:
          content = fr.read()

        original = content
        # 1) Corrige import dayjs
        content = corrigir_import_dayjs_ts(content)
        # 2) Remove standalone
        content = remover_standalone_declaracao(content)
        # 3) Converte import default -> named
        content = converter_import_default_para_named(content)
        # 4) Garante CommonModule etc. se for @Component com imports: [...]
        content = ensure_commonmodule_in_imports(content)

        if content != original:
          with open(fpath, "w", encoding="utf-8") as fw:
            fw.write(content)
          log(f"Arquivo TS ajustado: {fpath}")


################################################################################
# 3) REMOVER REGRA DO ESLINT/PRETTIER QUE PEDE "DELETE ?"
################################################################################

def remover_regra_delete_interrogacao():
  """
  Tenta remover a regra do ESLint/Prettier que exige a remoção de '?' (ex: "Delete `?`").
  Abre .eslintrc*, .prettierrc* e faz substituições.
  """
  possible_files = [
    ".eslintrc",
    ".eslintrc.js",
    ".eslintrc.json",
    ".prettierrc",
    ".prettierrc.js",
    ".prettierrc.json",
  ]
  for fname in possible_files:
    full_path = os.path.join(BASE_DIR, fname)
    if os.path.isfile(full_path):
      with open(full_path, "r", encoding="utf-8") as fr:
        content = fr.read()
      original = content
      # Remove literal "Delete `?`" se existir
      content = content.replace("Delete `?`", "")
      # Apagar algo como "noQuestionMark": true
      content = re.sub(r'"noQuestionMark"\s*:\s*true\s*,?', "", content)

      if content != original:
        with open(full_path, "w", encoding="utf-8") as fw:
          fw.write(content)
        log(f"Removida regra 'Delete `?`' em {fname}")


################################################################################
# 4) EXECUTAR BUILD E GRAVAR ERROS EM error.txt
################################################################################

def limpar_arquivo_erro():
  # Limpa error.txt antes de cada tentativa
  with open(ERROR_LOG_FILE, "w", encoding="utf-8") as ef:
    ef.write("")


def rodar_builds():
  """
  Roda 'npm run webapp:build' e './mvnw' e redireciona saída/erros para error.txt
  """
  # 1) npm run webapp:build
  run_cmd("npm run webapp:build", capture_to_error_log=True)
  # 2) ./mvnw
  # Se Windows, .\mvnw / se Linux, ./mvnw
  # Vou assumir Windows.
  mvnw_cmd = ".\\mvnw"
  if os.name != 'nt':
    mvnw_cmd = "./mvnw"

  run_cmd(mvnw_cmd, capture_to_error_log=True)


################################################################################
# FUNÇÃO PRINCIPAL
################################################################################

def main():
  log("=== Iniciando super_script de correções ===")

  # 0) Limpa o arquivo de erros
  limpar_arquivo_erro()

  # 1) Corrigir pom.xml
  corrigir_pom_xml()

  # 2) Ajustar arquivos TS
  processar_arquivos_ts()

  # 3) Remover regras do ESLint/Prettier que obrigam "Delete `?`"
  remover_regra_delete_interrogacao()

  # 4) Rodar builds e redirecionar erros para error.txt
  log("=== Rodando builds (npm run webapp:build e mvnw) => error.txt ===")
  rodar_builds()

  log("=== Script finalizado! Verifique o arquivo error.txt para logs de erros. ===")


if __name__ == "__main__":
  main()
