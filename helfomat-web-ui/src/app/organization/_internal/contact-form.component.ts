import {Component, Input} from "@angular/core";
import {ContactPerson, Organization} from "../../_internal/resources/organization.service";
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ContactService} from "../../_internal/resources/contact.service";
import {ReCaptchaV3Service} from "ng-recaptcha";
import {catchError, mergeMap} from "rxjs/operators";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {NgForm, NgModel} from "@angular/forms";
import {LoadingOverlayService} from "../../_internal/components/loading-overlay/loading-overlay.service";
import {ContactFormConfirmComponent} from "./contact-form-confirm.component";
import {of} from "rxjs";

@Component({
    templateUrl: './contact-form.component.html'
})
export class ContactFormComponent {

    @Input()
    public contact: ContactPerson;

    @Input()
    public organization: Organization;

    public privacyNotice: boolean = false;
    public contactFormContent: ContactFormContent = {
        name: '',
        email: '',
        subject: '',
        message: ''
    };

    constructor(
        public modal: NgbActiveModal,
        private contactService: ContactService,
        private recaptchaV3Service: ReCaptchaV3Service,
        private toastr: ToastrService,
        private translateService: TranslateService,
        private loadingOverlayService: LoadingOverlayService,
        private modalService: NgbModal
    ) {
    }

    public hasError(field: NgModel): boolean {
        return field.invalid && (field.dirty || field.touched);
    }

    public submit(form: NgForm) {
        if (!form.valid || !this.privacyNotice) {
            for (const i in form.controls) {
                form.controls[i].markAsTouched();
            }
            this.toastr.warning(this.translateService.instant('error.invalidForm'));
            return;
        }
        this.loadingOverlayService.open();
        this.recaptchaV3Service.execute('submit')
            .pipe(
                catchError(error => {
                    this.toastr.error(this.translateService.instant('error.captchaInvalid'));
                    this.loadingOverlayService.close();
                    console.warn('failed during submit', error);
                    return of()
                }),
                mergeMap(
                    (token: string) =>
                        this.contactService.createContactRequest({
                                name: this.contactFormContent.name,
                                email: this.contactFormContent.email,
                                subject: this.contactFormContent.subject,
                                message: this.contactFormContent.message,
                                captcha: token,
                                organizationId: {value: this.organization.id},
                                organizationContactPersonIndex: this.organization.contactPersons.indexOf(this.contact)
                            }
                        )
                )
            )
            .subscribe(contactRequestResult => {
                this.loadingOverlayService.close();
                const ref = this.modalService.open(ContactFormConfirmComponent, {
                    size: 'md',
                });
                ref.componentInstance.contact = this.contact;
                ref.componentInstance.organization = this.organization;
                ref.componentInstance.contactRequestResult = contactRequestResult;
                this.modal.close({});
            }, (error) => {
                this.toastr.error(this.translateService.instant('dialog.contact-organization.error.errorSubmit'));
                this.loadingOverlayService.close();
                console.warn('failed during submit', error);
            });
    }

}

export interface ContactFormContent {
    name: string;
    email: string;
    subject: string;
    message: string;
}