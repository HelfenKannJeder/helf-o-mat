import {ComponentRef, Injectable} from "@angular/core";
import {Overlay, OverlayConfig, OverlayRef} from "@angular/cdk/overlay";
import {LoadingOverlayComponent} from "./loading-overlay.component";
import {ComponentPortal} from "@angular/cdk/portal";

@Injectable({providedIn: 'root'})
export class LoadingOverlayService {

    private overlayRef: OverlayRef = null;

    constructor(
        private overlay: Overlay
    ) {
    }

    public open() {
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
        LoadingOverlayService.attachDialogContainer(this.overlayRef);
    }

    public close() {
        if (this.overlayRef != null) {
            this.overlayRef.dispose();
            this.overlayRef = null;
        }
    }

    private static attachDialogContainer(overlayRef: OverlayRef): LoadingOverlayComponent {
        const containerPortal = new ComponentPortal(LoadingOverlayComponent);
        const containerRef: ComponentRef<LoadingOverlayComponent> = overlayRef.attach(containerPortal);
        return containerRef.instance;
    }

}