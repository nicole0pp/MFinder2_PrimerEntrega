import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MusicGenres } from 'app/shared/model/music-genres.model';
import { MusicGenresService } from './music-genres.service';
import { MusicGenresComponent } from './music-genres.component';
import { MusicGenresDetailComponent } from './music-genres-detail.component';
import { MusicGenresUpdateComponent } from './music-genres-update.component';
import { MusicGenresDeletePopupComponent } from './music-genres-delete-dialog.component';
import { IMusicGenres } from 'app/shared/model/music-genres.model';

@Injectable({ providedIn: 'root' })
export class MusicGenresResolve implements Resolve<IMusicGenres> {
  constructor(private service: MusicGenresService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMusicGenres> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MusicGenres>) => response.ok),
        map((musicGenres: HttpResponse<MusicGenres>) => musicGenres.body)
      );
    }
    return of(new MusicGenres());
  }
}

export const musicGenresRoute: Routes = [
  {
    path: '',
    component: MusicGenresComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.musicGenres.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MusicGenresDetailComponent,
    resolve: {
      musicGenres: MusicGenresResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.musicGenres.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MusicGenresUpdateComponent,
    resolve: {
      musicGenres: MusicGenresResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.musicGenres.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MusicGenresUpdateComponent,
    resolve: {
      musicGenres: MusicGenresResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.musicGenres.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const musicGenresPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MusicGenresDeletePopupComponent,
    resolve: {
      musicGenres: MusicGenresResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.musicGenres.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
