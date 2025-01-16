import { API_CONFIG } from './config';
import { Advertise } from '../interfaces/interfaces';

export const getAdvertises = async (): Promise<Advertise[]> => {
    try {
        const response = await fetch(`${API_CONFIG.BASE_URL}/anuncios`, {
            headers: API_CONFIG.getAuthHeader()
        });

        if (!response.ok) {
            throw new Error('Erro na requisição');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Erro ao buscar anúncios:', error);
        throw error;
    }
};

export const getAdvertisesByCategory = async (category: string): Promise<Advertise[]> => {
    try {
        const response = await fetch(`${API_CONFIG.BASE_URL}/anuncios/category`, {
            method: 'POST',
            headers: {
                ...API_CONFIG.getAuthHeader(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ category })
        });

        if (!response.ok) {
            throw new Error('Erro na requisição');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Erro ao buscar anúncios por categoria:', error);
        throw error;
    }
};

export const getAdvertisesByLocation = async (latitude: number, longitude: number): Promise<Advertise[]> => {
    try {
        const response = await fetch(`${API_CONFIG.BASE_URL}/anuncios/nearby?lat=${latitude}&lng=${longitude}&radius=10`, {
            headers: API_CONFIG.getAuthHeader()
        });

        if (!response.ok) {
            throw new Error('Erro na requisição');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Erro ao buscar anúncios próximos:', error);
        throw error;
    }
};

export const createAdvertise = async (advertiseData: Advertise, images: File[]): Promise<Advertise> => {
    try {
        const formData = new FormData();
        formData.append('anuncio', JSON.stringify(advertiseData));
        images.forEach((image, index) => formData.append(`images`, image));

        const response = await fetch(`${API_CONFIG.BASE_URL}/anuncios/save2`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: formData
        });

        if (!response.ok) {
            throw new Error('Erro ao salvar anúncio');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Erro ao salvar anúncio:', error);
        throw error;
    }
};

export const deleteAdvertise = async (id: number): Promise<void> => {
    try {
        const token = localStorage.getItem("token");

        const response = await fetch(`${API_CONFIG.BASE_URL}/anuncios/delete`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ id }),
        });

        if (!response.ok) {
            throw new Error("Erro ao deletar anúncio");
        }
    } catch (error) {
        console.error("Erro ao deletar anúncio:", error);
        throw error;
    }
};

