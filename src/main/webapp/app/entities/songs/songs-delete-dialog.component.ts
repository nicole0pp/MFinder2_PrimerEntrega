import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISongs } from 'app/shared/model/songs.model';
import { SongsService } from './songs.service';

@Component({
  selector: 'jhi-songs-delete-dialog',
  templateUrl: './songs-delete-dialog.component.html'
})
export class SongsDeleteDialogComponent {
  songs: ISongs;

  constructor(protected songsService: SongsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.songsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'songsListModification',
        content: 'Deleted an songs'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-songs-delete-popup',
  template: ''
})
export class SongsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ songs }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SongsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.songs = songs;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/songs', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/songs', { outlets: { popup: null } }]);
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
