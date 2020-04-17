import {Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Group} from '../_internal/resources/organization.service';

@Pipe({
    name: 'groupNames'
})
export class GroupNamesPipe implements PipeTransform {

    constructor(private translateService: TranslateService) {
    }

    public transform(value: Array<Group>, allGroups?: Array<Group>): string {
        if (allGroups == null || (value.length <= (allGroups.length / 2) && value.length > 0)) {
            return this.toReadableGroupNameList(value);
        } else if (value.length == allGroups.length || value.length == 0) {
            return this.translateService.instant('organization.groupNames.all');
        } else {
            let resultGroupList: Array<Group> = [];
            let groupNameList = value.map(group => group.name);
            for (let group of allGroups) {
                if (groupNameList.indexOf(group.name) < 0) {
                    resultGroupList.push(group);
                }
            }
            return this.translateService.instant('organization.groupNames.allWithout')
                + ' ' + this.toReadableGroupNameList(resultGroupList);
        }
    }

    private toReadableGroupNameList(groups: Array<Group>): string {
        let groupNames = groups.map((group) => group.name);
        if (groupNames.length == 1) {
            return groupNames[0];
        }
        let groupNameSeparator = this.translateService.instant('organization.groupNames.separator');
        let suffix = this.translateService.instant('organization.groupNames.and') + groupNames.pop();
        return groupNames.join(groupNameSeparator) + suffix;
    }
}