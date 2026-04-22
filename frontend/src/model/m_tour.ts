export interface TourModel {
    uuid?: string,
    name?: string,
    description?: string,
    from?: MapLocation,
    to?: MapLocation,
    transportType?: string,
    distance?: number,
    estimatedTime?: string,
    creator?: string,
    popularity?: number,
    childfriendliness?: number,
    wayPoints?: number[][],
}

export interface MapLocation {
    latitude: number,
    longitude: number
}
