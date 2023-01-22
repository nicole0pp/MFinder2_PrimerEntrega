import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMusicGenre } from 'app/shared/model/music-genres.model';
import { MusicGenreService } from './music-genres.service';

@Component({
  selector: 'jhi-music-genres-delete-dialog',
  templateUrl: './music-genres-delete-dialog.component.html'
})
export class MusicGenreDeleteDialogComponent {
  MusicGenre: IMusicGenre;

  constructor(
    protected MusicGenreService: MusicGenreService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.MusicGenreService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'MusicGenreListModification',
        content: 'Deleted an MusicGenre'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-music-genres-delete-popup',
  template: ''
})
export class MusicGenreDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ MusicGenre }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MusicGenreDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.MusicGenre = MusicGenre;
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
