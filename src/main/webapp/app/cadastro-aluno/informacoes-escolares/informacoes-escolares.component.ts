import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-informacoes-escolares',
  templateUrl: './informacoes-escolares.component.html',
  styleUrls: ['./informacoes-escolares.component.css'],
})
export class InformacoesEscolaresComponent {
  @Input() form!: FormGroup;
}
