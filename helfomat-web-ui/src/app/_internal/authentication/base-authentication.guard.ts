import {environment} from "../../../environments/environment";
import {NullValidationHandler, OAuthService} from "angular-oauth2-oidc";
import {RouterStateSnapshot} from "@angular/router";

export class BaseAuthenticationGuard {

    constructor(protected oAuthService: OAuthService) {
    }

    protected setupOAuthContext(state: RouterStateSnapshot) {
        let config = {...environment.auth};
        config.redirectUri = location.origin + BaseAuthenticationGuard.removeOAuthUrlParams(state.url);
        config.issuer = BaseAuthenticationGuard.getProtocol() + BaseAuthenticationGuard.resolveAuthenticationProviderHostName(window.location.host) + config.issuer;
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

    public static getProtocol(): string {
        if (environment.useHttps) {
            return "https://";
        } else {
            return "http://";
        }
    }

    public static resolveAuthenticationProviderHostName(currentHost: string): string {
        if (currentHost.startsWith("localhost")) {
            return "localhost:8085";
        } else {
            if (currentHost.startsWith("www.")) {
                currentHost = currentHost.substring(4);
            }
            return "login." + currentHost;
        }
    }

}