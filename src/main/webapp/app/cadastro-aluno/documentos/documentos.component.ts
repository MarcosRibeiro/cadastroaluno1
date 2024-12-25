import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-documentos',
  templateUrl: './documentos.component.html',
  styleUrls: ['./documentos.component.css'],
})
export class DocumentosComponent {
  @Input() form!: FormGroup;
}
