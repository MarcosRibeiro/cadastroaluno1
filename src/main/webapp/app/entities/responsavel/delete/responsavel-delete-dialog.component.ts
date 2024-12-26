import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SharedModule } from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IResponsavel } from '../responsavel.model';
import { ResponsavelService } from '../service/responsavel.service';

@Component({
  templateUrl: './responsavel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResponsavelDeleteDialogComponent {
  responsavel?: IResponsavel;

  protected responsavelService = inject(ResponsavelService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.responsavelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
