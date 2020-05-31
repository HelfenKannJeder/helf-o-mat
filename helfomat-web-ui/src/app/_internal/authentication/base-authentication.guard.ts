import {NullValidationHandler, OAuthService} from "angular-oauth2-oidc";
import {getOAuth2Configuration} from "./util";

export class BaseAuthenticationGuard {

    constructor(protected oAuthService: OAuthService) {
    }

    protected setupOAuthContext() {
        this.oAuthService.configure(getOAuth2Configuration());
        this.oAuthService.setupAutomaticSilentRefresh();
        this.oAuthService.tokenValidationHandler = new NullValidationHandler();
    }

    public static removeOAuthUrlParams(url: string) {
        return url
            .replace(/[&?]code=[^&$]*/, '')
            .replace(/[&?]scope=[^&$]*/, '')
            .replace(/[&?]state=[^&$]*/, '')
            .replace(/[&?]session_state=[^&$]*/, '');
    }


}