import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReproductionLists } from 'app/shared/model/reproduction-lists.model';
import { ReproductionListsService } from './reproduction-lists.service';

@Component({
  selector: 'jhi-reproduction-lists-delete-dialog',
  templateUrl: './reproduction-lists-delete-dialog.component.html'
})
export class ReproductionListsDeleteDialogComponent {
  reproductionLists: IReproductionLists;

  constructor(
    protected reproductionListsService: ReproductionListsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.reproductionListsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'reproductionListsListModification',
        content: 'Deleted an reproductionLists'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-reproduction-lists-delete-popup',
  template: ''
})
export class ReproductionListsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ reproductionLists }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ReproductionListsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.reproductionLists = reproductionLists;
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
