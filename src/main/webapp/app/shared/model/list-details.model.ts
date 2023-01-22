import { ISong } from 'app/shared/model/Song.model';

export interface IListDetails {
  id?: number;
  listId?: number;
  Song?: ISong[];
}

export class ListDetails implements IListDetails {
  constructor(public id?: number, public listId?: number, public Song?: ISong[]) {}
}
