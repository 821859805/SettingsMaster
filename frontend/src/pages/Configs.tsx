import { FormEvent, useEffect, useMemo, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  createCustomConfig,
  deleteCustomConfig,
  fetchCustomConfig,
  fetchCustomConfigs,
  updateCustomConfig
} from '../api/customConfig';
import type { CustomConfig } from '../types/customConfig';
import { useNamespaceStore } from '../store/namespaceStore';

interface ConfigFormState {
  id?: number;
  name: string;
  namespace: string;
  resourceType: string;
  description: string;
  yamlContent: string;
}

const initialForm = (namespace: string): ConfigFormState => ({
  name: '',
  namespace,
  resourceType: 'deployment',
  description: '',
  yamlContent: ''
});

export const ConfigsPage = () => {
  const { namespace } = useNamespaceStore();
  const queryClient = useQueryClient();
  const [keyword, setKeyword] = useState('');
  const [form, setForm] = useState<ConfigFormState>(() => initialForm(namespace));

  useEffect(() => {
    setForm((prev) => ({ ...prev, namespace }));
  }, [namespace]);

  const listQuery = useQuery({
    queryKey: ['configs', keyword],
    queryFn: () => fetchCustomConfigs(keyword || undefined)
  });

  const fetchMutation = useMutation({
    mutationFn: (id: number) => fetchCustomConfig(id),
    onSuccess: (config) =>
      setForm({
        id: config.id,
        name: config.name,
        namespace: config.namespace,
        resourceType: config.resourceType,
        description: config.description ?? '',
        yamlContent: config.yamlContent
      })
  });

  const createMutation = useMutation({
    mutationFn: () =>
      createCustomConfig({
        name: form.name,
        namespace: form.namespace,
        resourceType: form.resourceType,
        description: form.description || undefined,
        yamlContent: form.yamlContent
      }),
    onSuccess: () => {
      setForm(initialForm(namespace));
      queryClient.invalidateQueries({ queryKey: ['configs'] });
    }
  });

  const updateMutation = useMutation({
    mutationFn: () =>
      updateCustomConfig(form.id!, {
        name: form.name,
        namespace: form.namespace,
        resourceType: form.resourceType,
        description: form.description || undefined,
        yamlContent: form.yamlContent
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['configs'] });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => deleteCustomConfig(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['configs'] })
  });

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();
    if (!form.name || !form.namespace || !form.yamlContent) {
      alert('名称、命名空间、YAML 内容不能为空');
      return;
    }
    if (form.id) {
      updateMutation.mutate();
    } else {
      createMutation.mutate();
    }
  };

  const handleReset = () => {
    setForm(initialForm(namespace));
  };

  const isBusy = createMutation.isLoading || updateMutation.isLoading;

  return (
    <div className="card">
      <section>
        <h2>配置列表</h2>
        <form
          className="form-row"
          onSubmit={(event) => {
            event.preventDefault();
            queryClient.invalidateQueries({ queryKey: ['configs', keyword] });
          }}
        >
          <div>
            <label htmlFor="keyword">搜索</label>
            <input
              id="keyword"
              className="input"
              value={keyword}
              onChange={(event) => setKeyword(event.target.value)}
              placeholder="按名称、命名空间或类型搜索"
            />
          </div>
          <div style={{ alignSelf: 'flex-end' }}>
            <button type="submit" className="button secondary">
              刷新
            </button>
          </div>
        </form>
        {listQuery.isLoading && <p>加载中...</p>}
        {listQuery.error && <p className="badge-red">加载失败：{(listQuery.error as Error).message}</p>}
        {listQuery.data && listQuery.data.length > 0 ? (
          <table className="table" style={{ marginTop: 16 }}>
            <thead>
              <tr>
                <th>名称</th>
                <th>命名空间</th>
                <th>类型</th>
                <th>更新时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              {listQuery.data.map((config) => (
                <tr key={config.id}>
                  <td>{config.name}</td>
                  <td>{config.namespace}</td>
                  <td>{config.resourceType}</td>
                  <td>{new Date(config.updatedAt).toLocaleString()}</td>
                  <td>
                    <div className="actions">
                      <button className="button" onClick={() => fetchMutation.mutate(config.id)}>
                        编辑
                      </button>
                      <button className="button danger" onClick={() => deleteMutation.mutate(config.id)}>
                        删除
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          !listQuery.isLoading && <p>暂无配置。</p>
        )}
      </section>

      <section style={{ marginTop: 32 }}>
        <h2>{form.id ? '编辑配置' : '新增配置'}</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div>
              <label htmlFor="configName">名称</label>
              <input
                id="configName"
                className="input"
                value={form.name}
                onChange={(event) => setForm((prev) => ({ ...prev, name: event.target.value }))}
              />
            </div>
            <div>
              <label htmlFor="configNamespace">命名空间</label>
              <input
                id="configNamespace"
                className="input"
                value={form.namespace}
                onChange={(event) => setForm((prev) => ({ ...prev, namespace: event.target.value }))}
              />
            </div>
            <div>
              <label htmlFor="configType">类型</label>
              <select
                id="configType"
                className="select"
                value={form.resourceType}
                onChange={(event) => setForm((prev) => ({ ...prev, resourceType: event.target.value }))}
              >
                <option value="deployment">Deployment</option>
                <option value="statefulset">StatefulSet</option>
                <option value="configmap">ConfigMap</option>
                <option value="secret">Secret</option>
              </select>
            </div>
          </div>
          <label htmlFor="configDesc">描述</label>
          <input
            id="configDesc"
            className="input"
            value={form.description}
            onChange={(event) => setForm((prev) => ({ ...prev, description: event.target.value }))}
          />
          <label htmlFor="configYaml">YAML 内容</label>
          <textarea
            id="configYaml"
            className="textarea"
            value={form.yamlContent}
            onChange={(event) => setForm((prev) => ({ ...prev, yamlContent: event.target.value }))}
            placeholder="粘贴 Kubernetes YAML"
          />
          <div className="actions" style={{ marginTop: 12 }}>
            <button type="submit" className="button" disabled={isBusy}>
              {form.id ? '保存修改' : '创建配置'}
            </button>
            <button type="button" className="button secondary" onClick={handleReset}>
              重置
            </button>
          </div>
        </form>
        <ErrorHint errors={[createMutation.error, updateMutation.error, fetchMutation.error, deleteMutation.error]} />
      </section>
    </div>
  );
};

const ErrorHint = ({ errors }: { errors: Array<unknown> }) => {
  const message = useMemo(() => {
    const error = errors.find(Boolean) as Error | undefined;
    return error?.message;
  }, [errors]);

  if (!message) {
    return null;
  }

  return <p className="badge-red" style={{ marginTop: 12 }}>操作失败：{message}</p>;
};
