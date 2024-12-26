import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ICadastroAluno } from '../cadastro-aluno.model';

@Component({
  selector: 'jhi-cadastro-aluno-detail',
  templateUrl: './cadastro-aluno-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class CadastroAlunoDetailComponent {
  cadastroAluno = input<ICadastroAluno | null>(null);

  previousState(): void {
    window.history.back();
  }
}
