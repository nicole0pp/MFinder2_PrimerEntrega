import { Moment } from 'moment';

export interface IAlbum {
  id?: number;
  name?: string;
  pictureContentType?: string;
  picture?: any;
  publicationDate?: Moment;
}

export class Album implements IAlbum {
  constructor(
    public id?: number,
    public name?: string,
    public pictureContentType?: string,
    public picture?: any,
    public publicationDate?: Moment
  ) {}
}
