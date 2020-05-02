import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {OAuthService} from "angular-oauth2-oidc";
import {hasRole, resolveAuthenticationProviderUrl, Roles} from "./_internal/authentication/util";

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

    constructor(
        translate: TranslateService,
        private oAuthService: OAuthService
    ) {
        translate.setDefaultLang('de');
        translate.use('de');
    }

    public toggleMenu() {
        this.menuVisible = this.menuVisible == 'hidden' ? 'visible' : 'hidden';
    }

    public getMyAccountUrl(): string {
        return resolveAuthenticationProviderUrl() + "/account";
    }

    public isReviewer(): boolean {
        let loggedIn = this.oAuthService.hasValidAccessToken();
        if (loggedIn) {
            const accessToken = this.oAuthService.getAccessToken();
            return hasRole(accessToken, Roles.REVIEWER) || hasRole(accessToken, Roles.ADMIN);
        }
        return false;
    }

}
