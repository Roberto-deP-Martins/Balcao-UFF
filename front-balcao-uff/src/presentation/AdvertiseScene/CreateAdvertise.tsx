import { useState } from "react";
import TextField from '@mui/material/TextField';
import Maps from "../../components/Maps";
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send';
import CancelIcon from '@mui/icons-material/Cancel';
import Stack from '@mui/material/Stack';



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
        <div className="w-screen h-full p-6 bg-gray-50">
            <div className="text-2xl font-semibold mb-6">
                Criar novo Anúncio
            </div>
            <form onSubmit={handleSave} className="space-y-6 bg-white p-6 rounded shadow-md">
                {error && <div className="text-red-500">{error}</div>}

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <TextField
                        id="title"
                        label="Título"
                        variant="outlined"
                        fullWidth
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                    <TextField
                        id="description"
                        label="Descrição"
                        variant="outlined"
                        fullWidth
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />
                    <TextField
                        id="category"
                        label="Categoria"
                        variant="outlined"
                        fullWidth
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                    />
                    <TextField
                        id="price"
                        label="Preço"
                        variant="outlined"
                        type="number"
                        fullWidth
                        value={price}
                        onChange={(e) => setPrice(e.target.value ? parseFloat(e.target.value) : "")}
                    />
                    <TextField
                        id="contactInfo"
                        label="Informações de contato"
                        variant="outlined"
                        fullWidth
                        value={contactInfo}
                        onChange={(e) => setContactInfo(e.target.value)}
                    />
                </div>

                <div className="text-xl font-semibold mb-4 text-left">
                    Selecionar Imagens
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                    <input
                        type="file"
                        onChange={(e) => setImage1(e.target.files ? e.target.files[0] : null)}
                        className="p-2 border border-gray-300 rounded"
                        accept="image/*"
                    />
                    <input
                        type="file"
                        onChange={(e) => setImage2(e.target.files ? e.target.files[0] : null)}
                        className="p-2 border border-gray-300 rounded"
                        accept="image/*"
                    />
                    <input
                        type="file"
                        onChange={(e) => setImage3(e.target.files ? e.target.files[0] : null)}
                        className="p-2 border border-gray-300 rounded"
                        accept="image/*"
                    />
                </div>

                <div className="text-xl font-semibold mb-4 text-left">
                    Endereço
                </div>

                <Maps
                    onLocationSelect={(lat, lng, address) => {
                        setLatitude(lat);
                        setLongitude(lng);
                        setAddress(address);
                    }}
                />

                <Stack direction="row" spacing={2} justifyContent="flex-end">
                    <Button
                        type="submit"
                        variant="contained"
                        endIcon={<SendIcon />}
                        color="primary"
                    >
                        Salvar
                    </Button>
                    <Button
                        variant="outlined"
                        startIcon={<CancelIcon />}
                        color="error"
                        onClick={() => navigate('/advertises')}
                    >
                        Cancelar
                    </Button>
                </Stack>


            </form>
        </div>
    );
};

export default CreateAdvertise;
