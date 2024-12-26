import os
import subprocess
import time
import datetime
import json
import shutil
import sys
import re
import traceback
import argparse
from typing import List, Dict, Any, Tuple, Optional, Callable
import threading
import queue

# =============================================================================
#                             CONFIGURAÇÕES GERAIS
# =============================================================================

PROJECT_ROOT = "."  # Diretório raiz do projeto (ajuste se necessário)
AGENT_NAME = "programador_autonomo_avancado"
AGENT_VERSION = "1.0.0"
AGENT_CODE_FILE = os.path.abspath(__file__) # Caminho absoluto para este script

# Chaves de API (coloque as suas chaves aqui ou use variáveis de ambiente)
GOOGLE_GEMINI_API_KEY = os.environ.get("GEMINI_API_KEY", "")
OPENAI_API_KEY = os.environ.get("OPENAI_API_KEY", "")

# Parâmetros de Execução
TIMEOUT_COMANDO = 600  # Timeout para execução de comandos (segundos)
INTERVALO_ESPERA = 5   # Intervalo de espera ativa (segundos)
MAX_ITERACOES = 100   # Número máximo de iterações do loop principal
MAX_ERROS_CONSECUTIVOS = 3 # Número máximo de erros consecutivos antes de desistir (era 5, alterado para 3 para agilizar testes)

# Palavras-chave para detecção de erros (expansível)
PALAVRAS_CHAVE_ERRO = [
  "error:",
  "exception:",
  "failed:",
  "build failure:",
  "not found:",
  "compilation error:",
  "runtime error:",
  "segmentation fault:",
  "traceback (most recent call last):"
]

# Comandos de verificação e build (configuráveis para diferentes tipos de projeto)
COMANDOS_VERIFICACAO = [
  "npm run lint",        # Exemplo para projetos Node.js
  "flake8 .",          # Exemplo para projetos Python
  "pylint --rcfile=pylintrc .", # Exemplo com configuração Pylint
  "npx tsc --noEmit"    # Exemplo para TypeScript
]
COMANDO_BUILD_FINAL = ".\\mvnw verify"  # Build completo do Maven (ou outro comando de build)

# =============================================================================
#                         CONFIGURAÇÕES DE IA
# =============================================================================

# Configurações para o Google Gemini
GEMINI_MODEL_NAME = "gemini-exp-1206"
GEMINI_GENERATION_CONFIG = {
  "temperature": 0.9,
  "top_p": 0.95,
  "top_k": 64,
  "max_output_tokens": 8192,
  "response_mime_type": "text/plain",
}

# Configurações para OpenAI GPT
OPENAI_MODEL_NAME = "gpt-3.5-turbo"

# =============================================================================
#                            CONFIGURAÇÕES DE ARQUIVOS E PASTAS
# =============================================================================

# Arquivos de log
LOG_DIR = os.path.join(PROJECT_ROOT, "logs")
ERROS_FILE = os.path.join(LOG_DIR, "erros.log")
ACOES_FILE = os.path.join(LOG_DIR, "acoes.log")  # Log de ações tomadas pelo agente
DEBUG_FILE = os.path.join(LOG_DIR, "debug.log")  # Log de depuração detalhado
SOLUCOES_APLICADAS_FILE = os.path.join(LOG_DIR, "solucoes_aplicadas.log")

# Arquivos de dados do agente
HISTORICO_ERROS_SOLUCOES = os.path.join(PROJECT_ROOT, "historico_erros_solucoes.json")
BASE_CONHECIMENTO = os.path.join(PROJECT_ROOT, "base_conhecimento.json")
ESTADO_AGENTE_FILE = os.path.join(PROJECT_ROOT, "estado_agente.json")

# Diretórios
BACKUP_PROJETO_DIR = os.path.join(PROJECT_ROOT, "backup_projeto")
BACKUP_AGENTE_DIR = os.path.join(PROJECT_ROOT, "backup_agente")

# =============================================================================
#                      CLASSES DE ESTADO DO PROJETO E DO AGENTE
# =============================================================================

