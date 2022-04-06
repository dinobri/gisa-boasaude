import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ConveniadoService } from '../service/conveniado.service';

import { ConveniadoComponent } from './conveniado.component';

describe('Conveniado Management Component', () => {
  let comp: ConveniadoComponent;
  let fixture: ComponentFixture<ConveniadoComponent>;
  let service: ConveniadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ConveniadoComponent],
    })
      .overrideTemplate(ConveniadoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConveniadoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConveniadoService);

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
    expect(comp.conveniados?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
