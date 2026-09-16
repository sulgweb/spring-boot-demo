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

### 商品与购物车

登录后请在需要认证的接口中传入 `Authorization: Bearer <token>`。

- `GET /products`：分页查询已上架商品，支持 `keyword`、`page`、`size`
- `GET /products/{productId}`：查询商品及启用的 SKU
- `POST /admin/products`：创建商品和 SKU
- `PUT /admin/products/{productId}`：修改商品基本信息
- `PATCH /admin/products/{productId}/status`：修改商品状态
- `POST /admin/products/{productId}/skus`：新增 SKU
- `PATCH /admin/products/{productId}/skus/{skuId}`：局部修改 SKU，只传需要修改的字段
- `GET /cart`：连表查询当前用户购物车
- `POST /cart/items`：加入购物车，相同 SKU 自动合并数量
- `PATCH /cart/items/{itemId}`：修改数量
- `DELETE /cart/items/{itemId}`：删除购物车条目

状态码约定：

- 商品状态：`0` 草稿、`1` 上架、`2` 下架
- SKU 状态：`0` 禁用、`1` 启用
