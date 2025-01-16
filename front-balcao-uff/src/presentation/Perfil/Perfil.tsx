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
import { Checkbox, FormControlLabel, Rating, Typography } from '@mui/material';
import Swal from 'sweetalert2';

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
  // const [filterAvailable, setFilterAvailable] = useState(true);
  const [statusFilter, setStatusFilter] = useState<{ available: boolean; unavailable: boolean }>({
    available: false,
    unavailable: false,
  });
  const [filteredAds, setFilteredAds] = useState<Ad[]>([]);

  const getToken = () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate('/login');
      return null;
    }
    return token;
  };

  // Efeito para pegar os anúncios
  useEffect(() => {
    if (!idUser) {
      navigate('/login');
    } else {
      fetchAds(idUser).then((data) => {
        if (data) {
          setAds(data);
          setFilteredAds(data); // Inicialmente, mostramos todos os anúncios
        }
      });
    }
  }, [idUser, navigate]);

  useEffect(() => {
    // Filtra os anúncios com base nos filtros de status
    const filtered = ads.filter((ad) => {
      // Se ambos os filtros estiverem desmarcados, mostramos todos os anúncios
      if (!statusFilter.available && !statusFilter.unavailable) {
        return true;
      }
      if (statusFilter.available && ad.available) {
        return true;
      }
      if (statusFilter.unavailable && !ad.available) {
        return true;
      }
      return false;
    });
    setFilteredAds(filtered);
  }, [statusFilter, ads]);

  // Função para lidar com as mudanças de filtro de status
  const handleStatusFilterChange = (event: React.ChangeEvent<HTMLInputElement>, status: string) => {
    setStatusFilter((prevState) => ({
      ...prevState,
      [status]: event.target.checked,
    }));
  };
  //fim de filtro de status


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
    return (
      <Rating
        name="half-rating-read"
        value={reputation}
        precision={0.5}
        readOnly
      />
    );
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
      Swal.fire("Atenção", "Por favor, forneça uma nota e comentário.", "warning");
      return;
    }

    const token = getToken();
    if (!token) {
      Swal.fire("Erro", "Usuário não autenticado.", "error");
      return;
    }

    const reviewerId = currentUserId;
    const reviewedId =
      selectedTransaction.anuncianteId === currentUserId
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
        Swal.fire("Sucesso!", "Avaliação enviada com sucesso!", "success");
        fetchTransactions(idUser!); // Atualizar transações
        handleCloseDialog(); // Fechar o diálogo
      } else {
        Swal.fire("Erro", "Usuário já avaliado!", "error");
      }
    } catch (error) {
      console.error("Erro ao enviar avaliação:", error);
      Swal.fire("Erro", "Ocorreu um erro ao enviar a avaliação.", "error");
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
              Anúncios
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
          <div className="w-full mt-6">
            {/* Filtro de status */}
            <div className="flex mb-4 gap-4">
              <FormControlLabel
                control={
                  <Checkbox
                    checked={statusFilter.available}
                    onChange={(e) => handleStatusFilterChange(e, 'available')}
                    name="statusAvailable"
                    color="primary"
                  />
                }
                label="Disponível"
              />
              <FormControlLabel
                control={
                  <Checkbox
                    checked={statusFilter.unavailable}
                    onChange={(e) => handleStatusFilterChange(e, 'unavailable')}
                    name="statusUnavailable"
                    color="primary"
                  />
                }
                label="Indisponível/Deletado"
              />
            </div>

            {/* Exibição dos anúncios filtrados */}
            <div className="w-full mt-6 space-y-4">
              {filteredAds.length > 0 ? (
                filteredAds.map((ad, index) => (
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
                          {ad.available ? 'Disponível' : 'Indisponível/Deletado'}
                        </span>
                      </p>
                    </div>
                  </div>
                ))
              ) : (
                <p className="text-center text-gray-500">Nenhum anúncio encontrado.</p>
              )}
            </div>
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
                          <Rating name="half-rating-read" value={review.rating} precision={0.5} readOnly />
                          <span className="text-sm text-gray-500 ml-2">({review.rating})</span>
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
                      <div className="flex justify-between items-center mb-2">
                        <h3 className="text-xl font-semibold text-gray-800 text-left">Anúncio: {transaction.anuncioName}</h3>
                        <Button
                          variant="contained"
                          color="primary"
                          size="small"
                          onClick={() => handleOpenDialog(transaction)}
                        >
                          Avaliar usuário
                        </Button>
                      </div>
                      <p className="text-gray-600 text-left"><span className="font-semibold">Anúncio ID:</span> {transaction.anuncioId}</p>
                      <p className="text-gray-600 text-left"><span className="font-semibold">Data de Conclusão:</span> {new Date(transaction.dtConclusao.split(' ')[0].split('/').reverse().join('-')).toLocaleDateString()}</p>
                      <p className="text-gray-600 text-left">
                        <span className="font-semibold">Anunciante:</span>
                        <span className={transaction.anuncianteReview ? 'text-green-500' : 'text-red-500'}>
                          {transaction.anuncianteReview ? ' Avaliou' : ' Ainda não avaliou'}
                        </span>
                      </p>
                      <p className="text-gray-600 text-left">
                        <span className="font-semibold">Negociante:</span>
                        <span className={transaction.interessadoReview ? 'text-green-500' : 'text-red-500'}>
                          {transaction.interessadoReview ? ' Avaliou' : '   Ainda não avaliou'}
                        </span>
                      </p>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-center text-gray-500">Nenhuma transação realizada.</p>
              )}
            </div>
          )}


          <Dialog open={openDialog} onClose={handleCloseDialog} fullWidth maxWidth="md">
            <DialogTitle>Avaliação</DialogTitle>
            <DialogContent>
              <DialogContentText>Deixe sua avaliação para esse usuário.</DialogContentText>
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
                onChange={(e) => setComment(e.target.value)}
              />
                <div className="mt-4">
                <Typography component="legend">Nota</Typography>
                <Rating
                  name="half-rating"
                  value={rating ?? 0}
                  precision={1}
                  onChange={(event, newValue) => setRating(newValue)}
                />
                </div>
            </DialogContent>
            <DialogActions>
              <Button onClick={handleCloseDialog}>Cancelar</Button>
              <Button
                onClick={async () => {
                  handleCloseDialog();
                  setTimeout(async () => {
                    const result = await Swal.fire({
                      title: "Enviar Avaliação?",
                      text: "Você não poderá editar sua avaliação depois de enviada.",
                      icon: "warning",
                      showCancelButton: true,
                      confirmButtonText: "Sim, enviar!",
                      cancelButtonText: "Cancelar",
                    });

                    if (result.isConfirmed) {
                      await handleSubmitReview();
                    }
                  }, 300);
                }}
                variant="contained"
                color="primary"
              >
                Avaliar
              </Button>
            </DialogActions>
          </Dialog>

        </div>
      </main>
    </div>
  );
};

export default Profile;
