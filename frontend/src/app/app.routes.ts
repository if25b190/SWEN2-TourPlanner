import { Routes } from '@angular/router';
import {Home} from "./home/home";
import {Register} from "./register/register";
import {NotFound} from "./not-found/not-found";
import { Profile } from './profile/profile';

export const routes: Routes = [
    { path: 'profile', component: Profile },
    { path: 'home', component: Home },
    { path: 'register', component: Register },
    { path: 'terms', component: NotFound },
    { path: '**', redirectTo: 'home', pathMatch: 'full' },
];
