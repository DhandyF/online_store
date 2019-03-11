import { Moment } from 'moment';

export interface IWeight {
    id?: number;
    date?: Moment;
    weight?: number;
}

export class Weight implements IWeight {
    constructor(public id?: number, public date?: Moment, public weight?: number) {}
}
