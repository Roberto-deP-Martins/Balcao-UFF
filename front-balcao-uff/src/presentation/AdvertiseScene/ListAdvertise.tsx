import { useEffect, useState } from "react";
import AdvertiseCard from "../../components/AdvertiseCard";
import { FaSearch, FaMapMarkerAlt } from "react-icons/fa";
import { Link } from "react-router-dom";  // Importando o Link para navegação

const ListAdvertise = () => {
  const [advertises, setAdvertises] = useState<any[]>([]);
  const [category, setCategory] = useState<string>("");
  const [isNearby, setIsNearby] = useState<boolean>(false); // Estado para controlar o checkbox de anúncios próximos
  const [showNearby, setShowNearby] = useState<boolean>(false); // Estado para controlar se os anúncios próximos estão sendo exibidos

  useEffect(() => {
    fetchAdvertises();
  }, []);

  const fetchAdvertises = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch("http://localhost:8080/anuncios", {
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
    } catch (error) {
      console.error("Erro ao buscar anúncios:", error);
    }
  };

  const handleSearchByCategory = async () => {
    if (!category.trim()) {
      fetchAdvertises();
      return;
    }

    try {
      const token = localStorage.getItem("token");

      const response = await fetch("http://localhost:8080/anuncios/category", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ category }),
      });

      if (!response.ok) {
        throw new Error("Erro na requisição");
      }

      const data = await response.json();
      setAdvertises(data);
    } catch (error) {
      console.error("Erro ao buscar anúncios por categoria:", error);
    }
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
      {/* Adicionando o label "Anúncios" */}
      <div className="text-xl font-semibold mb-4">
        Anúncios
      </div>
      <div className="mb-4 flex items-center justify-between">
        <input
          type="text"
          placeholder="Digite a categoria"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              handleSearchByCategory();
            }
          }}
          className="p-2 border border-gray-300 rounded mb-2 w-2/3"
        />
        <button
          onClick={handleSearchByCategory}
          className="w-10 h-10 flex items-center justify-center p-2 bg-blue-600 text-white rounded-full hover:bg-blue-700 ml-2"
        >
          <FaSearch />
        </button>
        <Link to="/create-advertise" className="px-4 py-2 bg-blue-600 text-white rounded-full hover:bg-blue-700 shadow-md ml-2">
          Criar Novo Anúncio
        </Link>
      </div>

      {/* Botão de "Buscar Anúncios Próximos" */}
      <div className="mb-4 flex items-center space-x-2">
        <button
          onClick={handleSearchByLocation}
          className="w-10 h-10 flex items-center justify-center p-2 bg-red-600 text-white rounded-full hover:bg-red-700"
          >
          <FaMapMarkerAlt />
        </button>
          <span>{showNearby ? "Todos os anúncios" : "Buscar Anúncios Próximos"}</span>
      </div>

      {advertises.length > 0 ? (
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
          {advertises.map((ad: any) => (
            <AdvertiseCard key={ad.id} ad={ad} />
          ))}
        </div>
      ) : (
        <p className="text-gray-700">
          Carregando ou nenhum anúncio disponível...
        </p>
      )}
    </div>
  );
};

export default ListAdvertise;
