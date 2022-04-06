import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DisponibilidadeExameDetailComponent } from './disponibilidade-exame-detail.component';

describe('DisponibilidadeExame Management Detail Component', () => {
  let comp: DisponibilidadeExameDetailComponent;
  let fixture: ComponentFixture<DisponibilidadeExameDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DisponibilidadeExameDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ disponibilidadeExame: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DisponibilidadeExameDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DisponibilidadeExameDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load disponibilidadeExame on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.disponibilidadeExame).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
