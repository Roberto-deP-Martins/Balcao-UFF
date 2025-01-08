import React, { ReactNode } from 'react';
import { useJsApiLoader } from '@react-google-maps/api';

const GoogleMapsLoader: React.FC<{ children: ReactNode }> = ({ children }) => {
  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: 'AIzaSyCF4qzXfMtRlNCNwiS2fQK1HZM2mL9vSLA',
    libraries: ['places'],
  });

  if (!isLoaded) {
    return <div>Loading...</div>;
  }

  return <>{children}</>;
};

export default GoogleMapsLoader;
