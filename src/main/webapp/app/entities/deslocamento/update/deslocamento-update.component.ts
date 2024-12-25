import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICadastroAluno } from 'app/entities/cadastro-aluno/cadastro-aluno.model';
import { CadastroAlunoService } from 'app/entities/cadastro-aluno/service/cadastro-aluno.service';
import { IDeslocamento } from '../deslocamento.model';
import { DeslocamentoService } from '../service/deslocamento.service';
import { DeslocamentoFormGroup, DeslocamentoFormService } from './deslocamento-form.service';

@Component({
  selector: 'jhi-deslocamento-update',
  templateUrl: './deslocamento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DeslocamentoUpdateComponent implements OnInit {
  isSaving = false;
  deslocamento: IDeslocamento | null = null;

  cadastroAlunosSharedCollection: ICadastroAluno[] = [];

  protected deslocamentoService = inject(DeslocamentoService);
  protected deslocamentoFormService = inject(DeslocamentoFormService);
  protected cadastroAlunoService = inject(CadastroAlunoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DeslocamentoFormGroup = this.deslocamentoFormService.createDeslocamentoFormGroup();

  compareCadastroAluno = (o1: ICadastroAluno | null, o2: ICadastroAluno | null): boolean =>
    this.cadastroAlunoService.compareCadastroAluno(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deslocamento }) => {
      this.deslocamento = deslocamento;
      if (deslocamento) {
        this.updateForm(deslocamento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deslocamento = this.deslocamentoFormService.getDeslocamento(this.editForm);
    if (deslocamento.id !== null) {
      this.subscribeToSaveResponse(this.deslocamentoService.update(deslocamento));
    } else {
      this.subscribeToSaveResponse(this.deslocamentoService.create(deslocamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeslocamento>>): void {
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

  protected updateForm(deslocamento: IDeslocamento): void {
    this.deslocamento = deslocamento;
    this.deslocamentoFormService.resetForm(this.editForm, deslocamento);

    this.cadastroAlunosSharedCollection = this.cadastroAlunoService.addCadastroAlunoToCollectionIfMissing<ICadastroAluno>(
      this.cadastroAlunosSharedCollection,
      deslocamento.cadastroAluno,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cadastroAlunoService
      .query()
      .pipe(map((res: HttpResponse<ICadastroAluno[]>) => res.body ?? []))
      .pipe(
        map((cadastroAlunos: ICadastroAluno[]) =>
          this.cadastroAlunoService.addCadastroAlunoToCollectionIfMissing<ICadastroAluno>(cadastroAlunos, this.deslocamento?.cadastroAluno),
        ),
      )
      .subscribe((cadastroAlunos: ICadastroAluno[]) => (this.cadastroAlunosSharedCollection = cadastroAlunos));
  }
}
