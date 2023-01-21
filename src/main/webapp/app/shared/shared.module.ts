import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MFinder2SharedLibsModule, MFinder2SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [MFinder2SharedLibsModule, MFinder2SharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [MFinder2SharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2SharedModule {
  static forRoot() {
    return {
      ngModule: MFinder2SharedModule
    };
  }
}
