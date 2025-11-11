import { PropsWithChildren } from 'react';
import { Link, NavLink } from 'react-router-dom';
import './layout.css';

export const Layout = ({ children }: PropsWithChildren) => {
  return (
    <div className="app-container">
      <header className="app-header">
        <Link to="/" className="logo">
          Kubernetes 配置管理系统
        </Link>
        <nav className="nav">
          <NavLink to="/" end className={({ isActive }) => (isActive ? 'active' : '')}>
            资源概览
          </NavLink>
          <NavLink to="/configs" className={({ isActive }) => (isActive ? 'active' : '')}>
            自定义配置
          </NavLink>
        </nav>
      </header>
      <main className="app-main">{children}</main>
    </div>
  );
};
