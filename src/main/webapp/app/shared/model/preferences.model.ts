export const enum Units {
    KG = 'KG',
    LB = 'LB'
}

export interface IPreferences {
    id?: number;
    weeklyGoal?: number;
    weightUnits?: Units;
}

export class Preferences implements IPreferences {
    constructor(public id?: number, public weeklyGoal?: number, public weightUnits?: Units) {}
}
