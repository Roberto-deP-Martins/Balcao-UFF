/* eslint-disable @typescript-eslint/no-explicit-any */
// src/layouts/MainLayout.js
import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar';

const MainLayout = () => {
  return (
    <>
      <div className="pt-16"> {/* Adjust `pt-16` as needed based on navbar height */}
      <Navbar />
        <main><Outlet /></main>
      </div>
    </>
  );
};

export default MainLayout;
