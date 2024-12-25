import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CepService {
  constructor(private http: HttpClient) {}

  buscar(cep: string): Observable<any> {
    // Remove caracteres não numéricos
    cep = cep.replace(/\D/g, '');

    // Verifica se o CEP tem o tamanho correto
    if (cep.length !== 8) {
      return of({});
    }

    // Expressão regular para validar o CEP
    const validacep = /^[0-9]{8}$/;

    // Valida o formato do CEP
    if (validacep.test(cep)) {
      return this.http.get(`//viacep.com.br/ws/${cep}/json`).pipe(
        map(dados => {
          if (!('erro' in dados)) {
            return dados;
          } else {
            // CEP pesquisado não foi encontrado
            return {};
          }
        }),
      );
    } else {
      // CEP inválido
      return of({});
    }
  }
}
