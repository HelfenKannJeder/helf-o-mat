import {Directive, EventEmitter, HostBinding, HostListener, Output} from '@angular/core';

/**
 * source: https://medium.com/@mariemchabeni/angular-7-drag-and-drop-simple-file-uploadin-in-less-than-5-minutes-d57eb010c0dc
 */
@Directive({
    selector: '[dragDrop]'
})
export class DragDropDirective {

    @Output() onFileDropped = new EventEmitter<FileList>();

    @HostBinding('style.opacity')
    private opacity = '1';

    @HostListener('dragover', ['$event'])
    public onDragOver(evt: Event) {
        evt.preventDefault();
        evt.stopPropagation();
        this.opacity = '0.8'
    }

    @HostListener('dragleave', ['$event'])
    public onDragLeave(evt: Event) {
        evt.preventDefault();
        evt.stopPropagation();
        this.opacity = '1'
    }

    @HostListener('drop', ['$event'])
    public ondrop(evt: DragEvent) {
        evt.preventDefault();
        evt.stopPropagation();
        this.opacity = '1';
        let files = evt.dataTransfer.files;
        if (files.length > 0) {
            this.onFileDropped.emit(files)
        }
    }

}
