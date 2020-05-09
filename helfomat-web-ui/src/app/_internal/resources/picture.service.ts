import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable()
export class PictureService {

    constructor(private httpClient: HttpClient) {
    }

    public uploadPicture(pictureId: PictureId, file: File): Observable<void> {
        const formData = new FormData();
        formData.set('file', file);
        return this.httpClient.post(`api/picture/${pictureId.value}`, formData)
            .pipe(
                map(() => {
                })
            )
    }

}

export interface PictureId {
    value: string;
}