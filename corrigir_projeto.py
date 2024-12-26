import os
import re

BASE_DIR = "C:\\cadastroaluno"  # Substitua se o caminho for diferente

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

            injection = """
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
                new_content = re.sub(r"(</project>)", injection + r"\n\1", original, 1, flags=re.IGNORECASE)
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

def corrigir_arquivo_especifico(filepath, correcoes):
    """
    Lê um arquivo específico, aplica as correções e sobrescreve se necessário.
    """
    try:
        with open(filepath, "r+", encoding="utf-8") as f:
            original = f.read()
            content = original

            for padrao, substituto in correcoes:
                content = re.sub(padrao, substituto, content)

            if content != original:
                f.seek(0)
                f.write(content)
                f.truncate()
                print(f"[OK] Modificado: {filepath}")
            else:
                print(f"[INFO] Nenhuma modificação necessária em: {filepath}")

    except Exception as e:
        print(f"[ERRO] Erro ao processar arquivo {filepath}: {e}")

def main():
    print("[INFO] Script de correção iniciado (modo ultra-focado).")

    # 1) Corrigir pom.xml (dependências)
    corrigir_pom_xml()

    # 2) Correções em arquivos .ts específicos
    arquivos_para_corrigir = {
        "src/main/webapp/app/shared/shared.module.ts": [
            # Substituir importações incorretas no shared.module.ts
            (r"import\s+AlertComponent\s+from\s+'\./alert/alert\.component';",
             "import { AlertComponent } from './alert/alert.component';"),
            (r"import\s+AlertErrorComponent\s+from\s+'\./alert/alert-error\.component';",
             "import { AlertErrorComponent } from './alert/alert-error.component';"),
            (r"import\s+SortByDirective\s+from\s+'\./sort/sort-by\.directive';",
             "import { SortByDirective } from './sort/sort-by.directive';"),
            (r"import\s+SortDirective\s+from\s+'\./sort/sort\.directive';",
             "import { SortDirective } from './sort/sort.directive';")
        ],
        "src/main/webapp/app/entities/cadastro-aluno/service/cadastro-aluno.service.ts": [
            # Corrigir erro TS2345 em cadastro-aluno.service.ts
            (r"(\.includes\()(\w+), (\w+)(\))", r"\1\2 as number, \3\4"),
            (r"(\.push\()(\w+)(\))", r"\1\2 as number\3")
        ],
        "src/main/webapp/app/entities/cadastro-aluno/cadastro-aluno.routes.ts": [
            # Corrigir erro de digitação no cadastro-aluno.routes.ts
            (r"(\.\.\/cadastro-aluno\.model';)", r"../cadastro-aluno.model';")
        ],
        "src/main/webapp/app/cadastro-aluno/cadastro-aluno/cadastro-aluno.component.ts": [
            # 4) create(cadastroAluno) -> create({ ...cadastroAluno, id: null })
            (r"(\.create\s*\(\s*)cadastroAluno(\s*\))", r"\1{ ...cadastroAluno, id: null }\2")
        ],
        "src/main/webapp/app/cadastro-aluno/cadastro-aluno.module.ts": [
            # 3) withInterceptorsFromDi -> withInterceptorsFromDi()
            (r"withInterceptorsFromDi", r"withInterceptorsFromDi()")
        ]
    }

    for filepath, correcoes in arquivos_para_corrigir.items():
        caminho_completo = os.path.join(BASE_DIR, filepath)
        corrigir_arquivo_especifico(caminho_completo, correcoes)

    # 3) Remover 'standalone: true' e 'imports: []' dos decorators MENCIONADOS NOS ERROS
    #  (Usa a função 'remover_decorator_entries' do script anterior,
    #   mas agora aplicada a arquivos específicos)
    arquivos_remover_decorator = [
        "src/main/webapp/app/account/activate/activate.component.ts",
        "src/main/webapp/app/account/password-reset/finish/password-reset-finish.component.ts",
        "src/main/webapp/app/account/password-reset/init/password-reset-init.component.ts",
        "src/main/webapp/app/account/password/password-strength-bar/password-strength-bar.component.ts",
        "src/main/webapp/app/account/password/password.component.ts",
        "src/main/webapp/app/account/register/register.component.ts",
        "src/main/webapp/app/account/settings/settings.component.ts",
        "src/main/webapp/app/admin/configuration/configuration.component.ts",
        "src/main/webapp/app/admin/health/health.component.ts",
        "src/main/webapp/app/admin/health/modal/health-modal.component.ts",
        "src/main/webapp/app/admin/logs/logs.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/jvm-memory/jvm-memory.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/jvm-threads/jvm-threads.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/metrics-cache/metrics-cache.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/metrics-datasource/metrics-datasource.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/metrics-endpoints-requests/metrics-endpoints-requests.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/metrics-garbagecollector/metrics-garbagecollector.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/metrics-modal-threads/metrics-modal-threads.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/metrics-request/metrics-request.component.ts",
        "src/main/webapp/app/admin/metrics/blocks/metrics-system/metrics-system.component.ts",
        "src/main/webapp/app/admin/metrics/metrics.component.ts",
        "src/main/webapp/app/admin/user-management/delete/user-management-delete-dialog.component.ts",
        "src/main/webapp/app/admin/user-management/detail/user-management-detail.component.ts",
        "src/main/webapp/app/admin/user-management/list/user-management.component.ts",
        "src/main/webapp/app/admin/user-management/update/user-management-update.component.ts",
        "src/main/webapp/app/entities/admin/authority/delete/authority-delete-dialog.component.ts",
        "src/main/webapp/app/entities/admin/authority/detail/authority-detail.component.ts",
        "src/main/webapp/app/entities/admin/authority/list/authority.component.ts",
        "src/main/webapp/app/entities/admin/authority/update/authority-update.component.ts",
        "src/main/webapp/app/entities/cadastro-aluno/detail/cadastro-aluno-detail.component.ts",
        "src/main/webapp/app/entities/cadastro-aluno/update/cadastro-aluno-update.component.ts",
        "src/main/webapp/app/entities/deslocamento/delete/deslocamento-delete-dialog.component.ts",
        "src/main/webapp/app/entities/deslocamento/detail/deslocamento-detail.component.ts",
        "src/main/webapp/app/entities/deslocamento/list/deslocamento.component.ts",
        "src/main/webapp/app/entities/deslocamento/update/deslocamento-update.component.ts",
        "src/main/webapp/app/entities/responsavel/delete/responsavel-delete-dialog.component.ts",
        "src/main/webapp/app/entities/responsavel/detail/responsavel-detail.component.ts",
        "src/main/webapp/app/entities/responsavel/list/responsavel.component.ts",
        "src/main/webapp/app/entities/responsavel/update/responsavel-update.component.ts",
        "src/main/webapp/app/home/home.component.ts",
        "src/main/webapp/app/layouts/error/error.component.ts",
        "src/main/webapp/app/layouts/navbar/navbar.component.ts",
        "src/main/webapp/app/layouts/profiles/page-ribbon.component.ts",
        "src/main/webapp/app/login/login.component.ts"
    ]

    for rel_filepath in arquivos_remover_decorator:
        filepath = os.path.join(BASE_DIR, rel_filepath)

        if os.path.isfile(filepath):  # Verifica se o arquivo existe
            try:
                with open(filepath, "r+", encoding="utf-8") as f:
                    original = f.read()
                    lines = original.split("\n")
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

                    if content != original:
                        f.seek(0)
                        f.write(content)
                        f.truncate()
                        print(f"[OK] Modificado (decorator): {filepath}")
                    else:
                        print(f"[INFO] Nenhuma modificação de decorator necessária em: {filepath}")
            except Exception as e:
                print(f"[ERRO] Erro ao processar arquivo {filepath}: {e}")
        else:
            print(f"[AVISO] Arquivo não encontrado: {filepath}")

    print("\n[FINAL] Concluído!")
    print("Agora execute:")
    print("  1) npm run clean-www && npm run webapp:build")
    print("  2) .\\mvnw")

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
                result.append(re.sub(r",?\s*$", "", line))
            else:
                result.append(line)
        else:
            result.append(line)
    return result

if __name__ == "__main__":
    main()