import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PictureId} from "./picture.service";

@Injectable()
export class OrganizationTemplateService {

    constructor(private httpClient: HttpClient) {
    }

    getOrganizationTemplateByOrganizationType(organizationType: string): Observable<OrganizationTemplate> {
        return this.httpClient.get<OrganizationTemplate>(`api/template/${organizationType}`);
    }

}

export class OrganizationTemplate {
    public name: string;
    public logoSuggestions: PictureId[];
    public groups: GroupTemplate[];
}

export class GroupTemplate {
    public name: string;
    public description: string;
}
