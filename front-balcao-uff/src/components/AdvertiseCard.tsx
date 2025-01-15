import { useNavigate } from 'react-router-dom';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css";
import Divider from '@mui/material/Divider';
import {Ads} from '../interfaces/interfaces';

const AdvertiseCard = ({ ad }: { ad: Ads }) => {
  const navigate = useNavigate();
  const handleVerMais = () => {
    navigate("/advertiseView", { state: { ad } });
  };

  return (
    <Card sx={{ 
      maxWidth: 345, 
      margin: 'auto', 
      boxShadow: 3, 
      borderRadius: 2, 
      display: 'flex', 
      flexDirection: 'column', 
      height: '100%' // Faz os cards terem a mesma altura
    }}>
      {ad.imagePaths && ad.imagePaths.length > 0 ? (
        <Carousel showThumbs={false} infiniteLoop useKeyboardArrows showStatus={false} autoPlay>
          {ad.imagePaths.map((path, index) => {
            const fileName = path.substring(path.lastIndexOf('/') + 1);
            return (
              <div key={index}>
                <img
                  src={`http://localhost:8080/anuncioImages/image/${fileName}`}
                  alt={`Imagem ${index + 1}`}
                  className="w-full h-56 object-cover"
                />
              </div>
            );
          })}
        </Carousel>
      ) : (
        <div className="w-full h-56 bg-gray-200 flex items-center justify-center">
          <span className="text-gray-500">Sem imagem disponível</span>
        </div>
      )}

      <CardContent sx={{ flex: 1 }}>
        <Typography gutterBottom variant="h5" component="div" sx={{ textAlign: 'left' }}>
          {ad.title}
        </Typography>
        
        <Typography variant="body2" sx={{ color: 'text.secondary', textAlign: 'left', mb: 1 }}>
          {ad.description}
        </Typography>

        <Divider sx={{ marginY: 1 }} /> {/* Divisória */}
        
        <Typography variant="h6" component="p" color="primary" sx={{ textAlign: 'left' }}>
          R$ {ad.price.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
        </Typography>
        
        <Divider sx={{ marginY: 1 }} /> {/* Divisória */}
        
        <Typography variant="body2" sx={{ textAlign: 'left', fontWeight: 'bold', color: 'secondary' }}>
          {ad.category}
        </Typography>

        <Divider sx={{ marginY: 1 }} /> {/* Divisória */}
        
        <Typography variant="body2" sx={{ textAlign: 'left' }}>
          Contato: {ad.contactInfo}
        </Typography>
        
        <Typography variant="body2" sx={{ textAlign: 'left' }}>
          Localização: {ad.location}
        </Typography>
      </CardContent>

      <CardActions>
        <Button
          size="small"
          variant="contained"
          color="primary"
          onClick={handleVerMais}
          sx={{ alignSelf: 'flex-end' }} // Botão alinhado à direita
        >
          Ver Mais
        </Button>
      </CardActions>
    </Card>
  );
};

export default AdvertiseCard;
