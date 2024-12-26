import os
import re

BASE_DIR = "C:\\\\cadastroaluno"  # Duas barras para evitar escapes (\c)

def corrigir_pom_xml():
    """
    Insere (se necessário) o dependencyManagement que força jaxb-api:2.3.1 e commons-text:1.12.0
    no pom.xml.
    """
    pom_path = os.path.join(BASE_DIR, "pom.xml")
    if not os.path.isfile(pom_path):
        print(f"[AVISO] pom.xml não encontrado em {pom_path}. Ignorando correções de pom.xml.")
        return

    try:
        with open(pom_path, "r+", encoding="utf-8") as f:
            original = f.read()

            injection = r"""
    <dependencyManagement>
      <dependencies>
        <dependency>
          <groupId>javax.xml.bind</groupId>
          <artifactId>jaxb-api</artifactId>
          <version>2.3.1</version>
          <scope>compile</scope>
        </dependency>
        <dependency>
          <groupId>org.apache.commons</groupId>
          <artifactId>commons-text</artifactId>
          <version>1.12.0</version>
        </dependency>
      </dependencies>
    </dependencyManagement>
    """

            if ("<dependencyManagement>" not in original
                    or "javax.xml.bind" not in original
                    or "org.apache.commons" not in original):
                print("[INFO] Inserindo dependencyManagement para jaxb-api 2.3.1 e commons-text 1.12.0.")
                new_content = re.sub(r"(</project>)", injection + "\n\\1", original, 1, flags=re.IGNORECASE)
                if new_content == original:
                    print("[AVISO] Não foi possível inserir dependencyManagement automaticamente no pom.xml.")
                    return
                f.seek(0)  # Retorna ao início do arquivo
                f.write(new_content)
                f.truncate()  # Remove o conteúdo restante, caso new_content seja menor que original
                print(f"[OK] Adicionado dependencyManagement no pom.xml")
            else:
                print("[INFO] dependencyManagement possivelmente já está configurado no pom.xml.")
    except Exception as e:
        print(f"[ERRO] Erro ao processar pom.xml: {e}")

def remover_decorator_entries(lines):
    """
    Remove de um bloco as linhas contendo 'standalone:' ou 'imports:'.
    Também remove vírgulas finais se a próxima linha for '})' ou '},'.
    """
    filtered = [line for line in lines if not re.search(
        r"\b(standalone|imports)\s*:", line)]
    result = []
    for i, line in enumerate(filtered):
        if i < len(filtered) - 1:
            next_line = filtered[i + 1].strip()
            if line.strip().endswith(",") and next_line.startswith(("})", "},")):
                result.append(re.sub(r",\s*$", "", line))
            else:
                result.append(line)
        else:
            result.append(line)
    return result

def process_ts_file(filepath):
    """
    Lê o arquivo, faz correções e sobrescreve.
    """
    try:
        with open(filepath, "r+", encoding="utf-8") as f:
            original = f.read()
            content = original

            # 1) export default class -> export class
            content = re.sub(r"\bexport\s+default\s+class\b", "export class", content)

            # 2) styleUrls absolutos -> relativos
            content = re.sub(r"styleUrls\s*:\s*\[\s*'[^']*([-\w]+\.component\.css)[^']*'\s*\]",
                             r"styleUrls: ['./\1']", content)

            # 3) withInterceptorsFromDi -> withInterceptorsFromDi()
            content = content.replace("withInterceptorsFromDi", "withInterceptorsFromDi()")

            # 4) create(cadastroAluno) -> create({ ...cadastroAluno, id: null })
            content = re.sub(r"(\.create\s*\(\s*)cadastroAluno(\s*\))",
                             r"\1{ ...cadastroAluno, id: null }\2", content)

            # 5) remover 'standalone: true' e 'imports: []' do decorator
            lines = content.split("\n")
            new_lines = []
            in_decorator = False
            brace_level = 0
            decorator_lines = []

            for line in lines:
                if not in_decorator and re.search(r"@(Component|Directive|Pipe)\s*\(", line):
                    in_decorator = True
                    brace_level = 0
                    decorator_lines = [line]
                elif in_decorator:
                    decorator_lines.append(line)
                    brace_level += line.count("{") - line.count("}")
                    if brace_level <= 0 and ")" in line:
                        in_decorator = False
                        new_lines.extend(remover_decorator_entries(decorator_lines))
                        decorator_lines = []
                else:
                    new_lines.append(line)

            content = "\n".join(new_lines)

            # 6) Correções de erros do Prettier e Typescript
            content = fix_prettier_and_typescript_errors(content)

            if content != original:
                f.seek(0)  # Retorna ao início do arquivo
                f.write(content)
                f.truncate()  # Remove o conteúdo restante
                print(f"[OK] Modificado: {filepath}")

    except Exception as e:
        print(f"[ERRO] Erro ao processar arquivo {filepath}: {e}")

