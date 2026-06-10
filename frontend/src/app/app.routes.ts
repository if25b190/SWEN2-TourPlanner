import { Routes } from '@angular/router';
import {Home} from "./home/home";
import {Register} from "./register/register";
import {NotFound} from "./not-found/not-found";
import { Profile } from './profile/profile';
import {Login} from "./login/login";
import {Tours} from "./tours/tours";
import {authGuard} from "./auth.guard";

export const routes: Routes = [
    { path: '', redirectTo: 'tours', pathMatch: 'full' },
    { path: 'tours', component: Tours, canActivate: [authGuard] },
    { path: 'profile', component: Profile, canActivate: [authGuard] },
    { path: 'register', component: Register },
    { path: 'login', component: Login },
    { path: 'terms', component: NotFound },
    { path: '**', redirectTo: 'tours', pathMatch: 'full' },
];
