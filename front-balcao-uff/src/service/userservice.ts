// userservice.ts
import { API_CONFIG } from "./config";
import { Ad, CreateReview, Reputation, Review, Transaction, UserData } from "../interfaces/interfaces";

export const getUserProfile = async (
  idUser: string
): Promise<UserData | null> => {
  const token = localStorage.getItem("token");
  if (!token) {
    return null;
  }

  try {
    const response = await fetch(`${API_CONFIG.BASE_URL}/users/${idUser}`, {
      method: "GET",
      headers: API_CONFIG.getAuthHeader(),
    });

    if (!response.ok) {
      throw new Error("Erro na requisição");
    }

    const data: UserData = await response.json();
    return data;
  } catch (error) {
    console.error("Erro ao buscar dados do usuário:", error);
    return null;
  }
};

export const getUserReviews = async (
  idUser: string
): Promise<Review[] | null> => {
  const token = localStorage.getItem("token");
  if (!token) return null;

  try {
    const response = await fetch(
      `${API_CONFIG.BASE_URL}/reviews/user/${idUser}`,
      {
        method: "GET",
        headers: API_CONFIG.getAuthHeader(),
      }
    );

    if (!response.ok) {
      throw new Error("Erro ao buscar avaliações");
    }

    const data: Review[] = await response.json();
    return data;
  } catch (error) {
    console.error("Erro ao buscar avaliações:", error);
    return null;
  }
};

export const fetchAds = async (idUser: string): Promise<Ad[] | null> => {
  const token = localStorage.getItem("token");
  if (!token) return null;

  try {
    const response = await fetch(
      `${API_CONFIG.BASE_URL}/anuncios/perfil?userId=${idUser}`,
      {
        method: "GET",
        headers: API_CONFIG.getAuthHeader(),
      }
    );

    if (!response.ok) {
      throw new Error("Erro ao buscar anúncios");
    }

    const data: Ad[] = await response.json();
    return data;
  } catch (error) {
    console.error("Erro ao buscar anúncios:", error);
    return null;
  }
};

export const getUserReputation = async (
  idUser: string
): Promise<Reputation | null> => {
  const token = localStorage.getItem("token");
  if (!token) return null;

  try {
    const response = await fetch(
      `${API_CONFIG.BASE_URL}/reviews/user/${idUser}/reputation`,
      {
        method: "GET",
        headers: API_CONFIG.getAuthHeader(),
      }
    );

    if (!response.ok) {
      throw new Error("Erro ao buscar reputação");
    }

    const data: Reputation = await response.json();
    return data;
  } catch (error) {
    console.error("Erro ao buscar reputação:", error);
    return null;
  }
};

export const getCurrentUser = async (): Promise<number | null> => {
    const token = localStorage.getItem('token');
    if (!token) return null;
  
    try {
      const response = await fetch(`${API_CONFIG.BASE_URL}/auth/current-user`, {
        method: "GET",
        headers: API_CONFIG.getAuthHeader(),
      });
  
      if (!response.ok) {
        throw new Error("Erro ao buscar usuário atual");
      }
  
      const data = await response.json();
      return data.id;
    } catch (error) {
      console.error("Erro ao buscar usuário atual:", error);
      return null;
    }
  };

  export const fetchTransactions = async (idUser: string): Promise<Transaction[] | null> => {
    const token = localStorage.getItem('token');
    if (!token) return null;
  
    try {
      const response = await fetch(`${API_CONFIG.BASE_URL}/transactions/${idUser}`, {
        method: "GET",
        headers: API_CONFIG.getAuthHeader(),
      });
  
      if (!response.ok) {
        throw new Error("Erro ao buscar transações");
      }
  
      const data: Transaction[] = await response.json();
      return data;  // Retorna as transações
    } catch (error) {
      console.error("Erro ao buscar transações:", error);
      return null;
    }
  };


  // Função para enviar uma avaliação
export const sendReview = async (
  reviewerId: number,
  reviewedId: number,
  rating: number,
  comment: string
): Promise<void> => {
  const token = localStorage.getItem("token");

  if (!token) {
    console.error("Token de autenticação não encontrado");
    return;
  }

  // Dados da avaliação
  const reviewData: CreateReview = {
    reviewerId,
    reviewedId,
    rating,
    comment,
  };

  try {
    const response = await fetch(`${API_CONFIG.BASE_URL}/reviews`, {
      method: "POST",
      headers: API_CONFIG.getAuthHeader(),
      body: JSON.stringify(reviewData),
    });

    if (!response.ok) {
      throw new Error("Erro ao enviar avaliação");
    }

    console.log("Avaliação enviada com sucesso!");
  } catch (error) {
    console.error("Erro ao enviar avaliação:", error);
  }
};