import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {OAuthService} from "angular-oauth2-oidc";
import {IsLoggedInGuard} from "./is-logged-in.guard";
import {hasRole, Roles} from "./util";

@Injectable({providedIn: 'root'})
export class IsReviewerGuard extends IsLoggedInGuard implements CanActivate {

    constructor(
        router: Router,
        oAuthService: OAuthService
    ) {
        super(router, oAuthService);
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
        return super.canActivate(route, state)
            .then(() => {
                const accessToken = this.oAuthService.getAccessToken();
                return hasRole(accessToken, Roles.ADMIN) || hasRole(accessToken, Roles.REVIEWER);
            });
    }
}