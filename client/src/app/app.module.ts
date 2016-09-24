import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {QuestionComponent} from "./question/question.component";
import {MapComponent} from "./map/map.component";
import {routing, appRoutingProviders} from "./app.routing";
import {ResultComponent} from "./result/result.component";
import {QuestionOverviewComponent} from "./question/questionOverview.component";


@NgModule({
    declarations: [
        AppComponent,
        QuestionComponent,
        QuestionOverviewComponent,
        MapComponent,
        ResultComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        routing
    ],
    providers: [
        appRoutingProviders
    ],
    bootstrap: [
        AppComponent
    ]
})
export class AppModule {
}
