import React, { useState, useCallback, useRef } from 'react';
import { GoogleMap, Marker, Autocomplete } from '@react-google-maps/api';
import GoogleMapsLoader from './GoogleMapsLoader';

const containerStyle = {
  width: '100%',
  height: '400px',
};

const defaultCenter = {
  lat: -22.906459400119765,
  lng: -43.13326835632324,
};

interface MapsProps {
  onLocationSelect: (lat: number, lng: number, address: string) => void;
}

const Maps: React.FC<MapsProps> = ({ onLocationSelect }) => {
  const [markerPosition, setMarkerPosition] = useState(defaultCenter);
  const autocompleteRef = useRef<google.maps.places.Autocomplete | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const updateAddress = async (lat: number, lng: number) => {
    const geocoder = new google.maps.Geocoder();
    const response = await geocoder.geocode({ location: { lat, lng } });
    const address = response.results[0]?.formatted_address || 'Endereço não encontrado';
    onLocationSelect(lat, lng, address);
    if (inputRef.current) {
      inputRef.current.value = address;
    }
  };

  const handleMapClick = useCallback((event: google.maps.MapMouseEvent) => {
    if (event.latLng) {
      const lat = event.latLng.lat();
      const lng = event.latLng.lng();
      setMarkerPosition({ lat, lng });
      updateAddress(lat, lng);
    }
  }, [onLocationSelect]);

  const handlePlaceSelect = () => {
    const place = autocompleteRef.current?.getPlace();
    if (place?.geometry?.location) {
      const lat = place.geometry.location.lat();
      const lng = place.geometry.location.lng();
      setMarkerPosition({ lat, lng });
      onLocationSelect(lat, lng, place.formatted_address || "Endereço não encontrado");
      if (inputRef.current) {
        inputRef.current.value = place.formatted_address || "Endereço não encontrado";
      }
    }
  };

  const handleMarkerDragEnd = (event: google.maps.MapMouseEvent) => {
    if (event.latLng) {
      const lat = event.latLng.lat();
      const lng = event.latLng.lng();
      setMarkerPosition({ lat, lng });
      updateAddress(lat, lng);
    }
  };

  return (
    <GoogleMapsLoader>
      <Autocomplete
        onLoad={(autocomplete) => (autocompleteRef.current = autocomplete)}
        onPlaceChanged={handlePlaceSelect}
      >
        <input
          ref={inputRef}
          type="text"
          placeholder="Digite o endereço..."
          className="w-full p-2 border border-gray-300 rounded mb-4"
        />
      </Autocomplete>
      <GoogleMap
        mapContainerStyle={containerStyle}
        center={markerPosition}
        zoom={18}
        onClick={handleMapClick}
      >
        <Marker 
          position={defaultCenter} 
          draggable
          onDragEnd={handleMarkerDragEnd} 
        />
      </GoogleMap>
    </GoogleMapsLoader>
  );
};

export default Maps;
