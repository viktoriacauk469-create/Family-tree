import { Layout, Button } from 'antd';
import ButtonGroup from 'antd/es/button/button-group';
import { useNavigate } from 'react-router-dom';
import logo from '/logo.jpg';


const headerStyle = {
  textAlign: 'center',
  height: 80,
  width: '100vw',
  display: 'flex',
  alignItems: 'center',
  background: '#faf4ea',
};
const buttonStyle = {
  background: '#819b57',
  marginLeft: '20px',
}
const imageStyle = {
  paddingLeft: '1px',
  width: '60px',
  height: '60px',
}
const profileStyle = {
  background: '#819b57',
  marginRight: '10px',
  marginLeft: 'auto',
}


export default function AppHeader(){
    const navigate = useNavigate();
    return (
        <Layout.Header style={headerStyle}>
        <img src={logo} alt='Logo' style={imageStyle}></img>
        <Button style={buttonStyle}>Передбачення</Button>
        <Button style={buttonStyle}>Редагувати дерево</Button>
        <Button style={profileStyle} onClick={() => navigate('/profile')}>Профіль</Button>
        
        
        
        </Layout.Header>
        
    )

}