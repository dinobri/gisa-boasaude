import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrestadorDetailComponent } from './prestador-detail.component';

describe('Prestador Management Detail Component', () => {
  let comp: PrestadorDetailComponent;
  let fixture: ComponentFixture<PrestadorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrestadorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prestador: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrestadorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrestadorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prestador on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prestador).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
