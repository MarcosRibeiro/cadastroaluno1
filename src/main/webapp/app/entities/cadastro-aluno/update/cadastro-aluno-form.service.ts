import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICadastroAluno, NewCadastroAluno } from '../cadastro-aluno.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICadastroAluno for edit and NewCadastroAlunoFormGroupInput for create.
 */
type CadastroAlunoFormGroupInput = ICadastroAluno | PartialWithRequiredKeyOf<NewCadastroAluno>;

type CadastroAlunoFormDefaults = Pick<NewCadastroAluno, 'id' | 'responsavels' | 'deslocamentos' | 'autorizacao'>;

type CadastroAlunoFormGroupContent = {
  id: FormControl<ICadastroAluno['id'] | NewCadastroAluno['id']>;
  dataCadastro: FormControl<ICadastroAluno['dataCadastro']>;
  matricula: FormControl<ICadastroAluno['matricula']>;
  grupo: FormControl<ICadastroAluno['grupo']>;
  nome: FormControl<ICadastroAluno['nome']>;
  dn: FormControl<ICadastroAluno['dn']>;
  cep: FormControl<ICadastroAluno['cep']>;
  endereco: FormControl<ICadastroAluno['endereco']>;
  qd: FormControl<ICadastroAluno['qd']>;
  lote: FormControl<ICadastroAluno['lote']>;
  endnumero: FormControl<ICadastroAluno['endnumero']>;
  bairro: FormControl<ICadastroAluno['bairro']>;
  municipio: FormControl<ICadastroAluno['municipio']>;
  uf: FormControl<ICadastroAluno['uf']>;
  telefone: FormControl<ICadastroAluno['telefone']>;
  certidao: FormControl<ICadastroAluno['certidao']>;
  termo: FormControl<ICadastroAluno['termo']>;
  cartorio: FormControl<ICadastroAluno['cartorio']>;
  naturalidade: FormControl<ICadastroAluno['naturalidade']>;
  rg: FormControl<ICadastroAluno['rg']>;
  cpf: FormControl<ICadastroAluno['cpf']>;
  nis: FormControl<ICadastroAluno['nis']>;
  cras: FormControl<ICadastroAluno['cras']>;
  filiacaoPai: FormControl<ICadastroAluno['filiacaoPai']>;
  paiTelefone: FormControl<ICadastroAluno['paiTelefone']>;
  paiNaturalidade: FormControl<ICadastroAluno['paiNaturalidade']>;
  paiUf: FormControl<ICadastroAluno['paiUf']>;
  paiRg: FormControl<ICadastroAluno['paiRg']>;
  paiDataNascimento: FormControl<ICadastroAluno['paiDataNascimento']>;
  paiCpf: FormControl<ICadastroAluno['paiCpf']>;
  paiNis: FormControl<ICadastroAluno['paiNis']>;
  paiTituloEleitor: FormControl<ICadastroAluno['paiTituloEleitor']>;
  paiZona: FormControl<ICadastroAluno['paiZona']>;
  paiSecao: FormControl<ICadastroAluno['paiSecao']>;
  paiMunicipio: FormControl<ICadastroAluno['paiMunicipio']>;
  filiacaoMae: FormControl<ICadastroAluno['filiacaoMae']>;
  maeTelefone: FormControl<ICadastroAluno['maeTelefone']>;
  maeNaturalidade: FormControl<ICadastroAluno['maeNaturalidade']>;
  maeUf: FormControl<ICadastroAluno['maeUf']>;
  maeRg: FormControl<ICadastroAluno['maeRg']>;
  maeDataNascimento: FormControl<ICadastroAluno['maeDataNascimento']>;
  maeCpf: FormControl<ICadastroAluno['maeCpf']>;
  maeNis: FormControl<ICadastroAluno['maeNis']>;
  maeTituloEleitor: FormControl<ICadastroAluno['maeTituloEleitor']>;
  maeZona: FormControl<ICadastroAluno['maeZona']>;
  maeSecao: FormControl<ICadastroAluno['maeSecao']>;
  maeMunicipio: FormControl<ICadastroAluno['maeMunicipio']>;
  nomeEscola: FormControl<ICadastroAluno['nomeEscola']>;
  anoCursando: FormControl<ICadastroAluno['anoCursando']>;
  turno: FormControl<ICadastroAluno['turno']>;
  mediaEscolar: FormControl<ICadastroAluno['mediaEscolar']>;
  prioritario: FormControl<ICadastroAluno['prioritario']>;
  obs: FormControl<ICadastroAluno['obs']>;
  comportamentoCasa: FormControl<ICadastroAluno['comportamentoCasa']>;
  comportamentoEscola: FormControl<ICadastroAluno['comportamentoEscola']>;
  deficiencia: FormControl<ICadastroAluno['deficiencia']>;
  adaptacoes: FormControl<ICadastroAluno['adaptacoes']>;
  medicacao: FormControl<ICadastroAluno['medicacao']>;
  medicacaoDesc: FormControl<ICadastroAluno['medicacaoDesc']>;
  alergia: FormControl<ICadastroAluno['alergia']>;
  alergiaDesc: FormControl<ICadastroAluno['alergiaDesc']>;
  historicoMedico: FormControl<ICadastroAluno['historicoMedico']>;
  rendaFamiliar: FormControl<ICadastroAluno['rendaFamiliar']>;
  pessoasTrabalham: FormControl<ICadastroAluno['pessoasTrabalham']>;
  numPessoasLar: FormControl<ICadastroAluno['numPessoasLar']>;
  beneficioSocial: FormControl<ICadastroAluno['beneficioSocial']>;
  beneficios: FormControl<ICadastroAluno['beneficios']>;
  tipoResidencia: FormControl<ICadastroAluno['tipoResidencia']>;
  tipoResidenciaDesc: FormControl<ICadastroAluno['tipoResidenciaDesc']>;
  situacaoResidencia: FormControl<ICadastroAluno['situacaoResidencia']>;
  situacaoResidenciaDesc: FormControl<ICadastroAluno['situacaoResidenciaDesc']>;
  contatoEmergencia: FormControl<ICadastroAluno['contatoEmergencia']>;
  foneEmergencia: FormControl<ICadastroAluno['foneEmergencia']>;
  relacaoEmergencia: FormControl<ICadastroAluno['relacaoEmergencia']>;
  autorizacao: FormControl<ICadastroAluno['autorizacao']>;
  fotoAluno: FormControl<ICadastroAluno['fotoAluno']>;
  fotoMae: FormControl<ICadastroAluno['fotoMae']>;
  responsavels: FormControl<ICadastroAluno['responsavels']>;
  deslocamentos: FormControl<ICadastroAluno['deslocamentos']>;
};

