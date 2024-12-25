import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResponsavel } from '../responsavel.model';
import { ResponsavelService } from '../service/responsavel.service';

const responsavelResolve = (route: ActivatedRouteSnapshot): Observable<null | IResponsavel> => {
  const id = route.params.id;
  if (id) {
    return inject(ResponsavelService)
      .find(id)
      .pipe(
        mergeMap((responsavel: HttpResponse<IResponsavel>) => {
          if (responsavel.body) {
            return of(responsavel.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default responsavelResolve;
