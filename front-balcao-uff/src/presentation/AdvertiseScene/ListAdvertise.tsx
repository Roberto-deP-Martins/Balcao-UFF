/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from "react";
import AdvertiseCard from "../../components/AdvertiseCard";

const ListAdvertise = () => {
  const [activeTab, setActiveTab] = useState<'list' | 'create'>('list');
  const [name, setName] = useState<string>('');
  const [duration, setDuration] = useState<string>('');
  const [time, setTime] = useState<string>('');
  const [advertises, setAdvertises] = useState<any[]>([]);

  useEffect(() => {
    // Fetch para buscar anúncios da API
    const fetchAdvertises = async () => {
      try {
        const response = await fetch('http://localhost:8080/anuncios', {
          method: 'GET',
        });
        const data = await response.json();
        setAdvertises(data);
      } catch (error) {
        console.error('Erro ao buscar anúncios:', error);
      }
    };

    if (activeTab === 'list') {
      fetchAdvertises();
    }
  }, [activeTab]);

  const handleTabChange = (tab: 'list' | 'create') => {
    setActiveTab(tab);
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log('Name:', name);
    console.log('Duration:', duration);
    console.log('Time:', time);
  };

  return (
    <div className="w-screen h-full">
      <div className="flex">
        <div
          className={`flex-1 text-center p-4 cursor-pointer ${
            activeTab === 'list' ? 'bg-blue-600 text-white' : 'bg-gray-200'
          }`}
          onClick={() => handleTabChange('list')}
        >
          Listagem
        </div>
        <div
          className={`flex-1 text-center p-4 cursor-pointer ${
            activeTab === 'create' ? 'bg-blue-600 text-white' : 'bg-gray-200'
          }`}
          onClick={() => handleTabChange('create')}
        >
          Criação
        </div>
      </div>

      {activeTab === 'list' && (
        <div className="p-4">
          {advertises.length > 0 ? (
            advertises.map((ad: any) => <AdvertiseCard key={ad.id} ad={ad} />)
          ) : (
            <p className="text-gray-700">Carregando ou nenhum anúncio disponível...</p>
          )}
        </div>
      )}

      {activeTab === 'create' && (
        <form onSubmit={handleSubmit} className="space-y-4 p-4">
          <input
            type="text"
            placeholder="Nome"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="text"
            placeholder="Duração"
            value={duration}
            onChange={(e) => setDuration(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          />
          <input
            type="text"
            placeholder="Horário"
            value={time}
            onChange={(e) => setTime(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded"
          />
          <button type="submit" className="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700">
            Submit
          </button>
        </form>
      )}
    </div>
  );
};

export default ListAdvertise;