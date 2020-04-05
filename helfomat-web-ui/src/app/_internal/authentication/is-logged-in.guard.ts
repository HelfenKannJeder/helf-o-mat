import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {NullValidationHandler, OAuthService} from "angular-oauth2-oidc";
import {environment} from "../../../environments/environment";

@Injectable({providedIn: 'root'})
export class IsLoggedInGuard implements CanActivate {

    constructor(private router: Router,
                private oAuthService: OAuthService) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
        let config = {...environment.auth};
        config.redirectUri = location.origin + state.url
            .replace(/[&?]code=[^&$]*/, '')
            .replace(/[&?]scope=[^&$]*/, '')
            .replace(/[&?]state=[^&$]*/, '')
            .replace(/[&?]session_state=[^&$]*/, '');
        config.issuer = IsLoggedInGuard.getProtocol() + IsLoggedInGuard.resolveAuthenticationProviderHostName(window.location.host) + config.issuer;
        this.oAuthService.configure(config);
        this.oAuthService.tokenValidationHandler = new NullValidationHandler();
        return this.oAuthService.loadDiscoveryDocumentAndLogin()
            .then(() => {
                return this.oAuthService.hasValidAccessToken();
            });
    }

    private static getProtocol(): string {
        if (environment.useHttps) {
            return "https://";
        } else {
            return "http://";
        }
    }

    private static resolveAuthenticationProviderHostName(currentHost: string): string {
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