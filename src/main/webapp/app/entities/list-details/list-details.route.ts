import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ListDetails } from 'app/shared/model/list-details.model';
import { ListDetailsService } from './list-details.service';
import { ListDetailsComponent } from './list-details.component';
import { ListDetailsDetailComponent } from './list-details-detail.component';
import { ListDetailsUpdateComponent } from './list-details-update.component';
import { ListDetailsDeletePopupComponent } from './list-details-delete-dialog.component';
import { IListDetails } from 'app/shared/model/list-details.model';

@Injectable({ providedIn: 'root' })
export class ListDetailsResolve implements Resolve<IListDetails> {
  constructor(private service: ListDetailsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IListDetails> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ListDetails>) => response.ok),
        map((listDetails: HttpResponse<ListDetails>) => listDetails.body)
      );
    }
    return of(new ListDetails());
  }
}

export const listDetailsRoute: Routes = [
  {
    path: '',
    component: ListDetailsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.listDetails.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ListDetailsDetailComponent,
    resolve: {
      listDetails: ListDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.listDetails.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ListDetailsUpdateComponent,
    resolve: {
      listDetails: ListDetailsResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.listDetails.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ListDetailsUpdateComponent,
    resolve: {
      listDetails: ListDetailsResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.listDetails.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const listDetailsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ListDetailsDeletePopupComponent,
    resolve: {
      listDetails: ListDetailsResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.listDetails.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
