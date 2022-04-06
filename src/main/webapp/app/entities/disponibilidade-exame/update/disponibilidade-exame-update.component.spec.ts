import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DisponibilidadeExameService } from '../service/disponibilidade-exame.service';
import { IDisponibilidadeExame, DisponibilidadeExame } from '../disponibilidade-exame.model';
import { IPrestador } from 'app/entities/prestador/prestador.model';
import { PrestadorService } from 'app/entities/prestador/service/prestador.service';
import { ITipoExame } from 'app/entities/tipo-exame/tipo-exame.model';
import { TipoExameService } from 'app/entities/tipo-exame/service/tipo-exame.service';

import { DisponibilidadeExameUpdateComponent } from './disponibilidade-exame-update.component';

describe('DisponibilidadeExame Management Update Component', () => {
  let comp: DisponibilidadeExameUpdateComponent;
  let fixture: ComponentFixture<DisponibilidadeExameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let disponibilidadeExameService: DisponibilidadeExameService;
  let prestadorService: PrestadorService;
  let tipoExameService: TipoExameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DisponibilidadeExameUpdateComponent],
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
      .overrideTemplate(DisponibilidadeExameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisponibilidadeExameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    disponibilidadeExameService = TestBed.inject(DisponibilidadeExameService);
    prestadorService = TestBed.inject(PrestadorService);
    tipoExameService = TestBed.inject(TipoExameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Prestador query and add missing value', () => {
      const disponibilidadeExame: IDisponibilidadeExame = { id: 456 };
      const prestador: IPrestador = { id: 94764 };
      disponibilidadeExame.prestador = prestador;

      const prestadorCollection: IPrestador[] = [{ id: 13453 }];
      jest.spyOn(prestadorService, 'query').mockReturnValue(of(new HttpResponse({ body: prestadorCollection })));
      const additionalPrestadors = [prestador];
      const expectedCollection: IPrestador[] = [...additionalPrestadors, ...prestadorCollection];
      jest.spyOn(prestadorService, 'addPrestadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disponibilidadeExame });
      comp.ngOnInit();

      expect(prestadorService.query).toHaveBeenCalled();
      expect(prestadorService.addPrestadorToCollectionIfMissing).toHaveBeenCalledWith(prestadorCollection, ...additionalPrestadors);
      expect(comp.prestadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TipoExame query and add missing value', () => {
      const disponibilidadeExame: IDisponibilidadeExame = { id: 456 };
      const tipoExame: ITipoExame = { id: 4126 };
      disponibilidadeExame.tipoExame = tipoExame;

      const tipoExameCollection: ITipoExame[] = [{ id: 17375 }];
      jest.spyOn(tipoExameService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoExameCollection })));
      const additionalTipoExames = [tipoExame];
      const expectedCollection: ITipoExame[] = [...additionalTipoExames, ...tipoExameCollection];
      jest.spyOn(tipoExameService, 'addTipoExameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disponibilidadeExame });
      comp.ngOnInit();

      expect(tipoExameService.query).toHaveBeenCalled();
      expect(tipoExameService.addTipoExameToCollectionIfMissing).toHaveBeenCalledWith(tipoExameCollection, ...additionalTipoExames);
      expect(comp.tipoExamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const disponibilidadeExame: IDisponibilidadeExame = { id: 456 };
      const prestador: IPrestador = { id: 24600 };
      disponibilidadeExame.prestador = prestador;
      const tipoExame: ITipoExame = { id: 90848 };
      disponibilidadeExame.tipoExame = tipoExame;

      activatedRoute.data = of({ disponibilidadeExame });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(disponibilidadeExame));
      expect(comp.prestadorsSharedCollection).toContain(prestador);
      expect(comp.tipoExamesSharedCollection).toContain(tipoExame);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DisponibilidadeExame>>();
      const disponibilidadeExame = { id: 123 };
      jest.spyOn(disponibilidadeExameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilidadeExame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disponibilidadeExame }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(disponibilidadeExameService.update).toHaveBeenCalledWith(disponibilidadeExame);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DisponibilidadeExame>>();
      const disponibilidadeExame = new DisponibilidadeExame();
      jest.spyOn(disponibilidadeExameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilidadeExame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disponibilidadeExame }));
      saveSubject.complete();

      // THEN
      expect(disponibilidadeExameService.create).toHaveBeenCalledWith(disponibilidadeExame);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DisponibilidadeExame>>();
      const disponibilidadeExame = { id: 123 };
      jest.spyOn(disponibilidadeExameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilidadeExame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(disponibilidadeExameService.update).toHaveBeenCalledWith(disponibilidadeExame);
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

    describe('trackTipoExameById', () => {
      it('Should return tracked TipoExame primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoExameById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
