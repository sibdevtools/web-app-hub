import React from 'react';
import './sidebar.css';
import { Home01Icon, Settings01Icon } from 'hugeicons-react';
import { Nav, NavItem, NavLink } from 'react-bootstrap';
import { useLocation } from 'react-router-dom';

interface SidebarNavItemProps {
  href: string;
  icon: React.ElementType;
}

const SidebarNavItem: React.FC<SidebarNavItemProps> = ({ href, icon: Icon }) => {
  const location = useLocation();
  const isActive = location.pathname === href;

  return (
    <NavItem>
      <NavLink
        href={href}
        className={`text-center ${isActive ? 'active' : ''}`}
      >
        <Icon color="white" />
      </NavLink>
    </NavItem>
  );
};

const Sidebar = () => {
  return (
    <Nav
      variant="pills"
      className="sidebar d-flex flex-column flex-shrink-0 p-3 text-bg-dark"
    >
      <SidebarNavItem href="/" icon={Home01Icon} />
      <hr />
      <SidebarNavItem href="/apps/web.app.settings" icon={Settings01Icon} />
    </Nav>
  );
};

export default Sidebar;
