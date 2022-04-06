import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EspecialidadeDetailComponent } from './especialidade-detail.component';

describe('Especialidade Management Detail Component', () => {
  let comp: EspecialidadeDetailComponent;
  let fixture: ComponentFixture<EspecialidadeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EspecialidadeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ especialidade: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EspecialidadeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EspecialidadeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load especialidade on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.especialidade).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
