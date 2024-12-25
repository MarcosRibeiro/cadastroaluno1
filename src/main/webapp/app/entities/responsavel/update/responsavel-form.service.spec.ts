import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../responsavel.test-samples';

import { ResponsavelFormService } from './responsavel-form.service';

describe('Responsavel Form Service', () => {
  let service: ResponsavelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResponsavelFormService);
  });

  describe('Service methods', () => {
    describe('createResponsavelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResponsavelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            parentesco: expect.any(Object),
            cadastroAluno: expect.any(Object),
          }),
        );
      });

      it('passing IResponsavel should create a new form with FormGroup', () => {
        const formGroup = service.createResponsavelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            parentesco: expect.any(Object),
            cadastroAluno: expect.any(Object),
          }),
        );
      });
    });

    describe('getResponsavel', () => {
      it('should return NewResponsavel for default Responsavel initial value', () => {
        const formGroup = service.createResponsavelFormGroup(sampleWithNewData);

        const responsavel = service.getResponsavel(formGroup) as any;

        expect(responsavel).toMatchObject(sampleWithNewData);
      });

      it('should return NewResponsavel for empty Responsavel initial value', () => {
        const formGroup = service.createResponsavelFormGroup();

        const responsavel = service.getResponsavel(formGroup) as any;

        expect(responsavel).toMatchObject({});
      });

      it('should return IResponsavel', () => {
        const formGroup = service.createResponsavelFormGroup(sampleWithRequiredData);

        const responsavel = service.getResponsavel(formGroup) as any;

        expect(responsavel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResponsavel should not enable id FormControl', () => {
        const formGroup = service.createResponsavelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResponsavel should disable id FormControl', () => {
        const formGroup = service.createResponsavelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
