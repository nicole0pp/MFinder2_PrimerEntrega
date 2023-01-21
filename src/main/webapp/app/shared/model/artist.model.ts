export interface IArtist {
  id?: number;
  artistic_name?: string;
}

export class Artist implements IArtist {
  constructor(public id?: number, public artistic_name?: string) {}
}
