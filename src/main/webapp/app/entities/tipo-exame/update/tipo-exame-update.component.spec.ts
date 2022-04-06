import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoExameService } from '../service/tipo-exame.service';
import { ITipoExame, TipoExame } from '../tipo-exame.model';

import { TipoExameUpdateComponent } from './tipo-exame-update.component';

describe('TipoExame Management Update Component', () => {
  let comp: TipoExameUpdateComponent;
  let fixture: ComponentFixture<TipoExameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoExameService: TipoExameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TipoExameUpdateComponent],
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
      .overrideTemplate(TipoExameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoExameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoExameService = TestBed.inject(TipoExameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoExame: ITipoExame = { id: 456 };

      activatedRoute.data = of({ tipoExame });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoExame));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoExame>>();
      const tipoExame = { id: 123 };
      jest.spyOn(tipoExameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoExame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoExame }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoExameService.update).toHaveBeenCalledWith(tipoExame);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoExame>>();
      const tipoExame = new TipoExame();
      jest.spyOn(tipoExameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoExame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoExame }));
      saveSubject.complete();

      // THEN
      expect(tipoExameService.create).toHaveBeenCalledWith(tipoExame);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoExame>>();
      const tipoExame = { id: 123 };
      jest.spyOn(tipoExameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoExame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoExameService.update).toHaveBeenCalledWith(tipoExame);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
