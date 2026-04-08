import { Routes } from '@angular/router';
import {Home} from "./home/home";
import {Register} from "./register/register";
import {NotFound} from "./not-found/not-found";
import { Profile } from './profile/profile';
import {Login} from "./login/login";
import {Tours} from "./tours/tours";

export const routes: Routes = [
    { path: 'tours', component: Tours },
    { path: 'profile', component: Profile },
    { path: 'register', component: Register },
    { path: 'login', component: Login },
    { path: 'terms', component: NotFound },
    { path: '**', redirectTo: 'tours', pathMatch: 'full' },
];
