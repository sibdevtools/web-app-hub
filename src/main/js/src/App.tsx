import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'hugeicons-react'
import Sidebar from './componenets/Sidebar';

const App = () => {
  return (
    <main className="d-flex flex-nowrap">
      <Sidebar />
      <iframe style={{ width: '100vw', height: '100vh', border: 0 }} src={'/web/app/base64/'} />
    </main>
  );
};

export default App;
