import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IListDetails, ListDetails } from 'app/shared/model/list-details.model';
import { ListDetailsService } from './list-details.service';
import { IFavoriteList } from 'app/shared/model/reproduction-lists.model';
import { FavoriteListService } from 'app/entities/reproduction-lists';

@Component({
  selector: 'jhi-list-details-update',
  templateUrl: './list-details-update.component.html'
})
export class ListDetailsUpdateComponent implements OnInit {
  listDetails: IListDetails;
  isSaving: boolean;

  lists: IFavoriteList[];

  editForm = this.fb.group({
    id: [],
    listId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected listDetailsService: ListDetailsService,
    protected FavoriteListService: FavoriteListService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ listDetails }) => {
      this.updateForm(listDetails);
      this.listDetails = listDetails;
    });
    this.FavoriteListService.query({ filter: 'listdetails-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IFavoriteList[]>) => mayBeOk.ok),
        map((response: HttpResponse<IFavoriteList[]>) => response.body)
      )
      .subscribe(
        (res: IFavoriteList[]) => {
          if (!this.listDetails.listId) {
            this.lists = res;
          } else {
            this.FavoriteListService.find(this.listDetails.listId)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IFavoriteList>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IFavoriteList>) => subResponse.body)
              )
              .subscribe(
                (subRes: IFavoriteList) => (this.lists = [subRes].concat(res)),
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

  trackFavoriteListById(index: number, item: IFavoriteList) {
    return item.id;
  }
}
