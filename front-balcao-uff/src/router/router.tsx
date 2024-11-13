// src/router/router.js
import { createBrowserRouter } from 'react-router-dom';
import Home from '../presentation/Home/Home';
import Login from '../presentation/Login/Login';
import ListAdvertise from '../presentation/AdvertiseScene/ListAdvertise';
import MainLayout from '../components/MainLayout';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      {
        path: "/",
        element: <Home />,
      },
      {
        path: "/advertises",
        element: <ListAdvertise />,
      },
    ],
  },
  {
    path: "/login",
    element: <Login />,
  },
]);
