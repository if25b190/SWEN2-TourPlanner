import { Injectable } from '@angular/core';
import {environment} from "../environments/environment";
import {TourLogModel} from "../model/m_tourlog";
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";

type TourLogApiModel = Omit<TourLogModel, 'totalTime'> & {
  totalTime?: string;
};

@Injectable({
  providedIn: 'root',
})
export class TourLogService {
  private readonly baseApiUrl = environment.baseApiUrl;

  constructor(private http: HttpClient) {
  }

  fetchAllTourLogs(uuid: string): Observable<TourLogModel[]> {
    return this.http.get<TourLogApiModel[]>(`${this.baseApiUrl}/api/v1/tours/${uuid}/logs`, {
      withCredentials: true,
    }).pipe(map((logs) => logs.map((log) => this.fromApiModel(log))));
  }

  createTourLog(tourLog: TourLogModel): Observable<TourLogModel> {
    return this.http.post<TourLogApiModel>(`${this.baseApiUrl}/api/v1/logs`, this.toApiModel(tourLog), {
      withCredentials: true,
    }).pipe(map((log) => this.fromApiModel(log)));
  }

  updateTourLog(tourLog: TourLogModel): Observable<TourLogModel> {
    return this.http.put<TourLogApiModel>(`${this.baseApiUrl}/api/v1/logs`, this.toApiModel(tourLog), {
      withCredentials: true,
    }).pipe(map((log) => this.fromApiModel(log)));
  }

  deleteTourLog(uuid: string): Observable<void> {
    return this.http.delete<void>(`${this.baseApiUrl}/api/v1/logs/${uuid}`, {
      withCredentials: true,
    });
  }

  private fromApiModel(log: TourLogApiModel): TourLogModel {
    return {
      ...log,
      totalTime: this.parseTotalTime(log.totalTime),
    };
  }

  private toApiModel(log: TourLogModel): TourLogApiModel {
    return {
      ...log,
      totalTime: this.formatTotalTime(log.totalTime),
    };
  }

  private parseTotalTime(totalTime: string | undefined): number | undefined {
    if (!totalTime) {
      return undefined;
    }

    const [hours = 0, minutes = 0, seconds = 0] = totalTime.split(':').map(Number);
    return hours * 60 + minutes + Math.round(seconds / 60);
  }

  private formatTotalTime(totalTime: number | undefined): string | undefined {
    if (totalTime == undefined) {
      return undefined;
    }

    const roundedTotalTime = Math.floor(totalTime);
    const hours = Math.floor(roundedTotalTime / 60);
    const minutes = roundedTotalTime % 60;
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:00`;
  }
}
