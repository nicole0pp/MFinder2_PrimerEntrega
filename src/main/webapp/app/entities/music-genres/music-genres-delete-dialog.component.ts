import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMusicGenres } from 'app/shared/model/music-genres.model';
import { MusicGenresService } from './music-genres.service';

@Component({
  selector: 'jhi-music-genres-delete-dialog',
  templateUrl: './music-genres-delete-dialog.component.html'
})
export class MusicGenresDeleteDialogComponent {
  musicGenres: IMusicGenres;

  constructor(
    protected musicGenresService: MusicGenresService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.musicGenresService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'musicGenresListModification',
        content: 'Deleted an musicGenres'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-music-genres-delete-popup',
  template: ''
})
export class MusicGenresDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ musicGenres }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MusicGenresDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.musicGenres = musicGenres;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/music-genres', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/music-genres', { outlets: { popup: null } }]);
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
