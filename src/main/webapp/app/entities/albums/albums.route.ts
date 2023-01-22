import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Album } from 'app/shared/model/Album.model';
import { AlbumService } from './Album.service';
import { AlbumComponent } from './Album.component';
import { AlbumDetailComponent } from './Album-detail.component';
import { AlbumUpdateComponent } from './Album-update.component';
import { AlbumDeletePopupComponent } from './Album-delete-dialog.component';
import { IAlbum } from 'app/shared/model/Album.model';

@Injectable({ providedIn: 'root' })
export class AlbumResolve implements Resolve<IAlbum> {
  constructor(private service: AlbumService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAlbum> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Album>) => response.ok),
        map((Album: HttpResponse<Album>) => Album.body)
      );
    }
    return of(new Album());
  }
}

export const AlbumRoute: Routes = [
  {
    path: '',
    component: AlbumComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.Album.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AlbumDetailComponent,
    resolve: {
      Album: AlbumResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.Album.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AlbumUpdateComponent,
    resolve: {
      Album: AlbumResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.Album.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AlbumUpdateComponent,
    resolve: {
      Album: AlbumResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.Album.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const AlbumPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AlbumDeletePopupComponent,
    resolve: {
      Album: AlbumResolve
    },
    data: {
      authorities: [],
      pageTitle: 'mFinder2App.Album.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
