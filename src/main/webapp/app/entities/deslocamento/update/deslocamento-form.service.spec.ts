import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../deslocamento.test-samples';

import { DeslocamentoFormService } from './deslocamento-form.service';

describe('Deslocamento Form Service', () => {
  let service: DeslocamentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeslocamentoFormService);
  });

  describe('Service methods', () => {
    describe('createDeslocamentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeslocamentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            grau: expect.any(Object),
            cadastroAluno: expect.any(Object),
          }),
        );
      });

      it('passing IDeslocamento should create a new form with FormGroup', () => {
        const formGroup = service.createDeslocamentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            grau: expect.any(Object),
            cadastroAluno: expect.any(Object),
          }),
        );
      });
    });

    describe('getDeslocamento', () => {
      it('should return NewDeslocamento for default Deslocamento initial value', () => {
        const formGroup = service.createDeslocamentoFormGroup(sampleWithNewData);

        const deslocamento = service.getDeslocamento(formGroup) as any;

        expect(deslocamento).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeslocamento for empty Deslocamento initial value', () => {
        const formGroup = service.createDeslocamentoFormGroup();

        const deslocamento = service.getDeslocamento(formGroup) as any;

        expect(deslocamento).toMatchObject({});
      });

      it('should return IDeslocamento', () => {
        const formGroup = service.createDeslocamentoFormGroup(sampleWithRequiredData);

        const deslocamento = service.getDeslocamento(formGroup) as any;

        expect(deslocamento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeslocamento should not enable id FormControl', () => {
        const formGroup = service.createDeslocamentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeslocamento should disable id FormControl', () => {
        const formGroup = service.createDeslocamentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
