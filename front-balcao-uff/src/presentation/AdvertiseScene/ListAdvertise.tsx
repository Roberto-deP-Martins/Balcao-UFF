import { useEffect, useState } from "react";
import AdvertiseCard from "../../components/AdvertiseCard";
import AddIcon from "@mui/icons-material/Add";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { Advertise } from "../../interfaces/interfaces";
import { getAdvertises } from "../../service/AnuncioService";

const ListAdvertise = () => {
  const [advertises, setAdvertises] = useState<Advertise[]>([]);
  const [filteredAdvertises, setFilteredAdvertises] = useState<Advertise[]>([]);
  const [searchTitle, setSearchTitle] = useState<string>("");
  const [searchCategory, setSearchCategory] = useState<string>("");
  const [minPrice, setMinPrice] = useState<number | null>(null);
  const [maxPrice, setMaxPrice] = useState<number | null>(null);
  const [user, setUser] = useState<any>(null); // Estado para armazenar o usuário
  const [showNearby, setShowNearby] = useState<boolean>(false); // Estado para controle de busca por localização
  const navigate = useNavigate();
  const categories = [
    "Eletrônicos",
    "Roupas",
    "Curos e Aulas",
    "Automóveis",
    "Imóveis",
    "Beleza",
    "Esportes",
    "Móveis",
    "Brinquedos",
    "Livros",
    "Música",
    "Jardinagem",
    "Ferramentas",
    "Utensílios Domésticos",
    "Computadores",
    "Celulares",
    "Roupas de Bebê",
    "Animais",
    "Jogos",
    "Artesanato",
    "Colecionáveis",
    "Papelaria",
  ];


  useEffect(() => {
    fetchAdvertises();
    fetchCurrentUser(); // Buscar dados do usuário
  }, []);

  useEffect(() => {
    handleSearch();
  }, [searchTitle, searchCategory, minPrice, maxPrice]);

  const fetchAdvertises = async () => {
    try {
      const data = await getAdvertises();
      setAdvertises(data);
      setFilteredAdvertises(data);
    } catch (error) {
      console.error("Erro ao buscar anúncios:", error);
    }
  };

  const fetchCurrentUser = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch("http://localhost:8080/auth/current-user", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Erro na requisição");
      }

      const data = await response.json();
      setUser(data);
    } catch (error) {
      console.error("Erro ao buscar usuário:", error);
    }
  };

  const handleSearch = () => {
    const filtered = advertises.filter((ad) => {
      const matchTitle =
        searchTitle.trim() === "" ||
        ad.title.toLowerCase().includes(searchTitle.toLowerCase());
      const matchCategory =
        searchCategory.trim() === "" ||
        ad.category.toLowerCase().includes(searchCategory.toLowerCase());
      const matchPrice =
        (minPrice === null || ad.price >= minPrice) &&
        (maxPrice === null || ad.price <= maxPrice);
      return matchTitle && matchCategory && matchPrice;
    });
    setFilteredAdvertises(filtered);
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

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const { latitude, longitude } = position.coords;
        console.log("Latitude:", latitude);
        console.log("Longitude:", longitude);

        try {
          const token = localStorage.getItem("token");
          if (!token) {
            console.error("Token não encontrado.");
            return;
          }
          const response = await fetch(
            `http://localhost:8080/anuncios/nearby?lat=${latitude}&lng=${longitude}&radius=10`,
            {
              method: "GET",
              headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
              },
            }
          );

          if (!response.ok) {
            console.error("Erro ao buscar anúncios próximos. Status:", response.status);
            const errorResponse = await response.json();
            console.error("Detalhes do erro:", errorResponse);
            return;
          }

          const data = await response.json();
          console.log("Anúncios próximos:", data);

          setAdvertises(data);
          setFilteredAdvertises(data);
          setShowNearby(true);
        } catch (error) {
          console.error("Erro ao buscar anúncios próximos:", error);
        }
      },
      (error) => {
        console.error("Erro ao obter localização:", error);
      },
      {
        enableHighAccuracy: true, // Opção de alta precisão
        timeout: 5000, // Tempo máximo de espera (5 segundos)
        maximumAge: 0 // Não usar localização armazenada em cache
      }
    );
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
        const token = localStorage.getItem("token");

        const response = await fetch(
          "http://localhost:8080/anuncios/delete",
          {
            method: "POST",
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ id }),
          }
        );

        if (!response.ok) {
          throw new Error("Erro ao deletar anúncio");
        }

        await fetchAdvertises(); // Atualiza a tabela de anúncios
        Swal.fire(
          "Deletado!",
          "O anúncio foi excluído com sucesso.",
          "success"
        );
      } catch (error) {
        console.error("Erro ao deletar anúncio:", error);
        Swal.fire("Erro", "Ocorreu um erro ao deletar o anúncio.", "error");
      }
    }
  };

  return (
    <div className="w-screen h-full p-4">
      <div className="text-xl font-semibold mb-4">Anúncios</div>

      <div className="mb-4 flex flex-wrap gap-4 items-end">
        {/* Input for Title */}
        <div className="flex flex-col w-1/4">
          <label className="mb-2 font-medium">Título</label>
          <input
            type="text"
            placeholder="Pesquisar por título"
            value={searchTitle}
            onChange={(e) => setSearchTitle(e.target.value)}
            className="p-2 border border-gray-300 rounded"
          />
        </div>

        {/* Input for Category */}
        <div className="flex flex-col w-1/4">
          <label className="mb-2 font-medium">Categoria</label>
          <select
            value={searchCategory}
            onChange={(e) => setSearchCategory(e.target.value)}
            className="p-2 border border-gray-300 rounded bg-white"
          >
            <option value="">Todas as Categorias</option>
            {categories.map((category, index) => (
              <option key={index} value={category}>
          {category}
              </option>
            ))}
          </select>
        </div>


        {/* Input for Minimum Price */}
        <div className="flex flex-col w-1/6">
          <label className="mb-2 font-medium">Preço mínimo</label>
          <input
            type="number"
            placeholder="Preço mínimo"
            value={minPrice || ""}
            onChange={(e) =>
              setMinPrice(e.target.value ? parseFloat(e.target.value) : null)
            }
            className="p-2 border border-gray-300 rounded"
          />
        </div>

        {/* Input for Maximum Price */}
        <div className="flex flex-col w-1/6">
          <label className="mb-2 font-medium">Preço máximo</label>
          <input
            type="number"
            placeholder="Preço máximo"
            value={maxPrice || ""}
            onChange={(e) =>
              setMaxPrice(e.target.value ? parseFloat(e.target.value) : null)
            }
            className="p-2 border border-gray-300 rounded"
          />
        </div>

        {/* Button for Nearby Search */}
        <button
          onClick={handleSearchByLocation}
          className={`flex items-center px-4 py-2 ${showNearby ? "bg-green-600" : "bg-blue-600"
            } text-white rounded hover:bg-blue-700 shadow-md`}
        >
          {showNearby ? "Mostrar Todos" : "Buscar Próximos"}
        </button>



        {/* Criar Anúncio Button */}
        <button
          onClick={() => navigate("/create-advertise")}
          className="flex items-center px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 shadow-md ml-2"
        >
          <AddIcon className="mr-2" />
          Criar Anúncio
        </button>
      </div>

      {/* Lista de Anúncios */}
      {filteredAdvertises.length > 0 ? (
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
          {filteredAdvertises.map((ad: any) => (
            <div key={ad.id} className="relative">
              <AdvertiseCard ad={ad} />
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
        <p className="text-gray-700">Nenhum anúncio encontrado...</p>
      )}
    </div>
  );
};

export default ListAdvertise;
