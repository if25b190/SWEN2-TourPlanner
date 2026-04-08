export interface TourModel {
    uuid?: string,
    name?: string,
    description?: string,
    from?: MapLocation,
    to?: MapLocation,
    transportType?: string,
    distance?: string,
    estimatedTime?: string,
    creator?: string,
    popularity?: number,
    childfriendliness?: number,
}

export interface MapLocation {
    latitude: number,
    longitude: number
}
