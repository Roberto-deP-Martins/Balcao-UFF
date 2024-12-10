import { useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css";

const AdvertiseView = () => {
  // Mock para dados de exemplo
  const location = useLocation();
  const ad = location.state?.ad;
  const { userId, id } = ad || {}; // Ajustado para evitar erros se `ad` for indefinido

  const [isNormalUser] = useState(true); // Alterar para `true` ou `false` para testar os dois casos
  const [isWriting, setIsWriting] = useState(false); 
  const [comment, setComment] = useState('');
  const [selectedConversation, setSelectedConversation] = useState(null); 
  const [listConversation, setListConversation] = useState([])

  const getConversas = async () => {
    try {
      const token = localStorage.getItem("token");
      console.log("token", token)
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
      console.log(data)
      return data
    } catch (error) {
      console.error("Erro ao buscar AGASDADHASD:", error);
    }
  }

  const initConversa = async (mensagem: unknown) => {
    try {
      const token = localStorage.getItem("token");
      console.log(mensagem)
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
      console.log(data)
      return data
    } catch (error) {
      console.error("Erro ao buscar anúncios:", error);
    }
  }

  const continuaConversa = async (mensagem: unknown) => {
    try {
      const token = localStorage.getItem("token");
      console.log(selectedConversation.id)
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
  

  const getChats = async () => {
    const result = await getConversas() 
    setListConversation(result)
    if(result.length > 0 && isNormalUser){
      setSelectedConversation(result[0])
    }
  }

  useEffect(() => {
    async function init() {
     await getChats()
    }
    init()
  },[])

  // Função para enviar comentário
  const handleCommentSubmit = async () => {
    console.log("Comentário enviado:", comment);
    const result = await continuaConversa(comment)
    if(result?.conversaId){
      await getChats()
    }
    setComment('');
    setIsWriting(false);
  };
  const handleCommentSubmitInit = async () => {
    console.log("Comentário enviado:", comment);
    const result = await initConversa(comment)
    if(result?.conversaId){
      await getChats()
    }
    setComment('');
    setIsWriting(false);
  };

  // Renderizar lista de conversas (anunciante)
  const renderConversationList = () => (
    listConversation.length == 0 ?  <p className="text-gray-500">Ninguém comentou no seu anúncio</p>  : 
    <div className="space-y-4">
      {listConversation.map((conversa) => (
        <div key={conversa.id} className="bg-gray-100 p-3 rounded-lg shadow-md">
          <h3 className="text-gray-700 font-semibold mb-2">Conversa {conversa.id}</h3>
          <div className="space-y-2">
            {conversa.mensagens.slice(-2).map((mensagem) => (
              <div
                key={mensagem.id}
                className={`flex ${mensagem.senderId === userId ? 'justify-end' : 'justify-start'}`}
              >
                <div
                  className={`${
                    mensagem.senderId === userId ? 'bg-gray-300 text-gray-900' : 'bg-orange-500 text-white'
                  } max-w-xs p-2 rounded-lg shadow-md text-sm`}
                >
                  <p>{mensagem.conteudo}</p>
                  <p className="text-xs text-gray-500 mt-1">{new Date(mensagem.dataEnvio).toLocaleString('pt-BR')}</p>
                </div>
              </div>
            ))}
          </div>
          <button
            className="mt-3 bg-orange-500 text-white py-1 px-3 rounded-lg hover:bg-orange-600 transition"
            onClick={() => setSelectedConversation(conversa)}
          >
            Visualizar Conversa
          </button>
        </div>
      ))}
    </div>
  );

  const placeText1 = isNormalUser ? 'justify-start' : 'justify-end';
  const placeText2 = isNormalUser ? 'justify-end' : 'justify-start';

  const renderFullConversation = () => (
    <div>
      {!isNormalUser &&
        <button
          className="mb-4 bg-gray-300 text-gray-700 py-1 px-3 rounded-lg hover:bg-gray-400 transition"
          onClick={() => setSelectedConversation(null)}
        >
          Voltar
        </button>
      }
      <div className="space-y-4">
        {selectedConversation.mensagens.map((mensagem) => (
          <div
            key={mensagem.id}
            
            className={`flex ${mensagem.senderId === userId ? placeText1 : placeText2}`}
          >
            <div
              className={`${
                mensagem.senderId === userId ? 'bg-gray-300 text-gray-900' : 'bg-orange-500 text-white'
              } max-w-xs p-2 rounded-lg shadow-md text-sm`}
            >
              <p>{mensagem.conteudo}</p>
              <p className="text-xs text-gray-500 mt-1">{new Date(mensagem.dataEnvio).toLocaleString('pt-BR')}</p>
            </div>
          </div>
        ))}
      </div>
      <div className="flex gap-2 mt-4">
        <input
          type="text"
          placeholder="Digite sua mensagem"
          className="flex-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter') handleCommentSubmit();
          }}
        />
        <button
          className="bg-orange-500 text-white py-2 px-4 rounded-lg hover:bg-orange-600 transition"
          onClick={handleCommentSubmit}
        >
          Enviar
        </button>
      </div>
    </div>
  );

  if (!ad) {
    return (
      <div className="flex items-center justify-center h-screen">
        <p className="text-gray-500 text-xl">Detalhes do anúncio não encontrados.</p>
      </div>
    );
  }

  const a = (
    !selectedConversation ?
    (isWriting ? (
      <div className="flex gap-2">
        <input
          type="text"
          placeholder="Digite seu comentário"
          className="flex-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
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
          className="bg-orange-500 text-white py-2 px-4 rounded-lg hover:bg-orange-600 transition"
          onClick={handleCommentSubmitInit}
        >
          Enviar
        </button>
      </div>
    ) : (
      <button
        className="bg-orange-500 text-white py-2 px-4 rounded-lg hover:bg-orange-600 transition"
        onClick={() => setIsWriting(true)}
      >
        Iniciar Conversa
      </button>
    )) : 
    renderFullConversation()
  )

  const b = (
    selectedConversation ? (
      renderFullConversation()
    ) : (
      renderConversationList()
    )
  )

  return (
    <div className="max-w-7xl mx-auto px-6 py-10">
      {/* Renderizar o anúncio */}
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
  
      {/* Renderizar barra de comentários */}
      <div className="mt-10 border-t pt-4">
        {isNormalUser ? a : b}
      </div>
    </div>
  );
  
};

export default AdvertiseView;
