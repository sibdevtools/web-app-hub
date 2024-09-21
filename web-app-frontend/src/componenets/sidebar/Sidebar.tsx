import React from 'react';
import './sidebar.css'
import { Home01Icon, More03Icon } from 'hugeicons-react';

const Sidebar = () => {
  return (
    <div
      className="sidebar d-flex flex-column flex-shrink-0 p-3 text-bg-dark me-2">
      <a href="/"
         className="align-items-center text-center text-decoration-none">
        <Home01Icon color={'white'} />
      </a>
      <hr />
      {/*<ul className="nav nav-pills flex-column mb-auto">*/}
      {/*  <li className="nav-item">*/}
      {/*    <a href="#" className="nav-link active text-center" aria-current="page">*/}
      {/*      <More03Icon />*/}
      {/*    </a>*/}
      {/*  </li>*/}
      {/*  <li>*/}
      {/*    <a href="#" className="nav-link text-white text-center">*/}
      {/*      <More03Icon />*/}
      {/*    </a>*/}
      {/*  </li>*/}
      {/*</ul>*/}
    </div>
  );
};

export default Sidebar;
