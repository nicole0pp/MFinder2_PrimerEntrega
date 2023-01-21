import { ISongs } from 'app/shared/model/songs.model';

export interface IListDetails {
  id?: number;
  listId?: number;
  songs?: ISongs[];
}

export class ListDetails implements IListDetails {
  constructor(public id?: number, public listId?: number, public songs?: ISongs[]) {}
}
