import {Component, OnInit} from "@angular/core";
import {OAuthService} from "angular-oauth2-oidc";

@Component({
    selector: 'profile',
    templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

    public name: string;

    constructor(private oAuthService: OAuthService) {
    }

    ngOnInit(): void {
        const accessToken = this.oAuthService.getAccessToken();
        const parsedToken = this.parseJwt(accessToken);
        this.name = parsedToken.name;
    }

    parseJwt(token: string): {name: string} {
        try {
            return JSON.parse(atob(token.split('.')[1]));
        } catch (e) {
            return null;
        }
    };

}