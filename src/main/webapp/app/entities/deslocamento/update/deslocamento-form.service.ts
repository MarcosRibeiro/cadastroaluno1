import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDeslocamento, NewDeslocamento } from '../deslocamento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeslocamento for edit and NewDeslocamentoFormGroupInput for create.
 */
type DeslocamentoFormGroupInput = IDeslocamento | PartialWithRequiredKeyOf<NewDeslocamento>;

type DeslocamentoFormDefaults = Pick<NewDeslocamento, 'id'>;

type DeslocamentoFormGroupContent = {
  id: FormControl<IDeslocamento['id'] | NewDeslocamento['id']>;
  nome: FormControl<IDeslocamento['nome']>;
  grau: FormControl<IDeslocamento['grau']>;
  cadastroAluno: FormControl<IDeslocamento['cadastroAluno']>;
};

export type DeslocamentoFormGroup = FormGroup<DeslocamentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeslocamentoFormService {
  createDeslocamentoFormGroup(deslocamento: DeslocamentoFormGroupInput = { id: null }): DeslocamentoFormGroup {
    const deslocamentoRawValue = {
      ...this.getFormDefaults(),
      ...deslocamento,
    };
    return new FormGroup<DeslocamentoFormGroupContent>({
      id: new FormControl(
        { value: deslocamentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(deslocamentoRawValue.nome, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      grau: new FormControl(deslocamentoRawValue.grau, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      cadastroAluno: new FormControl(deslocamentoRawValue.cadastroAluno),
    });
  }

  getDeslocamento(form: DeslocamentoFormGroup): IDeslocamento | NewDeslocamento {
    return form.getRawValue() as IDeslocamento | NewDeslocamento;
  }

  resetForm(form: DeslocamentoFormGroup, deslocamento: DeslocamentoFormGroupInput): void {
    const deslocamentoRawValue = { ...this.getFormDefaults(), ...deslocamento };
    form.reset(
      {
        ...deslocamentoRawValue,
        id: { value: deslocamentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeslocamentoFormDefaults {
    return {
      id: null,
    };
  }
}
