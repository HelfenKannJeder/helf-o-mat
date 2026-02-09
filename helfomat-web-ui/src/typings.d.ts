// Typings reference file, see links for more information
// https://github.com/typings/typings
// https://www.typescriptlang.org/docs/handbook/writing-declaration-files.html

declare var System: any;

interface UmamiPageViewProperties {
    hostname: string;
    language: string;
    referrer: string;
    screen: string;
    title: string;
    url: string;
    website: string;
}

interface UmamiTracker {
    track(event?: string, data?: Record<string, string | number>): void;
    track(callback: (props: UmamiPageViewProperties) => UmamiPageViewProperties): void;
}

declare var umami: UmamiTracker | undefined;
