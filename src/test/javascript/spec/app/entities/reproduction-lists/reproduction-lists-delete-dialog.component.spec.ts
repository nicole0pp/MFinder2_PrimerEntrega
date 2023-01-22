/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MFinder2TestModule } from '../../../test.module';
import { FavoriteListDeleteDialogComponent } from 'app/entities/reproduction-lists/reproduction-lists-delete-dialog.component';
import { FavoriteListService } from 'app/entities/reproduction-lists/reproduction-lists.service';

describe('Component Tests', () => {
  describe('FavoriteList Management Delete Component', () => {
    let comp: FavoriteListDeleteDialogComponent;
    let fixture: ComponentFixture<FavoriteListDeleteDialogComponent>;
    let service: FavoriteListService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [FavoriteListDeleteDialogComponent]
      })
        .overrideTemplate(FavoriteListDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FavoriteListDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FavoriteListService);
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
