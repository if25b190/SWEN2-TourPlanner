import { Injectable } from '@angular/core';
import {environment} from "../environments/environment";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {TourLogModel} from "../model/m_tourlog";

@Injectable({
  providedIn: 'root',
})
export class TourLogService {
  private readonly baseApiUrl = environment.baseApiUrl;

  constructor(private http: HttpClient, private toastr: ToastrService) {
  }

  fetchAllTourLogs(uuid: string, callback: (tour_logs: TourLogModel[]) => void) {
    this.http.get(`${this.baseApiUrl}/api/v1/tours/${uuid}/logs`, {
      observe: 'response',
      responseType: 'json',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<Object>) => {
        const data = res.body as TourLogModel[];
        callback(data);
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to fetch tourlogs!");
      }
    });
  }

  createTourLog(tour_log: TourLogModel, callback: (tour_log: TourLogModel) => void) {
    this.http.post(`${this.baseApiUrl}/api/v1/logs`, tour_log, {
      observe: 'response',
      responseType: 'json',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<Object>) => {
        const data = res.body as TourLogModel;
        callback(data);
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to create tourlog!");
      }
    });
  }

  updateTourLog(tour_log: TourLogModel, callback: (tour_log: TourLogModel) => void) {
    this.http.put(`${this.baseApiUrl}/api/v1/logs`, tour_log, {
      observe: 'response',
      responseType: 'json',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<Object>) => {
        const data = res.body as TourLogModel;
        callback(data);
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to update tourlog!");
      }
    });
  }

  deleteTourLog(uuid: string, callback: () => void) {
    this.http.delete(`${this.baseApiUrl}/api/v1/logs/${uuid}`, {
      observe: 'response',
      responseType: 'text',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<String>) => {
        callback();
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to delete tourlog!");
      }
    });
  }
}
