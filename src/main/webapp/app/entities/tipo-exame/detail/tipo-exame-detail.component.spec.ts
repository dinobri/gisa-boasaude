import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoExameDetailComponent } from './tipo-exame-detail.component';

describe('TipoExame Management Detail Component', () => {
  let comp: TipoExameDetailComponent;
  let fixture: ComponentFixture<TipoExameDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoExameDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoExame: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoExameDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoExameDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoExame on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoExame).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
