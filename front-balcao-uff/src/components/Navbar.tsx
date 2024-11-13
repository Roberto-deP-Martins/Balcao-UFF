import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="bg-gray-800 text-white fixed top-0 left-0 h-full w-64 p-6 flex flex-col space-y-6">
      <h1 className="text-2xl font-bold mb-6">Balcão UFF</h1>
      <Link to="/" className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
        Home
      </Link>
      <Link to="/advertises" className="hover:bg-gray-700 px-4 py-3 rounded transition-colors">
        List Advertise
      </Link>
    </nav>
  );
};

export default Navbar;
