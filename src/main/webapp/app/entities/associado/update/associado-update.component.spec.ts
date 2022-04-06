import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssociadoService } from '../service/associado.service';
import { IAssociado, Associado } from '../associado.model';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { IPlano } from 'app/entities/plano/plano.model';
import { PlanoService } from 'app/entities/plano/service/plano.service';

import { AssociadoUpdateComponent } from './associado-update.component';

describe('Associado Management Update Component', () => {
  let comp: AssociadoUpdateComponent;
  let fixture: ComponentFixture<AssociadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let associadoService: AssociadoService;
  let enderecoService: EnderecoService;
  let planoService: PlanoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssociadoUpdateComponent],
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
      .overrideTemplate(AssociadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssociadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    associadoService = TestBed.inject(AssociadoService);
    enderecoService = TestBed.inject(EnderecoService);
    planoService = TestBed.inject(PlanoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call endereco query and add missing value', () => {
      const associado: IAssociado = { id: 456 };
      const endereco: IEndereco = { id: 4405 };
      associado.endereco = endereco;

      const enderecoCollection: IEndereco[] = [{ id: 77886 }];
      jest.spyOn(enderecoService, 'query').mockReturnValue(of(new HttpResponse({ body: enderecoCollection })));
      const expectedCollection: IEndereco[] = [endereco, ...enderecoCollection];
      jest.spyOn(enderecoService, 'addEnderecoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ associado });
      comp.ngOnInit();

      expect(enderecoService.query).toHaveBeenCalled();
      expect(enderecoService.addEnderecoToCollectionIfMissing).toHaveBeenCalledWith(enderecoCollection, endereco);
      expect(comp.enderecosCollection).toEqual(expectedCollection);
    });

    it('Should call Plano query and add missing value', () => {
      const associado: IAssociado = { id: 456 };
      const plano: IPlano = { id: 9291 };
      associado.plano = plano;

      const planoCollection: IPlano[] = [{ id: 63604 }];
      jest.spyOn(planoService, 'query').mockReturnValue(of(new HttpResponse({ body: planoCollection })));
      const additionalPlanos = [plano];
      const expectedCollection: IPlano[] = [...additionalPlanos, ...planoCollection];
      jest.spyOn(planoService, 'addPlanoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ associado });
      comp.ngOnInit();

      expect(planoService.query).toHaveBeenCalled();
      expect(planoService.addPlanoToCollectionIfMissing).toHaveBeenCalledWith(planoCollection, ...additionalPlanos);
      expect(comp.planosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const associado: IAssociado = { id: 456 };
      const endereco: IEndereco = { id: 47398 };
      associado.endereco = endereco;
      const plano: IPlano = { id: 40067 };
      associado.plano = plano;

      activatedRoute.data = of({ associado });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(associado));
      expect(comp.enderecosCollection).toContain(endereco);
      expect(comp.planosSharedCollection).toContain(plano);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Associado>>();
      const associado = { id: 123 };
      jest.spyOn(associadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ associado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: associado }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(associadoService.update).toHaveBeenCalledWith(associado);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Associado>>();
      const associado = new Associado();
      jest.spyOn(associadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ associado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: associado }));
      saveSubject.complete();

      // THEN
      expect(associadoService.create).toHaveBeenCalledWith(associado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Associado>>();
      const associado = { id: 123 };
      jest.spyOn(associadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ associado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(associadoService.update).toHaveBeenCalledWith(associado);
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

    describe('trackPlanoById', () => {
      it('Should return tracked Plano primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlanoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
