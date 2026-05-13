import { HttpClient, HttpResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-profile',
  imports: [],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile {
  private readonly baseApiUrl = environment.baseApiUrl;

  constructor(private http: HttpClient) {
    this.http
      .get(`${this.baseApiUrl}/api/v1/profile`, {
        observe: 'response',
        responseType: 'text',
        withCredentials: true,
      })
      .subscribe({
        next: (res: HttpResponse<String>) => {
          console.log('PROFILE DEBUG:');
          console.log(res.status);
          console.log(res.body);
        },
        error: (err: HttpResponse<String>) => {
          console.error(err);
        },
      });
  }
}
