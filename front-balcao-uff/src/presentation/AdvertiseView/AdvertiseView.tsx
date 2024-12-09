import { useLocation } from 'react-router-dom';
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css";

const AdvertiseView = () => {
  const location = useLocation();
  const ad = location.state?.ad;

  if (!ad) {
    return (
      <div className="flex items-center justify-center h-screen">
        <p className="text-gray-500 text-xl">Detalhes do anúncio não encontrados.</p>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-6 py-10">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
        {/* Galeria de Imagens */}
        <div className="rounded-lg shadow-lg overflow-hidden">
          {ad.imagePaths && ad.imagePaths.length > 0 ? (
            <Carousel showThumbs={false} infiniteLoop showStatus={false} className="rounded-lg">
              {ad.imagePaths.map((path: string, index: number) => {
                const fileName = path.substring(path.lastIndexOf('/') + 1);
                return (
                  <div key={index}>
                    <img
                      src={`http://localhost:8080/anuncioImages/image/${fileName}`}
                      alt={`Imagem ${index + 1}`}
                      className="w-full object-cover rounded-lg"
                    />
                  </div>
                );
              })}
            </Carousel>
          ) : (
            <div className="w-full h-96 bg-gray-200 flex items-center justify-center rounded-lg">
              <span className="text-gray-500">Sem imagem disponível</span>
            </div>
          )}
        </div>

        <div>
          <h1 className="text-3xl font-bold text-gray-800 mb-4">{ad.title}</h1>
          <p className="text-2xl text-green-600 font-bold mb-4">
            R$ {ad.price.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
          </p>
          <p className="text-gray-700 text-base mb-4">
            <span className="font-semibold">Categoria:</span> {ad.category}
          </p>
          <p className="text-gray-700 text-base mb-6">
            <span className="font-semibold">Descrição:</span> {ad.description}
          </p>

          <div className="border-t pt-4 mt-4">
            <p className="text-gray-700 text-base mb-2">
              <span className="font-semibold">Contato:</span> {ad.contactInfo}
            </p>
            <p className="text-gray-700 text-base">
              <span className="font-semibold">Localização:</span> {ad.location}
            </p>
          </div>

          <div className="mt-8">
            <button
              className="w-full bg-orange-500 text-white py-3 rounded-lg hover:bg-orange-600 transition"
              onClick={() => alert("Entrando em contato!")}
            >
              Contatar Vendedor
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdvertiseView;
