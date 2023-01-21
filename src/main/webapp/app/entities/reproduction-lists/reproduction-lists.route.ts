import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ReproductionLists } from 'app/shared/model/reproduction-lists.model';
import { ReproductionListsService } from './reproduction-lists.service';
import { ReproductionListsComponent } from './reproduction-lists.component';
import { ReproductionListsDetailComponent } from './reproduction-lists-detail.component';
import { ReproductionListsUpdateComponent } from './reproduction-lists-update.component';
import { ReproductionListsDeletePopupComponent } from './reproduction-lists-delete-dialog.component';
import { IReproductionLists } from 'app/shared/model/reproduction-lists.model';

@Injectable({ providedIn: 'root' })
export class ReproductionListsResolve implements Resolve<IReproductionLists> {
  constructor(private service: ReproductionListsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IReproductionLists> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ReproductionLists>) => response.ok),
        map((reproductionLists: HttpResponse<ReproductionLists>) => reproductionLists.body)
      );
    }
    return of(new ReproductionLists());
  }
}

export const reproductionListsRoute: Routes = [
  {
    path: '',
    component: ReproductionListsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.reproductionLists.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ReproductionListsDetailComponent,
    resolve: {
      reproductionLists: ReproductionListsResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.reproductionLists.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ReproductionListsUpdateComponent,
    resolve: {
      reproductionLists: ReproductionListsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.reproductionLists.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ReproductionListsUpdateComponent,
    resolve: {
      reproductionLists: ReproductionListsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.reproductionLists.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const reproductionListsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ReproductionListsDeletePopupComponent,
    resolve: {
      reproductionLists: ReproductionListsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.reproductionLists.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
