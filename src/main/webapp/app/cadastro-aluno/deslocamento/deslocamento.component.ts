import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-deslocamento',
  templateUrl: './deslocamento.component.html',
  styleUrls: ['./deslocamento.component.css'],
})
export class DeslocamentoComponent implements OnInit {
  @Input() form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {}

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
