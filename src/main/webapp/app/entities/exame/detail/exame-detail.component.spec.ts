import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExameDetailComponent } from './exame-detail.component';

describe('Exame Management Detail Component', () => {
  let comp: ExameDetailComponent;
  let fixture: ComponentFixture<ExameDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExameDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ exame: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExameDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExameDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load exame on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.exame).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
