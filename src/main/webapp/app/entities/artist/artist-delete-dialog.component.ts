import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IArtist } from 'app/shared/model/artist.model';
import { ArtistService } from './artist.service';

@Component({
  selector: 'jhi-artist-delete-dialog',
  templateUrl: './artist-delete-dialog.component.html'
})
export class ArtistDeleteDialogComponent {
  artist: IArtist;

  constructor(protected artistService: ArtistService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.artistService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'artistListModification',
        content: 'Deleted an artist'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-artist-delete-popup',
  template: ''
})
export class ArtistDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ artist }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ArtistDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.artist = artist;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/artist', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/artist', { outlets: { popup: null } }]);
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
