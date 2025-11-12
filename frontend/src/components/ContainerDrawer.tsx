import type { UIEventHandler } from 'react';
import { useMemo } from 'react';
import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchPodsPaged } from '../api/k8s';
import { useNamespaceStore } from '../store/namespaceStore';
import type { K8sResourceSummary } from '../types/k8s';

const PAGE_SIZE = 20;

interface ContainerDrawerProps {
  open: boolean;
  onClose: () => void;
}

export const ContainerDrawer = ({ open, onClose }: ContainerDrawerProps) => {
  const { namespace } = useNamespaceStore();

  const { data, isLoading, error, fetchNextPage, hasNextPage, isFetchingNextPage, isRefetching } =
    useInfiniteQuery({
      queryKey: ['pods-drawer', namespace],
      queryFn: ({ pageParam = 1 }) => fetchPodsPaged(namespace, pageParam, PAGE_SIZE),
      getNextPageParam: (lastPage) => {
        const totalPages = Math.ceil(lastPage.total / lastPage.pageSize);
        return lastPage.page < totalPages ? lastPage.page + 1 : undefined;
      },
      enabled: open,
      refetchOnReconnect: true,
      refetchOnWindowFocus: false,
      refetchOnMount: 'always'
    });

  const items = useMemo(
    () => (data?.pages ? data.pages.flatMap((page) => page.items) : []),
    [data]
  );
  const total = data?.pages?.[0]?.total ?? 0;

  const handleScroll: UIEventHandler<HTMLDivElement> = (event) => {
    if (!hasNextPage || isFetchingNextPage) {
      return;
    }
    const target = event.currentTarget;
    if (target.scrollTop + target.clientHeight >= target.scrollHeight - 24) {
      fetchNextPage();
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
          {(isLoading || isRefetching) && <p>加载中...</p>}
          {error && <p className="badge-red">加载失败：{(error as Error).message}</p>}
          {!isLoading && !isRefetching && !error && items.length === 0 && <p>当前命名空间暂无容器。</p>}
          {!isLoading &&
            !isRefetching &&
            !error &&
            items.map((item: K8sResourceSummary) => (
              <div key={`${item.namespace}-${item.name}`} className="container-item">
                <h4>{item.name}</h4>
                <p>命名空间：{item.namespace}</p>
                <p>类型：{item.type}</p>
                <p>状态：{item.status}</p>
              </div>
            ))}
          {isFetchingNextPage && <p>加载更多...</p>}
        </div>
        {items.length > 0 && (
          <div className="drawer-footer">
            已加载 {items.length} / {total || items.length} 个容器
          </div>
        )}
      </aside>
    </div>
  );
};
