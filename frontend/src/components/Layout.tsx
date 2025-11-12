import { PropsWithChildren, useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { ContainerDrawer } from './ContainerDrawer';
import './layout.css';

export const Layout = ({ children }: PropsWithChildren) => {
  const [drawerOpen, setDrawerOpen] = useState(false);

  return (
    <div className="app-container">
      <header className="app-header">
        <Link to="/" className="logo">
          Kubernetes 配置管理系统
        </Link>
        <div className="header-right">
          <nav className="nav">
            <NavLink to="/" end className={({ isActive }) => (isActive ? 'active' : '')}>
              资源概览
            </NavLink>
            <NavLink to="/configs" className={({ isActive }) => (isActive ? 'active' : '')}>
              自定义配置
            </NavLink>
          </nav>
          <button className="button outline" onClick={() => setDrawerOpen(true)}>
            查看容器
          </button>
        </div>
      </header>
      <main className="app-main">{children}</main>
      <ContainerDrawer open={drawerOpen} onClose={() => setDrawerOpen(false)} />
    </div>
  );
};
