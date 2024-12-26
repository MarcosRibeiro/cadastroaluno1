import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SharedModule } from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Turno } from 'app/entities/enumerations/turno.model';
import { SimNao } from 'app/entities/enumerations/sim-nao.model';
import { Comportamento } from 'app/entities/enumerations/comportamento.model';
import { TipoResidencia } from 'app/entities/enumerations/tipo-residencia.model';
import { SituacaoResidencia } from 'app/entities/enumerations/situacao-residencia.model';
import { CadastroAlunoService } from '../service/cadastro-aluno.service';
import { ICadastroAluno } from '../cadastro-aluno.model';
import { CadastroAlunoFormGroup, CadastroAlunoFormService } from './cadastro-aluno-form.service';

@Component({
  selector: 'jhi-cadastro-aluno-update',
  templateUrl: './cadastro-aluno-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CadastroAlunoUpdateComponent implements OnInit {
  isSaving = false;
  cadastroAluno: ICadastroAluno | null = null;
  turnoValues = Object.keys(Turno);
  simNaoValues = Object.keys(SimNao);
  comportamentoValues = Object.keys(Comportamento);
  tipoResidenciaValues = Object.keys(TipoResidencia);
  situacaoResidenciaValues = Object.keys(SituacaoResidencia);

  protected cadastroAlunoService = inject(CadastroAlunoService);
  protected cadastroAlunoFormService = inject(CadastroAlunoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CadastroAlunoFormGroup = this.cadastroAlunoFormService.createCadastroAlunoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cadastroAluno }) => {
      this.cadastroAluno = cadastroAluno;
      if (cadastroAluno) {
        this.updateForm(cadastroAluno);
      }
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
  }
}
