import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IResponsavel, NewResponsavel } from '../responsavel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResponsavel for edit and NewResponsavelFormGroupInput for create.
 */
type ResponsavelFormGroupInput = IResponsavel | PartialWithRequiredKeyOf<NewResponsavel>;

type ResponsavelFormDefaults = Pick<NewResponsavel, 'id'>;

type ResponsavelFormGroupContent = {
  id: FormControl<IResponsavel['id'] | NewResponsavel['id']>;
  nome: FormControl<IResponsavel['nome']>;
  parentesco: FormControl<IResponsavel['parentesco']>;
  cadastroAluno: FormControl<IResponsavel['cadastroAluno']>;
};

export type ResponsavelFormGroup = FormGroup<ResponsavelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResponsavelFormService {
  createResponsavelFormGroup(responsavel: ResponsavelFormGroupInput = { id: null }): ResponsavelFormGroup {
    const responsavelRawValue = {
      ...this.getFormDefaults(),
      ...responsavel,
    };
    return new FormGroup<ResponsavelFormGroupContent>({
      id: new FormControl(
        { value: responsavelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(responsavelRawValue.nome, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      parentesco: new FormControl(responsavelRawValue.parentesco, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      cadastroAluno: new FormControl(responsavelRawValue.cadastroAluno),
    });
  }

  getResponsavel(form: ResponsavelFormGroup): IResponsavel | NewResponsavel {
    return form.getRawValue() as IResponsavel | NewResponsavel;
  }

  resetForm(form: ResponsavelFormGroup, responsavel: ResponsavelFormGroupInput): void {
    const responsavelRawValue = { ...this.getFormDefaults(), ...responsavel };
    form.reset(
      {
        ...responsavelRawValue,
        id: { value: responsavelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResponsavelFormDefaults {
    return {
      id: null,
    };
  }
}
