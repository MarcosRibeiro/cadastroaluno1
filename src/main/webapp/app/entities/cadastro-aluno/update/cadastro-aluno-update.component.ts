import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CadastroAlunoFormService, CadastroAlunoFormGroup } from './cadastro-aluno-form.service';
import { ICadastroAluno } from '../cadastro-aluno.model';
import { CadastroAlunoService } from '../service/cadastro-aluno.service';
import { IResponsavel } from 'app/entities/responsavel/responsavel.model';
import { ResponsavelService } from 'app/entities/responsavel/service/responsavel.service';
import { IDeslocamento } from 'app/entities/deslocamento/deslocamento.model';
import { DeslocamentoService } from 'app/entities/deslocamento/service/deslocamento.service';
import { Turno } from 'app/entities/enumerations/turno.model';
import { SimNao } from 'app/entities/enumerations/sim-nao.model';
import { Comportamento } from 'app/entities/enumerations/comportamento.model';
import { TipoResidencia } from 'app/entities/enumerations/tipo-residencia.model';
import { SituacaoResidencia } from 'app/entities/enumerations/situacao-residencia.model';

@Component({
  selector: 'jhi-cadastro-aluno-update',
  templateUrl: './cadastro-aluno-update.component.html',
})
export class CadastroAlunoUpdateComponent implements OnInit {
  isSaving = false;
  cadastroAluno: ICadastroAluno | null = null;
  turnoValues = Object.keys(Turno);
  simNaoValues = Object.keys(SimNao);
  comportamentoValues = Object.keys(Comportamento);
  tipoResidenciaValues = Object.keys(TipoResidencia);
  situacaoResidenciaValues = Object.keys(SituacaoResidencia);

  responsavelsSharedCollection: IResponsavel[] = [];
  deslocamentosSharedCollection: IDeslocamento[] = [];

  editForm: CadastroAlunoFormGroup = this.cadastroAlunoFormService.createCadastroAlunoFormGroup();

  constructor(
    protected cadastroAlunoService: CadastroAlunoService,
    protected cadastroAlunoFormService: CadastroAlunoFormService,
    protected responsavelService: ResponsavelService,
    protected deslocamentoService: DeslocamentoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareResponsavel = (o1: IResponsavel | null, o2: IResponsavel | null): boolean => this.responsavelService.compareResponsavel(o1, o2);

  compareDeslocamento = (o1: IDeslocamento | null, o2: IDeslocamento | null): boolean =>
    this.deslocamentoService.compareDeslocamento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cadastroAluno }) => {
      this.cadastroAluno = cadastroAluno;
      if (cadastroAluno) {
        this.updateForm(cadastroAluno);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cadastroAluno = this.cadastroAlunoFormService.getCadastroAluno(this.editForm);
    if (cadastroAluno.id !== null) {
      this.subscribeToSaveResponse(this.cadastroAlunoService.update(cadastroAluno));
    } else {
      this.subscribeToSaveResponse(this.cadastroAlunoService.create(cadastroAluno));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICadastroAluno>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cadastroAluno: ICadastroAluno): void {
    this.cadastroAluno = cadastroAluno;
    this.cadastroAlunoFormService.resetForm(this.editForm, cadastroAluno);

    this.responsavelsSharedCollection = this.responsavelService.addResponsavelToCollectionIfMissing<IResponsavel>(
      this.responsavelsSharedCollection,
      ...(cadastroAluno.responsavels ?? []),
    );
    this.deslocamentosSharedCollection = this.deslocamentoService.addDeslocamentoToCollectionIfMissing<IDeslocamento>(
      this.deslocamentosSharedCollection,
      ...(cadastroAluno.deslocamentos ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.responsavelService
      .query()
      .pipe(map((res: HttpResponse<IResponsavel[]>) => res.body ?? []))
      .pipe(
        map((responsavels: IResponsavel[]) =>
          this.responsavelService.addResponsavelToCollectionIfMissing<IResponsavel>(
            responsavels,
            ...(this.cadastroAluno?.responsavels ?? []),
          ),
        ),
      )
      .subscribe((responsavels: IResponsavel[]) => (this.responsavelsSharedCollection = responsavels));

    this.deslocamentoService
      .query()
      .pipe(map((res: HttpResponse<IDeslocamento[]>) => res.body ?? []))
      .pipe(
        map((deslocamentos: IDeslocamento[]) =>
          this.deslocamentoService.addDeslocamentoToCollectionIfMissing<IDeslocamento>(
            deslocamentos,
            ...(this.cadastroAluno?.deslocamentos ?? []),
          ),
        ),
      )
      .subscribe((deslocamentos: IDeslocamento[]) => (this.deslocamentosSharedCollection = deslocamentos));
  }
}
