/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MFinder2TestModule } from '../../../test.module';
import { SongDeleteDialogComponent } from 'app/entities/Song/Song-delete-dialog.component';
import { SongService } from 'app/entities/Song/Song.service';

describe('Component Tests', () => {
  describe('Song Management Delete Component', () => {
    let comp: SongDeleteDialogComponent;
    let fixture: ComponentFixture<SongDeleteDialogComponent>;
    let service: SongService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [SongDeleteDialogComponent]
      })
        .overrideTemplate(SongDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SongDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SongService);
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
