import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFavoriteList } from 'app/shared/model/reproduction-lists.model';
import { FavoriteListService } from './reproduction-lists.service';

@Component({
  selector: 'jhi-reproduction-lists-delete-dialog',
  templateUrl: './reproduction-lists-delete-dialog.component.html'
})
export class FavoriteListDeleteDialogComponent {
  FavoriteList: IFavoriteList;

  constructor(
    protected FavoriteListService: FavoriteListService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.FavoriteListService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'FavoriteListListModification',
        content: 'Deleted an FavoriteList'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-reproduction-lists-delete-popup',
  template: ''
})
export class FavoriteListDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ FavoriteList }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FavoriteListDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.FavoriteList = FavoriteList;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/reproduction-lists', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/reproduction-lists', { outlets: { popup: null } }]);
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
