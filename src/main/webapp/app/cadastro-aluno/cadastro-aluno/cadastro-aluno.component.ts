import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { CadastroAlunoService } from '../../entities/cadastro-aluno/service/cadastro-aluno.service';
import { ICadastroAluno } from '../../entities/cadastro-aluno/cadastro-aluno.model';

@Component({
  selector: 'jhi-cadastro-aluno',
  templateUrl: './cadastro-aluno.component.html',
  styleUrls: ['./cadastro-aluno.component.css'],
})
export class CadastroAlunoComponent {
  cadastroAlunoForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private cadastroAlunoService: CadastroAlunoService,
  ) {
    this.cadastroAlunoForm = this.fb.group({
      dadosAluno: this.fb.group({
        dataCadastro: [null, Validators.required],
        nome: [null, [Validators.required, Validators.maxLength(255)]],
        dn: [null, Validators.required],
        grupo: [null, Validators.maxLength(50)],
      }),
      endereco: this.fb.group({
        cep: [null, [Validators.required, Validators.maxLength(10)]],
        endereco: [null, [Validators.required, Validators.maxLength(5000)]],
        qd: [null, Validators.maxLength(10)],
        lote: [null, Validators.maxLength(10)],
        endnumero: [null, Validators.maxLength(10)],
        bairro: [null, Validators.maxLength(100)],
        municipio: [null, Validators.maxLength(100)],
        uf: [null, Validators.maxLength(2)],
      }),
      documentos: this.fb.group({
        matricula: [null, Validators.maxLength(20)],
        certidao: [null, Validators.maxLength(50)],
        termo: [null, Validators.maxLength(50)],
        cartorio: [null, Validators.maxLength(100)],
        naturalidade: [null, Validators.maxLength(100)],
        rg: [null, Validators.maxLength(20)],
        cpf: [null, [Validators.required, Validators.maxLength(14)]],
        nis: [null, Validators.maxLength(15)],
        cras: [null, Validators.maxLength(100)],
      }),
      filiacao: this.fb.group({
        filiacaoPai: [null, Validators.maxLength(255)],
        paiTelefone: [null, Validators.maxLength(15)],
        paiNaturalidade: [null, Validators.maxLength(100)],
        paiUf: [null, Validators.maxLength(2)],
        paiRg: [null, Validators.maxLength(20)],
        paiDataNascimento: [null],
        paiCpf: [null, Validators.maxLength(14)],
        paiNis: [null, Validators.maxLength(15)],
        paiTituloEleitor: [null, Validators.maxLength(20)],
        paiZona: [null, Validators.maxLength(10)],
        paiSecao: [null, Validators.maxLength(10)],
        paiMunicipio: [null, Validators.maxLength(100)],
        filiacaoMae: [null, [Validators.required, Validators.maxLength(255)]],
        maeTelefone: [null, Validators.maxLength(15)],
        maeNaturalidade: [null, Validators.maxLength(100)],
        maeUf: [null, Validators.maxLength(2)],
        maeRg: [null, Validators.maxLength(20)],
        maeDataNascimento: [null],
        maeCpf: [null, Validators.maxLength(14)],
        maeNis: [null, Validators.maxLength(15)],
        maeTituloEleitor: [null, Validators.maxLength(20)],
        maeZona: [null, Validators.maxLength(10)],
        maeSecao: [null, Validators.maxLength(10)],
        maeMunicipio: [null, Validators.maxLength(100)],
      }),
      informacoesEscolares: this.fb.group({
        nomeEscola: [null, Validators.maxLength(255)],
        anoCursando: [null, Validators.maxLength(50)],
        turno: [null, Validators.required],
        mediaEscolar: [null, [Validators.min(0), Validators.max(10)]],
      }),
      informacoesComplementares: this.fb.group({
        prioritario: [null],
        obs: [null, Validators.maxLength(5000)],
        comportamentoCasa: [null],
        comportamentoEscola: [null],
        deficiencia: [null],
        adaptacoes: [null],
        medicacao: [null],
        medicacaoDesc: [null, Validators.maxLength(5000)],
        alergia: [null],
        alergiaDesc: [null, Validators.maxLength(5000)],
        historicoMedico: [null, Validators.maxLength(5000)],
        rendaFamiliar: [null, Validators.maxLength(20)],
        pessoasTrabalham: [null],
        numPessoasLar: [null],
        beneficioSocial: [null],
        beneficios: [null, Validators.maxLength(5000)],
        tipoResidencia: [null],
        tipoResidenciaDesc: [null, Validators.maxLength(100)],
        situacaoResidencia: [null],
        situacaoResidenciaDesc: [null, Validators.maxLength(100)],
      }),
      responsaveis: this.fb.group({
        responsaveis: this.fb.array([]),
      }),
      deslocamento: this.fb.group({
        deslocamentos: this.fb.array([]),
      }),
      contatoEmergencia: this.fb.group({
        contatoEmergencia: [null, [Validators.required, Validators.maxLength(255)]],
        foneEmergencia: [null, [Validators.required, Validators.maxLength(15)]],
        relacaoEmergencia: [null, [Validators.required, Validators.maxLength(50)]],
      }),
      autorizacaoFotos: this.fb.group({
        autorizacao: [null, Validators.required],
        fotoAluno: [null],
        fotoMae: [null],
      }),
    });
  }

  get responsaveis(): FormArray {
    return this.cadastroAlunoForm.get('responsaveis.responsaveis') as FormArray;
  }

  get deslocamentos(): FormArray {
    return this.cadastroAlunoForm.get('deslocamento.deslocamentos') as FormArray;
  }

  onSubmit(): void {
    if (this.cadastroAlunoForm.valid) {
      const cadastroAluno: ICadastroAluno = this.cadastroAlunoForm.value;
      this.cadastroAlunoService.create(cadastroAluno).subscribe({
        next: () => {
          this.onClear();
        },
        error: () => {
          this.onError();
        },
      });
    } else {
      this.cadastroAlunoForm.markAllAsTouched();
    }
  }

  onClear(): void {
    this.cadastroAlunoForm.reset();
  }

  protected onError(): void {
    console.error('Erro ao cadastrar.');
  }
}
