import type { UIEventHandler } from 'react';
import { useEffect, useMemo, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchPods } from '../api/k8s';
import { useNamespaceStore } from '../store/namespaceStore';
import type { K8sResourceSummary } from '../types/k8s';

const PAGE_SIZE = 20;

interface ContainerDrawerProps {
  open: boolean;
  onClose: () => void;
}

export const ContainerDrawer = ({ open, onClose }: ContainerDrawerProps) => {
  const { namespace } = useNamespaceStore();
  const [visibleCount, setVisibleCount] = useState(PAGE_SIZE);

  const { data, isLoading, error } = useQuery({
    queryKey: ['pods-drawer', namespace],
    queryFn: () => fetchPods(namespace),
    enabled: open
  });

  useEffect(() => {
    if (open) {
      setVisibleCount(PAGE_SIZE);
    }
  }, [open]);

  const items = data ?? [];
  const visibleItems = useMemo(
    () => items.slice(0, Math.min(visibleCount, items.length)),
    [items, visibleCount]
  );

  const handleScroll: UIEventHandler<HTMLDivElement> = (event) => {
    const target = event.currentTarget;
    if (target.scrollTop + target.clientHeight >= target.scrollHeight - 24) {
      setVisibleCount((prev) => (prev >= items.length ? prev : Math.min(prev + PAGE_SIZE, items.length)));
    }
  };

  if (!open) {
    return null;
  }

  return (
    <div className="drawer-backdrop" onClick={onClose}>
      <aside className="drawer" onClick={(event) => event.stopPropagation()}>
        <div className="drawer-header">
          <h3 className="drawer-title">容器列表</h3>
          <button className="button secondary" onClick={onClose}>
            关闭
          </button>
        </div>
        <div className="drawer-body" onScroll={handleScroll}>
          {isLoading && <p>加载中...</p>}
          {error && <p className="badge-red">加载失败：{(error as Error).message}</p>}
          {!isLoading && !error && visibleItems.length === 0 && <p>当前命名空间暂无容器。</p>}
          {!isLoading &&
            !error &&
            visibleItems.map((item: K8sResourceSummary) => (
              <div key={`${item.namespace}-${item.name}`} className="container-item">
                <h4>{item.name}</h4>
                <p>命名空间：{item.namespace}</p>
                <p>类型：{item.type}</p>
                <p>状态：{item.status}</p>
              </div>
            ))}
        </div>
        {items.length > 0 && (
          <div className="drawer-footer">
            已加载 {visibleItems.length} / {items.length} 个容器
          </div>
        )}
      </aside>
    </div>
  );
};
