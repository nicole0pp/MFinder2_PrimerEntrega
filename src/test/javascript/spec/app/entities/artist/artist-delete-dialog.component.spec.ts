/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MFinder2TestModule } from '../../../test.module';
import { ArtistDeleteDialogComponent } from 'app/entities/artist/artist-delete-dialog.component';
import { ArtistService } from 'app/entities/artist/artist.service';

describe('Component Tests', () => {
  describe('Artist Management Delete Component', () => {
    let comp: ArtistDeleteDialogComponent;
    let fixture: ComponentFixture<ArtistDeleteDialogComponent>;
    let service: ArtistService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [ArtistDeleteDialogComponent]
      })
        .overrideTemplate(ArtistDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArtistDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArtistService);
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
