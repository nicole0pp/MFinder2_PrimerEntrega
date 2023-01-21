import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IListDetails, ListDetails } from 'app/shared/model/list-details.model';
import { ListDetailsService } from './list-details.service';
import { IReproductionLists } from 'app/shared/model/reproduction-lists.model';
import { ReproductionListsService } from 'app/entities/reproduction-lists';

@Component({
  selector: 'jhi-list-details-update',
  templateUrl: './list-details-update.component.html'
})
export class ListDetailsUpdateComponent implements OnInit {
  listDetails: IListDetails;
  isSaving: boolean;

  lists: IReproductionLists[];

  editForm = this.fb.group({
    id: [],
    listId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected listDetailsService: ListDetailsService,
    protected reproductionListsService: ReproductionListsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ listDetails }) => {
      this.updateForm(listDetails);
      this.listDetails = listDetails;
    });
    this.reproductionListsService
      .query({ filter: 'listdetails-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IReproductionLists[]>) => mayBeOk.ok),
        map((response: HttpResponse<IReproductionLists[]>) => response.body)
      )
      .subscribe(
        (res: IReproductionLists[]) => {
          if (!this.listDetails.listId) {
            this.lists = res;
          } else {
            this.reproductionListsService
              .find(this.listDetails.listId)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IReproductionLists>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IReproductionLists>) => subResponse.body)
              )
              .subscribe(
                (subRes: IReproductionLists) => (this.lists = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(listDetails: IListDetails) {
    this.editForm.patchValue({
      id: listDetails.id,
      listId: listDetails.listId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const listDetails = this.createFromForm();
    if (listDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.listDetailsService.update(listDetails));
    } else {
      this.subscribeToSaveResponse(this.listDetailsService.create(listDetails));
    }
  }

  private createFromForm(): IListDetails {
    const entity = {
      ...new ListDetails(),
      id: this.editForm.get(['id']).value,
      listId: this.editForm.get(['listId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IListDetails>>) {
    result.subscribe((res: HttpResponse<IListDetails>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackReproductionListsById(index: number, item: IReproductionLists) {
    return item.id;
  }
}
