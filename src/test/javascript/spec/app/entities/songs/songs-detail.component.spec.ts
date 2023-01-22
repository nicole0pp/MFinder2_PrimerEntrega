/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { SongDetailComponent } from 'app/entities/Song/Song-detail.component';
import { Song } from 'app/shared/model/Song.model';

describe('Component Tests', () => {
  describe('Song Management Detail Component', () => {
    let comp: SongDetailComponent;
    let fixture: ComponentFixture<SongDetailComponent>;
    const route = ({ data: of({ Song: new Song(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [SongDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SongDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SongDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.Song).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
