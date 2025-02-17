import { useEffect } from 'react';
import { useNavigate, Outlet } from 'react-router-dom';
import Navbar from '../../components/Navbar';

const Home = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    }
  }, [navigate]);

  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />
      {/* Adiciona padding-top equivalente à altura da navbar */}
      <main className="flex-1 flex flex-col gap-4 p-4 lg:gap-6 lg:p-6" style={{ paddingTop: '64px' }}>
        <div
          className="flex flex-1 items-center justify-center rounded-lg border border-dashed shadow-sm"
          x-chunk="dashboard-02-chunk-1"
        >
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export default Home;