class EstadoProjeto:
  """
  Representa o estado atual do projeto monitorado pelo agente.
  """

  def __init__(self, raiz: str = PROJECT_ROOT):
    self.raiz = raiz
    self.erros_detectados: List[str] = []
    self.solucoes_aplicadas: List[str] = []
    self.ultima_verificacao: Optional[datetime.datetime] = None
    self.ultimo_build: Optional[datetime.datetime] = None
    self.build_bem_sucedido: bool = False
    self.arquivos_modificados: Dict[str, float] = {}  # {caminho_arquivo: timestamp_modificacao}
    self.metricas: Dict[str, Any] = {} # Dicionário para armazenar métricas de desempenho

  def to_dict(self) -> Dict[str, Any]:
    """Converte o estado do projeto em um dicionário (para serialização)."""
    return {
      "raiz": self.raiz,
      "erros_detectados": self.erros_detectados,
      "solucoes_aplicadas": self.solucoes_aplicadas,
      "ultima_verificacao": self.ultima_verificacao.isoformat() if self.ultima_verificacao else None,
      "ultimo_build": self.ultimo_build.isoformat() if self.ultimo_build else None,
      "build_bem_sucedido": self.build_bem_sucedido,
      "arquivos_modificados": self.arquivos_modificados,
      "metricas": self.metricas
    }

  @staticmethod
  def from_dict(dados: Dict[str, Any]) -> "EstadoProjeto":
    """Cria um objeto EstadoProjeto a partir de um dicionário."""
    estado = EstadoProjeto(dados["raiz"])
    estado.erros_detectados = dados.get("erros_detectados", [])
    estado.solucoes_aplicadas = dados.get("solucoes_aplicadas", [])
    estado.ultima_verificacao = datetime.datetime.fromisoformat(dados["ultima_verificacao"]) if dados.get("ultima_verificacao") else None
    estado.ultimo_build = datetime.datetime.fromisoformat(dados["ultimo_build"]) if dados.get("ultimo_build") else None
    estado.build_bem_sucedido = dados.get("build_bem_sucedido", False)
    estado.arquivos_modificados = dados.get("arquivos_modificados", {})
    estado.metricas = dados.get("metricas", {})
    return estado

  def __str__(self):
    """Retorna uma representação em string do estado do projeto."""
    return json.dumps(self.to_dict(), indent=2, sort_keys=True)

class EstadoAgente:
  """
  Representa o estado interno do agente autônomo.
  """

  def __init__(self):
    self.iteracoes: int = 0
    self.erros_consecutivos: int = 0
    self.estado_projeto: EstadoProjeto = EstadoProjeto()
    self.acoes_recentes: List[str] = []  # Lista de ações recentes tomadas pelo agente
    self.metricas_agente: Dict[str, Any] = {} # Dicionário para métricas internas do agente

  def to_dict(self) -> Dict[str, Any]:
    """Converte o estado do agente em um dicionário."""
    return {
      "iteracoes": self.iteracoes,
      "erros_consecutivos": self.erros_consecutivos,
      "estado_projeto": self.estado_projeto.to_dict(),  # Serializa o estado do projeto
      "acoes_recentes": self.acoes_recentes,
      "metricas_agente": self.metricas_agente
    }

  @staticmethod
  def from_dict(dados: Dict[str, Any]) -> "EstadoAgente":
    """Cria um objeto EstadoAgente a partir de um dicionário."""
    estado = EstadoAgente()
    estado.iteracoes = dados.get("iteracoes", 0)
    estado.erros_consecutivos = dados.get("erros_consecutivos", 0)
    estado.estado_projeto = EstadoProjeto.from_dict(dados["estado_projeto"])  # Desserializa o estado do projeto
    estado.acoes_recentes = dados.get("acoes_recentes", [])
    estado.metricas_agente = dados.get("metricas_agente",{})
    return estado

  def registrar_acao(self, acao: str):
    """Registra uma ação recente tomada pelo agente."""
    timestamp = datetime.datetime.now().isoformat()
    self.acoes_recentes.append(f"[{timestamp}] {acao}")
    if len(self.acoes_recentes) > 10:  # Mantém apenas as 10 ações mais recentes
      self.acoes_recentes.pop(0)

  def __str__(self):
    """Retorna uma representação em string do estado do agente."""
    return json.dumps(self.to_dict(), indent=2, sort_keys=True)

