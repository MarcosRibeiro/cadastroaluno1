import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICadastroAluno } from '../cadastro-aluno.model';
import { CadastroAlunoService } from '../service/cadastro-aluno.service';

const cadastroAlunoResolve = (route: ActivatedRouteSnapshot): Observable<null | ICadastroAluno> => {
  const id = route.params.id;
  if (id) {
    return inject(CadastroAlunoService)
      .find(id)
      .pipe(
        mergeMap((cadastroAluno: HttpResponse<ICadastroAluno>) => {
          if (cadastroAluno.body) {
            return of(cadastroAluno.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cadastroAlunoResolve;
