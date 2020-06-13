import {environment} from "../../../environments/environment";

export enum Roles {
    ADMIN = "helfomat_admin",
    REVIEWER = "helfomat_reviewer"
}

export function hasRole(token: string, role: Roles) {
    return getRoles(token).indexOf(role) >= 0;
}

export function getRoles(token: string): string[] {
    let jwtToken = parseJwtToken(token);
    return jwtToken && jwtToken.realm_access && jwtToken.realm_access.roles || [];
}

export function parseJwtToken(token: string) {
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
}

export function getProtocol(): string {
    if (environment.useHttps) {
        return "https://";
    } else {
        return "http://";
    }
}

export function resolveAuthenticationProviderHostName(currentHost?: string): string {
    currentHost = currentHost || window.location.host;
    if (currentHost.startsWith("localhost")) {
        return "localhost:8085";
    } else {
        if (currentHost.startsWith("www.")) {
            currentHost = currentHost.substring(4);
        } else if (currentHost.startsWith("test.")) {
            currentHost = currentHost.substring(5);
        }
        return "login." + currentHost;
    }
}

export function getOAuth2Configuration() {
    let hostWithContextPath = window.location.host;
    if (!hostWithContextPath.startsWith("localhost")) {
        hostWithContextPath += '/helf-o-mat';
    }
    return {
        ...environment.auth,
        issuer: resolveAuthenticationProviderUrl(),
        redirectUri: `${getProtocol()}${hostWithContextPath}/authenticate`
    };
}

export function resolveAuthenticationProviderUrl(currentHost?: string): string {
    return getProtocol() + resolveAuthenticationProviderHostName(currentHost) + environment.auth.issuer;
}