import {Component, EventEmitter, Inject, OnInit} from "@angular/core";
import {ObservableUtil} from "../../shared/observable.util";
import {debounceTime, distinctUntilChanged, first, map, mergeMap, switchMap} from "rxjs/operators";
import {
    Address,
    Group,
    Organisation,
    OrganisationService,
    OrganizationEvent,
    PictureId
} from "../../_internal/resources/organisation.service";
import {ActivatedRoute, Router} from "@angular/router";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {PageScrollInstance, PageScrollService} from "ngx-page-scroll";
import {DOCUMENT} from "@angular/common";
import {
    GroupTemplate,
    OrganizationTemplate,
    OrganizationTemplateService
} from "../../_internal/resources/organization-template.service";
import {NgbModal, NgbTimeAdapter, NgbTypeaheadSelectItemEvent} from "@ng-bootstrap/ng-bootstrap";
import {TimepickerAdapterService} from "./_internal/timepicker-adapter.service";
import {NgModel} from "@angular/forms";
import {PublishChangesConfirmationComponent, PublishContent} from "./_internal/publish-changes-confirmation.component";
import {ChangesSentForReviewComponent} from "./_internal/changes-sent-for-review.component";
import {ToastrService} from 'ngx-toastr';
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'organisation-edit',
    templateUrl: './edit.component.html',
    styleUrls: [
        './edit.component.scss'
    ],
    providers: [
        {provide: NgbTimeAdapter, useValue: new TimepickerAdapterService()}
    ]
})
export class EditComponent implements OnInit {

    public organisation: Observable<Organisation>;
    public changes: Subject<Array<OrganizationEvent>> = new BehaviorSubject([]);
    public newOrganization: Subject<Organisation> = new EventEmitter<Organisation>();
    public originalOrganization: Organisation;
    public organizationTemplate: OrganizationTemplate;
    public files: UploadedFile[] = [];
    public publishContent: PublishContent = {} as PublishContent;

    public weekdays: string[] = [
        "MONDAY",
        "TUESDAY",
        "WEDNESDAY",
        "THURSDAY",
        "FRIDAY",
        "SATURDAY",
        "SUNDAY"
    ];

    constructor(
        private route: ActivatedRoute,
        private organisationService: OrganisationService,
        organizationTemplateService: OrganizationTemplateService,
        private pageScrollService: PageScrollService,
        private modalService: NgbModal,
        private router: Router,
        private toastr: ToastrService,
        private translateService: TranslateService,
        @Inject(DOCUMENT) private document: Document
    ) {
        this.organisation = ObservableUtil.extractObjectMember(this.route.params, 'organisation')
            .pipe(
                switchMap((organisationName: string) => this.organisationService.getOrganisation(organisationName))
            );
        this.organisation.pipe(first()).subscribe(organization => this.originalOrganization = organization as Organisation);

        this.organisation
            .pipe(
                map(organization => organization.organizationType),
                switchMap(organizationType => organizationTemplateService.getOrganizationTemplateByOrganizationType(organizationType))
            )
            .subscribe(organizationTemplate => this.organizationTemplate = organizationTemplate);

        this.newOrganization
            .pipe(
                mergeMap(organization => this.organisationService.compareOrganization(this.originalOrganization, organization))
            )
            .subscribe(changes => this.changes.next(changes));
    }

    ngOnInit(): void {
    }

    add<T>(items: T[], organisation: Organisation) {
        let documentId = 'newItem' + Math.random();
        items.push({
            __expanded: true,
            __id: documentId
        } as any);
        window.setTimeout(() => {
            this.pageScrollService.start(PageScrollInstance.newInstance({
                document: this.document,
                scrollTarget: '#' + documentId
            }))
        }, 0);
        this.calculateChanges(organisation);
    }

    drop<T>(list: T[], event: CdkDragDrop<string[]>, organisation: Organisation) {
        moveItemInArray(list, event.previousIndex, event.currentIndex);
        this.calculateChanges(organisation);
    }

