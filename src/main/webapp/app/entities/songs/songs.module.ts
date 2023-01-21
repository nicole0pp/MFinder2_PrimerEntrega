import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MFinder2SharedModule } from 'app/shared';
import {
  SongsComponent,
  SongsDetailComponent,
  SongsUpdateComponent,
  SongsDeletePopupComponent,
  SongsDeleteDialogComponent,
  songsRoute,
  songsPopupRoute
} from './';

const ENTITY_STATES = [...songsRoute, ...songsPopupRoute];

@NgModule({
  imports: [MFinder2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [SongsComponent, SongsDetailComponent, SongsUpdateComponent, SongsDeleteDialogComponent, SongsDeletePopupComponent],
  entryComponents: [SongsComponent, SongsUpdateComponent, SongsDeleteDialogComponent, SongsDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2SongsModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
