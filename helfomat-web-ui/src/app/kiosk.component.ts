import {Component} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'app-root',
    templateUrl: './kiosk.component.html',
    styleUrls: []
})
export class KioskComponent {

    constructor(
        translate: TranslateService
    ) {
        translate.setDefaultLang('de');
        translate.use('de');
    }

}