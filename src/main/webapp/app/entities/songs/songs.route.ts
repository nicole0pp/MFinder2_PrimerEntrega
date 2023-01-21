import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Songs } from 'app/shared/model/songs.model';
import { SongsService } from './songs.service';
import { SongsComponent } from './songs.component';
import { SongsDetailComponent } from './songs-detail.component';
import { SongsUpdateComponent } from './songs-update.component';
import { SongsDeletePopupComponent } from './songs-delete-dialog.component';
import { ISongs } from 'app/shared/model/songs.model';

@Injectable({ providedIn: 'root' })
export class SongsResolve implements Resolve<ISongs> {
  constructor(private service: SongsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISongs> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Songs>) => response.ok),
        map((songs: HttpResponse<Songs>) => songs.body)
      );
    }
    return of(new Songs());
  }
}

export const songsRoute: Routes = [
  {
    path: '',
    component: SongsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.songs.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SongsDetailComponent,
    resolve: {
      songs: SongsResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.songs.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SongsUpdateComponent,
    resolve: {
      songs: SongsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.songs.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SongsUpdateComponent,
    resolve: {
      songs: SongsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.songs.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const songsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SongsDeletePopupComponent,
    resolve: {
      songs: SongsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.songs.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
