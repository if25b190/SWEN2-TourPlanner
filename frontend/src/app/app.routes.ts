import { Routes } from '@angular/router';
import {Home} from "./home/home";
import {Register} from "./register/register";
import {NotFound} from "./not-found/not-found";

export const routes: Routes = [
    { path: 'home', component: Home },
    { path: 'register', component: Register },
    { path: 'terms', component: NotFound },
    { path: '**', redirectTo: 'home', pathMatch: 'full' },
];
