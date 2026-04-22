import { Injectable } from '@angular/core';
import {environment} from "../environments/environment";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {TourModel} from "../model/m_tour";
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root',
})
export class TourService {
  private readonly baseApiUrl = environment.baseApiUrl;

  constructor(private http: HttpClient, private toastr: ToastrService) {
  }

  searchTours(searchTerm: string, callback: (tours: TourModel[]) => void) {
    this.http.get(`${this.baseApiUrl}/api/v1/tours/search/${searchTerm}?size=9999`, {
      observe: 'response',
      responseType: 'json',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<Object>) => {
        const data = (res.body as any).content as TourModel[];
        callback(data);
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to fetch tours!");
      }
    });
  }

  fetchAllTours(callback: (tours: TourModel[]) => void) {
    this.http.get(`${this.baseApiUrl}/api/v1/tours`, {
      observe: 'response',
      responseType: 'json',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<Object>) => {
        const data = res.body as TourModel[];
        callback(data);
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to fetch tours!");
      }
    });
  }

  createTour(tour: TourModel, callback: (tour: TourModel) => void) {
    this.http.post(`${this.baseApiUrl}/api/v1/tours`, tour, {
      observe: 'response',
      responseType: 'json',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<Object>) => {
        const data = res.body as TourModel;
        callback(data);
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to create tour!");
      }
    });
  }

  updateTour(tour: TourModel, callback: (tour: TourModel) => void) {
    this.http.put(`${this.baseApiUrl}/api/v1/tours`, tour, {
      observe: 'response',
      responseType: 'json',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<Object>) => {
        const data = res.body as TourModel;
        callback(data);
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to update tour!");
      }
    });
  }

  deleteTour(uuid: string, callback: () => void) {
    this.http.delete(`${this.baseApiUrl}/api/v1/tours/${uuid}`, {
      observe: 'response',
      responseType: 'text',
      withCredentials: true,
    }).subscribe({
      next: (res: HttpResponse<String>) => {
        callback();
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to delete tour!");
      }
    });
  }

  getTourFileUrl(uuid: string, version = 0): string {
    const url = `${this.baseApiUrl}/api/v1/files/${uuid}.png`;
    return version > 0 ? `${url}?v=${version}` : url;
  }

  uploadTourFile(uuid: string, file: File, callback: () => void, errorCallback: () => void = () => {}) {
    const formData = new FormData();
    formData.append('file', file);

    this.http.post(`${this.baseApiUrl}/api/v1/files/${uuid}`, formData, {
      observe: 'response',
      responseType: 'text',
      withCredentials: true,
    }).subscribe({
      next: () => {
        callback();
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to upload tour image!");
        errorCallback();
      }
    });
  }

  uploadJsonFile(file: File, callback: () => void, errorCallback: () => void = () => {}) {
    const formData = new FormData();
    formData.append('file', file);

    this.http.post(`${this.baseApiUrl}/api/v1/tours/import`, formData, {
      observe: 'response',
      responseType: 'text',
      withCredentials: true,
    }).subscribe({
      next: () => {
        callback();
      },
      error: (err: HttpResponse<String>) => {
        console.error(err);
        this.toastr.error("Failed to upload JSON file!");
        errorCallback();
      }
    });
  }
}
