// src/router/router.js
import { createBrowserRouter } from 'react-router-dom';
import Home from '../presentation/Home/Home';
import Login from '../presentation/Login/Login';
import ListAdvertise from '../presentation/AdvertiseScene/ListAdvertise';
import MainLayout from '../components/MainLayout';

export const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />, // Login route (doesn't use MainLayout)
  },
  {
    element: <MainLayout />, // MainLayout wraps all these routes
    children: [
      {
        path: "/",
        element: <Home />, // Home route inside MainLayout
      },
      {
        path: "/advertises",
        element: <ListAdvertise />, // ListAdvertise route inside MainLayout
      },
    ],
  },
]);