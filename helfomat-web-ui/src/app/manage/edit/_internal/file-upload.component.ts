import {Component, EventEmitter, Output} from "@angular/core";
import {PictureId, PictureService} from "../../../_internal/resources/picture.service";
import {v4 as uuidv4} from "uuid";
import {BehaviorSubject, Subject} from "rxjs";
import {debounceTime, distinctUntilChanged} from "rxjs/operators";
import {Ng2ImgMaxService} from "ng2-img-max";

@Component({
    selector: 'file-upload',
    templateUrl: './file-upload.component.html',
    styleUrls: [
        './file-upload.component.scss'
    ]
})
export class FileUploadComponent {

    public uploadProgress: Subject<number> = new BehaviorSubject(null);

    @Output()
    private onPictureUploaded: EventEmitter<PictureId> = new EventEmitter<PictureId>();

    constructor(
        private ng2ImgMax: Ng2ImgMaxService,
        private pictureService: PictureService
    ) {
        this.uploadProgress
            .pipe(
                distinctUntilChanged(),
                debounceTime(5000),
            )
            .subscribe(percentage => {
                if (percentage == 100) {
                    this.uploadProgress.next(null);
                }
            })
    }

    uploadFile(event: FileList) {
        const numberOfImages = event.length;
        let aggregatedPercentComplete = 0;
        this.uploadProgress.next(1);
        for (let index: number = 0; index < numberOfImages; index++) {
            const pictureId: PictureId = {value: uuidv4()};
            this.ng2ImgMax.compressImage(event[index], 3).subscribe(
                result => {
                    const imageToUpload = new File([result], result.name, {type: event[index].type});
                    aggregatedPercentComplete += 50;
                    this.uploadProgress.next(aggregatedPercentComplete / numberOfImages);
                    this.pictureService.uploadPicture(pictureId, imageToUpload)
                        .subscribe(() => {
                            aggregatedPercentComplete += 50;
                            this.onPictureUploaded.emit(pictureId);
                            this.uploadProgress.next(aggregatedPercentComplete / numberOfImages);
                        });
                },
                error => {
                    console.error('Scaling went wrong, tyring to just upload it as is', error);
                    this.pictureService.uploadPicture(pictureId, event[index])
                        .subscribe(() => {
                            aggregatedPercentComplete += 100;
                            this.onPictureUploaded.emit(pictureId);
                            this.uploadProgress.next(aggregatedPercentComplete / numberOfImages);
                        });
                }
            );
        }
    }
}