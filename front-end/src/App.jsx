import { Layout } from 'antd';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Profile from './components/Profile';
import Home from './components/Home';


export default function App () {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home /> } />
        <Route path="/profile" element={<Profile />} />
      </Routes>
    </BrowserRouter>

      
  );
}


