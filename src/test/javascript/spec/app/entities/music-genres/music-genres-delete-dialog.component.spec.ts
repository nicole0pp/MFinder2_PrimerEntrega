/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MFinder2TestModule } from '../../../test.module';
import { MusicGenreDeleteDialogComponent } from 'app/entities/music-genres/music-genres-delete-dialog.component';
import { MusicGenreService } from 'app/entities/music-genres/music-genres.service';

describe('Component Tests', () => {
  describe('MusicGenre Management Delete Component', () => {
    let comp: MusicGenreDeleteDialogComponent;
    let fixture: ComponentFixture<MusicGenreDeleteDialogComponent>;
    let service: MusicGenreService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [MusicGenreDeleteDialogComponent]
      })
        .overrideTemplate(MusicGenreDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MusicGenreDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MusicGenreService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
