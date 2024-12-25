import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-filiacao',
  templateUrl: './filiacao.component.html',
  styleUrls: ['./filiacao.component.css'],
})
export class FiliacaoComponent implements OnInit {
  @Input() form!: FormGroup;

  constructor() {}

  ngOnInit(): void {}
}
