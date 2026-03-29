import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {Appbar} from "./appbar/appbar";
import {AuthService} from "../service/auth";

@Component({
  selector: 'app-root',
    imports: [RouterOutlet, Appbar],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  constructor(private router: Router, private auth: AuthService) {
    if (!auth.isLoggedIn()) {
      router.navigate(['/register']);
    }
  }
}
