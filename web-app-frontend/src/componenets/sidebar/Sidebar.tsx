import React from 'react';
import './sidebar.css'
import { Home01Icon, More03Icon, Settings01Icon } from 'hugeicons-react';

const Sidebar = () => {
  return (
    <div
      className="sidebar d-flex flex-column flex-shrink-0 p-3 text-bg-dark me-2">
      <a href="/"
         className="align-items-center text-center text-decoration-none">
        <Home01Icon color={'white'} />
      </a>
      <hr />
      <ul className="nav nav-pills flex-column mb-auto">
        <li className="nav-item">
          <a href="/apps/web.app.settings" className="nav-link text-center" aria-current="page">
            <Settings01Icon color={'white'} />
          </a>
        </li>
      </ul>
    </div>
  );
};

export default Sidebar;
