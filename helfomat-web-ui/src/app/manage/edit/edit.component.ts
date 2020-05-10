import {Component, EventEmitter, Inject, OnInit} from "@angular/core";
import {ObservableUtil} from "../../shared/observable.util";
import {debounceTime, distinctUntilChanged, filter, first, map, mergeMap, switchMap} from "rxjs/operators";
import {
    Address,
    Group,
    Organization,
    OrganizationEvent,
    OrganizationService,
} from "../../_internal/resources/organization.service";
import {ActivatedRoute, Router} from "@angular/router";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {PageScrollInstance, PageScrollService} from "ngx-page-scroll-core";
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
import {EditAddressComponent} from "./_internal/edit-address.component";
import {PictureId, PictureService} from "../../_internal/resources/picture.service";
import {v4 as uuidv4} from 'uuid';
import {Ng2ImgMaxService} from "ng2-img-max";

@Component({
    selector: 'organization-edit',
    templateUrl: './edit.component.html',
    styleUrls: [
        './edit.component.scss'
    ],
    providers: [
        {provide: NgbTimeAdapter, useValue: new TimepickerAdapterService()}
    ]
})
export class EditComponent implements OnInit {

    public organization$: Observable<Organization>;
    public _organization$: Subject<Organization> = new BehaviorSubject<Organization>(null);
    public changes: Subject<Array<OrganizationEvent>> = new BehaviorSubject([]);
    public newOrganization: Subject<Organization> = new EventEmitter<Organization>();
    public originalOrganization: Organization;
    public organizationTemplate: OrganizationTemplate;
    public publishContent: PublishContent = {} as PublishContent;
    public uploadProgress: Subject<number> = new BehaviorSubject(null);

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
        private organizationService: OrganizationService,
        organizationTemplateService: OrganizationTemplateService,
        private pageScrollService: PageScrollService,
        private modalService: NgbModal,
        private router: Router,
        private toastr: ToastrService,
        private translateService: TranslateService,
        private pictureService: PictureService,
        private ng2ImgMax: Ng2ImgMaxService,
        @Inject(DOCUMENT) private document: Document
    ) {
        ObservableUtil.extractObjectMember(this.route.params, 'organization')
            .pipe(
                switchMap((organizationName: string) => this.organizationService.getOrganization(organizationName)),
                map(organization => {
                    organization.groups = organization.groups.map((group: any) => {
                        group.__id = 'newItem' + Math.random();
                        return group;
                    })
                    return organization;
                })
            )
            .subscribe(organization => this._organization$.next(organization));
        this.organization$ = this._organization$.asObservable();
        this.organization$.pipe(
            first(organization => organization != null)
        ).subscribe(organization => this.originalOrganization = EditComponent.deepCopy(organization as Organization));

        this.organization$
            .pipe(
                filter(organization => organization != null),
                map(organization => organization.organizationType),
                switchMap(organizationType => organizationTemplateService.getOrganizationTemplateByOrganizationType(organizationType))
            )
            .subscribe(organizationTemplate => this.organizationTemplate = organizationTemplate);

        this.newOrganization
            .pipe(
                map(organization => {
                    return this.removeIncompleteFields(organization);
                }),
                mergeMap(organization => this.organizationService.compareOrganization(this.originalOrganization, organization))
            )
            .subscribe(changes => this.changes.next(changes));

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

    private removeIncompleteFields(organization: Organization) {
        const o = {...organization};
        o.attendanceTimes = o.attendanceTimes.filter(attendanceTime => attendanceTime.day != undefined && attendanceTime.start != undefined && attendanceTime.start.length != 2 && attendanceTime.end != undefined && attendanceTime.end.length != 2);
        o.groups = o.groups.filter(group => group.name !== undefined);
        return o;
    }

    ngOnInit(): void {
    }

    add<T>(items: T[], organization: Organization) {
        let documentId = 'newItem' + Math.random();
        items.push({
            __expanded: true,
            __id: documentId
        } as any);
        window.setTimeout(() => {
            this.pageScrollService.start(new PageScrollInstance({
                document: this.document,
                scrollTarget: '#' + documentId
            }))
        }, 0);
        this.calculateChanges({...organization});
    }

    drop<T>(list: T[], event: CdkDragDrop<string[]>, organization: Organization) {
        moveItemInArray(list, event.previousIndex, event.currentIndex);
        this.calculateChanges(organization);
    }

    remove<T>(list: T[], itemToRemove: T, organization: Organization) {
        let index = list.indexOf(itemToRemove);
        list.splice(index, 1);
        this.calculateChanges(organization);
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

    uploadFile(pictures: PictureId[], event: FileList) {
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
                            pictures.push(pictureId);
                            this.uploadProgress.next(aggregatedPercentComplete / numberOfImages);
                        });
                },
                error => {
                    console.error('Scaling went wrong, tyring to just upload it as is', error);
                    this.pictureService.uploadPicture(pictureId, event[index])
                        .subscribe(() => {
                            aggregatedPercentComplete += 100;
                            pictures.push(pictureId);
                            this.uploadProgress.next(aggregatedPercentComplete / numberOfImages);
                        });
                }
            );
        }
    }

    calculateChanges(organization: Organization) {
        if (organization.addresses != null && organization.addresses.length > 0) {
            organization.defaultAddress = organization.addresses[0];
        }
        this._organization$.next(organization);
        this.newOrganization.next(organization);
    }

    editAddress(addresses: Address[], address: Address, organization: Organization) {
        const oldIndex = addresses.indexOf(address);
        const isDefaultAddress = Address.isEqual(organization.defaultAddress, address);
        let modalRef = this.modalService.open(EditAddressComponent, {
            size: 'lg',
        });
        modalRef.componentInstance.address = {...address};
        modalRef.componentInstance.organization = {...organization};
        modalRef.result
            .then((newAddress: Address) => {
                const newOrganization = {...organization};
                if (oldIndex < 0) {
                    newOrganization.addresses.push(newAddress);
                } else {
                    newOrganization.addresses[oldIndex] = newAddress;
                }
                if (isDefaultAddress) {
                    newOrganization.defaultAddress = newAddress;
                }
                this.calculateChanges(newOrganization);
            });
    }

    openPublishChangesConfirmation(organization: Organization, tab: number = 0) {
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

                    return this.organizationService.submitOrganizationEvents(
                        {value: this.originalOrganization.id},
                        this.publishContent.describeSources,
                        this.publishContent.changes
                    )
                        .toPromise()
                        .catch((result) => {
                            const title = this.translateService.instant('edit.organization.changes.message.title');
                            const description = this.translateService.instant('edit.organization.changes.message.description');
                            this.toastr.error(description, title);
                            return Promise.reject(result);
                        })
                })
                .then(() => {
                    const navigate = () => {
                        this.router.navigate(['/volunteer/organization/' + this.originalOrganization.urlName]);
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

    public getId(prefix: string, element: any): string {
        return prefix + '-' + element.__id;
    }

    private static deepCopy<T>(object: T): T {
        return JSON.parse(JSON.stringify(object));
    }

}