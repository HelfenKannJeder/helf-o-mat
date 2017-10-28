declare module 'node-js-marker-clusterer' {
    import OverlayView = google.maps.OverlayView;

    export default class MarkerClusterer extends OverlayView {
        public sizes: number[];
        public options: {};

        constructor(map: google.maps.Map, opt_markers: google.maps.Marker[], opt_options: {});

        public fitMapToMarkers(): void;

        public setStyles(styles: {}): void;

        public getStyles(): {};

        public isZoomOnClick(): boolean;

        public isAverageCenter(): boolean;

        public getMarkers(): google.maps.Marker[];

        public getTotalMarkers(): number;

        public setMaxZoom(maxZoom: number): void;

        public getMaxZoom(): number;

        public setCalculator(calculator: ((text: any[], index: number) => any)): void;

        public getCalculator(): ((text: any[], index: number) => any);

        public addMarkers(markers: google.maps.Marker[], opt_nodraw: boolean): void;

        public addMarker(marker: google.maps.Marker, opt_nodraw: boolean): void;

        public removeMarker(marker: google.maps.Marker, opt_nodraw: boolean): boolean;

        public removeMarkers(markers: google.maps.Marker[], opt_nodraw: boolean): void;

        public getTotalClusters(): number;

        public getMap(): google.maps.Map;

        public setMap(map: google.maps.Map): void;

        public getGridSize(): number;

        public setGridSize(size: number): void;

        public getMinClusterSize(): number;

        public setMinClusterSize(size: number): void;

        public getExtendedBounds(bounds: google.maps.LatLngBounds): google.maps.LatLngBounds;

        public clearMarkers(): void;

        public resetViewport(opt_hide: boolean): void;

        public repaint(): void;

        public redraw(): void;
    }
}