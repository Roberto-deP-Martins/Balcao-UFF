import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const Navbar = () => {
  interface User {
    id: string;
    name: string;
  }

  const [user, setUser] = useState<User | null>(null);
  const navigate = useNavigate();

  const getCurrentUser = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('http://localhost:8080/auth/current-user', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('Erro ao buscar usuário');
      }

      const data = await response.json();
      setUser(data);
    } catch (error) {
      console.error('Erro ao buscar usuário:', error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  useEffect(() => {
    getCurrentUser();
  }, []);

  return (
    <nav className="bg-gray-800 text-white fixed top-0 left-0 w-full p-4 flex items-center justify-between z-50">
      <Link to="/" className="text-2xl font-bold hover:text-gray-300 transition-colors">
        Balcão UFF
      </Link>
      <div className="flex space-x-4">
        <Link to="/advertises" className="hover:bg-gray-700 px-4 py-2 rounded transition-colors">
          Lista de anúncios
        </Link>
        <Link to="/users" className="hover:bg-gray-700 px-4 py-2 rounded transition-colors">
          Anunciantes
        </Link>
        {user && (
          <Link to={`/perfil/${user.id}`} className="hover:bg-gray-700 px-4 py-2 rounded transition-colors">
            Perfil: {user.name}
          </Link>
        )}
        <button
          onClick={handleLogout}
          className="bg-red-600 hover:bg-red-700 px-4 py-2 rounded transition-colors text-white"
        >
          Logout
        </button>
      </div>
    </nav>
  );
};

export default Navbar;
