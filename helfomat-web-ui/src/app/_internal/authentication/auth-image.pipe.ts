import {Pipe, PipeTransform} from "@angular/core";
import {HttpClient} from "@angular/common/http";

/**
 * source: https://medium.com/javascript-in-plain-english/loading-images-with-authorization-8aab33663ba6
 */
@Pipe({
    name: 'authImage'
})
export class AuthImagePipe implements PipeTransform {

    constructor(
        private http: HttpClient
    ) {
    }

    async transform(src: string): Promise<string> {
        const imageBlob = await this.http.get(src, {responseType: 'blob'}).toPromise();
        const reader = new FileReader();
        return new Promise<string>((resolve) => {
            reader.onloadend = () => resolve(reader.result as string);
            reader.readAsDataURL(imageBlob);
        });
    }

}
