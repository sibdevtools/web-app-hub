import React from 'react';
import './sidebar.css'
import { Home01Icon, Settings01Icon } from 'hugeicons-react';
import { Nav, NavItem, NavLink } from 'react-bootstrap';

const Sidebar = () => {
  return (
    <div
      className="sidebar d-flex flex-column flex-shrink-0 p-3 text-bg-dark me-2">
      <a href="/"
         className="align-items-center text-center text-decoration-none">
        <Home01Icon color={'white'} />
      </a>
      <hr />
      <Nav variant={'pills'} className="flex-column mb-auto">
        <NavItem>
          <NavLink href="/apps/web.app.settings" className="text-center" aria-current="page">
            <Settings01Icon color={'white'} />
          </NavLink>
        </NavItem>
      </Nav>
    </div>
  );
};

export default Sidebar;
