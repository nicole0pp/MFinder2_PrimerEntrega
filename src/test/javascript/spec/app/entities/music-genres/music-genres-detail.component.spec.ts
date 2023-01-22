/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { MusicGenreDetailComponent } from 'app/entities/music-genres/music-genres-detail.component';
import { MusicGenre } from 'app/shared/model/music-genres.model';

describe('Component Tests', () => {
  describe('MusicGenre Management Detail Component', () => {
    let comp: MusicGenreDetailComponent;
    let fixture: ComponentFixture<MusicGenreDetailComponent>;
    const route = ({ data: of({ MusicGenre: new MusicGenre(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [MusicGenreDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MusicGenreDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MusicGenreDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.MusicGenre).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
