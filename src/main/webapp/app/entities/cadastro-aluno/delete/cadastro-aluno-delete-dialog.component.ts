import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SharedModule } from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICadastroAluno } from '../cadastro-aluno.model';
import { CadastroAlunoService } from '../service/cadastro-aluno.service';

@Component({
  templateUrl: './cadastro-aluno-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CadastroAlunoDeleteDialogComponent {
  cadastroAluno?: ICadastroAluno;

  protected cadastroAlunoService = inject(CadastroAlunoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cadastroAlunoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
