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
    <nav className="bg-gray-800 text-white fixed top-0 left-0 h-full w-64 p-6 flex flex-col space-y-6">
      <h1 className="text-2xl font-bold mb-6">Balcão UFF</h1>
      
      {user && (
        <Link to={`/perfil/${user.id}`} className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
          Profile: {user.name}
        </Link>
      )}
      
      <Link to="/" className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
        Home
      </Link>
      <Link to="/advertises" className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
        List Advertise
      </Link>
      <Link to="/users" className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
        Advertisers
      </Link>
      
      <button
        onClick={handleLogout}
        className="mt-auto bg-red-600 hover:bg-red-700 px-4 py-3 rounded transition-colors text-white"
      >
        Logout
      </button>
    </nav>
  );
};

export default Navbar;
