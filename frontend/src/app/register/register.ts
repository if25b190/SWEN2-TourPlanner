import {Component, model} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {AuthService} from "../../service/auth";
import {ToastrService} from "ngx-toastr";
import {Router, RouterLink} from "@angular/router";

@Component({
    selector: 'app-register',
    imports: [
        FormsModule,
        RouterLink
    ],
    templateUrl: './register.html',
    styleUrl: './register.scss'
})
export class Register {
    username = model<string>('');
    password = model<string>('');
    passwordConfirm = model<string>('');

    constructor(private router: Router, private auth: AuthService, private toastr: ToastrService) {
    }

    isUsernameValid(): boolean {
        return this.username().length >= 4;
    }

    isPasswordValid(): boolean {
        return this.password().length >= 8;
    }

    isPasswordConfirmValid(): boolean {
        return this.passwordConfirm().length > 0 && this.password() === this.passwordConfirm();
    }

    isFormValid(): boolean {
        return this.isUsernameValid() && this.isPasswordValid() && this.isPasswordConfirmValid();
    }

    register() {
        this.auth.register(this.username(), this.password()).subscribe({
            next: () => {
                this.toastr.success("Account registered successfully!");
                this.router.navigate(['/login']);
            },
            error: (err) => {
                if (err.status === 400) {
                    this.toastr.error("Account already exists!");
                } else {
                    this.toastr.error("Server error!");
                }
            }
        });
    }
}