    remove<T>(list: T[], itemToRemove: T, organisation: Organisation) {
        let index = list.indexOf(itemToRemove);
        list.splice(index, 1);
        this.calculateChanges(organisation);
    }

    public searchGroup(selectedGroups: Group[]) {
        let selectedGroupNames: string[] = selectedGroups.map(group => group.name);
        return (text$: Observable<string>) => {
            return text$
                .pipe(
                    debounceTime<string>(200),
                    distinctUntilChanged(),
                    map(term => this.findGroupByTerm(term, selectedGroupNames))
                );
        };
    }

    private findGroupByTerm(term: string, selectedGroupNames: string[]): string[] {
        let groups: GroupTemplate[] = this.organizationTemplate.groups;
        if (term !== '') {
            groups = groups.filter(group => group.name.toLowerCase().indexOf(term.toLowerCase()) > -1);
        }
        groups = groups.filter(group => selectedGroupNames.indexOf(group.name) == -1);
        return groups.slice(0, 10).map(group => group.name);
    }

    public selectGroup(newGroup: NgbTypeaheadSelectItemEvent, group: Group) {
        let groupTemplates: GroupTemplate[] = this.organizationTemplate.groups.filter(group => group.name === newGroup.item);
        if (groupTemplates.length >= 1 && (group.description === '' || group.description === undefined)) {
            group.description = groupTemplates[0].description;
        }
    }

    areAddressesEqual(address1: Address, address2: Address): boolean {
        return Address.isEqual(address1, address2);
    }

    public hasError(field: NgModel): boolean {
        return field.invalid && (field.dirty || field.touched);
    }

    uploadFile(pictures: (PictureId | UploadedFile)[], event: FileList) {
        for (let index: number = 0; index < event.length; index++) {
            const element = event[index];
            let uploadedFile = new UploadedFile();
            let reader = new FileReader();
            reader.onload = (e) => {
                uploadedFile.fileDataUrl = e.target.result;
            };
            reader.readAsDataURL(element);
            pictures.push(uploadedFile);
        }
    }

    calculateChanges(organization: Organisation) {
        this.newOrganization.next(organization);
    }

    openPublishChangesConfirmation(organization: Organisation, tab: number = 0) {
        this.changes.pipe(first()).subscribe((changes) => {
            let modalRef = this.modalService.open(PublishChangesConfirmationComponent, {
                size: 'lg',
            });
            modalRef.componentInstance.activeTab = tab;
            modalRef.componentInstance.publish.organization = this.originalOrganization;
            modalRef.componentInstance.publish.changes = changes;
            modalRef.componentInstance.publish.describeSources = this.publishContent.describeSources;
            modalRef.result
                .then((result) => {
                    this.publishContent = result;

                    return this.organisationService.submitOrganizationEvents(
                        {value: this.originalOrganization.id},
                        this.publishContent.describeSources,
                        this.publishContent.changes
                    )
                        .toPromise()
                        .catch((result) => {
                            const title = this.translateService.instant('edit.organisation.changes.message.title');
                            const description = this.translateService.instant('edit.organisation.changes.message.description');
                            this.toastr.error(description, title);
                            return Promise.reject(result);
                        })
                })
                .then(() => {
                    const navigate = () => {
                        this.router.navigate(['/organisation/' + this.originalOrganization.urlName]);
                    };

                    let confirmedRef = this.modalService.open(ChangesSentForReviewComponent);
                    confirmedRef.componentInstance.organizationName = this.originalOrganization.name;
                    confirmedRef.result
                        .then(navigate)
                        .catch(navigate)
                })
                .catch((result) => {
                    // keep content in case the dialog is opened again
                    this.publishContent = result;
                });
        })
    }

}

class UploadedFile {
    fileDataUrl: string | ArrayBuffer | null;
}