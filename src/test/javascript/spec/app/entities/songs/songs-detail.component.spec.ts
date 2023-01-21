/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { SongsDetailComponent } from 'app/entities/songs/songs-detail.component';
import { Songs } from 'app/shared/model/songs.model';

describe('Component Tests', () => {
  describe('Songs Management Detail Component', () => {
    let comp: SongsDetailComponent;
    let fixture: ComponentFixture<SongsDetailComponent>;
    const route = ({ data: of({ songs: new Songs(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [SongsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SongsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SongsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.songs).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
