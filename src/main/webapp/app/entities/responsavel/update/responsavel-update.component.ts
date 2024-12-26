import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SharedModule } from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICadastroAluno } from 'app/entities/cadastro-aluno/cadastro-aluno.model';
import { CadastroAlunoService } from 'app/entities/cadastro-aluno/service/cadastro-aluno.service';
import { IResponsavel } from '../responsavel.model';
import { ResponsavelService } from '../service/responsavel.service';
import { ResponsavelFormGroup, ResponsavelFormService } from './responsavel-form.service';

@Component({
  selector: 'jhi-responsavel-update',
  templateUrl: './responsavel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResponsavelUpdateComponent implements OnInit {
  isSaving = false;
  responsavel: IResponsavel | null = null;

  cadastroAlunosSharedCollection: ICadastroAluno[] = [];

  protected responsavelService = inject(ResponsavelService);
  protected responsavelFormService = inject(ResponsavelFormService);
  protected cadastroAlunoService = inject(CadastroAlunoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResponsavelFormGroup = this.responsavelFormService.createResponsavelFormGroup();

  compareCadastroAluno = (o1: ICadastroAluno | null, o2: ICadastroAluno | null): boolean =>
    this.cadastroAlunoService.compareCadastroAluno(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ responsavel }) => {
      this.responsavel = responsavel;
      if (responsavel) {
        this.updateForm(responsavel);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const responsavel = this.responsavelFormService.getResponsavel(this.editForm);
    if (responsavel.id !== null) {
      this.subscribeToSaveResponse(this.responsavelService.update(responsavel));
    } else {
      this.subscribeToSaveResponse(this.responsavelService.create(responsavel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResponsavel>>): void {
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

  protected updateForm(responsavel: IResponsavel): void {
    this.responsavel = responsavel;
    this.responsavelFormService.resetForm(this.editForm, responsavel);

    this.cadastroAlunosSharedCollection = this.cadastroAlunoService.addCadastroAlunoToCollectionIfMissing<ICadastroAluno>(
      this.cadastroAlunosSharedCollection,
      responsavel.cadastroAluno,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cadastroAlunoService
      .query()
      .pipe(map((res: HttpResponse<ICadastroAluno[]>) => res.body ?? []))
      .pipe(
        map((cadastroAlunos: ICadastroAluno[]) =>
          this.cadastroAlunoService.addCadastroAlunoToCollectionIfMissing<ICadastroAluno>(cadastroAlunos, this.responsavel?.cadastroAluno),
        ),
      )
      .subscribe((cadastroAlunos: ICadastroAluno[]) => (this.cadastroAlunosSharedCollection = cadastroAlunos));
  }
}
