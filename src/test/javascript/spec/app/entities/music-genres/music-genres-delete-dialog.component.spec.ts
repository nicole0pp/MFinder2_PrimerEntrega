/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MFinder2TestModule } from '../../../test.module';
import { MusicGenresDeleteDialogComponent } from 'app/entities/music-genres/music-genres-delete-dialog.component';
import { MusicGenresService } from 'app/entities/music-genres/music-genres.service';

describe('Component Tests', () => {
  describe('MusicGenres Management Delete Component', () => {
    let comp: MusicGenresDeleteDialogComponent;
    let fixture: ComponentFixture<MusicGenresDeleteDialogComponent>;
    let service: MusicGenresService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [MusicGenresDeleteDialogComponent]
      })
        .overrideTemplate(MusicGenresDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MusicGenresDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MusicGenresService);
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
