import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISong } from 'app/shared/model/Song.model';
import { SongService } from './Song.service';

@Component({
  selector: 'jhi-Song-delete-dialog',
  templateUrl: './Song-delete-dialog.component.html'
})
export class SongDeleteDialogComponent {
  Song: ISong;

  constructor(protected SongService: SongService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.SongService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'SongListModification',
        content: 'Deleted an Song'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-Song-delete-popup',
  template: ''
})
export class SongDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ Song }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SongDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.Song = Song;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/Song', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/Song', { outlets: { popup: null } }]);
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
