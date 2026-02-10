import './polyfills.ts';

import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {enableProdMode} from '@angular/core';
import {environment} from './environments/environment';
import {AppModule} from './app/';
import {KioskModule} from "./app/kiosk.module";

if (environment.production) {
  enableProdMode();
}

function resolveUmamiConfig(): { src: string; websiteId: string } | undefined {
    if (!environment.umami) return undefined;
    const host = window.location.host;
    for (const [pattern, config] of Object.entries(environment.umami)) {
        if (host.includes(pattern)) {
            return config;
        }
    }
    return undefined;
}

const umamiConfig = resolveUmamiConfig();
if (umamiConfig) {
    const script = document.createElement('script');
    script.defer = true;
    script.src = umamiConfig.src;
    script.setAttribute('data-website-id', umamiConfig.websiteId);
    script.setAttribute('data-auto-track', 'false');
    document.head.appendChild(script);
}

if (environment.kiosk) {
    platformBrowserDynamic().bootstrapModule(KioskModule);
} else {
    platformBrowserDynamic().bootstrapModule(AppModule);
}
