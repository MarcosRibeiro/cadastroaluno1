import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DeslocamentoResolve from './route/deslocamento-routing-resolve.service';

const deslocamentoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/deslocamento.component').then(m => m.DeslocamentoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/deslocamento-detail.component').then(m => m.DeslocamentoDetailComponent),
    resolve: {
      deslocamento: DeslocamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/deslocamento-update.component').then(m => m.DeslocamentoUpdateComponent),
    resolve: {
      deslocamento: DeslocamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/deslocamento-update.component').then(m => m.DeslocamentoUpdateComponent),
    resolve: {
      deslocamento: DeslocamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default deslocamentoRoute;
