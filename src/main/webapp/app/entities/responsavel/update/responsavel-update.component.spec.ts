import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICadastroAluno } from 'app/entities/cadastro-aluno/cadastro-aluno.model';
import { CadastroAlunoService } from 'app/entities/cadastro-aluno/service/cadastro-aluno.service';
import { ResponsavelService } from '../service/responsavel.service';
import { IResponsavel } from '../responsavel.model';
import { ResponsavelFormService } from './responsavel-form.service';

import { ResponsavelUpdateComponent } from './responsavel-update.component';

describe('Responsavel Management Update Component', () => {
  let comp: ResponsavelUpdateComponent;
  let fixture: ComponentFixture<ResponsavelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let responsavelFormService: ResponsavelFormService;
  let responsavelService: ResponsavelService;
  let cadastroAlunoService: CadastroAlunoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ResponsavelUpdateComponent],
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
      .overrideTemplate(ResponsavelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResponsavelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    responsavelFormService = TestBed.inject(ResponsavelFormService);
    responsavelService = TestBed.inject(ResponsavelService);
    cadastroAlunoService = TestBed.inject(CadastroAlunoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CadastroAluno query and add missing value', () => {
      const responsavel: IResponsavel = { id: 25416 };
      const cadastroAluno: ICadastroAluno = { id: 20387 };
      responsavel.cadastroAluno = cadastroAluno;

      const cadastroAlunoCollection: ICadastroAluno[] = [{ id: 20387 }];
      jest.spyOn(cadastroAlunoService, 'query').mockReturnValue(of(new HttpResponse({ body: cadastroAlunoCollection })));
      const additionalCadastroAlunos = [cadastroAluno];
      const expectedCollection: ICadastroAluno[] = [...additionalCadastroAlunos, ...cadastroAlunoCollection];
      jest.spyOn(cadastroAlunoService, 'addCadastroAlunoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ responsavel });
      comp.ngOnInit();

      expect(cadastroAlunoService.query).toHaveBeenCalled();
      expect(cadastroAlunoService.addCadastroAlunoToCollectionIfMissing).toHaveBeenCalledWith(
        cadastroAlunoCollection,
        ...additionalCadastroAlunos.map(expect.objectContaining),
      );
      expect(comp.cadastroAlunosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const responsavel: IResponsavel = { id: 25416 };
      const cadastroAluno: ICadastroAluno = { id: 20387 };
      responsavel.cadastroAluno = cadastroAluno;

      activatedRoute.data = of({ responsavel });
      comp.ngOnInit();

      expect(comp.cadastroAlunosSharedCollection).toContainEqual(cadastroAluno);
      expect(comp.responsavel).toEqual(responsavel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResponsavel>>();
      const responsavel = { id: 29089 };
      jest.spyOn(responsavelFormService, 'getResponsavel').mockReturnValue(responsavel);
      jest.spyOn(responsavelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsavel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: responsavel }));
      saveSubject.complete();

      // THEN
      expect(responsavelFormService.getResponsavel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(responsavelService.update).toHaveBeenCalledWith(expect.objectContaining(responsavel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResponsavel>>();
      const responsavel = { id: 29089 };
      jest.spyOn(responsavelFormService, 'getResponsavel').mockReturnValue({ id: null });
      jest.spyOn(responsavelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsavel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: responsavel }));
      saveSubject.complete();

      // THEN
      expect(responsavelFormService.getResponsavel).toHaveBeenCalled();
      expect(responsavelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResponsavel>>();
      const responsavel = { id: 29089 };
      jest.spyOn(responsavelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsavel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(responsavelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCadastroAluno', () => {
      it('Should forward to cadastroAlunoService', () => {
        const entity = { id: 20387 };
        const entity2 = { id: 2158 };
        jest.spyOn(cadastroAlunoService, 'compareCadastroAluno');
        comp.compareCadastroAluno(entity, entity2);
        expect(cadastroAlunoService.compareCadastroAluno).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
