import {environment} from "../../../environments/environment";
import {NullValidationHandler, OAuthService} from "angular-oauth2-oidc";
import {RouterStateSnapshot} from "@angular/router";
import {resolveAuthenticationProviderUrl} from "./util";

export class BaseAuthenticationGuard {

    constructor(protected oAuthService: OAuthService) {
    }

    protected setupOAuthContext(state: RouterStateSnapshot) {
        let config = {...environment.auth};
        config.redirectUri = location.origin + BaseAuthenticationGuard.removeOAuthUrlParams(state.url);
        config.issuer = resolveAuthenticationProviderUrl();
        this.oAuthService.configure(config);
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