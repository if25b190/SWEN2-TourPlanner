import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    constructor(private router: Router, private http: HttpClient) {
    }

    register(username: String, password: String, callback: (res: HttpResponse<String>) => void) {
        this.http.post('http://localhost:8080/api/v1/register', {
            username: username,
            password: password
        }, {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        }).subscribe({
            next: (res: HttpResponse<String>) => {
                callback(res);
            },
            error: (err: HttpResponse<String>) => {
                callback(err);
            }
        });
    }

    login(username: String, password: String, isRemember: boolean, callback: (res: HttpResponse<String>) => void) {
        let formData = new HttpParams()
            .set('username', username as string)
            .set('password', password as string);
        if (isRemember) {
            formData = formData.set('remember-me', 'on');
        }
        this.http.post('http://localhost:8080/api/v1/login', formData.toString(), {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).subscribe({
            next: (res: HttpResponse<String>) => {
                if (isRemember) {
                    localStorage.setItem("isLoggedIn", "true");
                } else {
                    sessionStorage.setItem("isLoggedIn", "true");
                }
                callback(res);
            },
            error: (err: HttpResponse<String>) => {
                callback(err);
            }
        });
    }

    logout() {
        this.http.get('http://localhost:8080/api/v1/logout', {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        }).subscribe({
            complete: () => {
                this.router.navigate(['/register']);
                sessionStorage.removeItem("isLoggedIn");
                localStorage.removeItem("isLoggedIn");
            }
        });
    }

    update(email: String, password: String, passwordOld: String, callback: (res: HttpResponse<String>) => void) {
        this.http.post('http://localhost:8080/api/v1/profile', {
            email: email,
            password: password,
            passwordOld: passwordOld
        }, {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        }).subscribe({
            next: (res) => {
                if (res.status === 200) {
                    if (localStorage.getItem("currentEmail")) {
                        localStorage.setItem("currentEmail", email.toString());
                    } else {
                        sessionStorage.setItem("currentEmail", email.toString());
                    }
                }
                callback(res);
            },
            error: (err: HttpResponse<String>) => {
                callback(err);
            }
        });
    }

    delete(callback: (res: HttpResponse<String>) => void) {
        this.http.delete('http://localhost:8080/api/v1/delete', {
            observe: 'response',
            responseType: 'text',
            withCredentials: true,
        }).subscribe({
            next: (res: HttpResponse<String>) => {
                callback(res);
            },
            error: (err: HttpResponse<String>) => {
                callback(err);
            }
        });
    }

    isLoggedIn(): boolean {
        return sessionStorage.getItem("isLoggedIn") === "true" || localStorage.getItem("isLoggedIn") === "true";
    }
}
