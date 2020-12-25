import {Component, Input} from "@angular/core";
import {ContactPerson, Organization} from "../../_internal/resources/organization.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ContactService} from "../../_internal/resources/contact.service";
import {ReCaptchaV3Service} from "ng-recaptcha";
import {mergeMap} from "rxjs/operators";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";

@Component({
    templateUrl: './contact-form.component.html'
})
export class ContactFormComponent {

    @Input()
    public contact: ContactPerson;

    @Input()
    public organization: Organization;

    public contactFormContent: ContactFormContent = {
        email: '',
        subject: '',
        message: ''
    };

    constructor(
        public modal: NgbActiveModal,
        private contactService: ContactService,
        private recaptchaV3Service: ReCaptchaV3Service,
        private toastr: ToastrService,
        private translateService: TranslateService
    ) {
    }

    public submit() {
        this.recaptchaV3Service.execute('submit')
            .pipe(
                mergeMap(
                    token =>
                        this.contactService.createContactRequest({
                                email: this.contactFormContent.email,
                                subject: this.contactFormContent.subject,
                                message: this.contactFormContent.message,
                                captcha: token
                            }
                        )
                )
            )
            .subscribe(() => {
                this.modal.close({});
            }, (error) => {
                this.toastr.error(this.translateService.instant('error.captchaInvalid'));
                console.warn('failed during submit', error);
            });
    }

}

export interface ContactFormContent {
    email: string;
    subject: string;
    message: string;
}