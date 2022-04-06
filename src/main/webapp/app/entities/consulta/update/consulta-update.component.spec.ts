import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConsultaService } from '../service/consulta.service';
import { IConsulta, Consulta } from '../consulta.model';
import { IDisponibilidadeConsulta } from 'app/entities/disponibilidade-consulta/disponibilidade-consulta.model';
import { DisponibilidadeConsultaService } from 'app/entities/disponibilidade-consulta/service/disponibilidade-consulta.service';
import { IAssociado } from 'app/entities/associado/associado.model';
import { AssociadoService } from 'app/entities/associado/service/associado.service';

import { ConsultaUpdateComponent } from './consulta-update.component';

describe('Consulta Management Update Component', () => {
  let comp: ConsultaUpdateComponent;
  let fixture: ComponentFixture<ConsultaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let consultaService: ConsultaService;
  let disponibilidadeConsultaService: DisponibilidadeConsultaService;
  let associadoService: AssociadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConsultaUpdateComponent],
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
      .overrideTemplate(ConsultaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsultaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consultaService = TestBed.inject(ConsultaService);
    disponibilidadeConsultaService = TestBed.inject(DisponibilidadeConsultaService);
    associadoService = TestBed.inject(AssociadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call disponibilidadeConsulta query and add missing value', () => {
      const consulta: IConsulta = { id: 456 };
      const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 64158 };
      consulta.disponibilidadeConsulta = disponibilidadeConsulta;

      const disponibilidadeConsultaCollection: IDisponibilidadeConsulta[] = [{ id: 29817 }];
      jest
        .spyOn(disponibilidadeConsultaService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: disponibilidadeConsultaCollection })));
      const expectedCollection: IDisponibilidadeConsulta[] = [disponibilidadeConsulta, ...disponibilidadeConsultaCollection];
      jest.spyOn(disponibilidadeConsultaService, 'addDisponibilidadeConsultaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consulta });
      comp.ngOnInit();

      expect(disponibilidadeConsultaService.query).toHaveBeenCalled();
      expect(disponibilidadeConsultaService.addDisponibilidadeConsultaToCollectionIfMissing).toHaveBeenCalledWith(
        disponibilidadeConsultaCollection,
        disponibilidadeConsulta
      );
      expect(comp.disponibilidadeConsultasCollection).toEqual(expectedCollection);
    });

    it('Should call Associado query and add missing value', () => {
      const consulta: IConsulta = { id: 456 };
      const associado: IAssociado = { id: 30911 };
      consulta.associado = associado;

      const associadoCollection: IAssociado[] = [{ id: 37797 }];
      jest.spyOn(associadoService, 'query').mockReturnValue(of(new HttpResponse({ body: associadoCollection })));
      const additionalAssociados = [associado];
      const expectedCollection: IAssociado[] = [...additionalAssociados, ...associadoCollection];
      jest.spyOn(associadoService, 'addAssociadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consulta });
      comp.ngOnInit();

      expect(associadoService.query).toHaveBeenCalled();
      expect(associadoService.addAssociadoToCollectionIfMissing).toHaveBeenCalledWith(associadoCollection, ...additionalAssociados);
      expect(comp.associadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const consulta: IConsulta = { id: 456 };
      const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 33681 };
      consulta.disponibilidadeConsulta = disponibilidadeConsulta;
      const associado: IAssociado = { id: 33029 };
      consulta.associado = associado;

      activatedRoute.data = of({ consulta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(consulta));
      expect(comp.disponibilidadeConsultasCollection).toContain(disponibilidadeConsulta);
      expect(comp.associadosSharedCollection).toContain(associado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Consulta>>();
      const consulta = { id: 123 };
      jest.spyOn(consultaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consulta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consulta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(consultaService.update).toHaveBeenCalledWith(consulta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Consulta>>();
      const consulta = new Consulta();
      jest.spyOn(consultaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consulta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consulta }));
      saveSubject.complete();

      // THEN
      expect(consultaService.create).toHaveBeenCalledWith(consulta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Consulta>>();
      const consulta = { id: 123 };
      jest.spyOn(consultaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consulta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consultaService.update).toHaveBeenCalledWith(consulta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDisponibilidadeConsultaById', () => {
      it('Should return tracked DisponibilidadeConsulta primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDisponibilidadeConsultaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAssociadoById', () => {
      it('Should return tracked Associado primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssociadoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
