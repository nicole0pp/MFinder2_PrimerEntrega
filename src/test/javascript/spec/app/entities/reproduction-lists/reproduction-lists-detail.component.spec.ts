/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { ReproductionListsDetailComponent } from 'app/entities/reproduction-lists/reproduction-lists-detail.component';
import { ReproductionLists } from 'app/shared/model/reproduction-lists.model';

describe('Component Tests', () => {
  describe('ReproductionLists Management Detail Component', () => {
    let comp: ReproductionListsDetailComponent;
    let fixture: ComponentFixture<ReproductionListsDetailComponent>;
    const route = ({ data: of({ reproductionLists: new ReproductionLists(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [ReproductionListsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ReproductionListsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReproductionListsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.reproductionLists).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
