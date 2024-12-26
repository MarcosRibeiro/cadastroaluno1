import unittest
import os
from programador_autonomo import executar_comando, ResultadoComando

class TesteModuloInteracaoProjeto(unittest.TestCase):

  def test_executar_comando_sucesso(self):
    resultado = executar_comando("echo Hello World")
    self.assertEqual(resultado.codigo_retorno, 0)
    self.assertIn("Hello World", resultado.stdout)
    self.assertEqual(resultado.stderr, "")

  def test_executar_comando_erro(self):
    resultado = executar_comando("comando_inexistente")
    self.assertNotEqual(resultado.codigo_retorno, 0)
    self.assertNotEqual(resultado.stderr, "")

  def test_executar_comando_timeout(self):
    resultado = executar_comando("sleep 2", timeout=1)  # Comando que demora mais que o timeout
    self.assertEqual(resultado.codigo_retorno, -1) # -1 indica timeout
    self.assertIn("Timeout", resultado.stderr)

  def test_detectar_erros_na_saida(self):
    resultado = ResultadoComando("","", "", 0, 0.1)

    resultado.stdout = ""
    resultado.stderr = "Error: Arquivo não encontrado"
    self.assertTrue(resultado.tem_erros())

    resultado.stdout = "Compilação concluída com sucesso."
    resultado.stderr = ""
    self.assertFalse(resultado.tem_erros())

    resultado.stdout = ""
    resultado.stderr = "Exception in thread \"main\" java.lang.NullPointerException"
    self.assertTrue(resultado.tem_erros())

    resultado.stdout = "Build completed successfully."
    resultado.stderr = "Warning: Deprecated API will be removed in the next version."
    self.assertFalse(resultado.tem_erros()) # ignora warnings

  def test_detectar_erros_na_saida_case_insensitive(self):
    resultado = ResultadoComando("","", "", 0, 0.1)

    resultado.stdout = ""
    resultado.stderr = "error: Arquivo não encontrado"
    self.assertTrue(resultado.tem_erros())

    resultado.stdout = ""
    resultado.stderr = "EXCEPTION: NullPointerException"
    self.assertTrue(resultado.tem_erros())
