import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EspecialidadeService } from '../service/especialidade.service';
import { IEspecialidade, Especialidade } from '../especialidade.model';

import { EspecialidadeUpdateComponent } from './especialidade-update.component';

describe('Especialidade Management Update Component', () => {
  let comp: EspecialidadeUpdateComponent;
  let fixture: ComponentFixture<EspecialidadeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let especialidadeService: EspecialidadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EspecialidadeUpdateComponent],
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
      .overrideTemplate(EspecialidadeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EspecialidadeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    especialidadeService = TestBed.inject(EspecialidadeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const especialidade: IEspecialidade = { id: 456 };

      activatedRoute.data = of({ especialidade });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(especialidade));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Especialidade>>();
      const especialidade = { id: 123 };
      jest.spyOn(especialidadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: especialidade }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(especialidadeService.update).toHaveBeenCalledWith(especialidade);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Especialidade>>();
      const especialidade = new Especialidade();
      jest.spyOn(especialidadeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: especialidade }));
      saveSubject.complete();

      // THEN
      expect(especialidadeService.create).toHaveBeenCalledWith(especialidade);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Especialidade>>();
      const especialidade = { id: 123 };
      jest.spyOn(especialidadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ especialidade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(especialidadeService.update).toHaveBeenCalledWith(especialidade);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
