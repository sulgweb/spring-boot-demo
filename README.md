# Getting Started

### 安装

./mvnw dependency:resolve

### 启动依赖（MySQL + Redis）

```bash
# 启动
docker compose up -d

# 查看状态
docker compose ps

# 查看日志
docker compose logs -f

# 停止
docker compose stop

# 停止并删除容器（数据卷保留）
docker compose down

# 停止并删除容器和数据卷
docker compose down -v
```

启动后可用：

- MySQL：`127.0.0.1:3306`，库 `springboot_demo`，用户 `admin` / `admin123`
- Redis：`localhost:6379`，密码 `redis123`

首次启动 MySQL 可能需要十几秒，等 `docker compose ps` 显示 healthy 后再跑应用。

### dev

./mvnw spring-boot:run

### 接口文档

http://localhost:8080/swagger-ui/index.html#/
