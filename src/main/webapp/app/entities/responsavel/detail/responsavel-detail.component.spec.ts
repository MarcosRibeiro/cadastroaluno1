import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ResponsavelDetailComponent } from './responsavel-detail.component';

describe('Responsavel Management Detail Component', () => {
  let comp: ResponsavelDetailComponent;
  let fixture: ComponentFixture<ResponsavelDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponsavelDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./responsavel-detail.component').then(m => m.ResponsavelDetailComponent),
              resolve: { responsavel: () => of({ id: 29089 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ResponsavelDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResponsavelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load responsavel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ResponsavelDetailComponent);

      // THEN
      expect(instance.responsavel()).toEqual(expect.objectContaining({ id: 29089 }));
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
