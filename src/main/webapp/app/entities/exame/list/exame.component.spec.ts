import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ExameService } from '../service/exame.service';

import { ExameComponent } from './exame.component';

describe('Exame Management Component', () => {
  let comp: ExameComponent;
  let fixture: ComponentFixture<ExameComponent>;
  let service: ExameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ExameComponent],
    })
      .overrideTemplate(ExameComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExameComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ExameService);

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
    expect(comp.exames?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
