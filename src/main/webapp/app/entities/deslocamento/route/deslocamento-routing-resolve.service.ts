import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeslocamento } from '../deslocamento.model';
import { DeslocamentoService } from '../service/deslocamento.service';

const deslocamentoResolve = (route: ActivatedRouteSnapshot): Observable<null | IDeslocamento> => {
  const id = route.params.id;
  if (id) {
    return inject(DeslocamentoService)
      .find(id)
      .pipe(
        mergeMap((deslocamento: HttpResponse<IDeslocamento>) => {
          if (deslocamento.body) {
            return of(deslocamento.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default deslocamentoResolve;
