import { Navigate, createBrowserRouter } from 'react-router-dom';
import Home from '../presentation/Home/Home';
import ListAdvertise from '../presentation/AdvertiseScene/ListAdvertise';
import CreateAdvertise from '../presentation/AdvertiseScene/CreateAdvertise'; // Importe a nova tela
import Login from '../presentation/Login/Login';
import AdvertiseView from '../presentation/AdvertiseView/AdvertiseView';
import Perfil from '../presentation/Perfil/Perfil';

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
    children: [
      {
        index: true,
        element: <Navigate to="advertises" replace />,
      },
      {
        path: "advertises",
        element: <ListAdvertise />,
      },
      {
        path: "create-advertise",
        element: <CreateAdvertise />,
      },
    ],
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/advertiseView",
    element: <AdvertiseView />,
  },
  {
    path: "/perfil",
    element: <Perfil />,
  }
]);