# =============================================================================
#                      FUNÇÕES DE GERENCIAMENTO DE ESTADO
# =============================================================================

def carregar_estado_agente(caminho_arquivo: str = ESTADO_AGENTE_FILE) -> EstadoAgente:
  """
  Carrega o estado do agente a partir do arquivo JSON, criando um novo estado se o arquivo não existir.
  """
  try:
    with open(caminho_arquivo, "r", encoding="utf-8") as f:
      dados = json.load(f)
      estado_agente = EstadoAgente.from_dict(dados)
      registrar_acao("Estado do agente carregado com sucesso.")
      return estado_agente
  except FileNotFoundError:
    registrar_acao("Arquivo de estado do agente não encontrado. Criando um novo estado.")
    return EstadoAgente()
  except json.JSONDecodeError:
    registrar_erro("Erro ao decodificar o JSON do estado do agente. Criando um novo estado.")
    return EstadoAgente()
  except Exception as e:
    registrar_erro(f"Erro inesperado ao carregar o estado do agente: {e}. Criando um novo estado.")
    return EstadoAgente()

def salvar_estado_agente(estado_agente: EstadoAgente, caminho_arquivo: str = ESTADO_AGENTE_FILE):
  """
  Salva o estado do agente no arquivo JSON.
  """
  try:
    with open(caminho_arquivo, "w", encoding="utf-8") as f:
      json.dump(estado_agente.to_dict(), f, indent=2)
    registrar_acao("Estado do agente salvo com sucesso.")
  except Exception as e:
    registrar_erro(f"Erro ao salvar o estado do agente: {e}")

# =============================================================================
#                                   UTILIDADES
# =============================================================================

def timestamp_atual() -> str:
  """Retorna o timestamp atual no formato ISO."""
  return datetime.datetime.now().isoformat()

def formatar_timestamp(dt: datetime.datetime) -> str:
  """Formata um objeto datetime no formato desejado."""
  return dt.strftime("%Y-%m-%d %H:%M:%S")

def registrar_log(mensagem: str, arquivo: str, printar_console: bool = True):
  """Registra uma mensagem em um arquivo de log e, opcionalmente, no console."""
  timestamp = timestamp_atual()
  linha = f"[{timestamp}] {mensagem}\n"
  try:
    # Garante que o diretório de logs exista
    os.makedirs(os.path.dirname(arquivo), exist_ok=True)

    with open(arquivo, "a", encoding="utf-8") as f:
      f.write(linha)
    if printar_console:
      print(mensagem)
  except Exception as e:
    print(f"[ERRO] Falha ao registrar log em {arquivo}: {e}")

def registrar_erro(mensagem: str):
  """Registra um erro no arquivo de erros."""
  registrar_log(mensagem, ERROS_FILE, printar_console=True)

def registrar_acao(mensagem: str):
  """Registra uma ação no arquivo de ações."""
  registrar_log(mensagem, ACOES_FILE, printar_console=True)

def registrar_debug(mensagem: str):
  """Registra uma mensagem de depuração no arquivo de debug."""
  registrar_log(mensagem, DEBUG_FILE, printar_console=False)

def registrar_solucao(mensagem: str):
  """Registra uma solução aplicada no arquivo de soluções."""
  registrar_log(mensagem, SOLUCOES_APLICADAS_FILE, printar_console=True)

# =============================================================================
#                     MÓDULO DE INTERAÇÃO COM O PROJETO
# =============================================================================

class ResultadoComando:
  """Representa o resultado da execução de um comando."""
  def __init__(self, comando: str, stdout: str, stderr: str, codigo_retorno: int, tempo_execucao: float):
    self.comando = comando
    self.stdout = stdout
    self.stderr = stderr
    self.codigo_retorno = codigo_retorno
    self.tempo_execucao = tempo_execucao
    self.erros_detectados: List[str] = self.detectar_erros()

  def detectar_erros(self) -> List[str]:
    """Detecta erros na saída do comando com base em palavras-chave."""
    erros = []
    for linha in self.stdout.splitlines():
      if any(palavra in linha.lower() for palavra in PALAVRAS_CHAVE_ERRO):
        erros.append(linha)
    for linha in self.stderr.splitlines():
      if any(palavra in linha.lower() for palavra in PALAVRAS_CHAVE_ERRO):
        erros.append(linha)
    return erros

  def tem_erros(self) -> bool:
    """Verifica se o resultado do comando indica a presença de erros."""
    return self.codigo_retorno != 0 or len(self.erros_detectados) > 0

  def __str__(self):
    return json.dumps(self.__dict__, indent=2, default=str)

