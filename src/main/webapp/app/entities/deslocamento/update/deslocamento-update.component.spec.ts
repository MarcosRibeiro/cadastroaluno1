import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICadastroAluno } from 'app/entities/cadastro-aluno/cadastro-aluno.model';
import { CadastroAlunoService } from 'app/entities/cadastro-aluno/service/cadastro-aluno.service';
import { DeslocamentoService } from '../service/deslocamento.service';
import { IDeslocamento } from '../deslocamento.model';
import { DeslocamentoFormService } from './deslocamento-form.service';

import { DeslocamentoUpdateComponent } from './deslocamento-update.component';

describe('Deslocamento Management Update Component', () => {
  let comp: DeslocamentoUpdateComponent;
  let fixture: ComponentFixture<DeslocamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deslocamentoFormService: DeslocamentoFormService;
  let deslocamentoService: DeslocamentoService;
  let cadastroAlunoService: CadastroAlunoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DeslocamentoUpdateComponent],
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
      .overrideTemplate(DeslocamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeslocamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deslocamentoFormService = TestBed.inject(DeslocamentoFormService);
    deslocamentoService = TestBed.inject(DeslocamentoService);
    cadastroAlunoService = TestBed.inject(CadastroAlunoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CadastroAluno query and add missing value', () => {
      const deslocamento: IDeslocamento = { id: 3412 };
      const cadastroAluno: ICadastroAluno = { id: 20387 };
      deslocamento.cadastroAluno = cadastroAluno;

      const cadastroAlunoCollection: ICadastroAluno[] = [{ id: 20387 }];
      jest.spyOn(cadastroAlunoService, 'query').mockReturnValue(of(new HttpResponse({ body: cadastroAlunoCollection })));
      const additionalCadastroAlunos = [cadastroAluno];
      const expectedCollection: ICadastroAluno[] = [...additionalCadastroAlunos, ...cadastroAlunoCollection];
      jest.spyOn(cadastroAlunoService, 'addCadastroAlunoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deslocamento });
      comp.ngOnInit();

      expect(cadastroAlunoService.query).toHaveBeenCalled();
      expect(cadastroAlunoService.addCadastroAlunoToCollectionIfMissing).toHaveBeenCalledWith(
        cadastroAlunoCollection,
        ...additionalCadastroAlunos.map(expect.objectContaining),
      );
      expect(comp.cadastroAlunosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deslocamento: IDeslocamento = { id: 3412 };
      const cadastroAluno: ICadastroAluno = { id: 20387 };
      deslocamento.cadastroAluno = cadastroAluno;

      activatedRoute.data = of({ deslocamento });
      comp.ngOnInit();

      expect(comp.cadastroAlunosSharedCollection).toContainEqual(cadastroAluno);
      expect(comp.deslocamento).toEqual(deslocamento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeslocamento>>();
      const deslocamento = { id: 24932 };
      jest.spyOn(deslocamentoFormService, 'getDeslocamento').mockReturnValue(deslocamento);
      jest.spyOn(deslocamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deslocamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deslocamento }));
      saveSubject.complete();

      // THEN
      expect(deslocamentoFormService.getDeslocamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deslocamentoService.update).toHaveBeenCalledWith(expect.objectContaining(deslocamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeslocamento>>();
      const deslocamento = { id: 24932 };
      jest.spyOn(deslocamentoFormService, 'getDeslocamento').mockReturnValue({ id: null });
      jest.spyOn(deslocamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deslocamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deslocamento }));
      saveSubject.complete();

      // THEN
      expect(deslocamentoFormService.getDeslocamento).toHaveBeenCalled();
      expect(deslocamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeslocamento>>();
      const deslocamento = { id: 24932 };
      jest.spyOn(deslocamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deslocamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deslocamentoService.update).toHaveBeenCalled();
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
