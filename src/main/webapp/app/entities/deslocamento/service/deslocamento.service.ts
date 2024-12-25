import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDeslocamento, NewDeslocamento } from '../deslocamento.model';

export type PartialUpdateDeslocamento = Partial<IDeslocamento> & Pick<IDeslocamento, 'id'>;

export type EntityResponseType = HttpResponse<IDeslocamento>;
export type EntityArrayResponseType = HttpResponse<IDeslocamento[]>;

@Injectable({ providedIn: 'root' })
export class DeslocamentoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/deslocamentos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/deslocamentos/_search');

  create(deslocamento: NewDeslocamento): Observable<EntityResponseType> {
    return this.http.post<IDeslocamento>(this.resourceUrl, deslocamento, { observe: 'response' });
  }

  update(deslocamento: IDeslocamento): Observable<EntityResponseType> {
    return this.http.put<IDeslocamento>(`${this.resourceUrl}/${this.getDeslocamentoIdentifier(deslocamento)}`, deslocamento, {
      observe: 'response',
    });
  }

  partialUpdate(deslocamento: PartialUpdateDeslocamento): Observable<EntityResponseType> {
    return this.http.patch<IDeslocamento>(`${this.resourceUrl}/${this.getDeslocamentoIdentifier(deslocamento)}`, deslocamento, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeslocamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeslocamento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeslocamento[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IDeslocamento[]>()], asapScheduler)));
  }

  getDeslocamentoIdentifier(deslocamento: Pick<IDeslocamento, 'id'>): number {
    return deslocamento.id;
  }

  compareDeslocamento(o1: Pick<IDeslocamento, 'id'> | null, o2: Pick<IDeslocamento, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeslocamentoIdentifier(o1) === this.getDeslocamentoIdentifier(o2) : o1 === o2;
  }

  addDeslocamentoToCollectionIfMissing<Type extends Pick<IDeslocamento, 'id'>>(
    deslocamentoCollection: Type[],
    ...deslocamentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deslocamentos: Type[] = deslocamentosToCheck.filter(isPresent);
    if (deslocamentos.length > 0) {
      const deslocamentoCollectionIdentifiers = deslocamentoCollection.map(deslocamentoItem =>
        this.getDeslocamentoIdentifier(deslocamentoItem),
      );
      const deslocamentosToAdd = deslocamentos.filter(deslocamentoItem => {
        const deslocamentoIdentifier = this.getDeslocamentoIdentifier(deslocamentoItem);
        if (deslocamentoCollectionIdentifiers.includes(deslocamentoIdentifier)) {
          return false;
        }
        deslocamentoCollectionIdentifiers.push(deslocamentoIdentifier);
        return true;
      });
      return [...deslocamentosToAdd, ...deslocamentoCollection];
    }
    return deslocamentoCollection;
  }
}
