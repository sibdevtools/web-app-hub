import { Outlet } from 'react-router-dom';
import Sidebar from './sidebar/Sidebar';
import React from 'react';

const ApplicationLayout = () => {
  return (
    <main className="d-flex flex-nowrap">
      <Sidebar />

      <Outlet />
    </main>
  )
};

export default ApplicationLayout;
