import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DisponibilidadeConsultaService } from '../service/disponibilidade-consulta.service';
import { IDisponibilidadeConsulta, DisponibilidadeConsulta } from '../disponibilidade-consulta.model';
import { IPrestador } from 'app/entities/prestador/prestador.model';
import { PrestadorService } from 'app/entities/prestador/service/prestador.service';

import { DisponibilidadeConsultaUpdateComponent } from './disponibilidade-consulta-update.component';

describe('DisponibilidadeConsulta Management Update Component', () => {
  let comp: DisponibilidadeConsultaUpdateComponent;
  let fixture: ComponentFixture<DisponibilidadeConsultaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let disponibilidadeConsultaService: DisponibilidadeConsultaService;
  let prestadorService: PrestadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DisponibilidadeConsultaUpdateComponent],
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
      .overrideTemplate(DisponibilidadeConsultaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisponibilidadeConsultaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    disponibilidadeConsultaService = TestBed.inject(DisponibilidadeConsultaService);
    prestadorService = TestBed.inject(PrestadorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Prestador query and add missing value', () => {
      const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 456 };
      const prestador: IPrestador = { id: 23133 };
      disponibilidadeConsulta.prestador = prestador;

      const prestadorCollection: IPrestador[] = [{ id: 71943 }];
      jest.spyOn(prestadorService, 'query').mockReturnValue(of(new HttpResponse({ body: prestadorCollection })));
      const additionalPrestadors = [prestador];
      const expectedCollection: IPrestador[] = [...additionalPrestadors, ...prestadorCollection];
      jest.spyOn(prestadorService, 'addPrestadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disponibilidadeConsulta });
      comp.ngOnInit();

      expect(prestadorService.query).toHaveBeenCalled();
      expect(prestadorService.addPrestadorToCollectionIfMissing).toHaveBeenCalledWith(prestadorCollection, ...additionalPrestadors);
      expect(comp.prestadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const disponibilidadeConsulta: IDisponibilidadeConsulta = { id: 456 };
      const prestador: IPrestador = { id: 94615 };
      disponibilidadeConsulta.prestador = prestador;

      activatedRoute.data = of({ disponibilidadeConsulta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(disponibilidadeConsulta));
      expect(comp.prestadorsSharedCollection).toContain(prestador);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DisponibilidadeConsulta>>();
      const disponibilidadeConsulta = { id: 123 };
      jest.spyOn(disponibilidadeConsultaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilidadeConsulta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disponibilidadeConsulta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(disponibilidadeConsultaService.update).toHaveBeenCalledWith(disponibilidadeConsulta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DisponibilidadeConsulta>>();
      const disponibilidadeConsulta = new DisponibilidadeConsulta();
      jest.spyOn(disponibilidadeConsultaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilidadeConsulta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disponibilidadeConsulta }));
      saveSubject.complete();

      // THEN
      expect(disponibilidadeConsultaService.create).toHaveBeenCalledWith(disponibilidadeConsulta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DisponibilidadeConsulta>>();
      const disponibilidadeConsulta = { id: 123 };
      jest.spyOn(disponibilidadeConsultaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilidadeConsulta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(disponibilidadeConsultaService.update).toHaveBeenCalledWith(disponibilidadeConsulta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPrestadorById', () => {
      it('Should return tracked Prestador primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrestadorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
