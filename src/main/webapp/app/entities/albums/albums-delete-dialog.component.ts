import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAlbums } from 'app/shared/model/albums.model';
import { AlbumsService } from './albums.service';

@Component({
  selector: 'jhi-albums-delete-dialog',
  templateUrl: './albums-delete-dialog.component.html'
})
export class AlbumsDeleteDialogComponent {
  albums: IAlbums;

  constructor(protected albumsService: AlbumsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.albumsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'albumsListModification',
        content: 'Deleted an albums'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-albums-delete-popup',
  template: ''
})
export class AlbumsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ albums }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AlbumsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.albums = albums;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/albums', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/albums', { outlets: { popup: null } }]);
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