def fix_prettier_and_typescript_errors(content):
    """
    Corrige erros específicos do Prettier e TypeScript encontrados nos logs.
    """
    # Remover espaços extras antes de '?'
    content = re.sub(r"\s+\?", "?", content)

    # Remover '?' desnecessários (TS18048, TS7015)
    content = re.sub(r"TS18048: '(\w+)' is possibly 'undefined'", r"'\1' is possibly 'undefined'", content)
    content = re.sub(r"TS7015: Element implicitly has an 'any' type because index expression is not of type 'number'",
                     "Element implicitly has an 'any' type because index expression is not of type 'number'", content)
    content = re.sub(r"\[`\$\{(.+?)\}`\]", r"[\1]", content)  # Substitui [`${predicate},${order}`] por [predicate, order]
    content = re.sub(r"sortParam\s*\?\.\s*length", "sortParam?.length ? sortParam : undefined", content) # Adiciona condicional para sortParam?.length

    # Adicionar ';' faltantes (Parsing error: ';' expected)
    content = re.sub(r"([;|\n])(\s*})", r"\1;\2", content)

    # Adicionar ',' faltantes (Parsing error: ',' expected)
    content = re.sub(r"(\{\s*[\w\s.:]+\s*[\w\s.:]+)\s*(\})", r"\1,\2", content)
    content = re.sub(r"(,)\s*(\))", r"\2", content)
    content = re.sub(r"(\()\s*(,)", r"\1", content)
    content = re.sub(r"(\()(\s*cadastroAluno\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*authorities\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*firstName\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*lastName\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*email\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*login\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*activated\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*langKey\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*createdBy\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*createdDate\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*lastModifiedBy\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*lastModifiedDate\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*id\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*nome\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*cpf\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*matricula\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*dataNascimento\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*turno\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*curso\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*valorMensalidade\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*dataVencimento\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*comportamento\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*observacoes\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*cep\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*logradouro\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*bairro\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*localidade\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*uf\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*numero\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*complemento\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*tipoLogradouro\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*tipoResidencia\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*situacaoMoradia\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*possuiVeiculo\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*moramCom\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*responsavel\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*nome\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*dataNascimento\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*cpf\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*parentesco\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*profissao\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*telefone\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*email\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*cep\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*logradouro\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*bairro\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*localidade\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*uf\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*numero\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*complemento\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*tipoLogradouro\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*inicio\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*fim\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*km\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*valorVale\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*cadastroAluno\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*name\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*pageSize\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*page\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*predicate\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*ascending\s*)(\))", r"\1{\2}\3", content)
    content = re.sub(r"(\()(\s*fields\s*)(\))", r"\1{\2}\3", content)

    # Remover '?' de 'import' (TS2613)
    content = re.sub(r"TS2613: Module '(.+?)' has no default export\.", r"Module '\1' has no default export.", content)
    content = re.sub(r"import\s+(\w+)\s+from\s+'\./app/app\.component';", r"import { \1 } from './app/app.component';", content)

    return content

def corrigir_arquivos_ts():
    """
    Percorre recursivamente .ts em BASE_DIR, chamando process_ts_file.
    """
    for root, _, files in os.walk(BASE_DIR):
        for file in files:
            if file.endswith(".ts"):
                filepath = os.path.join(root, file)
                process_ts_file(filepath)

def main():
    print("[INFO] Script de correção iniciado.")

    # 1) Corrigir pom.xml
    corrigir_pom_xml()

    # 2) Corrigir .ts
    corrigir_arquivos_ts()

    print("\n[FINAL] Concluído!")
    print("Agora:")
    print("  1) npm run clean-www && npm run webapp:build")
    print("  2) .\\mvnw")

if __name__ == "__main__":
    main()