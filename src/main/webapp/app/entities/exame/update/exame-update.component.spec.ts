import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExameService } from '../service/exame.service';
import { IExame, Exame } from '../exame.model';
import { IDisponibilidadeExame } from 'app/entities/disponibilidade-exame/disponibilidade-exame.model';
import { DisponibilidadeExameService } from 'app/entities/disponibilidade-exame/service/disponibilidade-exame.service';
import { IAssociado } from 'app/entities/associado/associado.model';
import { AssociadoService } from 'app/entities/associado/service/associado.service';

import { ExameUpdateComponent } from './exame-update.component';

describe('Exame Management Update Component', () => {
  let comp: ExameUpdateComponent;
  let fixture: ComponentFixture<ExameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let exameService: ExameService;
  let disponibilidadeExameService: DisponibilidadeExameService;
  let associadoService: AssociadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExameUpdateComponent],
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
      .overrideTemplate(ExameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    exameService = TestBed.inject(ExameService);
    disponibilidadeExameService = TestBed.inject(DisponibilidadeExameService);
    associadoService = TestBed.inject(AssociadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call disponibilidadeExame query and add missing value', () => {
      const exame: IExame = { id: 456 };
      const disponibilidadeExame: IDisponibilidadeExame = { id: 17096 };
      exame.disponibilidadeExame = disponibilidadeExame;

      const disponibilidadeExameCollection: IDisponibilidadeExame[] = [{ id: 63664 }];
      jest.spyOn(disponibilidadeExameService, 'query').mockReturnValue(of(new HttpResponse({ body: disponibilidadeExameCollection })));
      const expectedCollection: IDisponibilidadeExame[] = [disponibilidadeExame, ...disponibilidadeExameCollection];
      jest.spyOn(disponibilidadeExameService, 'addDisponibilidadeExameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exame });
      comp.ngOnInit();

      expect(disponibilidadeExameService.query).toHaveBeenCalled();
      expect(disponibilidadeExameService.addDisponibilidadeExameToCollectionIfMissing).toHaveBeenCalledWith(
        disponibilidadeExameCollection,
        disponibilidadeExame
      );
      expect(comp.disponibilidadeExamesCollection).toEqual(expectedCollection);
    });

    it('Should call Associado query and add missing value', () => {
      const exame: IExame = { id: 456 };
      const associado: IAssociado = { id: 21006 };
      exame.associado = associado;

      const associadoCollection: IAssociado[] = [{ id: 99143 }];
      jest.spyOn(associadoService, 'query').mockReturnValue(of(new HttpResponse({ body: associadoCollection })));
      const additionalAssociados = [associado];
      const expectedCollection: IAssociado[] = [...additionalAssociados, ...associadoCollection];
      jest.spyOn(associadoService, 'addAssociadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exame });
      comp.ngOnInit();

      expect(associadoService.query).toHaveBeenCalled();
      expect(associadoService.addAssociadoToCollectionIfMissing).toHaveBeenCalledWith(associadoCollection, ...additionalAssociados);
      expect(comp.associadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const exame: IExame = { id: 456 };
      const disponibilidadeExame: IDisponibilidadeExame = { id: 23479 };
      exame.disponibilidadeExame = disponibilidadeExame;
      const associado: IAssociado = { id: 81749 };
      exame.associado = associado;

      activatedRoute.data = of({ exame });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(exame));
      expect(comp.disponibilidadeExamesCollection).toContain(disponibilidadeExame);
      expect(comp.associadosSharedCollection).toContain(associado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Exame>>();
      const exame = { id: 123 };
      jest.spyOn(exameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exame }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(exameService.update).toHaveBeenCalledWith(exame);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Exame>>();
      const exame = new Exame();
      jest.spyOn(exameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exame }));
      saveSubject.complete();

      // THEN
      expect(exameService.create).toHaveBeenCalledWith(exame);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Exame>>();
      const exame = { id: 123 };
      jest.spyOn(exameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(exameService.update).toHaveBeenCalledWith(exame);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDisponibilidadeExameById', () => {
      it('Should return tracked DisponibilidadeExame primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDisponibilidadeExameById(0, entity);
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
