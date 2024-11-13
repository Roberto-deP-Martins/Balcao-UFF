import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="bg-gray-800 text-white fixed top-0 left-0 h-full w-64 p-4 flex flex-col space-y-4">
      <Link to="/" className="hover:bg-gray-700 px-4 py-2 rounded">Home</Link>
      <Link to="/advertises" className="hover:bg-gray-700 px-4 py-2 rounded">List Advertise</Link>
    </nav>
  );
};

export default Navbar;