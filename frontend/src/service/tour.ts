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
}
