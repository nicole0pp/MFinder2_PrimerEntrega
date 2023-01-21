/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { ArtistDetailComponent } from 'app/entities/artist/artist-detail.component';
import { Artist } from 'app/shared/model/artist.model';

describe('Component Tests', () => {
  describe('Artist Management Detail Component', () => {
    let comp: ArtistDetailComponent;
    let fixture: ComponentFixture<ArtistDetailComponent>;
    const route = ({ data: of({ artist: new Artist(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [ArtistDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ArtistDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArtistDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.artist).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
