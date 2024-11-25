import "react-responsive-carousel/lib/styles/carousel.min.css";
import { Carousel } from 'react-responsive-carousel';

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
  return (
    <div className="border border-gray-300 rounded p-4 shadow-sm w-full bg-white hover:shadow-lg transition-shadow duration-300 ease-in-out">
      {ad.imagePaths && ad.imagePaths.length > 0 ? (
        <Carousel showThumbs={false} infiniteLoop useKeyboardArrows>
          {ad.imagePaths.map((path, index) => (
            <div key={index} className="relative group">
              <img
                src={`http://localhost:8080/anuncioImages/image/${path.split('\\').pop()}`}
                alt={`Imagem ${index + 1}`}
                className="w-full h-64 object-cover rounded transition-transform duration-300 ease-in-out group-hover:scale-105"
              />
            </div>
          ))}
        </Carousel>
      ) : (
        <div className="w-full h-64 bg-gray-200 mb-4 rounded flex items-center justify-center">
          <span className="text-gray-500">Sem imagem disponível</span>
        </div>
      )}
      <div className="p-4 bg-gray-100 rounded-lg text-left">
        <h3 className="text-lg font-semibold mb-2">{ad.title}</h3>
        <p className="text-sm text-gray-700"><span className="font-bold">Categoria:</span> {ad.category}</p>
        <p className="text-sm text-gray-700"><span className="font-bold">Descrição:</span> {ad.description}</p>
        <p className="text-sm text-gray-700"><span className="font-bold">Preço:</span> R$ {ad.price}</p>
        <p className="text-sm text-gray-700"><span className="font-bold">Contato:</span> {ad.contactInfo}</p>
        <p className="text-sm text-gray-700"><span className="font-bold">Localização:</span> {ad.location}</p>
      </div>
    </div>
  );
};

export default AdvertiseCard;
