// Typings reference file, see links for more information
// https://github.com/typings/typings
// https://www.typescriptlang.org/docs/handbook/writing-declaration-files.html

declare var System: any;

interface UmamiTracker {
    track(event?: string, data?: Record<string, string | number>): void;
}

declare var umami: UmamiTracker | undefined;
