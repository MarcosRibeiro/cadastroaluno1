import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-informacoes-complementares',
  templateUrl: './informacoes-complementares.component.html',
  styleUrls: ['./informacoes-complementares.component.css'],
})
export class InformacoesComplementaresComponent implements OnInit {
  @Input() form!: FormGroup;

  constructor() {}

  ngOnInit(): void {}
}
