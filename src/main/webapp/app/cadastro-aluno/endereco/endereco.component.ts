import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { CepService } from '../cep.service';

@Component({
  selector: 'jhi-endereco',
  templateUrl: './endereco.component.html',
  styleUrls: ['./endereco.component.css'],
})
export class EnderecoComponent {
  @Input() form!: FormGroup;

  constructor(private cepService: CepService) {}

  buscarCep(): void {
    const cep = this.form.get('cep')?.value;
    if (cep && cep.length === 9) {
      this.cepService.buscar(cep).subscribe(dados => {
        this.form.patchValue({
          endereco: dados.logradouro,
          bairro: dados.bairro,
          municipio: dados.localidade,
          uf: dados.uf,
        });
      });
    }
  }
}
