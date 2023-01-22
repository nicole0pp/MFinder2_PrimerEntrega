export interface ISongs {
  id?: number;
  name?: string;
  pictureContentType?: string;
  picture?: any;
  duration?: number;
  audioContentType?: string;
  audio?: any;
  artists?: string;
  musicGenreId?: number;
  AlbumId?: number;
  listDetailsId?: number;
}

export class Songs implements ISongs {
  constructor(
    public id?: number,
    public name?: string,
    public pictureContentType?: string,
    public picture?: any,
    public duration?: number,
    public audioContentType?: string,
    public audio?: any,
    public artists?: string,
    public musicGenreId?: number,
    public AlbumId?: number,
    public listDetailsId?: number
  ) {}
}
