import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-dados-aluno',
  templateUrl: './dados-aluno.component.html',
  styleUrls: ['./dados-aluno.component.css'],
})
export class DadosAlunoComponent {
  @Input() form!: FormGroup;
}
