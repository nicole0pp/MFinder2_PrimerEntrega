export interface IMusicGenres {
  id?: number;
  name?: string;
  type?: string;
}

export class MusicGenres implements IMusicGenres {
  constructor(public id?: number, public name?: string, public type?: string) {}
}
