import { FormEvent, useMemo, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  createResource,
  deleteResource,
  fetchDeployments,
  fetchPods,
  fetchResourceYaml,
  updateResource
} from '../api/k8s';
import type { K8sResourceSummary } from '../types/k8s';
import { useNamespaceStore } from '../store/namespaceStore';

interface SelectedResource {
  kind: string;
  name: string;
}

export const DashboardPage = () => {
  const queryClient = useQueryClient();
  const { namespace, setNamespace } = useNamespaceStore();
  const [namespaceInput, setNamespaceInput] = useState(namespace);
  const [createYaml, setCreateYaml] = useState('');
  const [updateYaml, setUpdateYaml] = useState('');
  const [updateTarget, setUpdateTarget] = useState<SelectedResource | null>(null);
  const [selected, setSelected] = useState<SelectedResource | null>(null);
  const [updateError, setUpdateError] = useState<string | null>(null);

  const deploymentsQuery = useQuery({
    queryKey: ['deployments', namespace],
    queryFn: () => fetchDeployments(namespace),
    staleTime: 10_000
  });

  const podsQuery = useQuery({
    queryKey: ['pods', namespace],
    queryFn: () => fetchPods(namespace),
    staleTime: 10_000
  });

  const resourceQuery = useQuery({
    queryKey: ['resource', namespace, selected?.kind, selected?.name],
    queryFn: () => fetchResourceYaml(namespace, selected!.kind, selected!.name),
    enabled: Boolean(selected)
  });

  const createMutation = useMutation({
    mutationFn: createResource,
    onSuccess: () => {
      setCreateYaml('');
      queryClient.invalidateQueries({ queryKey: ['deployments', namespace] });
      queryClient.invalidateQueries({ queryKey: ['pods', namespace] });
    }
  });

  const updateMutation = useMutation({
    mutationFn: (payload: { kind: string; name: string; yaml: string }) =>
      updateResource(payload.kind, payload.name, { namespace, yamlContent: payload.yaml }),
    onSuccess: () => {
      setUpdateYaml('');
      setUpdateTarget(null);
      queryClient.invalidateQueries({ queryKey: ['deployments', namespace] });
      queryClient.invalidateQueries({ queryKey: ['pods', namespace] });
      queryClient.invalidateQueries({ queryKey: ['resource', namespace] });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: ({ kind, name }: SelectedResource) => deleteResource(kind, name, namespace),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['deployments', namespace] });
      queryClient.invalidateQueries({ queryKey: ['pods', namespace] });
    }
  });

  const handleNamespaceSubmit = (event: FormEvent) => {
    event.preventDefault();
    setNamespace(namespaceInput.trim() || 'default');
  };

  const isLoading = deploymentsQuery.isLoading || podsQuery.isLoading;

  const failed = useMemo(() => deploymentsQuery.error ?? podsQuery.error, [deploymentsQuery.error, podsQuery.error]);

  return (
    <div className="card">
      <section>
        <h2>命名空间</h2>
        <form className="form-row" onSubmit={handleNamespaceSubmit}>
          <div>
            <label htmlFor="namespace">当前命名空间</label>
            <input
              id="namespace"
              className="input"
              value={namespaceInput}
              onChange={(event) => setNamespaceInput(event.target.value)}
            />
          </div>
          <div style={{ alignSelf: 'flex-end' }}>
            <button type="submit" className="button">
              切换
            </button>
          </div>
        </form>
      </section>

      <section style={{ marginTop: 24 }}>
        <h2>资源概览</h2>
        {isLoading && <p>加载中...</p>}
        {failed && <p className="badge-red">加载失败：{(failed as Error).message}</p>}
        {!isLoading && !failed && (
          <div style={{ display: 'flex', gap: 24 }}>
            <div style={{ flex: 1 }}>
              <h3>Deployment</h3>
              <ResourceTable
                resources={deploymentsQuery.data ?? []}
                onView={(item) => setSelected({ kind: 'deployment', name: item.name })}
                onDelete={(item) => deleteMutation.mutate({ kind: 'deployment', name: item.name })}
                onEdit={async (item) => {
                  setUpdateTarget({ kind: 'deployment', name: item.name });
                  setSelected({ kind: 'deployment', name: item.name });
                  try {
                    setUpdateError(null);
                    const yaml = await fetchResourceYaml(namespace, 'deployment', item.name);
                    setUpdateYaml(yaml);
                  } catch (error) {
                    setUpdateError((error as Error).message);
                  }
                }}
              />
            </div>
            <div style={{ flex: 1 }}>
              <h3>Pod</h3>
              <ResourceTable
                resources={podsQuery.data ?? []}
                hideActions
                onView={(item) => setSelected({ kind: 'pod', name: item.name })}
              />
            </div>
          </div>
        )}
      </section>

      <section style={{ marginTop: 24 }}>
        <h2>资源 YAML 详情</h2>
        {selected ? (
          resourceQuery.isLoading ? (
            <p>加载中...</p>
          ) : resourceQuery.error ? (
            <p className="badge-red">读取失败：{(resourceQuery.error as Error).message}</p>
          ) : (
            <pre style={{ background: '#0f172a', color: '#e2e8f0', padding: 16, borderRadius: 8, overflowX: 'auto' }}>
              {resourceQuery.data}
            </pre>
          )
        ) : (
          <p>选择左侧资源以查看 YAML 内容。</p>
        )}
      </section>

      <section style={{ marginTop: 24 }}>
        <h2>创建资源</h2>
        <textarea
          className="textarea"
          placeholder="粘贴或编写 Kubernetes YAML"
          value={createYaml}
          onChange={(event) => setCreateYaml(event.target.value)}
        />
        <button
          className="button"
          disabled={!createYaml || createMutation.isLoading}
          onClick={() => createMutation.mutate({ namespace, yamlContent: createYaml })}
        >
          提交
        </button>
        {createMutation.error && <p className="badge-red">创建失败：{(createMutation.error as Error).message}</p>}
      </section>

      <section style={{ marginTop: 24 }}>
        <h2>更新资源</h2>
        {updateTarget ? (
          <div className="form-row">
            <div>
              <label>类型</label>
              <input className="input" value={updateTarget.kind} disabled />
            </div>
            <div>
              <label>名称</label>
              <input className="input" value={updateTarget.name} disabled />
            </div>
          </div>
        ) : (
          <p>从上方 Deployment 表格中选择“编辑”以加载资源。</p>
        )}
        <textarea
          className="textarea"
          placeholder="更新后的 YAML 内容"
          value={updateYaml}
          onChange={(event) => setUpdateYaml(event.target.value)}
        />
        <div className="actions">
          <button
            className="button"
            disabled={!updateTarget || !updateYaml || updateMutation.isLoading}
            onClick={() =>
              updateTarget &&
              updateMutation.mutate({ kind: updateTarget.kind, name: updateTarget.name, yaml: updateYaml })
            }
          >
            更新
          </button>
          <button
            className="button secondary"
            onClick={() => {
              setUpdateTarget(null);
              setUpdateYaml('');
              setUpdateError(null);
            }}
          >
            清空
          </button>
        </div>
        {updateMutation.error && <p className="badge-red">更新失败：{(updateMutation.error as Error).message}</p>}
        {updateError && <p className="badge-red">读取待编辑资源失败：{updateError}</p>}
      </section>
    </div>
  );
};