export type CadastroAlunoFormGroup = FormGroup<CadastroAlunoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CadastroAlunoFormService {
  createCadastroAlunoFormGroup(cadastroAluno: CadastroAlunoFormGroupInput = { id: null }): CadastroAlunoFormGroup {
    const cadastroAlunoRawValue = {
      ...this.getFormDefaults(),
      ...cadastroAluno,
    };
    return new FormGroup<CadastroAlunoFormGroupContent>({
      id: new FormControl(
        { value: cadastroAlunoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataCadastro: new FormControl(cadastroAlunoRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      matricula: new FormControl(cadastroAlunoRawValue.matricula, {
        validators: [Validators.maxLength(20)],
      }),
      grupo: new FormControl(cadastroAlunoRawValue.grupo, {
        validators: [Validators.maxLength(50)],
      }),
      nome: new FormControl(cadastroAlunoRawValue.nome, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      dn: new FormControl(cadastroAlunoRawValue.dn, {
        validators: [Validators.required],
      }),
      cep: new FormControl(cadastroAlunoRawValue.cep, {
        validators: [Validators.required, Validators.minLength(8), Validators.maxLength(9), Validators.pattern(/^\\d{5}-\\d{3}$/)],
      }),
      endereco: new FormControl(cadastroAlunoRawValue.endereco, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      qd: new FormControl(cadastroAlunoRawValue.qd, {
        validators: [Validators.maxLength(10)],
      }),
      lote: new FormControl(cadastroAlunoRawValue.lote, {
        validators: [Validators.maxLength(10)],
      }),
      endnumero: new FormControl(cadastroAlunoRawValue.endnumero, {
        validators: [Validators.maxLength(10)],
      }),
      bairro: new FormControl(cadastroAlunoRawValue.bairro, {
        validators: [Validators.maxLength(100)],
      }),
      municipio: new FormControl(cadastroAlunoRawValue.municipio, {
        validators: [Validators.maxLength(100)],
      }),
      uf: new FormControl(cadastroAlunoRawValue.uf, {
        validators: [Validators.maxLength(2)],
      }),
      telefone: new FormControl(cadastroAlunoRawValue.telefone, {
        validators: [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(15),
          Validators.pattern(/^(\\\\d{2})\\\\s(\\\\d{4,5})-\\\\d{4}$/),
        ],
      }),
      certidao: new FormControl(cadastroAlunoRawValue.certidao, {
        validators: [Validators.maxLength(50)],
      }),
      termo: new FormControl(cadastroAlunoRawValue.termo, {
        validators: [Validators.maxLength(50)],
      }),
      cartorio: new FormControl(cadastroAlunoRawValue.cartorio, {
        validators: [Validators.maxLength(100)],
      }),
      naturalidade: new FormControl(cadastroAlunoRawValue.naturalidade, {
        validators: [Validators.maxLength(100)],
      }),
      rg: new FormControl(cadastroAlunoRawValue.rg, {
        validators: [Validators.maxLength(20)],
      }),
      cpf: new FormControl(cadastroAlunoRawValue.cpf, {
        validators: [
          Validators.required,
          Validators.minLength(11),
          Validators.maxLength(14),
          Validators.pattern(/^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$/),
        ],
      }),
      nis: new FormControl(cadastroAlunoRawValue.nis, {
        validators: [Validators.maxLength(15)],
      }),
      cras: new FormControl(cadastroAlunoRawValue.cras, {
        validators: [Validators.maxLength(100)],
      }),
      filiacaoPai: new FormControl(cadastroAlunoRawValue.filiacaoPai, {
        validators: [Validators.maxLength(255)],
      }),
      paiTelefone: new FormControl(cadastroAlunoRawValue.paiTelefone, {
        validators: [Validators.maxLength(15)],
      }),
      paiNaturalidade: new FormControl(cadastroAlunoRawValue.paiNaturalidade, {
        validators: [Validators.maxLength(100)],
      }),
      paiUf: new FormControl(cadastroAlunoRawValue.paiUf, {
        validators: [Validators.maxLength(2)],
      }),
      paiRg: new FormControl(cadastroAlunoRawValue.paiRg, {
        validators: [Validators.maxLength(20)],
      }),
      paiDataNascimento: new FormControl(cadastroAlunoRawValue.paiDataNascimento),
      paiCpf: new FormControl(cadastroAlunoRawValue.paiCpf, {
        validators: [Validators.minLength(11), Validators.maxLength(14), Validators.pattern(/^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$/)],
      }),
      paiNis: new FormControl(cadastroAlunoRawValue.paiNis, {
        validators: [Validators.maxLength(15)],
      }),
      paiTituloEleitor: new FormControl(cadastroAlunoRawValue.paiTituloEleitor, {
        validators: [Validators.maxLength(20)],
      }),
      paiZona: new FormControl(cadastroAlunoRawValue.paiZona, {
        validators: [Validators.maxLength(10)],
      }),
      paiSecao: new FormControl(cadastroAlunoRawValue.paiSecao, {
        validators: [Validators.maxLength(10)],
      }),
      paiMunicipio: new FormControl(cadastroAlunoRawValue.paiMunicipio, {
        validators: [Validators.maxLength(100)],
      }),
      filiacaoMae: new FormControl(cadastroAlunoRawValue.filiacaoMae, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      maeTelefone: new FormControl(cadastroAlunoRawValue.maeTelefone, {
        validators: [Validators.maxLength(15)],
      }),
      maeNaturalidade: new FormControl(cadastroAlunoRawValue.maeNaturalidade, {
        validators: [Validators.maxLength(100)],
      }),
      maeUf: new FormControl(cadastroAlunoRawValue.maeUf, {
        validators: [Validators.maxLength(2)],
      }),
      maeRg: new FormControl(cadastroAlunoRawValue.maeRg, {
        validators: [Validators.maxLength(20)],
      }),
      maeDataNascimento: new FormControl(cadastroAlunoRawValue.maeDataNascimento),
      maeCpf: new FormControl(cadastroAlunoRawValue.maeCpf, {
        validators: [Validators.minLength(11), Validators.maxLength(14), Validators.pattern(/^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$/)],
      }),
      maeNis: new FormControl(cadastroAlunoRawValue.maeNis, {
        validators: [Validators.maxLength(15)],
      }),
      maeTituloEleitor: new FormControl(cadastroAlunoRawValue.maeTituloEleitor, {
        validators: [Validators.maxLength(20)],
      }),
      maeZona: new FormControl(cadastroAlunoRawValue.maeZona, {
        validators: [Validators.maxLength(10)],
      }),
      maeSecao: new FormControl(cadastroAlunoRawValue.maeSecao, {
        validators: [Validators.maxLength(10)],
      }),
      maeMunicipio: new FormControl(cadastroAlunoRawValue.maeMunicipio, {
        validators: [Validators.maxLength(100)],
      }),
      nomeEscola: new FormControl(cadastroAlunoRawValue.nomeEscola, {
        validators: [Validators.maxLength(255)],
      }),
      anoCursando: new FormControl(cadastroAlunoRawValue.anoCursando, {
        validators: [Validators.maxLength(50)],
      }),
      turno: new FormControl(cadastroAlunoRawValue.turno),
      mediaEscolar: new FormControl(cadastroAlunoRawValue.mediaEscolar),
      prioritario: new FormControl(cadastroAlunoRawValue.prioritario),
      obs: new FormControl(cadastroAlunoRawValue.obs, {
        validators: [Validators.maxLength(5000)],
      }),
      comportamentoCasa: new FormControl(cadastroAlunoRawValue.comportamentoCasa),
      comportamentoEscola: new FormControl(cadastroAlunoRawValue.comportamentoEscola),
      deficiencia: new FormControl(cadastroAlunoRawValue.deficiencia),
      adaptacoes: new FormControl(cadastroAlunoRawValue.adaptacoes),
      medicacao: new FormControl(cadastroAlunoRawValue.medicacao),
      medicacaoDesc: new FormControl(cadastroAlunoRawValue.medicacaoDesc, {
        validators: [Validators.maxLength(5000)],
      }),
      alergia: new FormControl(cadastroAlunoRawValue.alergia),
      alergiaDesc: new FormControl(cadastroAlunoRawValue.alergiaDesc, {
        validators: [Validators.maxLength(5000)],
      }),
      historicoMedico: new FormControl(cadastroAlunoRawValue.historicoMedico, {
        validators: [Validators.maxLength(5000)],
      }),
      rendaFamiliar: new FormControl(cadastroAlunoRawValue.rendaFamiliar, {
        validators: [Validators.maxLength(20)],
      }),
      pessoasTrabalham: new FormControl(cadastroAlunoRawValue.pessoasTrabalham),
      numPessoasLar: new FormControl(cadastroAlunoRawValue.numPessoasLar),
      beneficioSocial: new FormControl(cadastroAlunoRawValue.beneficioSocial),
      beneficios: new FormControl(cadastroAlunoRawValue.beneficios, {
        validators: [Validators.maxLength(5000)],
      }),
      tipoResidencia: new FormControl(cadastroAlunoRawValue.tipoResidencia),
      tipoResidenciaDesc: new FormControl(cadastroAlunoRawValue.tipoResidenciaDesc, {
        validators: [Validators.maxLength(100)],
      }),
      situacaoResidencia: new FormControl(cadastroAlunoRawValue.situacaoResidencia),
      situacaoResidenciaDesc: new FormControl(cadastroAlunoRawValue.situacaoResidenciaDesc, {
        validators: [Validators.maxLength(100)],
      }),
      contatoEmergencia: new FormControl(cadastroAlunoRawValue.contatoEmergencia, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      foneEmergencia: new FormControl(cadastroAlunoRawValue.foneEmergencia, {
        validators: [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(15),
          Validators.pattern(/^(\\\\d{2})\\\\s(\\\\d{4,5})-\\\\d{4}$/),
        ],
      }),
      relacaoEmergencia: new FormControl(cadastroAlunoRawValue.relacaoEmergencia, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      autorizacao: new FormControl(cadastroAlunoRawValue.autorizacao, {
        validators: [Validators.required],
      }),
      fotoAluno: new FormControl(cadastroAlunoRawValue.fotoAluno),
      fotoMae: new FormControl(cadastroAlunoRawValue.fotoMae),
      responsavels: new FormControl(cadastroAlunoRawValue.responsavels ?? []),
      deslocamentos: new FormControl(cadastroAlunoRawValue.deslocamentos ?? []),
    });
  }

  getCadastroAluno(form: CadastroAlunoFormGroup): ICadastroAluno | NewCadastroAluno {
    return form.getRawValue() as ICadastroAluno | NewCadastroAluno;
  }

  resetForm(form: CadastroAlunoFormGroup, cadastroAluno: CadastroAlunoFormGroupInput): void {
    const cadastroAlunoRawValue = { ...this.getFormDefaults(), ...cadastroAluno };
    form.reset(
      {
        ...cadastroAlunoRawValue,
        id: { value: cadastroAlunoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CadastroAlunoFormDefaults {
    return {
      id: null,
      responsavels: [],
      deslocamentos: [],
      autorizacao: false,
    };
  }
}