def executar_comando(comando: str, timeout: int = TIMEOUT_COMANDO) -> ResultadoComando:
  """
  Executa um comando no shell, captura a saída e retorna um objeto ResultadoComando.
  """
  registrar_acao(f"Executando comando: {comando}")
  inicio = time.time()
  try:
    processo = subprocess.Popen(
      comando,
      shell=True,
      stdout=subprocess.PIPE,
      stderr=subprocess.PIPE,
      text=True,
      cwd=PROJECT_ROOT,
      encoding='utf-8',
      errors='replace'
    )
    stdout, stderr = processo.communicate(timeout=timeout)
    codigo_retorno = processo.returncode
  except subprocess.TimeoutExpired:
    registrar_erro(f"Timeout ao executar o comando: {comando}")
    return ResultadoComando(comando, "", f"Timeout após {timeout} segundos", -1, time.time() - inicio)
  except Exception as e:
    registrar_erro(f"Erro ao executar o comando: {comando}\nExceção: {e}")
    return ResultadoComando(comando, "", str(e), -1, time.time() - inicio)

  fim = time.time()
  tempo_execucao = fim - inicio
  resultado = ResultadoComando(comando, stdout, stderr, codigo_retorno, tempo_execucao)

  if resultado.tem_erros():
    for erro in resultado.erros_detectados:
      registrar_erro(f"Erro detectado na saída do comando '{comando}': {erro}")

  return resultado

# =============================================================================
#                     NÚCLEO DO AGENTE (ORQUESTRADOR)
# =============================================================================

