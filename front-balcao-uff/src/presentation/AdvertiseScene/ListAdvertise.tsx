import { useEffect, useState } from "react";
import AdvertiseCard from "../../components/AdvertiseCard";
import { FaSearch, FaMapMarkerAlt } from "react-icons/fa";
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import { Advertise } from "../../interfaces/interfaces";
import { deleteAdvertise, getAdvertises } from "../../service/AnuncioService";
import { getCurrentUser } from "../../service/userservice";

const ListAdvertise = () => {
  const [advertises, setAdvertises] = useState<Advertise[]>([]);
  // const [category, setCategory] = useState<string>("");
  const [showNearby, setShowNearby] = useState<boolean>(false);
  const [user, setUser] = useState<any>(null);
  const [searchText, setSearchText] = useState<string>("");

  useEffect(() => {
    fetchAdvertises();
    fetchCurrentUser();
  }, []);

  const fetchAdvertises = async () => {
    try {
      const data = await getAdvertises();
      setAdvertises(data);
    } catch (error) {
      console.error("Erro ao buscar anúncios:", error);
    }
  };

  const fetchCurrentUser = async () => {
    try {
      const userId = await getCurrentUser();
      if (userId !== null) {
        setUser({ id: userId });
      }
    } catch (error) {
      console.error("Erro ao buscar usuário:", error);
    }
  };

  const handleDelete = async (id: number) => {
    const result = await Swal.fire({
      title: "Tem certeza?",
      text: "Esta ação não pode ser desfeita!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sim, deletar!",
      cancelButtonText: "Cancelar",
    });

    if (result.isConfirmed) {
      try {
        await deleteAdvertise(id);
        setAdvertises(advertises.filter(ad => ad.id !== id));
        Swal.fire("Deletado!", "O anúncio foi excluído com sucesso.", "success");
      } catch (error) {
        console.error("Erro ao deletar anúncio:", error);
        Swal.fire("Erro", "Ocorreu um erro ao deletar o anúncio.", "error");
      }
    }
  };

  const handleSearch = () => {
    if (!searchText.trim()) {
      fetchAdvertises();
      return;
    }

    const filteredAds = advertises.filter(ad =>
      ad.title.toLowerCase().includes(searchText.toLowerCase()) ||
      ad.description.toLowerCase().includes(searchText.toLowerCase()) ||
      ad.category.toLowerCase().includes(searchText.toLowerCase())
    );
    setAdvertises(filteredAds);
  };

  const handleSearchByLocation = async () => {
    if (showNearby) {
      fetchAdvertises();
      setShowNearby(false);
      return;
    }

    if (!navigator.geolocation) {
      console.error("Geolocalização não é suportada pelo seu navegador.");
      return;
    }

    navigator.geolocation.getCurrentPosition(async (position) => {
      const { latitude, longitude } = position.coords;

      try {
        const token = localStorage.getItem("token");

        const response = await fetch(`http://localhost:8080/anuncios/nearby?lat=${latitude}&lng=${longitude}&radius=10`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error("Erro na requisição");
        }

        const data = await response.json();
        setAdvertises(data);
        setShowNearby(true);
      } catch (error) {
        console.error("Erro ao buscar anúncios próximos:", error);
      }
    }, (error) => {
      console.error("Erro ao obter localização:", error);
    });
  };

  return (
    <div className="w-screen h-full p-4">
      <div className="text-xl font-semibold mb-4">Anúncios</div>

      <div className="mb-4 flex items-center justify-between">
        {/* Input de busca por texto */}
        <input
          type="text"
          placeholder="Pesquisar por título, descrição, categoria ou local"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              handleSearch();
            }
          }}
          className="p-2 border border-gray-300 rounded mb-2 w-2/3"
        />
        <button
          onClick={handleSearch}
          className="w-10 h-10 flex items-center justify-center p-2 bg-blue-600 text-white rounded-full hover:bg-blue-700 ml-2"
        >
          <FaSearch />
        </button>

        <Link to="/create-advertise" className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 shadow-md ml-2">
          Criar Novo Anúncio
        </Link>
      </div>

      <div className="mb-4 flex items-center space-x-2">
        <button
          onClick={handleSearchByLocation}
          className="w-10 h-10 flex items-center justify-center p-2 bg-red-600 text-white rounded-full hover:bg-red-700"
        >
          <FaMapMarkerAlt />
        </button>
        <span>{showNearby ? "Todos os anúncios" : "Buscar Anúncios Próximos"}</span>
      </div>

      {/* Lista de Anúncios */}
      {advertises.length > 0 ? (
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
          {advertises.map((ad: any) => (
            <div key={ad.id} className="relative">
              <AdvertiseCard ad={ad} />
              {/* Botão de Deletar */}
              {user && (user.id === ad.userId || user.role === "ADMIN") && (
                <button
                  onClick={() => handleDelete(ad.id)}
                  className="absolute top-2 right-2 bg-red-600 text-white px-2 py-1 rounded hover:bg-red-700"
                >
                  Deletar
                </button>
              )}
            </div>
          ))}
        </div>
      ) : (
        <p className="text-gray-700">Carregando ou nenhum anúncio disponível...</p>
      )}
    </div>
  );
};

export default ListAdvertise;
