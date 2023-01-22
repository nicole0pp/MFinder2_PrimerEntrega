export interface IMusicGenre {
  id?: number;
  name?: string;
  type?: string;
}

export class MusicGenre implements IMusicGenre {
  constructor(public id?: number, public name?: string, public type?: string) {}
}
