import React, { useState } from "react";
import Maps from "../../components/Maps";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import Button from "@mui/material/Button";
import SendIcon from "@mui/icons-material/Send";
import CancelIcon from "@mui/icons-material/Cancel";
import Stack from "@mui/material/Stack";

const CreateAdvertise = () => {
    const [title, setTitle] = useState<string>("");
    const [description, setDescription] = useState<string>("");
    const [category, setCategory] = useState<string>("");
    const [price, setPrice] = useState<number | "">("");
    const [contactInfo, setContactInfo] = useState<string>("");
    const [address, setAddress] = useState<string>("");
    const [image1, setImage1] = useState<File | null>(null);
    const [image2, setImage2] = useState<File | null>(null);
    const [image3, setImage3] = useState<File | null>(null);
    const [preview1, setPreview1] = useState<string | null>(null);
    const [preview2, setPreview2] = useState<string | null>(null);
    const [preview3, setPreview3] = useState<string | null>(null);
    const [latitude, setLatitude] = useState<number | null>(null);
    const [longitude, setLongitude] = useState<number | null>(null);
    const [error, setError] = useState<string>("");
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



    const navigate = useNavigate();

    const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>, setImage: Function, setPreview: Function) => {
        const file = e.target.files?.[0] || null;
        setImage(file);

        if (file) {
            const reader = new FileReader();
            reader.onload = () => {
                setPreview(reader.result as string);
            };
            reader.readAsDataURL(file);
        } else {
            setPreview(null);
        }
    };

    const handleSave = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError("");

        // Verifica se todos os campos obrigatórios estão preenchidos
        if (!title || !description || !category || price === "" || !contactInfo || !address || !latitude || !longitude || !image1 || !image2 || !image3) {
            Swal.fire({
                title: "Erro",
                text: "Todos os campos são obrigatórios. Por favor, preencha todos os campos e envie 3 imagens.",
                icon: "error",
                confirmButtonText: "Ok",
            });
            return;
        }

        const result = await Swal.fire({
            title: "Você tem certeza?",
            text: "Deseja realmente salvar este anúncio?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Sim, salvar!",
            cancelButtonText: "Não, cancelar",
        });

        if (result.isConfirmed) {
            const token = localStorage.getItem("token");

            const newAd = {
                title,
                description,
                category,
                price: Number(price),
                contactInfo,
                address,
                latitude,
                longitude,
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
                    setError(errorData.message || "Erro ao salvar anúncio");
                    return;
                }

                const data = await response.json();
                console.log("Anúncio salvo com sucesso:", data);

                setTitle("");
                setDescription("");
                setCategory("");
                setPrice("");
                setContactInfo("");
                setAddress("");
                setImage1(null);
                setImage2(null);
                setImage3(null);
                setPreview1(null);
                setPreview2(null);
                setPreview3(null);

                Swal.fire("Sucesso!", "Seu anúncio foi salvo com sucesso!", "success");

                navigate("/advertises");
            } catch (error) {
                setError("Erro ao salvar anúncio.");
                console.error("Erro ao salvar anúncio:", error);
            }
        }
    };

    return (
        <div className="w-screen h-full p-6 bg-gray-50" style={{ paddingTop: "64px" }}>
            <div className="text-2xl font-semibold mb-6">Criar novo Anúncio</div>
            <form onSubmit={handleSave} className="space-y-6 bg-white p-6 rounded shadow-md">
                {error && <div className="text-red-500">{error}</div>}

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <input
                        type="text"
                        placeholder="Título"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-blue-300"
                    />
                    <input
                        placeholder="Descrição"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-blue-300"
                    />
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        className="w-full bg-white p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-blue-300"
                    >
                        <option value="">Selecione uma categoria</option>
                        {categories.map((cat) => (
                            <option key={cat} value={cat}>{cat}</option>
                        ))}
                    </select>

                    <input
                        type="number"
                        placeholder="Preço"
                        value={price}
                        onChange={(e) => setPrice(e.target.value ? parseFloat(e.target.value) : "")}
                        className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-blue-300"
                    />
                    <input
                        type="text"
                        placeholder="Informações de contato"
                        value={contactInfo}
                        onChange={(e) => setContactInfo(e.target.value)}
                        className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-blue-300"
                    />
                </div>

                <div className="text-xl font-semibold mb-4 text-left">Selecionar Imagens</div>

                <div className="grid grid-cols-1 sm:grid-cols-1 gap-4">
                    <div className="text-left">
                        {preview1 && <img src={preview1} alt="Pré-visualização 1" className="w-auto h-32 object-contain mb-2" />}
                        <input
                            type="file"
                            onChange={(e) => handleImageChange(e, setImage1, setPreview1)}
                            className="p-2 border border-gray-300 rounded w-full sm:w-auto"
                            accept="image/*"
                        />
                    </div>
                    <div className="text-left">
                        {preview2 && <img src={preview2} alt="Pré-visualização 2" className="w-auto h-32 object-contain mb-2" />}
                        <input
                            type="file"
                            onChange={(e) => handleImageChange(e, setImage2, setPreview2)}
                            className="p-2 border border-gray-300 rounded w-full sm:w-auto"
                            accept="image/*"
                        />
                    </div>
                    <div className="text-left">
                        {preview3 && <img src={preview3} alt="Pré-visualização 3" className="w-auto h-32 object-contain mb-2" />}
                        <input
                            type="file"
                            onChange={(e) => handleImageChange(e, setImage3, setPreview3)}
                            className="p-2 border border-gray-300 rounded w-full sm:w-auto"
                            accept="image/*"
                        />
                    </div>
                </div>

                <div className="text-xl font-semibold mb-4 text-left">Endereço</div>

                <Maps
                    onLocationSelect={(lat, lng, address) => {
                        setLatitude(lat);
                        setLongitude(lng);
                        setAddress(address);
                    }}
                />

                <Stack direction="row" spacing={2} justifyContent="flex-end">
                    <Button type="submit" variant="contained" endIcon={<SendIcon />} color="primary">
                        Salvar
                    </Button>
                    <Button
                        variant="outlined"
                        startIcon={<CancelIcon />}
                        color="error"
                        onClick={() => navigate("/advertises")}
                    >
                        Cancelar
                    </Button>
                </Stack>
            </form>
        </div>
    );
};

export default CreateAdvertise;
