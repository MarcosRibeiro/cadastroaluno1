import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-contato-emergencia',
  templateUrl: './contato-emergencia.component.html',
  styleUrls: ['./contato-emergencia.component.css'],
})
export class ContatoEmergenciaComponent {
  @Input() form!: FormGroup<any>;
}
