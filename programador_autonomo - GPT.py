#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
programador_autonomo.py

Este arquivo conterá a implementação passo-a-passo do Programador Autônomo,
capaz de monitorar, analisar, corrigir e aprender em um projeto de software
de forma automatizada e iterativa.
"""

import os
import sys
import json
import time
import shutil
import subprocess
import datetime
import traceback
from typing import List, Dict, Any, Tuple, Optional

# ------------------------------------------------------------------------------
#                           CONFIGURAÇÕES GERAIS
# ------------------------------------------------------------------------------

# Chaves de API para IAs (se necessário)
GOOGLE_GEMINI_API_KEY = ""  # coloque aqui se tiver
OPENAI_API_KEY = ""         # coloque aqui se tiver

# Nome de arquivos/pastas para logs, backups, etc.
ERRORS_FILE = "erros.txt"
LOCAL_HISTORY_FILE = "historico_local.json"
SOLUCOES_APLICADAS_LOG = "solucoes_aplicadas.log"
BACKUP_FOLDER = "backup_autonomo"

# Timeout padrão para comandos em segundos
DEFAULT_TIMEOUT = 300

# Palavras-chave que indicam erro na saída de um comando
ERROR_KEYWORDS = [
  "ERROR",
  "FAILED",
  "BUILD FAILURE",
  "Exception",
  "Traceback (most recent call last)",
  "Process exited with an error",
  "MojoFailureException",
  "Exit value: 1",
]

# Exemplo de comandos Node e Maven (ajuste conforme seu projeto)
NODE_INSTALL_CMD = "npm install"
NODE_LINT_CMD = "npm run lint"
NODE_BUILD_CMD = "npm run webapp:build"
NODE_FIX_CMD = "npx eslint . --fix"
MAVEN_BUILD_CMD = ".\\mvnw verify"

# Lista de módulos Python que podem ser necessários
REQUIRED_PYTHON_MODULES = [
  "openai",
  "requests",
  "google-generativeai",
]


# ------------------------------------------------------------------------------
#                      MÓDULO DE GERENCIAMENTO DE DEPENDÊNCIAS
# ------------------------------------------------------------------------------
class DependencyManager:
  """
  Gerencia a instalação/verificação de dependências Python e Node.
  Pode rodar 'npm install', checar módulos Python, etc.
  """

  def __init__(self):
    self.python_deps_ok = False
    self.node_deps_ok = False

  def check_and_install_python_deps(self):
    """
    Verifica se módulos Python (listados em REQUIRED_PYTHON_MODULES)
    estão instalados; se não, instala via pip.
    """
    for module in REQUIRED_PYTHON_MODULES:
      if not self._is_module_installed(module):
        print(f"[DependencyManager] Instalando {module} via pip...")
        self._install_module(module)
      else:
        print(f"[DependencyManager] Módulo {module} já instalado.")
    self.python_deps_ok = True

  def _is_module_installed(self, module_name: str) -> bool:
    try:
      __import__(module_name)
      return True
    except ImportError:
      return False

  def _install_module(self, module_name: str):
    cmd = f"python -m pip install {module_name}"
    proc = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    if proc.returncode != 0:
      print(f"[DependencyManager] Erro ao instalar {module_name}: {proc.stderr}")
    else:
      print(f"[DependencyManager] {module_name} instalado com sucesso!")

  def check_and_install_node_deps(self):
    """
    Roda 'npm install' (definido em NODE_INSTALL_CMD) se ainda não foi rodado.
    """
    if not self.node_deps_ok:
      print("[DependencyManager] Rodando npm install...")
      proc = subprocess.run(NODE_INSTALL_CMD, shell=True, capture_output=True, text=True)
      if proc.returncode != 0:
        print(f"[DependencyManager] Falha ao rodar npm install: {proc.stderr}")
      else:
        print("[DependencyManager] npm install concluído.")
      self.node_deps_ok = True

  def ensure_maven_ready(self):
    """
    Verifica se o Maven wrapper (.mvnw) existe.
    Apenas avisa se não existir, pois pode ser opcional
    em alguns fluxos de projeto.
    """
    # Em sistemas Windows, mvnw.cmd ou mvnw.bat
    # Em Unix-like, arquivo 'mvnw' executável
    if (
      os.path.exists("mvnw.cmd")
      or os.path.exists("mvnw.bat")
      or os.path.exists("mvnw")
    ):
      print("[DependencyManager] Maven wrapper encontrado. OK.")
    else:
      print("[DependencyManager] AVISO: Maven wrapper (.mvnw) não encontrado.")


# ------------------------------------------------------------------------------
#                         MÓDULO DE HISTÓRICO LOCAL
# ------------------------------------------------------------------------------

class LocalHistoryManager:
  """
  Armazena histórico local de erros e soluções aplicadas.
  Facilita a reutilização de soluções para erros recorrentes.
  """

  def __init__(self, filename=LOCAL_HISTORY_FILE):
    self.filename = filename
    self.data = {"erros_solucoes": []}
    self._load()

  def _load(self):
    """Carrega o histórico a partir do arquivo JSON, ou inicializa se não existir."""
    if not os.path.exists(self.filename):
      self._save()
    else:
      try:
        with open(self.filename, "r", encoding="utf-8") as f:
          self.data = json.load(f)
        print("[LocalHistoryManager] Histórico local carregado.")
      except (json.JSONDecodeError, FileNotFoundError) as e:
        print(f"[LocalHistoryManager] Erro ao carregar histórico: {e}. Iniciando novo.")
        self._save()

  def _save(self):
    """Salva o histórico atual no arquivo JSON."""
    with open(self.filename, "w", encoding="utf-8") as f:
      json.dump(self.data, f, ensure_ascii=False, indent=2)
    print("[LocalHistoryManager] Histórico local salvo.")

  def get_solution_for_error(self, error_str: str) -> Optional[str]:
    """
    Retorna a solução correspondente para um erro, se existir no histórico.

    :param error_str: String contendo a mensagem de erro.
    :return: Solução como string ou None se não encontrado.
    """
    for item in self.data.get("erros_solucoes", []):
      if item["erro_chave"].lower() in error_str.lower():
        print(f"[LocalHistoryManager] Solução encontrada para o erro: {item['erro_chave']}")
        return item["solucao"]
    print("[LocalHistoryManager] Nenhuma solução prévia encontrada para este erro.")
    return None

  def store_solution(self, error_str: str, solution: str):
    """
    Armazena um novo par erro-solução no histórico.

    :param error_str: String contendo a mensagem de erro.
    :param solution: String contendo a solução aplicada.
    """
    if not error_str or not solution:
      print("[LocalHistoryManager] Erro ou solução vazia. Não armazenando.")
      return
    # Verifica se já existe uma solução para este erro
    for item in self.data["erros_solucoes"]:
      if item["erro_chave"].lower() in error_str.lower():
        print("[LocalHistoryManager] Solução para este erro já está armazenada.")
        return
    # Adiciona a nova solução
    self.data["erros_solucoes"].append({
      "erro_chave": error_str[:200],  # Limita a chave para 200 caracteres
      "solucao": solution
    })
    self._save()
    print("[LocalHistoryManager] Nova solução armazenada no histórico.")


    # ------------------------------------------------------------------------------
#                             MÓDULO DE REPARO
# ------------------------------------------------------------------------------

class Reparo:
  """
  Interpreta soluções fornecidas pela IA e aplica correções ao projeto.
  """

  def __init__(self, process_monitor: 'ProcessMonitor', project_path: str = "."):
    """
    Inicializa o Reparo com o ProcessMonitor e o caminho do projeto.

    :param process_monitor: Instância do ProcessMonitor para executar comandos.
    :param project_path: Caminho raiz do projeto a ser reparado.
    """
    self.pm = process_monitor
    self.project_path = project_path
    self.backup_created = False

  def apply_solution(self, solution: str) -> bool:
    """
    Aplica a solução fornecida pela IA.

    :param solution: String contendo a solução proposta pela IA.
    :return: True se a solução foi aplicada com sucesso, False caso contrário.
    """
    print("[Reparo] Aplicando solução...")
    try:
      # Etapa 1: Criar backup antes de aplicar qualquer alteração
      self._create_backup()

      # Etapa 2: Interpretar e aplicar a solução
      # Neste exemplo, assumimos que a solução está no formato:
      # "Adicionar import { CommonModule } from '@angular/common'; no arquivo X.ts"
      # Você pode melhorar essa interpretação conforme suas necessidades.
      actions = self._parse_solution(solution)
      for action in actions:
        action_type = action.get("type")
        if action_type == "modify_file":
          self._modify_file(action)
        elif action_type == "execute_command":
          self._execute_command(action)
        else:
          print(f"[Reparo] Tipo de ação desconhecido: {action_type}")

      print("[Reparo] Solução aplicada com sucesso.")
      return True
    except Exception as e:
      print(f"[Reparo] Erro ao aplicar solução: {e}")
      self._rollback()
      return False

  def _create_backup(self):
    """
    Cria um backup do projeto antes de aplicar alterações.
    """
    if not self.backup_created:
      backup_path = os.path.join(self.project_path, BACKUP_FOLDER)
      if os.path.exists(backup_path):
        shutil.rmtree(backup_path)
      shutil.copytree(self.project_path, backup_path, dirs_exist_ok=True, ignore=shutil.ignore_patterns(BACKUP_FOLDER))
      self.backup_created = True
      print(f"[Reparo] Backup criado em {backup_path}.")

  def _rollback(self):
    """
    Reverte as alterações utilizando o backup criado.
    """
    print("[Reparo] Revertendo alterações usando o backup...")
    backup_path = os.path.join(self.project_path, BACKUP_FOLDER)
    if os.path.exists(backup_path):
      for root, dirs, files in os.walk(backup_path):
        for file in files:
          backup_file = os.path.join(root, file)
          relative_path = os.path.relpath(backup_file, backup_path)
          target_file = os.path.join(self.project_path, relative_path)
          target_dir = os.path.dirname(target_file)
          if not os.path.exists(target_dir):
            os.makedirs(target_dir)
          shutil.copy2(backup_file, target_file)
      print("[Reparo] Alterações revertidas com sucesso.")
    else:
      print("[Reparo] Backup não encontrado. Não é possível reverter alterações.")

  def _parse_solution(self, solution: str) -> List[Dict[str, Any]]:
    """
    Interpreta a solução fornecida pela IA e converte em ações estruturadas.

    :param solution: String contendo a solução proposta pela IA.
    :return: Lista de dicionários representando ações.
    """
    actions = []
    # Exemplo de parsing básico. Deve ser aprimorado conforme o formato das soluções.
    # Este exemplo identifica comandos de adicionar import e executar comandos shell.

    linhas = solution.split('\n')
    for linha in linhas:
      linha = linha.strip()
      if linha.startswith("Adicionar import"):
        # Exemplo: "Adicionar import { CommonModule } from '@angular/common'; no arquivo X.ts"
        import_part, arquivo_part = linha.split(" no arquivo ")
        import_statement = import_part.replace("Adicionar ", "").strip()
        arquivo = arquivo_part.strip()
        actions.append({
          "type": "modify_file",
          "file": arquivo,
          "operation": "add_import",
          "content": import_statement
        })
      elif linha.startswith("Execute"):
        # Exemplo: "Execute npm install <pacote>"
        command = linha.replace("Execute ", "").strip()
        actions.append({
          "type": "execute_command",
          "command": command
        })
      # Adicione mais parsing conforme necessário
    return actions

  def _modify_file(self, action: Dict[str, Any]):
    """
    Modifica um arquivo com base na ação fornecida.

    :param action: Dicionário contendo detalhes da modificação.
    """
    file_path = os.path.join(self.project_path, action["file"])
    operation = action["operation"]
    content = action["content"]

    print(f"[Reparo] Modificando arquivo {file_path} com operação {operation}.")

    if operation == "add_import":
      # Adiciona a linha de import no início do arquivo
      with open(file_path, "r+", encoding="utf-8") as f:
        lines = f.readlines()
        # Encontrar a primeira linha que começa com "import" ou "@"
        insert_index = 0
        for i, line in enumerate(lines):
          if line.startswith("import") or line.startswith("@"):
            insert_index = i
            break
        else:
          insert_index = len(lines)
        lines.insert(insert_index, f"{content}\n")
        f.seek(0)
        f.writelines(lines)
      print(f"[Reparo] Importação adicionada em {file_path}.")
    else:
      print(f"[Reparo] Operação de modificação desconhecida: {operation}")

  def _execute_command(self, action: Dict[str, Any]):
    """
    Executa um comando shell com base na ação fornecida.

    :param action: Dicionário contendo detalhes do comando.
    """
    command = action["command"]
    print(f"[Reparo] Executando comando: {command}")
    stdout, stderr, exit_code = self.pm.run_command(command)
    if exit_code != 0:
      raise Exception(f"Comando '{command}' falhou com exit code {exit_code}: {stderr}")
    else:
      print(f"[Reparo] Comando '{command}' executado com sucesso.")

# ------------------------------------------------------------------------------
#                             MÓDULO DE ORQUESTRAÇÃO
# ------------------------------------------------------------------------------

class BuildOrchestrator:
  """
  Orquestra a execução de lint, build, testes e coordena o reparo de erros.
  """

  def __init__(self,
               process_monitor: 'ProcessMonitor',
               local_history: 'LocalHistoryManager',
               ia_integrator: 'IAIntegrator',
               reparo: 'Reparo'):
    """
    Inicializa o BuildOrchestrator com as instâncias necessárias dos módulos.

    :param process_monitor: Instância do ProcessMonitor para executar comandos.
    :param local_history: Instância do LocalHistoryManager para gerenciar soluções locais.
    :param ia_integrator: Instância do IAIntegrator para consultar a IA.
    :param reparo: Instância do Reparo para aplicar soluções.
    """
    self.pm = process_monitor
    self.history = local_history
    self.ia = ia_integrator
    self.reparo = reparo

  def run_lint(self) -> bool:
    """
    Executa o comando de lint e retorna True se passar sem erros.

    :return: Boolean indicando sucesso ou falha.
    """
    print("[BuildOrchestrator] Executando lint...")
    stdout, stderr, exit_code = self.pm.run_command(NODE_LINT_CMD)
    if exit_code != 0:
      print("[BuildOrchestrator] Lint encontrou erros.")
      self._log_errors(stdout, stderr)
      return False
    print("[BuildOrchestrator] Lint passou sem erros.")
    return True

  def run_build(self) -> bool:
    """
    Executa o comando de build e retorna True se passar sem erros.

    :return: Boolean indicando sucesso ou falha.
    """
    print("[BuildOrchestrator] Executando build...")
    stdout, stderr, exit_code = self.pm.run_command(NODE_BUILD_CMD)
    if exit_code != 0:
      print("[BuildOrchestrator] Build encontrou erros.")
      self._log_errors(stdout, stderr)
      return False
    print("[BuildOrchestrator] Build passou sem erros.")
    return True

  def run_maven(self) -> bool:
    """
    Executa o comando Maven e retorna True se passar sem erros.

    :return: Boolean indicando sucesso ou falha.
    """
    print("[BuildOrchestrator] Executando Maven...")
    stdout, stderr, exit_code = self.pm.run_command(MAVEN_BUILD_CMD)
    if exit_code != 0:
      print("[BuildOrchestrator] Maven encontrou erros.")
      self._log_errors(stdout, stderr)
      return False
    print("[BuildOrchestrator] Maven passou sem erros.")
    return True

  def _log_errors(self, stdout: str, stderr: str):
    """
    Registra os erros encontrados durante a execução dos comandos.

    :param stdout: Saída padrão do comando.
    :param stderr: Saída de erro do comando.
    """
    with open(ERRORS_FILE, "a", encoding="utf-8") as f:
      f.write("=== Erro Detectado ===\n")
      f.write(stdout)
      f.write(stderr)
      f.write("\n")
    print(f"[BuildOrchestrator] Erros registrados em {ERRORS_FILE}.")

  def handle_errors(self):
    """
    Processa os erros registrados, consulta soluções e aplica reparos.
    """
    errors = self._read_errors()
    if not errors:
      print("[BuildOrchestrator] Nenhum erro para processar.")
      return

    for error in errors:
      print(f"[BuildOrchestrator] Processando erro: {error}")
      # Tenta obter solução do histórico local
      solution = self.history.get_solution_for_error(error)

      if not solution:
        # Se não encontrar no histórico, consulta a IA
        prompt = self._build_prompt(error)
        solution = self.ia.consult_ia(prompt)

      if solution:
        # Aplica a solução
        success = self.reparo.apply_solution(solution)
        if success:
          # Armazena a solução no histórico local
          self.history.store_solution(error, solution)
        else:
          print("[BuildOrchestrator] Falha ao aplicar a solução.")
      else:
        print("[BuildOrchestrator] Não foi possível obter uma solução para o erro.")

  def _read_errors(self) -> List[str]:
    """
    Lê os erros registrados no arquivo de erros.

    :return: Lista de strings de erro.
    """
    if not os.path.exists(ERRORS_FILE):
      return []
    with open(ERRORS_FILE, "r", encoding="utf-8") as f:
      content = f.read()
    # Supondo que cada erro está separado por "=== Erro Detectado ==="
    errors = content.split("=== Erro Detectado ===")
    # Limpa e filtra erros vazios
    errors = [e.strip() for e in errors if e.strip()]
    print(f"[BuildOrchestrator] {len(errors)} erro(s) encontrado(s).")
    return errors

  def _build_prompt(self, error: str) -> str:
    """
    Constrói um prompt detalhado para enviar à IA com base no erro.

    :param error: String contendo a mensagem de erro.
    :return: Prompt formatado para a IA.
    """
    prompt = (
      "Estou enfrentando o seguinte erro em meu projeto:\n\n"
      f"{error}\n\n"
      "Por favor, forneça uma solução detalhada para corrigir este erro, "
      "incluindo quaisquer alterações de código necessárias, comandos a serem executados, "
      "ou ajustes de configuração."
    )
    print(f"[BuildOrchestrator] Prompt para IA:\n{prompt}")
    return prompt


# ------------------------------------------------------------------------------
#                         MÓDULO PRINCIPAL: MegaAutoProgrammer
# ------------------------------------------------------------------------------

class MegaAutoProgrammer:
  """
  Classe principal que orquestra todos os módulos do Programador Autônomo.
  """

  def __init__(self):
    """
    Inicializa todas as instâncias dos módulos necessários.
    """
    print("[MegaAutoProgrammer] Inicializando módulos...")

    # Inicializa o Gerenciador de Dependências
    self.dependency_manager = DependencyManager()
    self.dependency_manager.check_and_install_python_deps()
    self.dependency_manager.check_and_install_node_deps()
    self.dependency_manager.ensure_maven_ready()

    # Inicializa o Histórico Local
    self.local_history = LocalHistoryManager()

    # Inicializa o Monitor de Processos
    self.process_monitor = ProcessMonitor(error_keywords=ERROR_KEYWORDS)

    # Inicializa o Integrador de IA
    self.ia_integrator = IAIntegrator(
      gemini_api_key=GOOGLE_GEMINI_API_KEY,
      openai_api_key=OPENAI_API_KEY
    )

    # Inicializa o Módulo de Reparo
    self.reparo = Reparo(process_monitor=self.process_monitor)

    # Inicializa o Orquestrador de Build
    self.build_orchestrator = BuildOrchestrator(
      process_monitor=self.process_monitor,
      local_history=self.local_history,
      ia_integrator=self.ia_integrator,
      reparo=self.reparo
    )

    print("[MegaAutoProgrammer] Todos os módulos foram inicializados.")

    def run(self):
      """
      Executa o fluxo principal do Programador Autônomo.
      """
      print("[MegaAutoProgrammer] Iniciando o fluxo principal...")

      # Etapa 1: Executar Lint
      lint_success = self.build_orchestrator.run_lint()

      # Etapa 2: Se Lint passou, executar Build
      if lint_success:
        build_success = self.build_orchestrator.run_build()
      else:
        build_success = False

      # Etapa 3: Se Build passou, executar Maven
      if build_success:
        maven_success = self.build_orchestrator.run_maven()
      else:
        maven_success = False

      # Etapa 4: Tratar Erros se houver
      if not (lint_success and build_success and maven_success):
        self.build_orchestrator.handle_errors()
      else:
        print("[MegaAutoProgrammer] Projeto está em bom estado. Nenhuma ação necessária.")

      print("[MegaAutoProgrammer] Fluxo principal concluído.")

      def start(self, max_iterations: int = 10, wait_seconds: int = 60):
    """
    Inicia o loop principal do agente, executando o fluxo principal em ciclos.

    :param max_iterations: Número máximo de iterações antes de parar.
    :param wait_seconds: Tempo de espera entre as iterações, em segundos.
    """
    print("[MegaAutoProgrammer] Iniciando o loop principal...")
    for iteration in range(1, max_iterations + 1):
      print(f"\n[MegaAutoProgrammer] --- Iteração {iteration} ---")
      self.run()

      # Verifica se o projeto está sem erros
      lint_success = self.build_orchestrator.run_lint()
      build_success = self.build_orchestrator.run_build() if lint_success else False
      maven_success = self.build_orchestrator.run_maven() if build_success else False

      if lint_success and build_success and maven_success:
        print("[MegaAutoProgrammer] Projeto sem erros. Finalizando execução.")
        break
      else:
        print(f"[MegaAutoProgrammer] Aguardando {wait_seconds} segundos antes da próxima iteração...")
        time.sleep(wait_seconds)
    else:
      print("[MegaAutoProgrammer] Número máximo de iterações atingido. Finalizando execução.")

    print("[MegaAutoProgrammer] Loop principal encerrado.")

    def shutdown(self):
      """
      Executa as ações de encerramento: salvar estado, gerar relatórios e finalizar.
      """
      print("\n[MegaAutoProgrammer] Executando ações de encerramento...")

      # Salvar estado final no Histórico Local (já é feito ao armazenar soluções)
      # Se houver outros estados a serem salvos, implemente aqui

      # Gerar relatório final
      self._generate_final_report()

      print("[MegaAutoProgrammer] Encerramento concluído. Agente finalizado.")

    def _generate_final_report(self):
      """
      Gera um relatório final com estatísticas de execução.
      """
      print("[MegaAutoProgrammer] Gerando relatório final...")
      # Exemplo básico: contar soluções aplicadas
      try:
        with open(SOLUCOES_APLICADAS_LOG, "r", encoding="utf-8") as f:
          solucoes = f.readlines()
        total_solucoes = len(solucoes)
        print(f"[MegaAutoProgrammer] Total de soluções aplicadas: {total_solucoes}")

        # Você pode expandir este método para incluir mais estatísticas e informações
      except FileNotFoundError:
        print("[MegaAutoProgrammer] Nenhuma solução aplicada registrada.")


        # Modifique o método start para incluir o shutdown ao final
        def start(self, max_iterations: int = 10, wait_seconds: int = 60):
          """
          Inicia o loop principal do agente, executando o fluxo principal em ciclos.

          :param max_iterations: Número máximo de iterações antes de parar.
          :param wait_seconds: Tempo de espera entre as iterações, em segundos.
          """
          print("[MegaAutoProgrammer] Iniciando o loop principal...")
          try:
            for iteration in range(1, max_iterations + 1):
              print(f"\n[MegaAutoProgrammer] --- Iteração {iteration} ---")
              self.run()

              # Verifica se o projeto está sem erros
              lint_success = self.build_orchestrator.run_lint()
              build_success = self.build_orchestrator.run_build() if lint_success else False
              maven_success = self.build_orchestrator.run_maven() if build_success else False

              if lint_success and build_success and maven_success:
                print("[MegaAutoProgrammer] Projeto sem erros. Finalizando execução.")
                break
              else:
                print(f"[MegaAutoProgrammer] Aguardando {wait_seconds} segundos antes da próxima iteração...")
                time.sleep(wait_seconds)
          except KeyboardInterrupt:
            print("\n[MegaAutoProgrammer] Interrupção manual detectada.")
          finally:
            self.shutdown()

        def shutdown(self):
          """
          Executa as ações de encerramento: salvar estado, gerar relatórios e finalizar.
          """
          print("\n[MegaAutoProgrammer] Executando ações de encerramento...")

          # Salvar estado final no Histórico Local (já é feito ao armazenar soluções)
          # Se houver outros estados a serem salvos, implemente aqui

          # Gerar relatório final
          self._generate_final_report()

          print("[MegaAutoProgrammer] Encerramento concluído. Agente finalizado.")

        def _generate_final_report(self):
          """
          Gera um relatório final com estatísticas de execução.
          """
          print("[MegaAutoProgrammer] Gerando relatório final...")
          # Exemplo básico: contar soluções aplicadas
          try:
            with open(SOLUCOES_APLICADAS_LOG, "r", encoding="utf-8") as f:
              solucoes = f.readlines()
            total_solucoes = len(solucoes)
            print(f"[MegaAutoProgrammer] Total de soluções aplicadas: {total_solucoes}")

            # Você pode expandir este método para incluir mais estatísticas e informações
          except FileNotFoundError:
            print("[MegaAutoProgrammer] Nenhuma solução aplicada registrada.")

# ------------------------------------------------------------------------------
#                               FUNÇÃO PRINCIPAL
# ------------------------------------------------------------------------------

def main():
  """
  Função principal que inicia o Programador Autônomo.
  """
  agente = MegaAutoProgrammer()
  agente.start(max_iterations=10, wait_seconds=60)

if __name__ == "__main__":
  main()