interface ResourceTableProps {
  resources: K8sResourceSummary[];
  onView: (resource: K8sResourceSummary) => void;
  onDelete?: (resource: K8sResourceSummary) => void;
  onEdit?: (resource: K8sResourceSummary) => void;
  hideActions?: boolean;
}

const ResourceTable = ({ resources, onView, onDelete, onEdit, hideActions }: ResourceTableProps) => {
  if (!resources.length) {
    return <p>暂无数据。</p>;
  }

  return (
    <table className="table">
      <thead>
        <tr>
          <th>名称</th>
          <th>命名空间</th>
          <th>类型</th>
          <th>副本</th>
          <th>状态</th>
          {!hideActions && <th>操作</th>}
        </tr>
      </thead>
      <tbody>
        {resources.map((item) => (
          <tr key={`${item.type}-${item.name}`}>
            <td>
              <button className="button secondary" onClick={() => onView(item)}>
                查看
              </button>{' '}
              {item.name}
            </td>
            <td>{item.namespace}</td>
            <td><span className="tag">{item.type}</span></td>
            <td>{item.replicas ?? '-'}</td>
            <td className={item.status?.toLowerCase().includes('ready') ? 'badge-green' : ''}>{item.status}</td>
            {!hideActions && (
              <td>
                <div className="actions">
                  {onEdit && (
                    <button className="button" onClick={() => onEdit(item)}>
                      编辑
                    </button>
                  )}
                  {onDelete && (
                    <button className="button danger" onClick={() => onDelete(item)}>
                      删除
                    </button>
                  )}
                </div>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  );
};
