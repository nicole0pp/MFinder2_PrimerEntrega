/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { ListDetailsDetailComponent } from 'app/entities/list-details/list-details-detail.component';
import { ListDetails } from 'app/shared/model/list-details.model';

describe('Component Tests', () => {
  describe('ListDetails Management Detail Component', () => {
    let comp: ListDetailsDetailComponent;
    let fixture: ComponentFixture<ListDetailsDetailComponent>;
    const route = ({ data: of({ listDetails: new ListDetails(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [ListDetailsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ListDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ListDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.listDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
