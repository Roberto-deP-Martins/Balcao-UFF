export interface Advertise {
    id: number;
    title: string;
    description: string;
    category: string;
    price: number;
    contactInfo: string;
    address: string;
    latitude: number | null;
    longitude: number | null;
    images: string[];
}
