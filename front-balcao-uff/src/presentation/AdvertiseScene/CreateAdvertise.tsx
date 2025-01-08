import { useState } from "react";
import Maps from "../../components/Maps";
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom'; // Importando o useNavigate

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
    const [latitude, setLatitude] = useState<number | null>(null);
    const [longitude, setLongitude] = useState<number | null>(null);
    const [error, setError] = useState<string>("");
    
    const navigate = useNavigate();

    const handleSave = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError("");
    
        if (!image1 || !image2 || !image3) {
            Swal.fire({
                title: 'Erro',
                text: "Por favor, envie exatamente 3 imagens.",
                icon: 'error',
                confirmButtonText: 'Ok',
            });
            return;
        }
    
        const result = await Swal.fire({
            title: 'Você tem certeza?',
            text: "Deseja realmente salvar este anúncio?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Sim, salvar!',
            cancelButtonText: 'Não, cancelar',
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
    
                Swal.fire(
                    'Sucesso!',
                    'Seu anúncio foi salvo com sucesso!',
                    'success'
                );
    
                navigate("/advertises");
    
            } catch (error) {
                setError("Erro ao salvar anúncio.");
                console.error("Erro ao salvar anúncio:", error);
            }
        }
    };
    

    return (
        <div className="w-screen h-full p-4">
            <div className="text-xl font-semibold mb-4">
                Criar novo Anúncio
            </div>
            <form onSubmit={handleSave} className="space-y-4">
                {error && <div className="text-red-500">{error}</div>}
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
                    onChange={(e) => setPrice(e.target.value ? parseFloat(e.target.value) : "")}
                    className="w-full p-2 border border-gray-300 rounded"
                />
                <input
                    type="text"
                    placeholder="Informações de contato"
                    value={contactInfo}
                    onChange={(e) => setContactInfo(e.target.value)}
                    className="w-full p-2 border border-gray-300 rounded"
                />

                <div className="w-full p-2 border border-gray-300 rounded mb-4">
                    Endereço: {address}
                </div>

                <Maps
                    onLocationSelect={(lat, lng, address) => {
                        setLatitude(lat);
                        setLongitude(lng);
                        setAddress(address);
                    }}
                />

                <input
                    type="file"
                    onChange={(e) => setImage1(e.target.files ? e.target.files[0] : null)}
                    className="w-full p-2 border border-gray-300 rounded"
                    accept="image/*"
                />
                <input
                    type="file"
                    onChange={(e) => setImage2(e.target.files ? e.target.files[0] : null)}
                    className="w-full p-2 border border-gray-300 rounded"
                    accept="image/*"
                />
                <input
                    type="file"
                    onChange={(e) => setImage3(e.target.files ? e.target.files[0] : null)}
                    className="w-full p-2 border border-gray-300 rounded"
                    accept="image/*"
                />

                <button
                    type="submit"
                    className="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                >
                    Salvar
                </button>
            </form>
        </div>
    );
};

export default CreateAdvertise;
