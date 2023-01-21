import { Moment } from 'moment';

export interface IAlbums {
  id?: number;
  name?: string;
  pictureContentType?: string;
  picture?: any;
  publicationDate?: Moment;
}

export class Albums implements IAlbums {
  constructor(
    public id?: number,
    public name?: string,
    public pictureContentType?: string,
    public picture?: any,
    public publicationDate?: Moment
  ) {}
}
