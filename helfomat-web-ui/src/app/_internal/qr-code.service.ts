import {Injectable} from "@angular/core";
import {QuestionId} from "./resources/organization.service";
import {Answer} from "../shared/answer.model";
import {environment} from "../../environments/environment";

@Injectable({providedIn: 'root'})
export class QrCodeService {

    private readonly updateLocationInterval = 3600 * 1000;
    private latitude: number = null;
    private longitude: number = null;
    private interval: number = null;

    constructor() {
    }

    public triggerUpdateLocation(): void {
        if (this.interval === null) {
            const updateLocation = () => {
                console.log('try to retrieve location');
                navigator.geolocation.getCurrentPosition((position) => {
                    this.latitude = position.coords.latitude;
                    this.longitude = position.coords.longitude;
                    console.log('user location is', this.latitude, this.longitude);
                }, () => {
                    console.log('failed to get location');
                });
            };
            updateLocation();
            this.interval = window.setInterval(updateLocation, this.updateLocationInterval);
        }
    }

    generateLink(organizationType: string, questionAnswers: QuestionAnswers[]) {
        let answers = "";
        for (const questionAnswer of questionAnswers) {
            const firstPartOfUuid = questionAnswer.questionId.value.split("-")[0];
            answers += `;${firstPartOfUuid}=${questionAnswer.answer}`;
        }
        const qrCodeUrl = this.getQrCodeUrl();
        return `${qrCodeUrl}/${organizationType}/${this.latitude}/${this.longitude}${answers}`;
    }

    private getQrCodeUrl() {
        const qrCodeUrl = environment.qrCodeUrl;

        const currentHost = window.location.host;
        if (currentHost.startsWith("localhost")) {
            return "http://localhost:4200/kiosk";
        } else if (currentHost.startsWith("kiosk.")) {
            return "https://" + currentHost.substring("kiosk.".length) + "/kiosk";
        } else {
            return qrCodeUrl;
        }
    }

}

export interface QuestionAnswers {
    questionId: QuestionId;
    answer: Answer;
}