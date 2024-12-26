import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CadastroAlunoResolve from './route/cadastro-aluno-routing-resolve.service';

const cadastroAlunoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cadastro-aluno.component').then(m => m.CadastroAlunoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cadastro-aluno-detail.component').then(m => m.CadastroAlunoDetailComponent),
    resolve: {
      cadastroAluno: CadastroAlunoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cadastro-aluno-update.component').then(m => m.CadastroAlunoUpdateComponent),
    resolve: {
      cadastroAluno: CadastroAlunoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cadastro-aluno-update.component').then(m => m.CadastroAlunoUpdateComponent),
    resolve: {
      cadastroAluno: CadastroAlunoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cadastroAlunoRoute;
