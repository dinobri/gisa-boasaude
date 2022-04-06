import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConveniadoService } from '../service/conveniado.service';
import { IConveniado, Conveniado } from '../conveniado.model';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { ITipoExame } from 'app/entities/tipo-exame/tipo-exame.model';
import { TipoExameService } from 'app/entities/tipo-exame/service/tipo-exame.service';

import { ConveniadoUpdateComponent } from './conveniado-update.component';

describe('Conveniado Management Update Component', () => {
  let comp: ConveniadoUpdateComponent;
  let fixture: ComponentFixture<ConveniadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conveniadoService: ConveniadoService;
  let enderecoService: EnderecoService;
  let tipoExameService: TipoExameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConveniadoUpdateComponent],
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
      .overrideTemplate(ConveniadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConveniadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conveniadoService = TestBed.inject(ConveniadoService);
    enderecoService = TestBed.inject(EnderecoService);
    tipoExameService = TestBed.inject(TipoExameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call endereco query and add missing value', () => {
      const conveniado: IConveniado = { id: 456 };
      const endereco: IEndereco = { id: 14037 };
      conveniado.endereco = endereco;

      const enderecoCollection: IEndereco[] = [{ id: 67487 }];
      jest.spyOn(enderecoService, 'query').mockReturnValue(of(new HttpResponse({ body: enderecoCollection })));
      const expectedCollection: IEndereco[] = [endereco, ...enderecoCollection];
      jest.spyOn(enderecoService, 'addEnderecoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ conveniado });
      comp.ngOnInit();

      expect(enderecoService.query).toHaveBeenCalled();
      expect(enderecoService.addEnderecoToCollectionIfMissing).toHaveBeenCalledWith(enderecoCollection, endereco);
      expect(comp.enderecosCollection).toEqual(expectedCollection);
    });

    it('Should call TipoExame query and add missing value', () => {
      const conveniado: IConveniado = { id: 456 };
      const tipoExames: ITipoExame[] = [{ id: 99390 }];
      conveniado.tipoExames = tipoExames;

      const tipoExameCollection: ITipoExame[] = [{ id: 41548 }];
      jest.spyOn(tipoExameService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoExameCollection })));
      const additionalTipoExames = [...tipoExames];
      const expectedCollection: ITipoExame[] = [...additionalTipoExames, ...tipoExameCollection];
      jest.spyOn(tipoExameService, 'addTipoExameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ conveniado });
      comp.ngOnInit();

      expect(tipoExameService.query).toHaveBeenCalled();
      expect(tipoExameService.addTipoExameToCollectionIfMissing).toHaveBeenCalledWith(tipoExameCollection, ...additionalTipoExames);
      expect(comp.tipoExamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const conveniado: IConveniado = { id: 456 };
      const endereco: IEndereco = { id: 57158 };
      conveniado.endereco = endereco;
      const tipoExames: ITipoExame = { id: 93186 };
      conveniado.tipoExames = [tipoExames];

      activatedRoute.data = of({ conveniado });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(conveniado));
      expect(comp.enderecosCollection).toContain(endereco);
      expect(comp.tipoExamesSharedCollection).toContain(tipoExames);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Conveniado>>();
      const conveniado = { id: 123 };
      jest.spyOn(conveniadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conveniado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conveniado }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(conveniadoService.update).toHaveBeenCalledWith(conveniado);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Conveniado>>();
      const conveniado = new Conveniado();
      jest.spyOn(conveniadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conveniado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conveniado }));
      saveSubject.complete();

      // THEN
      expect(conveniadoService.create).toHaveBeenCalledWith(conveniado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Conveniado>>();
      const conveniado = { id: 123 };
      jest.spyOn(conveniadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conveniado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conveniadoService.update).toHaveBeenCalledWith(conveniado);
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

    describe('trackTipoExameById', () => {
      it('Should return tracked TipoExame primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoExameById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTipoExame', () => {
      it('Should return option if no TipoExame is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTipoExame(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected TipoExame for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTipoExame(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this TipoExame is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTipoExame(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
