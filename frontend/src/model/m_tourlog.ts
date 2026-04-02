import {TourModel} from "./m_tour";

export const Difficulty = {
    Easy: 'Easy',
    Intermediate: 'Intermediate',
    Hard: 'Hard',
    Hell: 'Hell'
} as const;
export type Difficulty = (typeof Difficulty)[keyof typeof Difficulty];

export interface TourLogModel {
    uuid?: string,
    creator?: string,
    tour?: string,
    creationDate?: Date,
    comment?: string,
    difficulty?: string,
    distance?: number,
    totalTime?: Date,
    rating?: number
}
