import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CadastroAlunoService } from '../service/cadastro-aluno.service';
import { ICadastroAluno } from '../cadastro-aluno.model';
import { CadastroAlunoFormService } from './cadastro-aluno-form.service';

import { CadastroAlunoUpdateComponent } from './cadastro-aluno-update.component';

describe('CadastroAluno Management Update Component', () => {
  let comp: CadastroAlunoUpdateComponent;
  let fixture: ComponentFixture<CadastroAlunoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cadastroAlunoFormService: CadastroAlunoFormService;
  let cadastroAlunoService: CadastroAlunoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CadastroAlunoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CadastroAlunoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CadastroAlunoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cadastroAlunoFormService = TestBed.inject(CadastroAlunoFormService);
    cadastroAlunoService = TestBed.inject(CadastroAlunoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cadastroAluno: ICadastroAluno = { id: 2158 };

      activatedRoute.data = of({ cadastroAluno });
      comp.ngOnInit();

      expect(comp.cadastroAluno).toEqual(cadastroAluno);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICadastroAluno>>();
      const cadastroAluno = { id: 20387 };
      jest.spyOn(cadastroAlunoFormService, 'getCadastroAluno').mockReturnValue(cadastroAluno);
      jest.spyOn(cadastroAlunoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cadastroAluno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cadastroAluno }));
      saveSubject.complete();

      // THEN
      expect(cadastroAlunoFormService.getCadastroAluno).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cadastroAlunoService.update).toHaveBeenCalledWith(expect.objectContaining(cadastroAluno));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICadastroAluno>>();
      const cadastroAluno = { id: 20387 };
      jest.spyOn(cadastroAlunoFormService, 'getCadastroAluno').mockReturnValue({ id: null });
      jest.spyOn(cadastroAlunoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cadastroAluno: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cadastroAluno }));
      saveSubject.complete();

      // THEN
      expect(cadastroAlunoFormService.getCadastroAluno).toHaveBeenCalled();
      expect(cadastroAlunoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICadastroAluno>>();
      const cadastroAluno = { id: 20387 };
      jest.spyOn(cadastroAlunoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cadastroAluno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cadastroAlunoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
