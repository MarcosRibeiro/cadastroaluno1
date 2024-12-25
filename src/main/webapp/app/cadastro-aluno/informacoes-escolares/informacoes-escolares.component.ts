import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-informacoes-escolares',
  templateUrl: './informacoes-escolares.component.html',
  styleUrls: ['./informacoes-escolares.component.css'],
})
export class InformacoesEscolaresComponent implements OnInit {
  @Input() form!: FormGroup;

  constructor() {}

  ngOnInit(): void {}
}
