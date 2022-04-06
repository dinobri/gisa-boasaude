import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssociadoDetailComponent } from './associado-detail.component';

describe('Associado Management Detail Component', () => {
  let comp: AssociadoDetailComponent;
  let fixture: ComponentFixture<AssociadoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssociadoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ associado: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssociadoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssociadoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load associado on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.associado).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
