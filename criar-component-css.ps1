# Script PowerShell para criar arquivos .component.css vazios para os componentes do formulário de cadastro de aluno.

# Caminho base para a pasta do projeto (ajuste se necessário)
$basePath = "C:\cadastroaluno\src\main\webapp\app\cadastro-aluno"

# Lista de componentes (nome das pastas)
$components = @(
    "autorizacao-fotos",
    "cadastro-aluno",
    "contato-emergencia",
    "dados-aluno",
    "deslocamento",
    "documentos",
    "endereco",
    "filiacao",
    "informacoes-complementares",
    "informacoes-escolares",
    "responsaveis"
)

# Itera sobre cada componente
foreach ($component in $components) {
  # Cria o caminho completo para o arquivo .component.css
  $filePath = Join-Path -Path $basePath -ChildPath "$component\$component.component.css"

  # Verifica se o arquivo já existe
  if (!(Test-Path -Path $filePath)) {
    # Cria o arquivo vazio
    New-Item -Path $filePath -ItemType File -Force
    Write-Host "Arquivo criado: $filePath"

    # Adiciona um comentário inicial ao arquivo
    Add-Content -Path $filePath -Value "/* Estilos específicos para o componente $component */"
  } else {
    Write-Host "Arquivo já existe: $filePath"
  }
}

Write-Host "Script concluído."