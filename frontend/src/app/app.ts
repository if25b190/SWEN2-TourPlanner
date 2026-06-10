import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {Appbar} from "./appbar/appbar";

@Component({
  selector: 'app-root',
    imports: [RouterOutlet, Appbar],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
}
