import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAlbum } from 'app/shared/model/Album.model';
import { AlbumService } from './Album.service';

@Component({
  selector: 'jhi-Album-delete-dialog',
  templateUrl: './Album-delete-dialog.component.html'
})
export class AlbumDeleteDialogComponent {
  Album: IAlbum;

  constructor(protected AlbumService: AlbumService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.AlbumService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'AlbumListModification',
        content: 'Deleted an Album'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-Album-delete-popup',
  template: ''
})
export class AlbumDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ Album }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AlbumDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.Album = Album;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/Album', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/Album', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
