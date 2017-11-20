import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    animations: [
        trigger('menu', [
            state('visible', style({
                height: '100%'
            })),
            state('hidden', style({
                height: '0'
            })),
            transition('visible => hidden', animate('400ms ease-in-out')),
            transition('hidden => visible', animate('400ms ease-in-out'))
        ])
    ]
})
export class AppComponent {

    public menuVisible: 'visible' | 'hidden' = 'hidden';

    constructor(translate: TranslateService) {
        translate.setDefaultLang('de');
        translate.use('de');
    }

    public toggleMenu() {
        this.menuVisible = this.menuVisible == 'hidden' ? 'visible' : 'hidden';
    }

}
