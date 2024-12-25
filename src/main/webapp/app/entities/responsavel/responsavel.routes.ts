import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ResponsavelResolve from './route/responsavel-routing-resolve.service';

const responsavelRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/responsavel.component').then(m => m.ResponsavelComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/responsavel-detail.component').then(m => m.ResponsavelDetailComponent),
    resolve: {
      responsavel: ResponsavelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/responsavel-update.component').then(m => m.ResponsavelUpdateComponent),
    resolve: {
      responsavel: ResponsavelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/responsavel-update.component').then(m => m.ResponsavelUpdateComponent),
    resolve: {
      responsavel: ResponsavelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default responsavelRoute;
