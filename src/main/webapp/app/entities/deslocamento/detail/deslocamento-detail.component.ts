import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDeslocamento } from '../deslocamento.model';

@Component({
  selector: 'jhi-deslocamento-detail',
  templateUrl: './deslocamento-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DeslocamentoDetailComponent {
  deslocamento = input<IDeslocamento | null>(null);

  previousState(): void {
    window.history.back();
  }
}
