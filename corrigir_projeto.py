import os
import subprocess
import shutil

BASE_DIR = r"C:\cadastroaluno"

def run_cmd(cmd):
  print(f"==> {cmd}")
  proc = subprocess.Popen(cmd, shell=True, cwd=BASE_DIR, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
  out, err = proc.communicate()
  if out:
    print(out.decode("utf-8", errors="replace"))
  if err:
    print(err.decode("utf-8", errors="replace"))

def main():
  # 1) Fixar colorette
  run_cmd("npm install colorette@1.2.2 --save-exact --save-dev")

  # 2) Apagar node_modules e package-lock
  lock_path = os.path.join(BASE_DIR, "package-lock.json")
  node_modules_path = os.path.join(BASE_DIR, "node_modules")
  if os.path.isfile(lock_path):
    os.remove(lock_path)
  if os.path.isdir(node_modules_path):
    shutil.rmtree(node_modules_path)

  # 3) Reinstalar pacotes
  run_cmd("npm install")

  # 4) Migrar @import -> @use no Sass
  run_cmd("npx sass-migrator module --migrate-deps --verbose src/main/webapp")

  # 5) Rodar build
  run_cmd("npm run clean-www && npm run webapp:build")

  # 6) Rodar Maven
  run_cmd(".\\mvnw")

if __name__ == "__main__":
  main()
