import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Organisation, OrganizationEvent} from "../../../_internal/resources/organisation.service";

@Component({
    selector: 'publish-changes-confirmation',
    templateUrl: './publish-changes-confirmation.component.html'
})
export class PublishChangesConfirmationComponent {

    @Input()
    public publish: PublishContent = {} as PublishContent;

    @Input()
    public activeTab: number = 0;

    constructor(public modal: NgbActiveModal) {
    }

    openTab(tab: number) {
        this.activeTab = tab;
    }
}

export interface PublishContent {
    organization: Organisation;
    describeSources: string;
    changes: Array<OrganizationEvent>;
}