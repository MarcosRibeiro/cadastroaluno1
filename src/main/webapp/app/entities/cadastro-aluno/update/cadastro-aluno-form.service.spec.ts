import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cadastro-aluno.test-samples';

import { CadastroAlunoFormService } from './cadastro-aluno-form.service';

describe('CadastroAluno Form Service', () => {
  let service: CadastroAlunoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CadastroAlunoFormService);
  });

  describe('Service methods', () => {
    describe('createCadastroAlunoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCadastroAlunoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataCadastro: expect.any(Object),
            matricula: expect.any(Object),
            grupo: expect.any(Object),
            nome: expect.any(Object),
            dn: expect.any(Object),
            cep: expect.any(Object),
            endereco: expect.any(Object),
            qd: expect.any(Object),
            lote: expect.any(Object),
            endnumero: expect.any(Object),
            bairro: expect.any(Object),
            municipio: expect.any(Object),
            uf: expect.any(Object),
            fone: expect.any(Object),
            certidao: expect.any(Object),
            termo: expect.any(Object),
            cartorio: expect.any(Object),
            naturalidade: expect.any(Object),
            rg: expect.any(Object),
            cpf: expect.any(Object),
            nis: expect.any(Object),
            cras: expect.any(Object),
            filiacaoPai: expect.any(Object),
            paiTelefone: expect.any(Object),
            paiNaturalidade: expect.any(Object),
            paiUf: expect.any(Object),
            paiRg: expect.any(Object),
            paiDataNascimento: expect.any(Object),
            paiCpf: expect.any(Object),
            paiNis: expect.any(Object),
            paiTituloEleitor: expect.any(Object),
            paiZona: expect.any(Object),
            paiSecao: expect.any(Object),
            paiMunicipio: expect.any(Object),
            filiacaoMae: expect.any(Object),
            maeTelefone: expect.any(Object),
            maeNaturalidade: expect.any(Object),
            maeUf: expect.any(Object),
            maeRg: expect.any(Object),
            maeDataNascimento: expect.any(Object),
            maeCpf: expect.any(Object),
            maeNis: expect.any(Object),
            maeTituloEleitor: expect.any(Object),
            maeZona: expect.any(Object),
            maeSecao: expect.any(Object),
            maeMunicipio: expect.any(Object),
            nomeEscola: expect.any(Object),
            anoCursando: expect.any(Object),
            turno: expect.any(Object),
            mediaEscolar: expect.any(Object),
            prioritario: expect.any(Object),
            obs: expect.any(Object),
            comportamentoCasa: expect.any(Object),
            comportamentoEscola: expect.any(Object),
            deficiencia: expect.any(Object),
            adaptacoes: expect.any(Object),
            medicacao: expect.any(Object),
            medicacaoDesc: expect.any(Object),
            alergia: expect.any(Object),
            alergiaDesc: expect.any(Object),
            historicoMedico: expect.any(Object),
            rendaFamiliar: expect.any(Object),
            pessoasTrabalham: expect.any(Object),
            numPessoasLar: expect.any(Object),
            beneficioSocial: expect.any(Object),
            beneficios: expect.any(Object),
            tipoResidencia: expect.any(Object),
            tipoResidenciaDesc: expect.any(Object),
            situacaoResidencia: expect.any(Object),
            situacaoResidenciaDesc: expect.any(Object),
            contatoEmergencia: expect.any(Object),
            foneEmergencia: expect.any(Object),
            relacaoEmergencia: expect.any(Object),
            autorizacao: expect.any(Object),
            fotoAluno: expect.any(Object),
            fotoMae: expect.any(Object),
          }),
        );
      });

      it('passing ICadastroAluno should create a new form with FormGroup', () => {
        const formGroup = service.createCadastroAlunoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataCadastro: expect.any(Object),
            matricula: expect.any(Object),
            grupo: expect.any(Object),
            nome: expect.any(Object),
            dn: expect.any(Object),
            cep: expect.any(Object),
            endereco: expect.any(Object),
            qd: expect.any(Object),
            lote: expect.any(Object),
            endnumero: expect.any(Object),
            bairro: expect.any(Object),
            municipio: expect.any(Object),
            uf: expect.any(Object),
            fone: expect.any(Object),
            certidao: expect.any(Object),
            termo: expect.any(Object),
            cartorio: expect.any(Object),
            naturalidade: expect.any(Object),
            rg: expect.any(Object),
            cpf: expect.any(Object),
            nis: expect.any(Object),
            cras: expect.any(Object),
            filiacaoPai: expect.any(Object),
            paiTelefone: expect.any(Object),
            paiNaturalidade: expect.any(Object),
            paiUf: expect.any(Object),
            paiRg: expect.any(Object),
            paiDataNascimento: expect.any(Object),
            paiCpf: expect.any(Object),
            paiNis: expect.any(Object),
            paiTituloEleitor: expect.any(Object),
            paiZona: expect.any(Object),
            paiSecao: expect.any(Object),
            paiMunicipio: expect.any(Object),
            filiacaoMae: expect.any(Object),
            maeTelefone: expect.any(Object),
            maeNaturalidade: expect.any(Object),
            maeUf: expect.any(Object),
            maeRg: expect.any(Object),
            maeDataNascimento: expect.any(Object),
            maeCpf: expect.any(Object),
            maeNis: expect.any(Object),
            maeTituloEleitor: expect.any(Object),
            maeZona: expect.any(Object),
            maeSecao: expect.any(Object),
            maeMunicipio: expect.any(Object),
            nomeEscola: expect.any(Object),
            anoCursando: expect.any(Object),
            turno: expect.any(Object),
            mediaEscolar: expect.any(Object),
            prioritario: expect.any(Object),
            obs: expect.any(Object),
            comportamentoCasa: expect.any(Object),
            comportamentoEscola: expect.any(Object),
            deficiencia: expect.any(Object),
            adaptacoes: expect.any(Object),
            medicacao: expect.any(Object),
            medicacaoDesc: expect.any(Object),
            alergia: expect.any(Object),
            alergiaDesc: expect.any(Object),
            historicoMedico: expect.any(Object),
            rendaFamiliar: expect.any(Object),
            pessoasTrabalham: expect.any(Object),
            numPessoasLar: expect.any(Object),
            beneficioSocial: expect.any(Object),
            beneficios: expect.any(Object),
            tipoResidencia: expect.any(Object),
            tipoResidenciaDesc: expect.any(Object),
            situacaoResidencia: expect.any(Object),
            situacaoResidenciaDesc: expect.any(Object),
            contatoEmergencia: expect.any(Object),
            foneEmergencia: expect.any(Object),
            relacaoEmergencia: expect.any(Object),
            autorizacao: expect.any(Object),
            fotoAluno: expect.any(Object),
            fotoMae: expect.any(Object),
          }),
        );
      });
    });

    describe('getCadastroAluno', () => {
      it('should return NewCadastroAluno for default CadastroAluno initial value', () => {
        const formGroup = service.createCadastroAlunoFormGroup(sampleWithNewData);

        const cadastroAluno = service.getCadastroAluno(formGroup) as any;

        expect(cadastroAluno).toMatchObject(sampleWithNewData);
      });

      it('should return NewCadastroAluno for empty CadastroAluno initial value', () => {
        const formGroup = service.createCadastroAlunoFormGroup();

        const cadastroAluno = service.getCadastroAluno(formGroup) as any;

        expect(cadastroAluno).toMatchObject({});
      });

      it('should return ICadastroAluno', () => {
        const formGroup = service.createCadastroAlunoFormGroup(sampleWithRequiredData);

        const cadastroAluno = service.getCadastroAluno(formGroup) as any;

        expect(cadastroAluno).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICadastroAluno should not enable id FormControl', () => {
        const formGroup = service.createCadastroAlunoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCadastroAluno should disable id FormControl', () => {
        const formGroup = service.createCadastroAlunoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