class AgenteAutonomo:
  def __init__(self, raiz_projeto: str = PROJECT_ROOT):
    self.raiz_projeto = raiz_projeto
    self.estado = carregar_estado_agente()
    self.comandos_verificacao = COMANDOS_VERIFICACAO
    self.comando_build_final = COMANDO_BUILD_FINAL
    self.max_iteracoes = MAX_ITERACOES
    self.max_erros_consecutivos = MAX_ERROS_CONSECUTIVOS

    # Cria o diretório de logs, se não existir
    os.makedirs(LOG_DIR, exist_ok=True)

  def executar_verificacoes(self) -> List[ResultadoComando]:
    """Executa os comandos de verificação e retorna os resultados."""
    resultados = []
    for comando in self.comandos_verificacao:
      resultado = executar_comando(comando)
      resultados.append(resultado)
      if resultado.tem_erros():
        registrar_erro(f"Erros detectados na verificação '{comando}'.")
    return resultados

  def executar_build_final(self) -> ResultadoComando:
    """Executa o comando de build final."""
    resultado = executar_comando(self.comando_build_final)
    if resultado.codigo_retorno == 0:
      self.estado.estado_projeto.build_bem_sucedido = True
      self.estado.estado_projeto.ultimo_build = datetime.datetime.now()
    else:
      registrar_erro("Erro durante o build final.")
    return resultado

  def analisar_resultados(self, resultados: List[ResultadoComando]) -> List[str]:
    """
    Analisa os resultados dos comandos de verificação e do build final.
    Retorna uma lista consolidada de erros encontrados.
    """
    erros = []
    for resultado in resultados:
      erros.extend(resultado.erros_detectados)
    return erros

  def monitorar_mudancas_projeto(self):
    """Verifica se o projeto foi alterado externamente (implementação futura)."""
    # TODO: Implementar monitoramento de mudanças no projeto (ex: timestamps, hashes)
    pass

  def autoavaliar_desempenho(self):
    """Avalia o desempenho do agente e identifica áreas para melhoria (implementação futura)."""
    # TODO: Implementar autoavaliação de desempenho
    pass

  def analisar_e_sugerir_melhorias_agente(self):
    """Analisa o código do agente e busca sugestões de melhorias com as IAs (implementação futura)."""
    # TODO: Implementar análise e sugestões de melhorias para o agente
    pass

  def aplicar_solucao(self, erro: str, solucao: str):
    """
    Aplica uma solução ao projeto com base na descrição do erro e na solução proposta.
    """
    registrar_acao(f"Aplicando solução para: {erro[:50]}...")
    registrar_solucao(f"Solução para '{erro}':\n{solucao}")

    # 1. Analisar a solução e identificar o tipo de correção
    if "import" in solucao.lower() and "from" in solucao.lower():
      # Possível solução de import ausente
      self.corrigir_import_ausente(solucao)
    elif "prettier" in solucao.lower():
      # Possível solução relacionada ao Prettier
      self.executar_prettier()
    elif any(palavra in erro.lower() for palavra in ["mat-card", "mat-form-field", "mat-label", "mat-error", "mat-input", "mat-checkbox", "mat-select", "mat-option", "mat-icon", "mat-datepicker", "mat-datepicker-toggle", "mat-toolbar", "mat-sidenav", "mat-list", "mat-divider"]):
      # Possivel erro de falta de import de modulo do Angular Material
      self.corrigir_material_imports(erro)
    else:
      registrar_erro(f"Solução não reconhecida para o erro: {erro}")

  def corrigir_import_ausente(self, solucao: str):
    """
    Extrai informações de import da solução e tenta adicioná-las ao arquivo apropriado.
    """
    try:
      # Expressão regular para extrair informações de importação
      # Exemplo: "import { Componente } from './modulo';"
      padrao = r"import\s*\{?\s*([\w]+)\s*\}?\s*from\s*['\"](.+?)['\"];?"
      correspondencias = re.findall(padrao, solucao)

      if correspondencias:
        for (componente, modulo) in correspondencias:
          registrar_acao(f"Tentando adicionar import: {componente} de {modulo}")
          self.adicionar_import_angular(componente, modulo)
      else:
        registrar_erro(f"Não foi possível extrair informações de import da solução: {solucao}")

    except Exception as e:
      registrar_erro(f"Erro ao tentar corrigir import ausente: {e}")
      traceback.print_exc()

  def executar_prettier(self):
    """
    Executa o Prettier para formatar o código do projeto.
    """
    try:
      registrar_acao("Executando Prettier para formatar o código...")
      resultado = executar_comando("npx prettier --write .")
      if resultado.tem_erros():
        registrar_erro("Erro ao executar o Prettier.")
      else:
        registrar_acao("Prettier executado com sucesso.")
    except Exception as e:
      registrar_erro(f"Erro ao executar o Prettier: {e}")
      traceback.print_exc()

  def corrigir_material_imports(self, erro: str):
    """
    Tenta corrigir erros de importação para componentes do Angular Material.
    """
    try:
      # Analisa a mensagem de erro para identificar o componente ausente
      componente_ausente = re.search(r"'?(mat-\w+)'? is not a known element", erro)
      if componente_ausente:
        componente = componente_ausente.group(1)
        modulo_material = self.obter_modulo_material(componente)
        if modulo_material:
          registrar_acao(f"Tentando adicionar import do módulo {modulo_material} para o componente {componente}")
          self.adicionar_import_angular(modulo_material, f"@angular/material/{componente.replace('mat-', '')}")
        else:
          registrar_erro(f"Não foi possível determinar o módulo do Angular Material para o componente: {componente}")
      else:
        registrar_erro(f"Não foi possível identificar o componente do Angular Material ausente no erro: {erro}")
    except Exception as e:
      registrar_erro(f"Erro ao tentar corrigir import de componente do Angular Material: {e}")
      traceback.print_exc()

  def obter_modulo_material(self, componente: str) -> Optional[str]:
    """
    Retorna o nome do módulo do Angular Material correspondente ao nome do componente.
    Esta é uma implementação de exemplo e precisa ser expandida para cobrir todos os componentes do Material Design.
    """
    mapeamento = {
      "mat-card": "MatCardModule",
      "mat-form-field": "MatFormFieldModule",
      "mat-label": "MatFormFieldModule",
      "mat-error": "MatFormFieldModule",
      "mat-input": "MatInputModule",
      "mat-checkbox": "MatCheckboxModule",
      "mat-select": "MatSelectModule",
      "mat-option": "MatSelectModule",
      "mat-icon": "MatIconModule",
      "mat-datepicker": "MatDatepickerModule",
      "mat-datepicker-toggle": "MatDatepickerModule",
      "mat-toolbar": "MatToolbarModule",
      "mat-sidenav": "MatSidenavModule",
      "mat-list": "MatListModule",
      "mat-divider": "MatDividerModule",
      # Adicione mais mapeamentos conforme necessário
    }
    return mapeamento.get(componente)

  def adicionar_import_angular(self, componente: str, modulo: str):
    """
    Adiciona uma instrução de importação a um arquivo .ts do Angular.
    Esta é uma implementação simplificada e pode precisar de ajustes para lidar com diferentes cenários.
    """
    try:
      for diretorio, _, arquivos in os.walk(self.raiz_projeto):
        for arquivo in arquivos:
          if arquivo.endswith(".ts"):
            caminho_arquivo = os.path.join(diretorio, arquivo)
            with open(caminho_arquivo, "r+", encoding="utf-8") as f:
              conteudo = f.read()
              if f"import {{ {componente} }}" not in conteudo and f"from '{modulo}'" not in conteudo:
                # Adiciona a importação no início do arquivo
                f.seek(0, 0)
                f.write(f"import {{ {componente} }} from '{modulo}';\n{conteudo}")
                registrar_acao(f"Import de '{componente}' adicionado em '{caminho_arquivo}'.")
                return  # Assume que adicionar a importação em um arquivo é suficiente (pode ser necessário refinar)
      registrar_erro(f"Não foi possível encontrar um arquivo .ts para adicionar o import de '{componente}'.")
    except Exception as e:
      registrar_erro(f"Erro ao adicionar import de '{componente}': {e}")
      traceback.print_exc()

  def buscar_solucoes(self, erros: List[str]) -> Dict[str, str]:
    """
    Busca soluções para os erros encontrados (implementação futura).
    """
    # TODO: Implementar a busca de soluções (IA, histórico, base de conhecimento)
    solucoes: Dict[str, str] = {}
    return solucoes

  def criar_prompt_ia(self, erro: str) -> str:
    """
    Cria um prompt para consultar as IAs (implementação futura).
    """
    # TODO: Implementar a criação de prompts para a IA
    prompt = f"Erro encontrado no projeto {self.raiz_projeto}:\n\n{erro}\n\nSugira uma solução detalhada, incluindo as alterações de código necessárias:\n"
    return prompt

  def loop_principal(self):
    """
    Loop principal do agente autônomo.
    """
    registrar_acao(f"Iniciando loop principal do agente {AGENT_NAME} (versão {AGENT_VERSION})...")
    self.estado.iteracoes = 0
    self.estado.erros_consecutivos = 0
    while self.estado.iteracoes < self.max_iteracoes:
      self.estado.iteracoes += 1
      registrar_acao(f"Iniciando iteração {self.estado.iteracoes}/{self.max_iteracoes}...")

      # Monitorar mudanças no projeto
      self.monitorar_mudancas_projeto()

      # Executar verificações
      resultados_verificacao = self.executar_verificacoes()
      self.estado.estado_projeto.ultima_verificacao = datetime.datetime.now()

      # Analisar resultados das verificações
      erros_verificacao = self.analisar_resultados(resultados_verificacao)

      # Executar build final se as verificações passarem e o build ainda não tiver sido bem-sucedido
      if not erros_verificacao and not self.estado.estado_projeto.build_bem_sucedido:
        resultado_build = self.executar_build_final()
        erros_build = self.analisar_resultados([resultado_build])
      else:
        erros_build = []

      # Consolidar erros
      erros = erros_verificacao + erros_build

      if erros:
        self.estado.erros_consecutivos += 1
        registrar_erro(f"{len(erros)} erros encontrados.")

        # Detectar erros críticos e interromper o loop principal se necessário
        for erro in erros:
          if any(palavra in erro.lower() for palavra in ["error", "exception", "failed"]): # Verifica se é um erro crítico
            registrar_erro(f"Erro crítico detectado: {erro}")
            registrar_erro("Interrompendo o loop principal devido a erros críticos.")
            return  # Sai do loop principal

        if self.estado.erros_consecutivos >= self.max_erros_consecutivos:
          registrar_erro(f"Número máximo de erros consecutivos atingido ({self.max_erros_consecutivos}). Abortando.")
          break

        solucoes = self.buscar_solucoes(erros)  # Implementação futura

        for erro, solucao in solucoes.items():
          if solucao:
            try:
              self.aplicar_solucao(erro, solucao)  # Implementado nesta etapa!
              self.estado.erros_consecutivos = 0  # Reseta a contagem de erros consecutivos
            except Exception as e:
              registrar_erro(f"Erro ao aplicar solução para '{erro}': {e}")
              traceback.print_exc()
          else:
            registrar_erro(f"Não foi possível encontrar solução para: {erro}")
      else:
        registrar_acao("Nenhum erro encontrado.")
        if self.estado.estado_projeto.build_bem_sucedido:
          registrar_acao("Build final bem-sucedido. Encerrando loop principal.")
          break

      # Analisar e sugerir melhorias para o próprio agente (implementação futura)
      self.analisar_e_sugerir_melhorias_agente()

      # Autoavaliação de desempenho (implementação futura)
      self.autoavaliar_desempenho()

      # Salvar estado do agente
      salvar_estado_agente(self.estado)

      registrar_acao(f"Iteração {self.estado.iteracoes} concluída.")
      time.sleep(INTERVALO_ESPERA)

    registrar_acao("Loop principal do agente concluído.")

