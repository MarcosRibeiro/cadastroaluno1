import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-responsaveis',
  templateUrl: './responsaveis.component.html',
  styleUrls: ['./responsaveis.component.css'],
})
export class ResponsaveisComponent implements OnInit {
  @Input() form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {}

  get responsaveis(): FormArray {
    return this.form.get('responsaveis') as FormArray;
  }

  addResponsavel(): void {
    this.responsaveis.push(
      this.fb.group({
        nome: [null, [Validators.required, Validators.maxLength(255)]],
        parentesco: [null, [Validators.required, Validators.maxLength(50)]],
      }),
    );
  }

  removeResponsavel(index: number): void {
    this.responsaveis.removeAt(index);
  }
}
