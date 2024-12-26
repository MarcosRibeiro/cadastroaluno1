import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICadastroAluno, NewCadastroAluno } from '../cadastro-aluno.model';

export type PartialUpdateCadastroAluno = Partial<ICadastroAluno> & Pick<ICadastroAluno, 'id'>;

type RestOf<T extends ICadastroAluno | NewCadastroAluno> = Omit<T, 'dataCadastro' | 'dn' | 'paiDataNascimento' | 'maeDataNascimento'> & {
  dataCadastro?: string | null;
  dn?: string | null;
  paiDataNascimento?: string | null;
  maeDataNascimento?: string | null;
};

export type RestCadastroAluno = RestOf<ICadastroAluno>;

export type NewRestCadastroAluno = RestOf<NewCadastroAluno>;

export type PartialUpdateRestCadastroAluno = RestOf<PartialUpdateCadastroAluno>;

export type EntityResponseType = HttpResponse<ICadastroAluno>;
export type EntityArrayResponseType = HttpResponse<ICadastroAluno[]>;

@Injectable({ providedIn: 'root' })
export class CadastroAlunoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cadastro-alunos');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(cadastroAluno: NewCadastroAluno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cadastroAluno);
    return this.http
      .post<RestCadastroAluno>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cadastroAluno: ICadastroAluno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cadastroAluno);
    return this.http
      .put<RestCadastroAluno>(`${this.resourceUrl}/${this.getCadastroAlunoIdentifier(cadastroAluno)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cadastroAluno: PartialUpdateCadastroAluno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cadastroAluno);
    return this.http
      .patch<RestCadastroAluno>(`${this.resourceUrl}/${this.getCadastroAlunoIdentifier(cadastroAluno)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCadastroAluno>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCadastroAluno[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCadastroAlunoIdentifier(cadastroAluno: Pick<ICadastroAluno, 'id'>): number | undefined {
    return cadastroAluno.id;
  }

  compareCadastroAluno(o1: Pick<ICadastroAluno, 'id'> | null, o2: Pick<ICadastroAluno, 'id'> | null): boolean {
    return o1 && o2 ? this.getCadastroAlunoIdentifier(o1) === this.getCadastroAlunoIdentifier(o2) : o1 === o2;
  }

  addCadastroAlunoToCollectionIfMissing<Type extends Pick<ICadastroAluno, 'id'>>(
    cadastroAlunoCollection: Type[],
    ...cadastroAlunosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cadastroAlunos: Type[] = cadastroAlunosToCheck.filter(isPresent);
    if (cadastroAlunos.length > 0) {
      const cadastroAlunoCollectionIdentifiers = cadastroAlunoCollection.map(
        cadastroAlunoItem => this.getCadastroAlunoIdentifier(cadastroAlunoItem)!,
      );
      const cadastroAlunosToAdd = cadastroAlunos.filter(cadastroAlunoItem => {
        const cadastroAlunoIdentifier = this.getCadastroAlunoIdentifier(cadastroAlunoItem);
        if (cadastroAlunoCollectionIdentifiers.includes(cadastroAlunoIdentifier)) {
          return false;
        }
        cadastroAlunoCollectionIdentifiers.push(cadastroAlunoIdentifier);
        return true;
      });
      return [...cadastroAlunosToAdd, ...cadastroAlunoCollection];
    }
    return cadastroAlunoCollection;
  }

  protected convertDateFromClient<T extends ICadastroAluno | NewCadastroAluno | PartialUpdateCadastroAluno>(cadastroAluno: T): RestOf<T> {
    return {
      ...cadastroAluno,
      dataCadastro: cadastroAluno.dataCadastro?.format(DATE_FORMAT) ?? null,
      dn: cadastroAluno.dn?.format(DATE_FORMAT) ?? null,
      paiDataNascimento: cadastroAluno.paiDataNascimento?.format(DATE_FORMAT) ?? null,
      maeDataNascimento: cadastroAluno.maeDataNascimento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCadastroAluno: RestCadastroAluno): ICadastroAluno {
    return {
      ...restCadastroAluno,
      dataCadastro: restCadastroAluno.dataCadastro ? dayjs(restCadastroAluno.dataCadastro) : undefined,
      dn: restCadastroAluno.dn ? dayjs(restCadastroAluno.dn) : undefined,
      paiDataNascimento: restCadastroAluno.paiDataNascimento ? dayjs(restCadastroAluno.paiDataNascimento) : undefined,
      maeDataNascimento: restCadastroAluno.maeDataNascimento ? dayjs(restCadastroAluno.maeDataNascimento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCadastroAluno>): HttpResponse<ICadastroAluno> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCadastroAluno[]>): HttpResponse<ICadastroAluno[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
