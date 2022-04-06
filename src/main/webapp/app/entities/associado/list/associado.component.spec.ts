import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AssociadoService } from '../service/associado.service';

import { AssociadoComponent } from './associado.component';

describe('Associado Management Component', () => {
  let comp: AssociadoComponent;
  let fixture: ComponentFixture<AssociadoComponent>;
  let service: AssociadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssociadoComponent],
    })
      .overrideTemplate(AssociadoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssociadoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AssociadoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.associados?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
