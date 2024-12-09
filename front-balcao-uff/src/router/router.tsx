import { Navigate, createBrowserRouter } from 'react-router-dom';
import Home from '../presentation/Home/Home';
import ListAdvertise from '../presentation/AdvertiseScene/ListAdvertise';
import Login from '../presentation/Login/Login';
import AdvertiseView from '../presentation/AdvertiseView/AdvertiseView';

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
    ],
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/advertiseView",
    element: <AdvertiseView />
  }
]);
