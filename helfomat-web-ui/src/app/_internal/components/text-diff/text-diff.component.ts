import {Component, Input} from "@angular/core";

const JSDiff = require('diff');

@Component({
    selector: 'text-diff',
    templateUrl: './text-diff.component.html',
    styleUrls: [
        './text-diff.component.scss'
    ]
})
export class TextDiffComponent {

    @Input()
    private text1: string;

    @Input()
    private text2: string;

    @Input()
    private simple: boolean = false;

    public getDiff(): Array<{value: string, added: boolean, removed: boolean}> {
        if (this.simple) {
            if (this.text1 != this.text2) {
                return [
                    {
                        value: this.text1,
                        added: false,
                        removed: true
                    },
                    {
                        value: this.text2,
                        added: true,
                        removed: false
                    }
                ]
            } else {
                return [
                    {
                        value: this.text2,
                        added: false,
                        removed: false
                    }
                ]
            }
        } else {
            const text1 = this.text1 || "";
            const text2 = this.text2 || "";
            return JSDiff.diffChars(text1, text2);
        }
    }

}