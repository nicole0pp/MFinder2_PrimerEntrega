import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MusicGenre } from 'app/shared/model/music-genres.model';
import { MusicGenreService } from './music-genres.service';
import { MusicGenreComponent } from './music-genres.component';
import { MusicGenreDetailComponent } from './music-genres-detail.component';
import { MusicGenreUpdateComponent } from './music-genres-update.component';
import { MusicGenreDeletePopupComponent } from './music-genres-delete-dialog.component';
import { IMusicGenre } from 'app/shared/model/music-genres.model';

@Injectable({ providedIn: 'root' })
export class MusicGenreResolve implements Resolve<IMusicGenre> {
  constructor(private service: MusicGenreService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMusicGenre> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MusicGenre>) => response.ok),
        map((MusicGenre: HttpResponse<MusicGenre>) => MusicGenre.body)
      );
    }
    return of(new MusicGenre());
  }
}

export const MusicGenreRoute: Routes = [
  {
    path: '',
    component: MusicGenreComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      defaultSort: 'id,asc',
      pageTitle: 'mFinder2App.MusicGenre.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MusicGenreDetailComponent,
    resolve: {
      MusicGenre: MusicGenreResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_USER_ARTIST'],
      pageTitle: 'mFinder2App.MusicGenre.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MusicGenreUpdateComponent,
    resolve: {
      MusicGenre: MusicGenreResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.MusicGenre.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MusicGenreUpdateComponent,
    resolve: {
      MusicGenre: MusicGenreResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.MusicGenre.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const MusicGenrePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MusicGenreDeletePopupComponent,
    resolve: {
      MusicGenre: MusicGenreResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'mFinder2App.MusicGenre.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