# =============================================================================
#                                 PONTO DE ENTRADA
# =============================================================================

def main():
  """
  Função principal que processa os argumentos da linha de comando e inicia o agente.
  """
  parser = argparse.ArgumentParser(description="Agente Autônomo para Desenvolvimento de Software")
  parser.add_argument(
    "--project_root",
    type=str,
    default=PROJECT_ROOT,
    help=f"Caminho para o diretório raiz do projeto (padrão: {PROJECT_ROOT})",
  )
  parser.add_argument(
    "--comandos_verificacao",
    type=str,
    nargs="+",
    default=COMANDOS_VERIFICACAO,
    help=f"Lista de comandos para verificação do projeto (padrão: comandos predefinidos)",
  )
  parser.add_argument(
    "--comando_build_final",
    type=str,
    default=COMANDO_BUILD_FINAL,
    help=f"Comando para o build final do projeto (padrão: .\\mvnw verify)",
  )
  parser.add_argument(
    "--max_iteracoes",
    type=int,
    default=MAX_ITERACOES,
    help=f"Número máximo de iterações do loop principal (padrão: {MAX_ITERACOES})",
  )
  parser.add_argument(
    "--max_erros_consecutivos",
    type=int,
    default=MAX_ERROS_CONSECUTIVOS,
    help=f"Número máximo de erros consecutivos antes de desistir (padrão: {MAX_ERROS_CONSECUTIVOS})",
  )
  parser.add_argument(
    "--gemini_api_key",
    type=str,
    default=GOOGLE_GEMINI_API_KEY,
    help="Chave da API do Google Gemini (opcional, pode ser definida via variável de ambiente GEMINI_API_KEY)",
  )
  parser.add_argument(
    "--openai_api_key",
    type=str,
    default=OPENAI_API_KEY,
    help="Chave da API da OpenAI (opcional, pode ser definida via variável de ambiente OPENAI_API_KEY)",
  )
  args = parser.parse_args()

  # Atualizar configurações globais com os argumentos da linha de comando
  global PROJECT_ROOT, COMANDOS_VERIFICACAO, COMANDO_BUILD_FINAL, MAX_ITERACOES, MAX_ERROS_CONSECUTIVOS, GOOGLE_GEMINI_API_KEY, OPENAI_API_KEY
  PROJECT_ROOT = args.project_root
  COMANDOS_VERIFICACAO = args.comandos_verificacao
  COMANDO_BUILD_FINAL = args.comando_build_final
  MAX_ITERACOES = args.max_iteracoes
  MAX_ERROS_CONSECUTIVOS = args.max_erros_consecutivos
  GOOGLE_GEMINI_API_KEY = args.gemini_api_key
  OPENAI_API_KEY = args.openai_api_key

  agente = AgenteAutonomo(args.project_root)
  agente.loop_principal()

if __name__ == "__main__":
  main()
