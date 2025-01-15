import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import { UserData, Review, Ad, Transaction } from '../../interfaces/interfaces';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import { getUserProfile, getUserReviews, fetchAds, getUserReputation, getCurrentUser, fetchTransactions } from '../../service/userservice';
import { API_CONFIG } from '../../service/config';

const Profile = () => {
  const [userData, setUserData] = useState<UserData | null>(null);
  const [ads, setAds] = useState<Ad[]>([]);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [reputation, setReputation] = useState<number | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [selectedTab, setSelectedTab] = useState('ads');
  const [currentUserId, setCurrentUserId] = useState<number | null>(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState<Transaction | null>(null);
  const [rating, setRating] = useState<number | null>(null);
  const [comment, setComment] = useState('');
  const navigate = useNavigate();
  const { idUser } = useParams();

  const getToken = () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate('/login');
      return null;
    }
    return token;
  };


  useEffect(() => {
    if (!idUser) {
      navigate('/login');
    } else {
      getUserProfile(idUser);
      getUserReviews(idUser);
      fetchAds(idUser);
      getUserReputation(idUser);
      getCurrentUser();
    }
  }, [idUser, navigate]);


  useEffect(() => {
    if (!idUser) {
      navigate('/login');
    } else {
      getUserProfile(idUser).then(data => {
        if (data) {
          setUserData(data);
        }
      });

      getUserReviews(idUser).then(data => {
        if (data) {
          setReviews(data);
        }
      });

      fetchAds(idUser);
      getUserReputation(idUser);
      getCurrentUser();
    }
  }, [idUser, navigate]);

  useEffect(() => {
    if (!idUser) {
      navigate('/login');
    } else {
      getUserProfile(idUser).then(data => {
        if (data) {
          setUserData(data);
        }
      });

      getUserReviews(idUser).then(data => {
        if (data) {
          setReviews(data);
        }
      });

      fetchAds(idUser).then(data => {
        if (data) {
          setAds(data);
        }
      });

      getUserReputation(idUser);
      getCurrentUser();
    }
  }, [idUser, navigate]);

  useEffect(() => {
    if (!idUser) {
      navigate('/login');
    } else {
      getUserProfile(idUser).then(data => {
        if (data) {
          setUserData(data);
        }
      });

      getUserReviews(idUser).then(data => {
        if (data) {
          setReviews(data);
        }
      });

      fetchAds(idUser).then(data => {
        if (data) {
          setAds(data);
        }
      });

      getUserReputation(idUser).then(data => {
        if (data) {
          setReputation(data.reputation);
        }
      });

      getCurrentUser();
    }
  }, [idUser, navigate]);

  useEffect(() => {
    if (!idUser) {
      navigate('/login');
    } else {
      getUserProfile(idUser).then(data => {
        if (data) {
          setUserData(data);
        }
      });

      getUserReviews(idUser).then(data => {
        if (data) {
          setReviews(data);
        }
      });

      fetchAds(idUser).then(data => {
        if (data) {
          setAds(data);
        }
      });

      getUserReputation(idUser).then(data => {
        if (data) {
          setReputation(data.reputation);
        }
      });

      getCurrentUser().then(userId => {
        if (userId) {
          setCurrentUserId(userId);
        }
      });
    }
  }, [idUser, navigate]);

  useEffect(() => {
    if (!idUser) {
      navigate('/login');
    } else {
      getUserProfile(idUser).then(data => {
        if (data) {
          setUserData(data);
        }
      });

      getUserReviews(idUser).then(data => {
        if (data) {
          setReviews(data);
        }
      });

      fetchAds(idUser).then(data => {
        if (data) {
          setAds(data);
        }
      });

      getUserReputation(idUser).then(data => {
        if (data) {
          setReputation(data.reputation);
        }
      });

      getCurrentUser().then(userId => {
        if (userId) {
          setCurrentUserId(userId);
        }
      });

      fetchTransactions(idUser).then(data => {
        if (data) {
          setTransactions(data);
        }
      });
    }
  }, [idUser, navigate]);


  const renderStars = (reputation: number) => {
    const totalStars = 5;
    const filledStars = Math.round(reputation);
    const emptyStars = totalStars - filledStars;

    const stars = [];
    for (let i = 0; i < filledStars; i++) {
      stars.push("★");
    }
    for (let i = 0; i < emptyStars; i++) {
      stars.push("☆");
    }

    return stars.join(" ");
  };

  const handleOpenDialog = (transaction: Transaction) => {
    setSelectedTransaction(transaction);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setRating(null);
    setComment('');
  };

  const handleSubmitReview = async () => {
    if (!rating || !comment || !selectedTransaction) {
      alert("Por favor, forneça uma nota e comentário.");
      return;
    }
  
    const token = getToken();
    if (!token || !selectedTransaction) return;
  
    // Definindo reviewerId e reviewedId com base na transação
    const reviewerId = currentUserId; // Usuário logado
    const reviewedId = selectedTransaction.anuncianteId === currentUserId 
                       ? selectedTransaction.interessadoId 
                       : selectedTransaction.anuncianteId;
  
    const reviewData = {
      reviewerId,  
      reviewedId,  
      rating,      
      comment,     
    };
  
    try {
      const response = await fetch(`${API_CONFIG.BASE_URL}/reviews`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(reviewData),
      });
  
      if (response.ok) {
        alert("Avaliação enviada com sucesso!");
        handleCloseDialog();  // Fechar o Dialog
        fetchTransactions(idUser!);  // Atualizar transações
      } else {
        alert("Erro ao enviar avaliação");
      }
    } catch (error) {
      console.error("Erro ao enviar avaliação:", error);
    }
  };
  
  

  useEffect(() => {
    if (!idUser) {
      navigate('/login');
    } else {
      getUserProfile(idUser);
      getUserReviews(idUser);
      fetchAds(idUser);
      getUserReputation(idUser);
      getCurrentUser();
    }
  }, [idUser, navigate]);

  useEffect(() => {
    if (currentUserId !== null && currentUserId.toString() === idUser) {
      fetchTransactions(idUser);
    }
  }, [currentUserId, idUser]);

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

          <div className="flex items-center mb-4">
            <h1 className="text-4xl font-semibold">{userData?.name || 'Nome do Usuário'}</h1>
            {reputation !== null && (
              <span className="text-yellow-500 text-2xl ml-4">{renderStars(reputation)}</span>
            )}
          </div>

          <p className="text-lg text-gray-600 mb-6">{userData?.email || 'Email do Usuário'}</p>

            <div className="flex mb-6 gap-4">
            <button
              className={`px-6 py-2 text-lg font-medium rounded-full transition-all duration-200 ${selectedTab === 'ads' ? 'bg-blue-500 text-white shadow-lg' : 'bg-white text-blue-500 border-2 border-blue-500'} hover:bg-blue-600 hover:text-white`}
              onClick={() => setSelectedTab('ads')}
              style={{ textAlign: 'left' }}
            >
              Meus Anúncios
            </button>
            <button
              className={`px-6 py-2 text-lg font-medium rounded-full transition-all duration-200 ${selectedTab === 'reviews' ? 'bg-blue-500 text-white shadow-lg' : 'bg-white text-blue-500 border-2 border-blue-500'} hover:bg-blue-600 hover:text-white`}
              onClick={() => setSelectedTab('reviews')}
            >
              Avaliações de Usuários
            </button>
            {currentUserId === Number(idUser) && (
              <button
              className={`px-6 py-2 text-lg font-medium rounded-full transition-all duration-200 ${selectedTab === 'transactions' ? 'bg-blue-500 text-white shadow-lg' : 'bg-white text-blue-500 border-2 border-blue-500'} hover:bg-blue-600 hover:text-white`}
              onClick={() => setSelectedTab('transactions')}
              >
              Minhas Transações
              </button>
            )}
            </div>

          {selectedTab === 'ads' && (
            <div className="w-full mt-6 space-y-4">
              {ads.length > 0 ? (
                ads.map((ad, index) => (
                  <div key={index} className="flex flex-col bg-gray-50 p-4 rounded-lg shadow-sm border border-gray-200">
                    <div className="flex justify-between items-center mb-4">
                      <h3 className="text-xl font-semibold text-gray-800">{ad.title || 'Título não disponível'}</h3>
                    </div>

                    <div className="flex flex-col space-y-2">
                      <p className="text-left text-gray-600">
                      <span className="font-semibold">Categoria:</span> {ad.category || 'Não informada'}
                      </p>
                      <p className="text-left text-gray-600">
                      <span className="font-semibold">Localização:</span> {ad.location || 'Não informada'}
                      </p>
                      <p className="text-left text-gray-600">
                      <span className="font-semibold">Preço:</span> {ad.price ? `$${ad.price}` : 'Doação'}
                      </p>
                        <p className="text-left text-gray-600">
                        <span className="font-semibold">Status:</span> 
                        <span className={ad.available ? 'text-green-500' : 'text-red-500'}>
                        {ad.available ? ' Disponível' : ' Indisponível/Deletado'}
                        </span>
                        </p>
                    </div>
                  </div>
                ))
              ) : (
                <p className="text-center text-gray-500">Nenhum anúncio.</p>
              )}
            </div>
          )}

          {selectedTab === 'reviews' && (
            <div className="w-full mt-6">
              {reviews.length > 0 ? (
                <div className="w-full space-y-4">
                  {reviews.map((review) => (
                    <div key={review.reviewDate} className="flex flex-col bg-gray-50 p-4 rounded-lg shadow-sm border border-gray-200">
                      <div className="flex justify-between items-center mb-2">
                        <h3 className="text-xl font-semibold text-gray-800">{review.reviewerName}</h3>
                        <div className="flex items-center">
                          <span className="text-yellow-400 mr-2">{"★".repeat(review.rating)}</span>
                          <span className="text-sm text-gray-500">({review.rating})</span>
                        </div>
                      </div>
                      <p className="text-gray-600">{review.comment}</p>
                      <p className="text-sm text-gray-400 mt-2">Avaliado em: {new Date(review.reviewDate.split(' ')[0].split('/').reverse().join('-')).toLocaleDateString()}</p>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-center text-gray-500">Nenhuma avaliação recebida.</p>
              )}
            </div>
          )}

          {selectedTab === 'transactions' && (
            <div className="w-full mt-6">
              {transactions.length > 0 ? (
                <div className="w-full space-y-4">
                  {transactions.map((transaction) => (
                    <div key={transaction.anuncioId} className="flex flex-col bg-gray-50 p-4 rounded-lg shadow-sm border border-gray-200">
                      <p className="text-gray-600">Anúncio ID: {transaction.anuncioId}</p>
                      <p className="text-gray-600">Data de Conclusão: {new Date(transaction.dtConclusao).toLocaleDateString()}</p>
                      <p className="text-gray-600">
                        {transaction.anuncianteReview ? 'Anunciante avaliou.' : 'Anunciante ainda não avaliou.'}
                      </p>
                      <p className="text-gray-600">
                        {transaction.interessadoReview ? 'Interessado avaliou.' : 'Interessado ainda não avaliou.'}
                      </p>

                      {/* Alteração para exibir o botão para todas as transações */}
                      {currentUserId === Number(idUser) && (
                        <Button
                        variant="outlined"
                        onClick={() => handleOpenDialog(transaction)}
                        className="mt-4"
                      >
                        Avaliar
                      </Button>
                      
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-center text-gray-500">Nenhuma transação realizada.</p>
              )}
            </div>
          )}


          <Dialog open={openDialog} onClose={handleCloseDialog}>
            <DialogTitle>Avaliação</DialogTitle>
            <DialogContent>
              <DialogContentText>Deixe sua avaliação para esta transação.</DialogContentText>
              <TextField
                autoFocus
                required
                margin="dense"
                id="comment"
                label="Comentário"
                type="text"
                fullWidth
                variant="standard"
                value={comment}
                onChange={(e) => setComment(e.target.value)} // Atualiza o comentário
              />
              <div className="mt-4">
                <span>Nota: </span>
                <select
                  value={rating ?? ''}
                  onChange={(e) => setRating(Number(e.target.value))} // Atualiza a nota
                  className="p-2 border rounded-md"
                >
                  <option value="" disabled>Escolha uma nota</option>
                  {[1, 2, 3, 4, 5].map((i) => (
                    <option key={i} value={i}>{i}</option>
                  ))}
                </select>
              </div>
            </DialogContent>
            <DialogActions>
              <Button onClick={handleCloseDialog}>Cancelar</Button>
              <Button onClick={handleSubmitReview}>Avaliar</Button> {/* Envia a avaliação */}
            </DialogActions>
          </Dialog>
        </div>
      </main>
    </div>
  );
};

export default Profile;
