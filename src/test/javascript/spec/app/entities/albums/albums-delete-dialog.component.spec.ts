/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MFinder2TestModule } from '../../../test.module';
import { AlbumsDeleteDialogComponent } from 'app/entities/albums/albums-delete-dialog.component';
import { AlbumsService } from 'app/entities/albums/albums.service';

describe('Component Tests', () => {
  describe('Albums Management Delete Component', () => {
    let comp: AlbumsDeleteDialogComponent;
    let fixture: ComponentFixture<AlbumsDeleteDialogComponent>;
    let service: AlbumsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [AlbumsDeleteDialogComponent]
      })
        .overrideTemplate(AlbumsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlbumsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AlbumsService);
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
