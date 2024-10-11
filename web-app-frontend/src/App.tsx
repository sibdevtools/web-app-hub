import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'hugeicons-react'

import { ApplicationProvider } from './contexts/ApplicationContext';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import ApplicationLayout from './components/ApplicationLayout';
import { ApplicationsPage } from './pages/ApplicationsPage';
import { ApplicationPage } from './pages/ApplicationPage';

const App = () => {
  return (
    <ApplicationProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<ApplicationLayout />}>
            <Route index element={<ApplicationsPage />} />
            <Route path="apps/*" element={<ApplicationPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </ApplicationProvider>
  );
};

export default App;
