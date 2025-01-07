import { Link, useNavigate } from 'react-router-dom';

const Navbar = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="bg-gray-800 text-white fixed top-0 left-0 h-full w-64 p-6 flex flex-col space-y-6">
      <h1 className="text-2xl font-bold mb-6">Balcão UFF</h1>
      <Link to="/perfil" className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
        Perfil
      </Link>
      <Link to="/" className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
        Home
      </Link>
      <Link to="/advertises" className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
        List Advertise
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
