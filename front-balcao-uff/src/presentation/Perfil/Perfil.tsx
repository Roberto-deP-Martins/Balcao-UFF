import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';

const Profile = () => {
  const [userData, setUserData] = useState(null);
  const [ads, setAds] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [selectedTab, setSelectedTab] = useState('ads'); 
  const navigate = useNavigate();

  const getUserLogged = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/auth/current-user`, {
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
      setUserData(data);
    } catch (error) {
      console.error("Erro ao buscar dados do usuário:", error);
    }
  }

  const fetchAds = async () => {
    setAds([
      { id: 1, title: "Anúncio 1", description: "Descrição do anúncio 1" },
      { id: 2, title: "Anúncio 2", description: "Descrição do anúncio 2" },
    ]);
  };

  const fetchReviews = async () => {
    setReviews([
      { id: 1, productName: "Produto 1", rating: 0 },
      { id: 2, productName: "Produto 2", rating: 0 },
    ]);
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    } else {
      getUserLogged();
      fetchAds();
      fetchReviews();
    }
  }, [navigate]);

  return (
    <div className="flex flex-col h-screen">
      <Navbar />
      <main className="flex flex-1 flex-col items-center p-6">
        <div className="flex flex-col items-center bg-white rounded-xl shadow-xl p-8 w-full max-w-3xl">
          <div className="w-60 h-60 bg-gray-300 rounded-full overflow-hidden mb-8 border-4 border-gray-200">
            {userData?.profileImage ? (
              <img src={userData.profileImage} alt="Profile" className="w-full h-full object-cover" />
            ) : (
              <div className="w-full h-full bg-gray-200 flex items-center justify-center text-5xl text-gray-500">
                {userData?.name?.charAt(0)}
              </div>
            )}
          </div>

          <h1 className="text-4xl font-semibold mb-4">{userData?.name || 'Nome do Usuário'}</h1>
          <p className="text-lg text-gray-600 mb-6">{userData?.email || 'Email do Usuário'}</p>

          <div className="flex mb-6 gap-4">
            <button
              className={`px-6 py-2 text-lg font-medium rounded-full transition-all duration-200 ${selectedTab === 'ads' ? 'bg-blue-500 text-white shadow-lg' : 'bg-white text-blue-500 border-2 border-blue-500'} hover:bg-blue-600 hover:text-white`}
              onClick={() => setSelectedTab('ads')}
            >
              Meus Anúncios
            </button>
            <button
              className={`px-6 py-2 text-lg font-medium rounded-full transition-all duration-200 ${selectedTab === 'reviews' ? 'bg-blue-500 text-white shadow-lg' : 'bg-white text-blue-500 border-2 border-blue-500'} hover:bg-blue-600 hover:text-white`}
              onClick={() => setSelectedTab('reviews')}
            >
              Avaliar Produtos
            </button>
          </div>

          {selectedTab === 'ads' && (
            <div className="w-full">
              {ads.length > 0 ? (
                ads.map((ad) => (
                  <div key={ad.id} className="border-b mb-4 pb-4">
                    <h3 className="text-xl font-semibold">{ad.title}</h3>
                    <p className="text-gray-600">{ad.description}</p>
                  </div>
                ))
              ) : (
                <p className="text-center text-gray-500">Nenhum anúncio.</p>
              )}
            </div>
          )}

          {selectedTab === 'reviews' && (
            <div className="w-full">
              {reviews.length > 0 ? (
                reviews.map((review) => (
                  <div key={review.id} className="border-b mb-4 pb-4">
                    <h3 className="text-xl font-semibold">{review.productName}</h3>
                    <p className="text-gray-600">Avaliação: {review.rating === 0 ? 'Não avaliado' : review.rating}</p>
                    <button className="mt-2 text-blue-500">Avaliar Produto</button>
                  </div>
                ))
              ) : (
                <p className="text-center text-gray-500">Nenhum produto para avaliar.</p>
              )}
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default Profile;
