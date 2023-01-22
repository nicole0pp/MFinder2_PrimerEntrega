import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Song } from 'app/shared/model/Song.model';
import { SongService } from './Song.service';
import { SongComponent } from './Song.component';
import { SongDetailComponent } from './Song-detail.component';
import { SongUpdateComponent } from './Song-update.component';
import { SongDeletePopupComponent } from './Song-delete-dialog.component';
import { ISong } from 'app/shared/model/Song.model';

@Injectable({ providedIn: 'root' })
export class SongResolve implements Resolve<ISong> {
  constructor(private service: SongService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISong> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Song>) => response.ok),
        map((Song: HttpResponse<Song>) => Song.body)
      );
    }
    return of(new Song());
  }
}

export const SongRoute: Routes = [
  {
    path: '',
    component: SongComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.Song.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SongDetailComponent,
    resolve: {
      Song: SongResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.Song.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SongUpdateComponent,
    resolve: {
      Song: SongResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.Song.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SongUpdateComponent,
    resolve: {
      Song: SongResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.Song.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const SongPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SongDeletePopupComponent,
    resolve: {
      Song: SongResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.Song.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
