/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from "react";
import AdvertiseCard from "../../components/AdvertiseCard";

const ListAdvertise = () => {
  const [activeTab, setActiveTab] = useState<"list" | "create">("list");
  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [category, setCategory] = useState<string>("");
  const [price, setPrice] = useState<number | "">("");
  const [contactInfo, setContactInfo] = useState<string>("");
  const [location, setLocation] = useState<string>("");
  const [userId, setUserId] = useState<number | "">("");
  const [image1, setImage1] = useState<File | null>(null);
  const [image2, setImage2] = useState<File | null>(null);
  const [image3, setImage3] = useState<File | null>(null);
  const [advertises, setAdvertises] = useState<any[]>([]);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    if (activeTab === "list") {
      fetchAdvertises();
    }
  }, [activeTab]);

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

  const handleTabChange = (tab: "list" | "create") => {
    if (tab === "create") {
      setCategory("");
    }
    setActiveTab(tab);
  };

  const handleSave = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");

    if (!image1 || !image2 || !image3) {
      setError("Por favor, envie exatamente 3 imagens.");
      return;
    }

    const token = localStorage.getItem("token");

    const newAd = {
      title,
      description,
      category,
      price: Number(price),
      contactInfo,
      location,
      userId: Number(userId),
    };

    const formData = new FormData();
    formData.append("anuncio", JSON.stringify(newAd));
    formData.append("images", image1);
    formData.append("images", image2);
    formData.append("images", image3);

    try {
      const response = await fetch("http://localhost:8080/anuncios/save2", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) {
        const errorData = await response.json();
        setError(
          `Erro ao salvar anúncio: ${
            errorData.message || "Verifique os dados e tente novamente."
          }`
        );
        return;
      }

      const data = await response.json();
      console.log("Anúncio salvo com sucesso:", data);

      setTitle("");
      setDescription("");
      setCategory("");
      setPrice("");
      setContactInfo("");
      setLocation("");
      setUserId("");
      setImage1(null);
      setImage2(null);
      setImage3(null);
      setActiveTab("list");
    } catch (error) {
      setError("Erro ao salvar anúncio. Verifique os dados e tente novamente.");
      console.error("Erro ao salvar anúncio:", error);
    }
  };

  return (
    <div className="w-screen h-full">
      <div className="flex">
        <div
          className={`flex-1 text-center p-4 cursor-pointer ${
            activeTab === "list" ? "bg-blue-600 text-white" : "bg-gray-200"
          }`}
          onClick={() => handleTabChange("list")}
        >
          Listagem
        </div>
        <div
          className={`flex-1 text-center p-4 cursor-pointer ${
            activeTab === "create" ? "bg-blue-600 text-white" : "bg-gray-200"
          }`}
          onClick={() => handleTabChange("create")}
        >
          Criação
        </div>
      </div>

      {activeTab === "list" && (
        <div className="p-4">
          <div className="mb-4">
            <input
              type="text"
              placeholder="Buscar por categoria"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  handleSearchByCategory();
                }
              }}
              className="w-full p-2 border border-gray-300 rounded mb-2"
            />
            <button
              onClick={handleSearchByCategory}
              className="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              Buscar
            </button>
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
      )}

      {activeTab === "create" && (
        <form onSubmit={handleSave} className="space-y-4 p-4">
          {error && <div className="text-red-500 mb-4">{error}</div>}
          <input
            type="text"
            placeholder="Título"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="text"
            placeholder="Descrição"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="text"
            placeholder="Categoria"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="number"
            placeholder="Preço"
            value={price}
            onChange={(e) =>
              setPrice(e.target.value ? parseFloat(e.target.value) : "")
            }
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="text"
            placeholder="Informações de contato"
            value={contactInfo}
            onChange={(e) => setContactInfo(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="text"
            placeholder="Localização"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="number"
            placeholder="ID do Usuário"
            value={userId}
            onChange={(e) =>
              setUserId(e.target.value ? parseInt(e.target.value) : "")
            }
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="file"
            onChange={(e) =>
              setImage1(e.target.files ? e.target.files[0] : null)
            }
            className="w-full p-2 border border-gray-300 rounded"
            accept="image/*"
          />
          <input
            type="file"
            onChange={(e) =>
              setImage2(e.target.files ? e.target.files[0] : null)
            }
            className="w-full p-2 border border-gray-300 rounded"
            accept="image/*"
          />
          <input
            type="file"
            onChange={(e) =>
              setImage3(e.target.files ? e.target.files[0] : null)
            }
            className="w-full p-2 border border-gray-300 rounded"
            accept="image/*"
          />
          <button
            type="submit"
            className="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            Submit
          </button>
        </form>
      )}
    </div>
  );
};

export default ListAdvertise;
