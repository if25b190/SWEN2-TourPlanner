import { Injectable } from '@angular/core';
import {environment} from "../environments/environment";
import {HttpClient} from "@angular/common/http";
import {TourModel} from "../model/m_tour";
import {map, Observable} from "rxjs";

interface PageResponse<T> {
  content: T[];
}

@Injectable({
  providedIn: 'root',
})
export class TourService {
  private readonly baseApiUrl = environment.baseApiUrl;

  constructor(private http: HttpClient) {
  }

  searchTours(searchTerm: string): Observable<TourModel[]> {
    return this.http.get<PageResponse<TourModel>>(`${this.baseApiUrl}/api/v1/tours/search/${encodeURIComponent(searchTerm)}`, {
      withCredentials: true,
    }).pipe(map((res) => res.content));
  }

  fetchAllTours(): Observable<TourModel[]> {
    return this.http.get<TourModel[]>(`${this.baseApiUrl}/api/v1/tours`, {
      withCredentials: true,
    });
  }

  createTour(tour: TourModel): Observable<TourModel> {
    return this.http.post<TourModel>(`${this.baseApiUrl}/api/v1/tours`, tour, {
      withCredentials: true,
    });
  }

  updateTour(tour: TourModel): Observable<TourModel> {
    return this.http.put<TourModel>(`${this.baseApiUrl}/api/v1/tours`, tour, {
      withCredentials: true,
    });
  }

  deleteTour(uuid: string): Observable<void> {
    return this.http.delete<void>(`${this.baseApiUrl}/api/v1/tours/${uuid}`, {
      withCredentials: true,
    });
  }

  getTourFileUrl(uuid: string, version = 0): string {
    const url = `${this.baseApiUrl}/api/v1/files/${uuid}.png`;
    return version > 0 ? `${url}?v=${version}` : url;
  }

  uploadTourFile(uuid: string, file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.baseApiUrl}/api/v1/files/${uuid}`, formData, {
      responseType: 'text',
      withCredentials: true,
    }).pipe(map(() => void 0));
  }

  uploadJsonFile(file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.baseApiUrl}/api/v1/tours/import`, formData, {
      responseType: 'text',
      withCredentials: true,
    }).pipe(map(() => void 0));
  }
}
