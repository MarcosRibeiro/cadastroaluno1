import { Component, Input } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-deslocamento',
  templateUrl: './deslocamento.component.html',
  styleUrls: ['./deslocamento.component.css'],
})
export class DeslocamentoComponent {
  @Input() form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  get deslocamentos(): FormArray {
    return this.form.get('deslocamentos') as FormArray;
  }

  addDeslocamento(): void {
    this.deslocamentos.push(
      this.fb.group({
        nome: [null, [Validators.required, Validators.maxLength(255)]],
        grau: [null, [Validators.required, Validators.maxLength(50)]],
      }),
    );
  }

  removeDeslocamento(index: number): void {
    this.deslocamentos.removeAt(index);
  }
}
