export const Difficulty = {
    Easy: 'Easy',
    Intermediate: 'Intermediate',
    Hard: 'Hard',
    Hell: 'Hell'
} as const;
export type Difficulty = (typeof Difficulty)[keyof typeof Difficulty];

export const TourLogRatings = [1, 2, 3, 4, 5] as const;
export type TourLogRating = (typeof TourLogRatings)[number];

export interface TourLogModel {
    uuid?: string,
    creator?: string,
    tour?: string,
    creationDate?: Date,
    comment?: string,
    difficulty?: string,
    distance?: number,
    totalTime?: number,
    rating?: TourLogRating
}
