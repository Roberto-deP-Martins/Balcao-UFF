import "react-responsive-carousel/lib/styles/carousel.min.css";
import { Carousel, } from "react-responsive-carousel";
import { useNavigate } from 'react-router-dom';
interface Ad {
  imagePaths: string[];
  title: string;
  category: string;
  description: string;
  price: number;
  contactInfo: string;
  location: string;
}

const AdvertiseCard = ({ ad }: { ad: Ad }) => {
  const navigate = useNavigate();
  const handleVerMais = () => {
    navigate("/advertiseView", { state: { ad } });
  };
  return (
    <div className="border border-gray-300 rounded-lg shadow-lg bg-white hover:shadow-xl transition-shadow duration-300 ease-in-out overflow-hidden max-w-md mx-auto">
      {ad.imagePaths && ad.imagePaths.length > 0 ? (
        <Carousel
          showThumbs={false}
          infiniteLoop
          useKeyboardArrows
          showStatus={false}
          autoPlay
          className="relative"
        >
          {ad.imagePaths.map((path, index) => (
            <div key={index} className="relative">
              <img
                src={`http://localhost:8080/anuncioImages/image/${path.split("\\").pop()}`}
                alt={`Imagem ${index + 1}`}
                className="w-full h-56 object-cover"
              />
            </div>
          ))}
        </Carousel>
      ) : (
        <div className="w-full h-56 bg-gray-200 flex items-center justify-center">
          <span className="text-gray-500">Sem imagem disponível</span>
        </div>
      )}

      <div className="p-4">
        <p className="text-left text-gray-800 text-xl  font-bold mb-2">
          R$ {ad.price.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
        </p>
        <h3 className="text-left text-lg text-gray-800 mb-2">{ad.title}</h3>
        {/* <p className="text-sm text-gray-600 mb-1">
          <span className="font-bold">Categoria:</span> {ad.category}
        </p> */}
        <div className="flex pt-2">
          <p className="text-left text-sm text-gray-700 font-bold" >Descrição: <span className="text-left text-sm text-gray-700 font-normal">{ad.description}</span></p> 
        </div>

        <div className="flex pt-2">
          <p className="text-left text-sm text-gray-700 font-bold" >Contato: <span className="text-left text-sm text-gray-700 font-normal">{ad.contactInfo}</span></p> 
        </div>

        <div className="flex pt-2">
          <p className="text-left text-sm text-gray-700 font-bold" >Localização: <span className="text-left text-sm text-gray-700 font-normal">{ad.location}</span></p> 
        </div>
      </div>

      <div className="p-4 bg-gray-50 border-t flex justify-between items-center">
        <button className="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition">
          Contatar Vendedor
        </button>
        <button className="px-4 py-2 bg-gray-100 text-gray-800 rounded hover:bg-gray-200 transition" onClick={() => handleVerMais()}>
          Ver Mais
        </button>
      </div>
    </div>
  );
};

export default AdvertiseCard;
