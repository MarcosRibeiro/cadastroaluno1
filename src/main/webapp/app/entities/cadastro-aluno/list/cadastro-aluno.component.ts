import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ICadastroAluno } from '../cadastro-aluno.model';
import { CadastroAlunoService, EntityArrayResponseType } from '../service/cadastro-aluno.service';
import { CadastroAlunoDeleteDialogComponent } from '../delete/cadastro-aluno-delete-dialog.component';

@Component({
  selector: 'jhi-cadastro-aluno',
  templateUrl: './cadastro-aluno.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FormatMediumDatePipe, ItemCountComponent],
})
export class CadastroAlunoComponent implements OnInit {
  private static readonly NOT_SORTABLE_FIELDS_AFTER_SEARCH = [
    'matricula',
    'grupo',
    'nome',
    'cep',
    'endereco',
    'qd',
    'lote',
    'endnumero',
    'bairro',
    'municipio',
    'uf',
    'fone',
    'certidao',
    'termo',
    'cartorio',
    'naturalidade',
    'rg',
    'cpf',
    'nis',
    'cras',
    'filiacaoPai',
    'paiTelefone',
    'paiNaturalidade',
    'paiUf',
    'paiRg',
    'paiCpf',
    'paiNis',
    'paiTituloEleitor',
    'paiZona',
    'paiSecao',
    'paiMunicipio',
    'filiacaoMae',
    'maeTelefone',
    'maeNaturalidade',
    'maeUf',
    'maeRg',
    'maeCpf',
    'maeNis',
    'maeTituloEleitor',
    'maeZona',
    'maeSecao',
    'maeMunicipio',
    'nomeEscola',
    'anoCursando',
    'turno',
    'prioritario',
    'obs',
    'comportamentoCasa',
    'comportamentoEscola',
    'deficiencia',
    'adaptacoes',
    'medicacao',
    'medicacaoDesc',
    'alergia',
    'alergiaDesc',
    'historicoMedico',
    'rendaFamiliar',
    'beneficioSocial',
    'beneficios',
    'tipoResidencia',
    'tipoResidenciaDesc',
    'situacaoResidencia',
    'situacaoResidenciaDesc',
    'contatoEmergencia',
    'foneEmergencia',
    'relacaoEmergencia',
    'fotoAluno',
    'fotoMae',
  ];

  subscription: Subscription | null = null;
  cadastroAlunos = signal<ICadastroAluno[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  currentSearch = '';

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly cadastroAlunoService = inject(CadastroAlunoService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: ICadastroAluno): number => this.cadastroAlunoService.getCadastroAlunoIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  search(query: string): void {
    this.page = 1;
    this.currentSearch = query;
    const { predicate } = this.sortState();
    if (query && predicate && CadastroAlunoComponent.NOT_SORTABLE_FIELDS_AFTER_SEARCH.includes(predicate)) {
      this.navigateToWithComponentValues(this.getDefaultSortState());
      return;
    }
    this.navigateToWithComponentValues(this.sortState());
  }

  getDefaultSortState(): SortState {
    return this.sortService.parseSortParam(this.activatedRoute.snapshot.data[DEFAULT_SORT_DATA]);
  }

  delete(cadastroAluno: ICadastroAluno): void {
    const modalRef = this.modalService.open(CadastroAlunoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cadastroAluno = cadastroAluno;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event, this.currentSearch);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.currentSearch);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    if (params.has('search') && params.get('search') !== '') {
      this.currentSearch = params.get('search') as string;
      const { predicate } = this.sortState();
      if (predicate && CadastroAlunoComponent.NOT_SORTABLE_FIELDS_AFTER_SEARCH.includes(predicate)) {
        this.sortState.set({});
      }
    }
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.cadastroAlunos.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: ICadastroAluno[] | null): ICadastroAluno[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page, currentSearch } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      query: currentSearch,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    if (this.currentSearch && this.currentSearch !== '') {
      return this.cadastroAlunoService.search(queryObject).pipe(tap(() => (this.isLoading = false)));
    }
    return this.cadastroAlunoService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState, currentSearch?: string): void {
    const queryParamsObj = {
      search: currentSearch,
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
