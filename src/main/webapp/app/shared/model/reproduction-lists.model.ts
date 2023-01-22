export interface IFavoriteList {
  id?: number;
  name?: string;
  pictureContentType?: string;
  picture?: any;
}

export class FavoriteList implements IFavoriteList {
  constructor(public id?: number, public name?: string, public pictureContentType?: string, public picture?: any) {}
}
