import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ProfileComponent} from "./profile.component";
import {manageRoutes} from "./profile.routing";

@NgModule({
    declarations: [
        ProfileComponent
    ],
    imports: [
        RouterModule.forChild(manageRoutes)
    ],
    providers: []
})
export class ProfileModule {
}