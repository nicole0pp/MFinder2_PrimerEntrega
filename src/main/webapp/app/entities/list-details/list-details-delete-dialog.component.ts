import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IListDetails } from 'app/shared/model/list-details.model';
import { ListDetailsService } from './list-details.service';

@Component({
  selector: 'jhi-list-details-delete-dialog',
  templateUrl: './list-details-delete-dialog.component.html'
})
export class ListDetailsDeleteDialogComponent {
  listDetails: IListDetails;

  constructor(
    protected listDetailsService: ListDetailsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.listDetailsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'listDetailsListModification',
        content: 'Deleted an listDetails'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-list-details-delete-popup',
  template: ''
})
export class ListDetailsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ listDetails }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ListDetailsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.listDetails = listDetails;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/list-details', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/list-details', { outlets: { popup: null } }]);
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
