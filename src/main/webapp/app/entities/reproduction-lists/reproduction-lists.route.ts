import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FavoriteList } from 'app/shared/model/reproduction-lists.model';
import { FavoriteListService } from './reproduction-lists.service';
import { FavoriteListComponent } from './reproduction-lists.component';
import { FavoriteListDetailComponent } from './reproduction-lists-detail.component';
import { FavoriteListUpdateComponent } from './reproduction-lists-update.component';
import { FavoriteListDeletePopupComponent } from './reproduction-lists-delete-dialog.component';
import { IFavoriteList } from 'app/shared/model/reproduction-lists.model';

@Injectable({ providedIn: 'root' })
export class FavoriteListResolve implements Resolve<IFavoriteList> {
  constructor(private service: FavoriteListService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFavoriteList> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<FavoriteList>) => response.ok),
        map((FavoriteList: HttpResponse<FavoriteList>) => FavoriteList.body)
      );
    }
    return of(new FavoriteList());
  }
}

export const FavoriteListRoute: Routes = [
  {
    path: '',
    component: FavoriteListComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.FavoriteList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FavoriteListDetailComponent,
    resolve: {
      FavoriteList: FavoriteListResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.FavoriteList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FavoriteListUpdateComponent,
    resolve: {
      FavoriteList: FavoriteListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.FavoriteList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FavoriteListUpdateComponent,
    resolve: {
      FavoriteList: FavoriteListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.FavoriteList.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const FavoriteListPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FavoriteListDeletePopupComponent,
    resolve: {
      FavoriteList: FavoriteListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.FavoriteList.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
