import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-dados-aluno',
  templateUrl: './dados-aluno.component.html',
  styleUrls: ['./dados-aluno.component.css'],
})
export class DadosAlunoComponent implements OnInit {
  @Input() form!: FormGroup;

  constructor() {}

  ngOnInit(): void {}
}
