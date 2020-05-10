import {NgModule} from "@angular/core";
import {AuthImagePipe} from "./auth-image.pipe";

@NgModule({
    declarations: [
        AuthImagePipe
    ],
    exports: [
        AuthImagePipe
    ]
})
export class AuthenticationModule {
}