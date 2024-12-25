import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ICadastroAluno, CadastroAluno } from './cadastro-aluno.model';
import { CadastroAlunoService } from './service/cadastro-aluno.service';
import { CadastroAlunoComponent } from '../../cadastro-aluno/cadastro-aluno/cadastro-aluno.component'; // Importe o seu componente
import { CadastroAlunoDetailComponent } from './detail/cadastro-aluno-detail.component';
import { CadastroAlunoUpdateComponent } from './update/cadastro-aluno-update.component';
import CadastroAlunoResolve from './route/cadastro-aluno-routing-resolve.service';

const cadastroAlunoRoute: Routes = [
  {
    path: '',
    component: CadastroAlunoComponent,
    data: {
      authorities: [Authority.USER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CadastroAlunoDetailComponent,
    resolve: {
      cadastroAluno: CadastroAlunoResolve,
    },
    data: {
      authorities: [Authority.USER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CadastroAlunoUpdateComponent,
    resolve: {
      cadastroAluno: CadastroAlunoResolve,
    },
    data: {
      authorities: [Authority.USER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CadastroAlunoUpdateComponent,
    resolve: {
      cadastroAluno: CadastroAlunoResolve,
    },
    data: {
      authorities: [Authority.USER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'cadastro-aluno/new', // Nova rota para o seu componente
    component: CadastroAlunoComponent, // Seu componente de formulário
    data: {
      authorities: [Authority.USER], // Defina as permissões necessárias
      pageTitle: 'cadastroaluno1App.cadastroAluno.home.title', // Defina o título da página (para i18n)
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cadastroAlunoRoute;
