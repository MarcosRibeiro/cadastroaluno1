import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CadastroAlunoDetailComponent } from './cadastro-aluno-detail.component';

describe('CadastroAluno Management Detail Component', () => {
  let comp: CadastroAlunoDetailComponent;
  let fixture: ComponentFixture<CadastroAlunoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastroAlunoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cadastro-aluno-detail.component').then(m => m.CadastroAlunoDetailComponent),
              resolve: { cadastroAluno: () => of({ id: 20387 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CadastroAlunoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastroAlunoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cadastroAluno on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CadastroAlunoDetailComponent);

      // THEN
      expect(instance.cadastroAluno()).toEqual(expect.objectContaining({ id: 20387 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
