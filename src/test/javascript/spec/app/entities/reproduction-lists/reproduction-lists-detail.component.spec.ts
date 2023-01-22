/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { FavoriteListDetailComponent } from 'app/entities/reproduction-lists/reproduction-lists-detail.component';
import { FavoriteList } from 'app/shared/model/reproduction-lists.model';

describe('Component Tests', () => {
  describe('FavoriteList Management Detail Component', () => {
    let comp: FavoriteListDetailComponent;
    let fixture: ComponentFixture<FavoriteListDetailComponent>;
    const route = ({ data: of({ FavoriteList: new FavoriteList(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [FavoriteListDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FavoriteListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FavoriteListDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.FavoriteList).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
