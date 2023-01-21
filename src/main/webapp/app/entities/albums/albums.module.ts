import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MFinder2SharedModule } from 'app/shared';
import {
  AlbumsComponent,
  AlbumsDetailComponent,
  AlbumsUpdateComponent,
  AlbumsDeletePopupComponent,
  AlbumsDeleteDialogComponent,
  albumsRoute,
  albumsPopupRoute
} from './';

const ENTITY_STATES = [...albumsRoute, ...albumsPopupRoute];

@NgModule({
  imports: [MFinder2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [AlbumsComponent, AlbumsDetailComponent, AlbumsUpdateComponent, AlbumsDeleteDialogComponent, AlbumsDeletePopupComponent],
  entryComponents: [AlbumsComponent, AlbumsUpdateComponent, AlbumsDeleteDialogComponent, AlbumsDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2AlbumsModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
