import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-informacoes-complementares',
  templateUrl: './informacoes-complementares.component.html',
  styleUrls: ['./informacoes-complementares.component.css'],
})
export class InformacoesComplementaresComponent {
  @Input() form!: FormGroup;
}
