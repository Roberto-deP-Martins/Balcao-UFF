/* eslint-disable @typescript-eslint/no-explicit-any */
// src/layouts/MainLayout.js
import Navbar from '../components/Navbar';

const MainLayout = ({ children }: any) => {
  return (
    <>
      <Navbar />
      <div className="pt-16"> {/* Adjust `pt-16` as needed based on navbar height */}
        <main>{children}</main>
      </div>
    </>
  );
};

export default MainLayout;
