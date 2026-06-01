import { Component, inject } from '@angular/core';
import {RouterLink} from "@angular/router";
import {Theme} from "../theme/theme";
import {AuthService} from "../../service/auth";
import {toSignal} from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-appbar',
    imports: [
        RouterLink,
        Theme,
    ],
  templateUrl: './appbar.html',
  styleUrl: './appbar.scss'
})
export class Appbar {
    private readonly auth = inject(AuthService);
    readonly loggedIn = toSignal(this.auth.loggedIn$, {initialValue: this.auth.isLoggedIn()});

    isLogged(): boolean {
        return this.loggedIn();
    }

    logout() {
        this.auth.logout().subscribe({
            error: (err) => {
                console.error(err);
            }
        });
    }
}
