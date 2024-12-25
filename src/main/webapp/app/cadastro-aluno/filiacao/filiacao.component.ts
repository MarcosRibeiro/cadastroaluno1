import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-filiacao',
  templateUrl: './filiacao.component.html',
  styleUrls: ['./filiacao.component.css'],
})
export class FiliacaoComponent {
  @Input() form!: FormGroup;
}
