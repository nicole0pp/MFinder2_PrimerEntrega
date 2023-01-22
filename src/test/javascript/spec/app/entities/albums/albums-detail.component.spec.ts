/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { AlbumDetailComponent } from 'app/entities/Album/Album-detail.component';
import { Album } from 'app/shared/model/Album.model';

describe('Component Tests', () => {
  describe('Album Management Detail Component', () => {
    let comp: AlbumDetailComponent;
    let fixture: ComponentFixture<AlbumDetailComponent>;
    const route = ({ data: of({ Album: new Album(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [AlbumDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AlbumDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlbumDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.Album).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
