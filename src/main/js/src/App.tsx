import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'hugeicons-react'
import Sidebar from './componenets/Sidebar';
import { ApplicationProvider } from './contexts/ApplicationContext';
import { AppDisplay } from './AppDisplay';

const App = () => {
  return (
    <ApplicationProvider>
      <main className="d-flex flex-nowrap">
        <Sidebar />
        <AppDisplay />
      </main>
    </ApplicationProvider>
  );
};

export default App;
