# Kubernetes 配置管理系统

本项目提供一套前后端分离的 Kubernetes 配置管理解决方案，支持在线查看集群资源、创建/更新/删除工作负载，以及管理可复用的自定义 YAML 配置。后端基于 Spring Boot + MyBatis + MySQL，前端基于 React + TypeScript + Vite。

## 功能概览

- 查询指定命名空间下的 Deployment 与 Pod 列表，并展示副本数、状态等信息。
- 查看任意受支持资源（Deployment/StatefulSet/ConfigMap/Secret/Pod）的 YAML 配置。
- 通过上传 YAML 创建或更新 Kubernetes 资源，支持命名空间自动补全。
- 删除 Kubernetes 资源。
- 管理自定义配置模板：支持新增、编辑、删除与模糊搜索，配置持久化存储在 MySQL。

## 项目结构

```
backend/   Spring Boot 服务，集成 MyBatis 与 Kubernetes Client
frontend/  React + TypeScript 管理控制台（Vite 构建）
```

## 运行前准备

- JDK 17+
- Maven 3.9+
- Node.js 18+ 与 pnpm/npm/yarn 之一
- MySQL 8.0+（创建数据库 `k8s_config`）
- 可访问的 Kubernetes 集群，或本地 `~/.kube/config`

## 后端启动步骤

1. **配置数据库**

   ```sql
   CREATE DATABASE IF NOT EXISTS k8s_config DEFAULT CHARACTER SET utf8mb4;
   ```

   `backend/src/main/resources/schema.sql` 会在首次启动时自动建表。

2. **修改数据源与集群配置**  
   编辑 `backend/src/main/resources/application.yml`，调整以下项：

   - `spring.datasource.url/username/password`
   - `kubernetes.client.config-file`（可选，留空则使用集群内或环境默认配置）
   - `kubernetes.client.namespace`（默认命名空间）

3. **构建并运行**

   ```bash
   cd backend
   mvn spring-boot:run
   ```

   服务默认监听 `http://localhost:8080`。

## 前端启动步骤

```bash
cd frontend
npm install       # 或 pnpm install / yarn
npm run dev       # 启动开发服务器，默认 http://localhost:5173
```

Vite 已配置将 `/api` 前缀的请求代理到 `http://localhost:8080`。

## 常用 REST 接口

- `GET /api/k8s/namespaces/{namespace}/deployments`：列出 Deployment 摘要信息。
- `GET /api/k8s/namespaces/{namespace}/pods`：列出 Pod 摘要。
- `GET /api/k8s/namespaces/{namespace}/resources/{kind}/{name}`：获取资源 YAML。
- `POST /api/k8s/resources`：创建资源（请求体包含 `namespace`、`yamlContent`）。
- `PUT /api/k8s/resources/{kind}/{name}`：更新资源。
- `DELETE /api/k8s/resources/{kind}/{name}?namespace=xxx`：删除资源。
- `POST /api/configs` / `PUT /api/configs/{id}` / `DELETE /api/configs/{id}`：管理自定义配置。
- `GET /api/configs?keyword=xxx`：按关键字搜索配置模板。

## 提示

- 生产环境中建议结合 RBAC、审计日志等能力，限制对 Kubernetes 集群的操作范围。
- 如需支持更多资源类型，可在 `KubernetesServiceImpl` 中扩展 `fetchResource` 的分支逻辑。
- `frontend/src/pages` 和 `backend/src/main/resources/mapper` 内的示例可作为扩展新功能的参考。

祝使用愉快！欢迎根据实际业务继续扩展。 