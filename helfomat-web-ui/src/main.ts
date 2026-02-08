import './polyfills.ts';

import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {enableProdMode} from '@angular/core';
import {environment} from './environments/environment';
import {AppModule} from './app/';
import {KioskModule} from "./app/kiosk.module";

if (environment.production) {
  enableProdMode();
}

if (environment.umami) {
    const script = document.createElement('script');
    script.defer = true;
    script.src = environment.umami.src;
    script.setAttribute('data-website-id', environment.umami.websiteId);
    document.head.appendChild(script);
}

if (environment.kiosk) {
    platformBrowserDynamic().bootstrapModule(KioskModule);
} else {
    platformBrowserDynamic().bootstrapModule(AppModule);
}
