import { Rating } from '@mui/material';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface User {
    id: number;
    name: string;
    email: string;
    role: string;
}

interface Reputation {
    reputation: number;
}

const UserList = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [userReputations, setUserReputations] = useState<Map<number, number>>(new Map());
    const [error, setError] = useState('');
    const navigate = useNavigate(); 

    useEffect(() => {
        const fetchUsers = async () => {
            const token = localStorage.getItem('token'); 

            if (!token) {
                setError('Token não encontrado. Faça login para continuar.');
                return;
            }

            try {
                const response = await fetch('http://localhost:8080/users', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,  
                    },
                });

                if (!response.ok) {
                    throw new Error('Erro ao buscar usuários');
                }

                const data: User[] = await response.json();
                setUsers(data);

                
                data.forEach(async (user) => {
                    const reputationResponse = await fetch(`http://localhost:8080/reviews/user/${user.id}/reputation`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`,  
                        },
                    });

                    if (reputationResponse.ok) {
                        const reputationData: Reputation = await reputationResponse.json();
                        setUserReputations((prev) => new Map(prev.set(user.id, reputationData.reputation)));
                    } else {
                        console.error('Erro ao buscar reputação do usuário', user.id);
                    }
                });
            } catch (err) {
                setError('Erro ao carregar os usuários. Tente novamente mais tarde.');
                console.error(err);
            }
        };

        fetchUsers();
    }, []);

    const renderStars = (reputation: number) => {
        return (
            <Rating name="half-rating-read" value={reputation} precision={0.5} readOnly />
        );
    };


    return (
        <div className="container mx-auto p-4">
            <h2 className="text-2xl font-semibold mb-4">Usuários</h2>
            {error && <div className="text-red-500">{error}</div>}
            <div className="overflow-x-auto">
                <table className="min-w-full bg-white">
                    <thead>
                        <tr>
                            <th className="py-2 px-4 border-b-2 border-gray-200 bg-gray-100 text-center text-sm font-semibold text-gray-600 uppercase tracking-wider">Nome</th>
                            <th className="py-2 px-4 border-b-2 border-gray-200 bg-gray-100 text-center text-sm font-semibold text-gray-600 uppercase tracking-wider">Email</th>
                            <th className="py-2 px-4 border-b-2 border-gray-200 bg-gray-100 text-center text-sm font-semibold text-gray-600 uppercase tracking-wider">Função</th>
                            <th className="py-2 px-4 border-b-2 border-gray-200 bg-gray-100 text-center text-sm font-semibold text-gray-600 uppercase tracking-wider">Reputação</th>
                            <th className="py-2 px-4 border-b-2 border-gray-200 bg-gray-100 text-center text-sm font-semibold text-gray-600 uppercase tracking-wider">Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((user) => (
                            <tr key={user.id}>
                                <td className="py-2 px-4 border-b border-gray-200 text-lg text-left">{user.name}</td>
                                <td className="py-2 px-4 border-b border-gray-200 text-lg text-left">{user.email}</td>
                                <td className="py-2 px-4 border-b border-gray-200 text-lg text-left">{user.role}</td>
                                <td className="py-2 px-4 border-b border-gray-200 text-lg text-left">
                                    {renderStars(userReputations.get(user.id) || 0)}
                                </td>
                                <td className="py-2 px-4 border-b border-gray-200 text-lg text-left">
                                    <button 
                                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                                        onClick={() => navigate(`/perfil/${user.id}`)}
                                    >
                                        Ver Perfil
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserList;
