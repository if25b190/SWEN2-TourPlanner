import {Component, model} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../../service/auth";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  username = model<string>('');
  password = model<string>('');
  isRemember = model<boolean>(false);

  constructor(private router: Router, private auth: AuthService, private toastr: ToastrService) {
  }

  isUsernameValid(): boolean {
    return this.username().length >= 4;
  }

  isPasswordValid(): boolean {
    return this.password().length >= 8;
  }

  isFormValid(): boolean {
    return this.isUsernameValid() && this.isPasswordValid();
  }

  login() {
    this.auth.login(this.username(), this.password(), this.isRemember()).subscribe({
      next: () => {
        this.router.navigate(['/']);
        this.toastr.success("Logged in successfully!");
      },
      error: (err) => {
        if (err.status === 400 || (typeof err.error === "string" && err.error.includes("Account not found"))) {
          this.toastr.error("Invalid username or password!");
        } else {
          this.toastr.error("Server error!");
        }
      }
    });
  }
}
