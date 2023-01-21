import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Albums } from 'app/shared/model/albums.model';
import { AlbumsService } from './albums.service';
import { AlbumsComponent } from './albums.component';
import { AlbumsDetailComponent } from './albums-detail.component';
import { AlbumsUpdateComponent } from './albums-update.component';
import { AlbumsDeletePopupComponent } from './albums-delete-dialog.component';
import { IAlbums } from 'app/shared/model/albums.model';

@Injectable({ providedIn: 'root' })
export class AlbumsResolve implements Resolve<IAlbums> {
  constructor(private service: AlbumsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAlbums> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Albums>) => response.ok),
        map((albums: HttpResponse<Albums>) => albums.body)
      );
    }
    return of(new Albums());
  }
}

export const albumsRoute: Routes = [
  {
    path: '',
    component: AlbumsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.albums.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AlbumsDetailComponent,
    resolve: {
      albums: AlbumsResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.albums.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AlbumsUpdateComponent,
    resolve: {
      albums: AlbumsResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.albums.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AlbumsUpdateComponent,
    resolve: {
      albums: AlbumsResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.albums.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const albumsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AlbumsDeletePopupComponent,
    resolve: {
      albums: AlbumsResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.albums.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
