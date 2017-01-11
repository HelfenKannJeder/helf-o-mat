import Address from "./address.model";

export default class Organisation {
    public id: string;
    public name: string;
    public description: string;
    public website: string;
    public scoreNorm: number;
    public mapPin: string;
    public addresses: Address[] = [];
}