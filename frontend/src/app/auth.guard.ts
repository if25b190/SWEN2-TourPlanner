import {inject} from "@angular/core";
import {CanActivateFn, Router} from "@angular/router";
import {map} from "rxjs";
import {AuthService} from "../service/auth";

export const authGuard: CanActivateFn = () => {
    const auth = inject(AuthService);
    const router = inject(Router);

    return auth.refreshLoginState().pipe(
        map((loggedIn) => loggedIn ? true : router.parseUrl('/login'))
    );
};
