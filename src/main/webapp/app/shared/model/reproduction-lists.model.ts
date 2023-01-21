export interface IReproductionLists {
  id?: number;
  name?: string;
  pictureContentType?: string;
  picture?: any;
}

export class ReproductionLists implements IReproductionLists {
  constructor(public id?: number, public name?: string, public pictureContentType?: string, public picture?: any) {}
}
