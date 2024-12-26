import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SharedModule } from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDeslocamento } from '../deslocamento.model';
import { DeslocamentoService } from '../service/deslocamento.service';

@Component({
  templateUrl: './deslocamento-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DeslocamentoDeleteDialogComponent {
  deslocamento?: IDeslocamento;

  protected deslocamentoService = inject(DeslocamentoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deslocamentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
