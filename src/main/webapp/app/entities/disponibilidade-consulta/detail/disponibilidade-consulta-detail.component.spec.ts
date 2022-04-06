import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DisponibilidadeConsultaDetailComponent } from './disponibilidade-consulta-detail.component';

describe('DisponibilidadeConsulta Management Detail Component', () => {
  let comp: DisponibilidadeConsultaDetailComponent;
  let fixture: ComponentFixture<DisponibilidadeConsultaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DisponibilidadeConsultaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ disponibilidadeConsulta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DisponibilidadeConsultaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DisponibilidadeConsultaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load disponibilidadeConsulta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.disponibilidadeConsulta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
