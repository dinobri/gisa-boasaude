import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrestadorService } from '../service/prestador.service';
import { IPrestador, Prestador } from '../prestador.model';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { IEspecialidade } from 'app/entities/especialidade/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade/service/especialidade.service';

import { PrestadorUpdateComponent } from './prestador-update.component';

describe('Prestador Management Update Component', () => {
  let comp: PrestadorUpdateComponent;
  let fixture: ComponentFixture<PrestadorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prestadorService: PrestadorService;
  let enderecoService: EnderecoService;
  let especialidadeService: EspecialidadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrestadorUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PrestadorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrestadorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prestadorService = TestBed.inject(PrestadorService);
    enderecoService = TestBed.inject(EnderecoService);
    especialidadeService = TestBed.inject(EspecialidadeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call enderecoAtendimento query and add missing value', () => {
      const prestador: IPrestador = { id: 456 };
      const enderecoAtendimento: IEndereco = { id: 17406 };
      prestador.enderecoAtendimento = enderecoAtendimento;

      const enderecoAtendimentoCollection: IEndereco[] = [{ id: 21476 }];
      jest.spyOn(enderecoService, 'query').mockReturnValue(of(new HttpResponse({ body: enderecoAtendimentoCollection })));
      const expectedCollection: IEndereco[] = [enderecoAtendimento, ...enderecoAtendimentoCollection];
      jest.spyOn(enderecoService, 'addEnderecoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prestador });
      comp.ngOnInit();

      expect(enderecoService.query).toHaveBeenCalled();
      expect(enderecoService.addEnderecoToCollectionIfMissing).toHaveBeenCalledWith(enderecoAtendimentoCollection, enderecoAtendimento);
      expect(comp.enderecoAtendimentosCollection).toEqual(expectedCollection);
    });

    it('Should call Especialidade query and add missing value', () => {
      const prestador: IPrestador = { id: 456 };
      const especialidade: IEspecialidade = { id: 57770 };
      prestador.especialidade = especialidade;

      const especialidadeCollection: IEspecialidade[] = [{ id: 69851 }];
      jest.spyOn(especialidadeService, 'query').mockReturnValue(of(new HttpResponse({ body: especialidadeCollection })));
      const additionalEspecialidades = [especialidade];
      const expectedCollection: IEspecialidade[] = [...additionalEspecialidades, ...especialidadeCollection];
      jest.spyOn(especialidadeService, 'addEspecialidadeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prestador });
      comp.ngOnInit();

      expect(especialidadeService.query).toHaveBeenCalled();
      expect(especialidadeService.addEspecialidadeToCollectionIfMissing).toHaveBeenCalledWith(
        especialidadeCollection,
        ...additionalEspecialidades
      );
      expect(comp.especialidadesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prestador: IPrestador = { id: 456 };
      const enderecoAtendimento: IEndereco = { id: 63672 };
      prestador.enderecoAtendimento = enderecoAtendimento;
      const especialidade: IEspecialidade = { id: 71051 };
      prestador.especialidade = especialidade;

      activatedRoute.data = of({ prestador });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(prestador));
      expect(comp.enderecoAtendimentosCollection).toContain(enderecoAtendimento);
      expect(comp.especialidadesSharedCollection).toContain(especialidade);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Prestador>>();
      const prestador = { id: 123 };
      jest.spyOn(prestadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prestador }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(prestadorService.update).toHaveBeenCalledWith(prestador);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Prestador>>();
      const prestador = new Prestador();
      jest.spyOn(prestadorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prestador }));
      saveSubject.complete();

      // THEN
      expect(prestadorService.create).toHaveBeenCalledWith(prestador);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Prestador>>();
      const prestador = { id: 123 };
      jest.spyOn(prestadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prestadorService.update).toHaveBeenCalledWith(prestador);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEnderecoById', () => {
      it('Should return tracked Endereco primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEnderecoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEspecialidadeById', () => {
      it('Should return tracked Especialidade primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEspecialidadeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
