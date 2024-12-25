import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'cadastroaluno1App.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'cadastro-aluno',
    data: { pageTitle: 'cadastroaluno1App.cadastroAluno.home.title' },
    loadChildren: () => import('./cadastro-aluno/cadastro-aluno.routes'),
  },
  {
    path: 'deslocamento',
    data: { pageTitle: 'cadastroaluno1App.deslocamento.home.title' },
    loadChildren: () => import('./deslocamento/deslocamento.routes'),
  },
  {
    path: 'responsavel',
    data: { pageTitle: 'cadastroaluno1App.responsavel.home.title' },
    loadChildren: () => import('./responsavel/responsavel.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
