import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {environment} from "../environments/environment";
import {BehaviorSubject, catchError, finalize, map, Observable, of, tap} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly baseApiUrl = environment.baseApiUrl;
    private readonly loggedInSubject = new BehaviorSubject<boolean>(false);
    readonly loggedIn$ = this.loggedInSubject.asObservable();

    constructor(private router: Router, private http: HttpClient) {
    }

    register(username: string, password: string): Observable<HttpResponse<string>> {
        return this.http.post(`${this.baseApiUrl}/api/v1/register`, {
            username: username,
            password: password
        }, {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        });
    }

    login(username: string, password: string, isRemember: boolean): Observable<HttpResponse<string>> {
        let formData = new HttpParams()
            .set('username', username)
            .set('password', password);
        if (isRemember) {
            formData = formData.set('remember-me', 'on');
        }
        return this.http.post(`${this.baseApiUrl}/api/v1/login`, formData.toString(), {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).pipe(tap(() => this.loggedInSubject.next(true)));
    }

    logout(): Observable<HttpResponse<string>> {
        return this.http.get(`${this.baseApiUrl}/api/v1/logout`, {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        }).pipe(finalize(() => this.clearLoginState()));
    }

    update(email: string, password: string, passwordOld: string): Observable<HttpResponse<string>> {
        return this.http.post(`${this.baseApiUrl}/api/v1/profile`, {
            email: email,
            password: password,
            passwordOld: passwordOld
        }, {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        });
    }

    delete(): Observable<HttpResponse<string>> {
        return this.http.delete(`${this.baseApiUrl}/api/v1/delete`, {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        }).pipe(tap(() => this.clearLoginState()));
    }

    refreshLoginState(): Observable<boolean> {
        return this.http.get(`${this.baseApiUrl}/api/v1/profile`, {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        }).pipe(
            map(() => true),
            catchError(() => of(false)),
            tap((loggedIn) => this.loggedInSubject.next(loggedIn))
        );
    }

    isLoggedIn(): boolean {
        return this.loggedInSubject.value;
    }

    private clearLoginState(): void {
        this.loggedInSubject.next(false);
        this.router.navigate(['/login']);
    }
}
