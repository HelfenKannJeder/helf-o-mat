import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {OAuthService} from "angular-oauth2-oidc";
import {BaseAuthenticationGuard} from "./base-authentication.guard";

@Injectable({providedIn: 'root'})
export class NoAuthGuard extends BaseAuthenticationGuard implements CanActivate {

    constructor(private router: Router,
                oAuthService: OAuthService) {
        super(oAuthService);
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
        this.setupOAuthContext(state);
        return this.oAuthService.loadDiscoveryDocumentAndTryLogin()
            .then(() => {
                if (!this.oAuthService.hasValidAccessToken()) {
                    // Ensure no tokens are left to avoid unauthenticated
                    this.oAuthService.logOut(true);
                }
                return true;
            });
    }
}