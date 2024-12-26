import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'cadastroalunoApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'cadastro-aluno',
    data: { pageTitle: 'cadastroalunoApp.cadastroAluno.home.title' },
    loadChildren: () => import('./cadastro-aluno/cadastro-aluno.routes'),
  },
  {
    path: 'deslocamento',
    data: { pageTitle: 'cadastroalunoApp.deslocamento.home.title' },
    loadChildren: () => import('./deslocamento/deslocamento.routes'),
  },
  {
    path: 'responsavel',
    data: { pageTitle: 'cadastroalunoApp.responsavel.home.title' },
    loadChildren: () => import('./responsavel/responsavel.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
