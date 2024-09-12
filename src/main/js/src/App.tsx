import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'hugeicons-react'

import { ApplicationProvider } from './contexts/ApplicationContext';
import { ApplicationDisplay } from './componenets/ApplicationDisplay';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import ApplicationLayout from './componenets/ApplicationLayout';
import ApplicationCards from './componenets/ApplicationCards';

const App = () => {
  return (
    <ApplicationProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<ApplicationLayout />}>
            <Route index element={<ApplicationCards />} />
            <Route path="apps/*" element={<ApplicationDisplay />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </ApplicationProvider>
  );
};

export default App;
