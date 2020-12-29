import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ContactPerson, Organization} from "../../_internal/resources/organization.service";
import {ContactRequestResult, ContactService} from "../../_internal/resources/contact.service";
import {ReCaptchaV3Service} from "ng-recaptcha";
import {mergeMap} from "rxjs/operators";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";

@Component({
    templateUrl: './contact-form-confirm.component.html'
})
export class ContactFormConfirmComponent {

    @Input()
    public contact: ContactPerson;

    @Input()
    public organization: Organization;

    @Input()
    public contactRequestResult: ContactRequestResult;

    private numberOfResends: number = 1;

    constructor(
        public modal: NgbActiveModal,
        private recaptchaV3Service: ReCaptchaV3Service,
        private contactService: ContactService,
        private translateService: TranslateService,
        private toastr: ToastrService
    ) {
    }

    public triggerResend() {
        this.recaptchaV3Service.execute('resubmit')
            .pipe(
                mergeMap(
                    token => this.contactService.resendContactRequest({
                        captcha: token,
                        contactRequestId: this.contactRequestResult.contactRequestId
                    })
                )
            )
            .subscribe(() => {
                this.numberOfResends++;
                this.toastr.success(this.translateService.instant('dialog.contact-organization.toast.success'));
            }, () => {
                this.toastr.error(this.translateService.instant('dialog.contact-organization.toast.error'));
            });

    }

    public canTriggerResend(): boolean {
        return this.numberOfResends <= 3;
    }

}