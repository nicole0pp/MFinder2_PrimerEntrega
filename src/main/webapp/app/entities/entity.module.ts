import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'songs',
        loadChildren: './songs/songs.module#MFinder2SongsModule'
      },
      {
        path: 'albums',
        loadChildren: './albums/albums.module#MFinder2AlbumsModule'
      },
      {
        path: 'reproduction-lists',
        loadChildren: './reproduction-lists/reproduction-lists.module#MFinder2FavoriteListModule'
      },
      {
        path: 'list-details',
        loadChildren: './list-details/list-details.module#MFinder2ListDetailsModule'
      },
      {
        path: 'music-genres',
        loadChildren: './music-genres/music-genres.module#MFinder2MusicGenresModule'
      },
      {
        path: 'artist',
        loadChildren: './artist/artist.module#MFinder2ArtistModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2EntityModule {}
