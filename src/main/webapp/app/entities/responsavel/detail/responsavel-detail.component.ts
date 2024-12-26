import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { IResponsavel } from '../responsavel.model';

@Component({
  selector: 'jhi-responsavel-detail',
  templateUrl: './responsavel-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ResponsavelDetailComponent {
  responsavel = input<IResponsavel | null>(null);

  previousState(): void {
    window.history.back();
  }
}
