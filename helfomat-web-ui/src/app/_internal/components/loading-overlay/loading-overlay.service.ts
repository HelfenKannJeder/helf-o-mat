import {ComponentRef, Injectable, InjectionToken, Injector} from "@angular/core";
import {Overlay, OverlayConfig, OverlayRef} from "@angular/cdk/overlay";
import {LoadingOverlayComponent} from "./loading-overlay.component";
import {ComponentPortal} from "@angular/cdk/portal";

export const MESSAGE_DATA = new InjectionToken<string>('MESSAGE_DATA');

@Injectable({providedIn: 'root'})
export class LoadingOverlayService {

    private overlayRef: OverlayRef = null;

    constructor(
        private overlay: Overlay
    ) {
    }

    public open(message: string = '') {
        if (this.overlayRef != null) {
            return;
        }
        const positionStrategy = this.overlay.position()
            .global()
            .centerHorizontally()
            .centerVertically();

        const dialogConfig: OverlayConfig = new OverlayConfig({
            hasBackdrop: true,
            backdropClass: 'dark-backdrop',
            scrollStrategy: this.overlay.scrollStrategies.block(),
            positionStrategy
        });
        this.overlayRef = this.overlay.create(dialogConfig);
        LoadingOverlayService.attachDialogContainer(this.overlayRef, message);
    }

    public close() {
        if (this.overlayRef != null) {
            this.overlayRef.dispose();
            this.overlayRef = null;
        }
    }
    private static attachDialogContainer(overlayRef: OverlayRef, message: string): LoadingOverlayComponent {
        const injector = Injector.create({
            providers: [
                {provide: MESSAGE_DATA, useValue: message}
            ]
        });
        const containerPortal = new ComponentPortal(LoadingOverlayComponent, null, injector);
        const containerRef: ComponentRef<LoadingOverlayComponent> = overlayRef.attach(containerPortal);
        return containerRef.instance;
    }

}