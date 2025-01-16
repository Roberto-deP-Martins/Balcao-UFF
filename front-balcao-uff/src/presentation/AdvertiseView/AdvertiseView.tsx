import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css";
import { CreateConversa } from '../../interfaces/interfaces';

const AdvertiseView = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const ad = location.state?.ad;
  const { userId, id } = ad || {};

  const [isNormalUser, setIsNormalUser] = useState<boolean | undefined>();
  const [isWriting, setIsWriting] = useState(false);
  const [comment, setComment] = useState('');
  const [selectedConversation, setSelectedConversation] = useState<CreateConversa | null>(null);
  const [indexSelectedConversation, setIndexSelectedConversation] = useState(0);
  const [listConversation, setListConversation] = useState<CreateConversa[]>([])

  const getConversas = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/conversas/por-anuncio/${id}`, {
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
      return data
    } catch (error) {
      console.error("Erro ao buscar AGASDADHASD:", error);
    }
  }

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
      return data
    } catch (error) {
      console.error("Erro ao buscar AGASDADHASD:", error);
    }
  }

  const initConversa = async (mensagem: string) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/conversas/iniciar-conversa`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          anuncioId: id,
          mensagem: mensagem,
        })
      });

      if (!response.ok) {
        throw new Error("Erro na requisição");
      }

      const data = await response.json();
      return data
    } catch (error) {
      console.error("Erro ao buscar anúncios:", error);
    }
  }

  const continuaConversa = async (mensagem: string) => {
    if (!selectedConversation) return;

    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/messages/sendmessage`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          content: mensagem,
          conversaId: selectedConversation.id,
        }),
      });

      if (!response.ok) {
        throw new Error("Erro na requisição");
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error("Erro ao buscar anúncios:", error);
    }
  };

  const [aceitarNegocio, setAceitarNegocio] = useState(false);

  const handleAceitarNegocio = async (interessadoId: number) => {
    if(aceitarNegocio) return;

    const url = 'http://localhost:8080/transactions';
    console.log(interessadoId, id, userId)
    const body = JSON.stringify({
        anuncioId: id,
        anuncianteId: userId,
        interessadoId: interessadoId,
    });

    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: body,
    };

    try {
        const response = await fetch(url, options);

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Detalhes do erro:', errorData); 
            throw new Error(`Erro: ${errorData.message || 'Falha ao criar transação'}`);
        }

        const data = await response.json();
        setAceitarNegocio(true)
        console.log('Transação criada com sucesso:', data);
        return data; 
    } catch (error) {
        console.error('Erro ao criar transação:', error);
        throw error; // Repropaga o erro
    }
}


  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const getChats = async (notInit = false) => {
    const result = await getConversas()
    setListConversation(result)
    if (result.length > 0 && isNormalUser) {
      setSelectedConversation(result[0])
      console.log(result[0])
      setIndexSelectedConversation(0)
    }
    if (!isNormalUser && notInit) {
      setSelectedConversation(result[indexSelectedConversation])
    }
  }

  const funcVerifyUser = async () => {
    const result = await getUserLogged()
    setIsNormalUser(userId !== result.id)
  }

  useEffect(() => {
    async function init() {
      await funcVerifyUser()
    }
    init()
  }, [])

  useEffect(() => {
    async function verify() {
      await getChats()
    }
    verify()
  }, [isNormalUser])

  const handleCommentSubmit = async () => {
    await continuaConversa(comment)
    await getChats(true)
    setComment('');
    setIsWriting(false);
  };

  const [setFecharNegocio, setFecharNegocioLoading] = useState(false);

  const handleFecharNegocio = async () => {
      if (!selectedConversation) return;
      const url = `http://localhost:8080/conversas/fechar-negocio/${selectedConversation.id}`;
    
      const headers = {
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("token"),
      };
    
      try {
        const response = await fetch(url, {
          method: "PUT",
          headers: headers,
        });
    
        if (!response.ok) {
          const errorData = await response.json();
          console.error("Erro ao fechar negócio:", errorData);
          return;
        }
        setFecharNegocioLoading(true)
        const data = await response.json();
        console.log("Negócio fechado com sucesso:", data);
      } catch (error) {
        console.error("Erro na requisição:", error);
      }
  };

  const handleCommentSubmitInit = async () => {
    const result = await initConversa(comment)
    if (result?.conversaId) {
      await getChats()
    }
    setComment('');
    setIsWriting(false);
  };

  const renderConversationList = () => (
    listConversation.length == 0 ? <p className="text-gray-500">Ninguém comentou no seu anúncio</p> :
      <div className="space-y-4">
        {listConversation.map((conversa, index) => (
          <div key={conversa.id} className="bg-gray-100 p-3 rounded-lg shadow-md">
            <h3 className="text-gray-700 font-semibold mb-2">Conversa {conversa.id}</h3>
            <div className="space-y-2">
              {conversa.mensagens.slice(-2).map((mensagem) => (
                <div
                  key={mensagem.id}
                  className={`flex ${mensagem.senderId === userId ? 'justify-end' : 'justify-start'}`}
                >
                  <div
                      className={`${mensagem.senderId === userId ? 'bg-green-600 text-black' : 'bg-blue-500 text-white'
                        } max-w-xs p-2 rounded-lg shadow-md text-sm text-left`}
                    >
                      <p className="text-black text-left">{mensagem.senderName}</p>
                      <p className="text-left text-white">{mensagem.conteudo}</p>
                      <p className="text-xs text-black mt-1 text-left">
                        {new Date(mensagem.dataEnvio).toLocaleString('pt-BR')}
                      </p>
                  </div>

                </div>
              ))}
            </div>

            <div className='width-full flex gap-2 justify-center'>
                <button
                className="mt-3 bg-blue-900 text-white py-1 px-3 rounded-lg hover:bg-blue-600 transition"
                onClick={() => { setSelectedConversation(conversa); setIndexSelectedConversation(index) }}
                >
                Visualizar Conversa
                </button>
                {
                conversa.interessadoFecharNegocio &&
                  <button
                  className="mt-3 bg-black text-white py-1 px-3 rounded-lg hover:bg-gray-700 transition"
                  onClick={async () => { 
                    await handleAceitarNegocio(conversa?.mensagens[0].senderId);
                    navigate('/advertises');
                  }}  
                  >
                  {!aceitarNegocio ? "Aceitar Negócio" : "Negocio Fechado"}
                  </button> 
                }
            </div>
          </div>
        ))}
      </div>
  );

  const placeText1 = isNormalUser ? 'justify-start' : 'justify-end';
  const placeText2 = isNormalUser ? 'justify-end' : 'justify-start';

  const renderFullConversation = () => {
    if (!selectedConversation) return null;

    return (
      <div>
        {!isNormalUser && (
          <button
            className="mb-4 bg-gray-300 text-gray-700 py-1 px-3 rounded-lg hover:bg-gray-400 transition"
            onClick={() => { setSelectedConversation(null); setIndexSelectedConversation(0) }}
          >
            Voltar
          </button>
        )}
        <div className="space-y-4">
          {selectedConversation.mensagens.map((mensagem) => (
            <div
              key={mensagem.id}

              className={`flex ${mensagem.senderId === userId ? placeText1 : placeText2}`}
            >
              <div
                  className={`${mensagem.senderId === userId ? 'bg-green-600 text-black' : 'bg-blue-500 text-white'
                    } max-w-xs p-2 rounded-lg shadow-md text-sm text-left`}
                >
                  <p className="text-black text-left">{mensagem.senderName}</p>
                  <p className="text-left text-white">{mensagem.conteudo}</p>
                  <p className="text-xs text-black mt-1 text-left">
                    {new Date(mensagem.dataEnvio).toLocaleString('pt-BR')}
                  </p>
              </div>
            </div>
          ))}
        </div>
        <div className="flex gap-2 mt-4">
          <input
            type="text"
            placeholder="Digite sua mensagem"
            className="flex-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') handleCommentSubmit();
            }}
          />
          <button
            className="bg-black text-white py-2 px-4 rounded-lg hover:bg-gray-600 transition"
            onClick={handleCommentSubmit}
          >
            Enviar
          </button>
          {isNormalUser && !selectedConversation?.interessadoFecharNegocio && !setFecharNegocio ?
            <button
              className="bg-blue-900 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition"
              onClick={handleFecharNegocio}
            >
              Fechar Negocio
            </button> : (
              <div className="bg-blue-900 text-white py-2 px-4 rounded-lg ">
                Esperando Resposta!
              </div>
            )
          } 
        </div>
      </div>
    );
  };

  const handleGoBack = () => {
    navigate('/advertises');
  }

  const renderCommentSection = () => {
    if (!selectedConversation) {
      if (isWriting) {
        return (
          <div className="flex gap-2">
            <input
              type="text"
              placeholder="Digite seu comentário"
              className="flex-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  handleCommentSubmitInit();
                }
              }}
              autoFocus
            />
            <button
              className="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition"
              onClick={handleCommentSubmitInit}
            >
              Enviar
            </button>
          </div>
        );
      }
      return (
        <button
          className="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition"
          onClick={() => setIsWriting(true)}
        >
          Iniciar Conversa
        </button>
      );
    }
    return renderFullConversation();
  };

  if (!ad) {
    return (
      <div className="flex items-center justify-center h-screen">
        <p className="text-gray-500 text-xl">Detalhes do anúncio não encontrados.</p>
      </div>
    );
  }

  const b = (
    selectedConversation ? (
      renderFullConversation()
    ) : (
      renderConversationList()
    )
  )

  return (
    <div className="max-w-7xl mx-auto px-6 py-10 border border-blue-500 rounded-lg">
  
      <button
        className="mb-4 bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition flex items-center gap-2"
        onClick={handleGoBack}
      >
        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
          <path fillRule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clipRule="evenodd" />
        </svg>
        Voltar
      </button>
  
      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
  
        <div className="rounded-lg shadow-lg overflow-hidden">
          {ad.imagePaths && ad.imagePaths.length > 0 ? (
            <Carousel showThumbs={false} infiniteLoop showStatus={false} className="rounded-lg">
              {ad.imagePaths.map((path: string, index: number) => {
                const fileName = path.substring(path.lastIndexOf('/') + 1);
                return (
                  <div key={fileName}>
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
  
        <div className="text-left">
          <h1 className="text-3xl font-bold text-gray-800 mb-4">{ad.title}</h1>
          <p className="text-2xl text-blue-600 font-bold mb-4">
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
        </div>
      </div>
  
      {/* Renderizar barra de comentarios */}
      <div className="mt-10 border-t pt-4">
        {isNormalUser ? renderCommentSection() : b}
      </div>
    </div>
  );
  
};

export default AdvertiseView;
