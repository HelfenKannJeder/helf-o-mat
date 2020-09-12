import {Injectable} from "@angular/core";
import {QuestionId} from "./resources/organization.service";
import {Answer} from "../shared/answer.model";
import {environment} from "../../environments/environment";

@Injectable({providedIn: 'root'})
export class QrCodeService {

    private readonly updateLocationInterval = 3600*1000;
    private latitude: number = null;
    private longitude: number = null;

    constructor() {
        const updateLocation = () => {
            console.log('try to retrieve location');
            navigator.geolocation.getCurrentPosition((position) => {
                this.latitude  = position.coords.latitude;
                this.longitude = position.coords.longitude;
                console.log('user location is', this.latitude, this.longitude);
            }, () => {
                console.log('failed to get location');
            });
        };
        updateLocation();
        window.setInterval(updateLocation, this.updateLocationInterval);
    }

    generateLink(organizationType: string, questionAnswers: QuestionAnswers[]) {
        let answers = "";
        for (const questionAnswer of questionAnswers) {
            const firstPartOfUuid = questionAnswer.questionId.value.split("-")[0];
            answers += `${firstPartOfUuid}=${questionAnswer.answer};`;
        }
        return `${environment.qrCodeUrl}/${organizationType}/${this.latitude}/${this.longitude}/${answers}`;
    }
}

export interface QuestionAnswers {
    questionId: QuestionId;
    answer: Answer;
}