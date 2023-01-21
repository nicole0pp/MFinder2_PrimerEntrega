/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { MusicGenresDetailComponent } from 'app/entities/music-genres/music-genres-detail.component';
import { MusicGenres } from 'app/shared/model/music-genres.model';

describe('Component Tests', () => {
  describe('MusicGenres Management Detail Component', () => {
    let comp: MusicGenresDetailComponent;
    let fixture: ComponentFixture<MusicGenresDetailComponent>;
    const route = ({ data: of({ musicGenres: new MusicGenres(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [MusicGenresDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MusicGenresDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MusicGenresDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.musicGenres).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
