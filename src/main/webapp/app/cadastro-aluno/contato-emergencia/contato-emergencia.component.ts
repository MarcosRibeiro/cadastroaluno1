import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-contato-emergencia',
  templateUrl: './contato-emergencia.component.html',
  styleUrls: ['./contato-emergencia.component.css'],
})
export class ContatoEmergenciaComponent implements OnInit {
  @Input() form!: FormGroup;

  constructor() {}

  ngOnInit(): void {}
}
