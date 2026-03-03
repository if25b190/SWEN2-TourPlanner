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
  username = model<String>('');
  password = model<String>('');
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
    this.auth.login(this.username(), this.password(), this.isRemember(), res => {
      if (res.status === 400 || (res.body && res.body.includes("Account not found"))) {
        this.toastr.error("Invalid username or password!");
      } else if (res.status === 200) {
        this.router.navigate(['/']);
        this.toastr.success("Logged in successfully!");
      } else {
        this.toastr.error("Server error!");
      }
    });
  }
}
