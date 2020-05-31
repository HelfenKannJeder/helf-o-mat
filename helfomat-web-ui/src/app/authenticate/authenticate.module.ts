import {NgModule} from "@angular/core";
import {AuthenticateComponent} from "./authenticate.component";

@NgModule({
    declarations: [
        AuthenticateComponent
    ],
    exports: [
        AuthenticateComponent
    ]
})
export class AuthenticateModule {
}