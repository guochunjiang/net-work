# Network Enterprise API Framework

基于 **Spring Boot 2.7 + MyBatis + springdoc-openapi** 构建的企业级 RESTful API 框架。

采用 **Maven 多模块** 分层架构，遵循 **高内聚、低耦合** 的面向对象设计原则，提供统一的响应格式、全局异常处理、参数校验、自动接口文档等开箱即用的能力。

---

## 目录

- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [分层架构](#分层架构)
- [核心设计](#核心设计)
- [快速启动](#快速启动)
- [用户管理 API](#用户管理-api)
- [打包部署](#打包部署)
- [Docker 部署](#docker-部署)
- [扩展指南](#扩展指南)

---

## 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Java | 1.8 | JDK 8 |
| Spring Boot | 2.7.18 | 基础框架 |
| MyBatis | 3.5.14 | 数据访问 |
| MyBatis Spring Boot Starter | 2.3.2 | MyBatis 自动配置 |
| PageHelper | 5.3.2 / 1.4.6 | 物理分页 |
| springdoc-openapi | 1.7.0 | OpenAPI 3 接口文档 |
| H2 | 2.1.214 | 开发/测试内存数据库 |
| MySQL | 8.0 | 生产数据库 |
| Lombok | 1.18.30 | 消除样板代码 |
| Hutool | 5.8.28 | 工具类库 |

---

## 项目结构

```
network/
├── pom.xml                                  # 父 POM，统一版本管理
├── Dockerfile                               # 多阶段 Docker 构建
├── .dockerignore
├── sql/
│   └── init.sql                             # MySQL 初始化脚本
│
├── network-common/                          # ═══ 公共模块 ═══
│   ├── pom.xml
│   └── src/main/java/com/network/common/
│       ├── base/
│       │   ├── BaseResponse.java            # 统一响应体
│       │   ├── PageResult.java              # 分页结果封装
│       │   └── BaseEntity.java              # 实体基类
│       ├── constant/
│       │   └── ResultCode.java              # 响应码枚举
│       ├── dto/
│       │   └── PageParam.java               # 分页参数基类
│       └── exception/
│           ├── GlobalException.java         # 业务异常
│           └── GlobalExceptionHandler.java  # 全局异常处理器
│
├── network-dal/                             # ═══ 数据访问层 ═══
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/network/dal/
│       │   ├── entity/
│       │   │   └── UserEntity.java          # 用户实体
│       │   ├── mapper/
│       │   │   └── UserMapper.java          # MyBatis Mapper
│       │   └── config/
│       │       └── MyBatisConfig.java       # Mapper 扫描配置
│       └── resources/mapper/
│           └── UserMapper.xml               # SQL 映射文件
│
├── network-service/                         # ═══ 业务逻辑层 ═══
│   ├── pom.xml
│   └── src/main/java/com/network/service/
│       ├── UserService.java                 # 服务接口
│       └── impl/
│           └── UserServiceImpl.java          # 服务实现
│
└── network-web/                             # ═══ Web 表现层 ═══
    ├── pom.xml
    └── src/main/
        ├── java/com/network/
        │   ├── NetworkApplication.java      # Spring Boot 启动类
        │   └── web/
        │       ├── controller/
        │       │   └── UserController.java  # 用户 REST 控制器
        │       ├── dto/request/
        │       │   ├── UserCreateRequest.java  # 创建用户请求 DTO
        │       │   ├── UserUpdateRequest.java  # 更新用户请求 DTO
        │       │   └── UserQueryRequest.java   # 用户查询请求 DTO
        │       └── config/
        │           └── SwaggerConfig.java   # Swagger/OpenAPI 配置
        └── resources/
            ├── application.yml              # 主配置（默认 h2 profile）
            ├── application-h2.yml           # H2 数据源配置
            ├── schema-h2.sql                # H2 建表 DDL
            └── data-h2.sql                  # H2 初始数据 DML
```

---

## 分层架构

```
┌─────────────────────────────────────────────────┐
│                  network-web                     │
│   Controller  →  Request DTO  →  Swagger 文档    │
│   (接收请求/响应, 参数校验, 接口文档)              │
├─────────────────────────────────────────────────┤
│                 network-service                  │
│   Service Interface  →  ServiceImpl             │
│   (业务逻辑编排, 事务管理, 数据校验)              │
├─────────────────────────────────────────────────┤
│                  network-dal                     │
│   Entity  →  Mapper Interface  →  Mapper XML    │
│   (数据映射, SQL 执行, 分页)                      │
├─────────────────────────────────────────────────┤
│                network-common                    │
│   BaseResponse  │  PageResult  │  GlobalException│
│   (统一响应/分页/异常/工具, 被所有层依赖)          │
└─────────────────────────────────────────────────┘
```

### 依赖方向

**Common ← DAL ← Service ← Web**（下层对上层无感知）

- `network-common`：零业务依赖，只提供基础能力
- `network-dal`：依赖 `network-common`，专注数据访问
- `network-service`：依赖 `network-dal`，专注业务逻辑
- `network-web`：依赖 `network-service`，专注 HTTP 交互

---

## 核心设计

### 1. 统一响应格式 `BaseResponse<T>`

所有接口返回固定格式，前端可统一处理：

```json
{
  "code": 200,
  "message": "Success",
  "data": { ... }
}
```

```json
{
  "code": 422,
  "message": "用户名不能为空, 密码长度必须在6-100之间",
  "data": null
}
```

### 2. 分页结果 `PageResult<T>`

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "page": 1,
    "pageSize": 10,
    "total": 100,
    "pages": 10,
    "records": [ ... ]
  }
}
```

### 3. 全局异常处理

`GlobalExceptionHandler` 覆盖以下场景：

| 异常类型 | HTTP 状态码 | 说明 |
|----------|-------------|------|
| `GlobalException` | 200 (code=1001) | 业务异常 |
| `MethodArgumentNotValidException` | 200 (code=422) | 参数校验失败 |
| `ConstraintViolationException` | 200 (code=422) | 约束违反 |
| `MissingServletRequestParameterException` | 200 (code=422) | 缺少请求参数 |
| `MethodArgumentTypeMismatchException` | 200 (code=422) | 参数类型错误 |
| `HttpMessageNotReadableException` | 200 (code=422) | 请求体格式错误 |
| `HttpRequestMethodNotSupportedException` | 200 (code=405) | 请求方法不支持 |
| `HttpMediaTypeNotSupportedException` | 200 (code=415) | 媒体类型不支持 |
| `Exception`（兜底） | 200 (code=500) | 未预期异常 |

### 4. 参数校验（Bean Validation）

请求 DTO 使用 `javax.validation` 注解声明校验规则，`@Valid` 触发自动校验：

| 字段 | 校验规则 |
|------|----------|
| username | `@NotBlank` + `@Size(min=3, max=50)` |
| password | `@NotBlank` + `@Size(min=6, max=100)` |
| email | `@Email` |
| phone | `@Pattern(regexp="^1[3-9]\\d{9}$")` |

### 5. 接口文档（OpenAPI 3）

基于 `springdoc-openapi` 自动生成，访问 `http://localhost:8080/swagger-ui.html` 可交互式测试。

### 6. 数据源多 Profile 切换

默认使用 H2 内存数据库（零配置），添加 `--spring.profiles.active=mysql` 切换到 MySQL：

```
application.yml
├── spring.profiles.active: h2        # 默认
├── application-h2.yml                # H2 配置
└── application.yml → mysql profile   # MySQL 配置
```

---

## 快速启动

### 前提条件

- JDK 1.8+
- Maven 3.6+

### 方式一：直接运行（H2 内存数据库）

```bash
# 编译
mvn clean package -DskipTests

# 运行
java -jar network-web/target/network-web-1.0.0.jar
```

### 方式二：使用发布包

```bash
# 解压分发包
unzip network-web/target/network-web-1.0.0-distribution.zip

# 运行
cd network-web-1.0.0
java -jar network-web-1.0.0.jar
```

### 访问地址

| 地址 | 说明 |
|------|------|
| `http://localhost:8080/swagger-ui.html` | Swagger 接口文档 |
| `http://localhost:8080/h2-console` | H2 数据库控制台 |
| `http://localhost:8080/api/users` | 用户管理 API |

---

## 用户管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/users` | 创建用户 |
| PUT | `/api/users` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |
| GET | `/api/users/{id}` | 根据 ID 查询 |
| GET | `/api/users/username/{username}` | 根据用户名查询 |
| GET | `/api/users/page` | 分页查询 |
| GET | `/api/users` | 列表查询 |

### 创建用户示例

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "123456",
    "nickname": "New User",
    "email": "new@network.com",
    "phone": "13700137000",
    "status": 1
  }'
```

响应示例：

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 3,
    "username": "newuser",
    "password": "123456",
    "nickname": "New User",
    "email": "new@network.com",
    "phone": "13700137000",
    "status": 1,
    "createTime": "2026-06-18 16:00:00",
    "updateTime": "2026-06-18 16:00:00"
  }
}
```

---

## 打包部署

支持两种打包方式：

### 1. 标准 JAR 包

```bash
mvn clean package -DskipTests
java -jar network-web/target/network-web-1.0.0.jar
```

### 2. 分发包（带 libs + config）

使用 Maven Assembly 插件将应用打包为可分发的 ZIP：

```bash
mvn clean package -DskipTests
```

产物位于 `network-web/target/network-web-1.0.0-distribution.zip`，解压后结构：

```
network-web-1.0.0/
├── network-web-1.0.0.jar    # 业务代码（不含依赖和配置）
├── libs/                     # 所有运行时依赖（68 个 JAR）
└── config/                   # 外部化配置文件
    ├── application.yml
    ├── application-h2.yml
    ├── schema-h2.sql
    └── data-h2.sql
```

外部配置优先级高于 JAR 内配置，直接修改 `config/` 下的文件即可调整运行参数。

---

## Docker 部署

```bash
# 构建镜像
docker build -t network-api .

# 运行容器
docker run -d -p 8080:8080 --name network-api network-api
```

构建过程使用多阶段构建：
1. **builder 阶段**：`maven:3.8-jdk-8` 镜像编译打包
2. **runtime 阶段**：`openjdk:8-jre-slim` 镜像运行（镜像体积更小）

---

## 扩展指南

### 新增业务模块（以角色管理为例）

**1. 定义实体和 Mapper（network-dal）**

```java
// RoleEntity.java
public class RoleEntity extends BaseEntity {
    private String name;
    private String code;
    private String description;
}
```

```java
// RoleMapper.java
@Mapper
public interface RoleMapper {
    int insert(RoleEntity role);
    RoleEntity selectById(Long id);
    List<RoleEntity> selectList(RoleEntity query);
}
```

在 `mapper/RoleMapper.xml` 中编写 SQL。

**2. 定义 Service（network-service）**

```java
// RoleService.java
public interface RoleService {
    RoleEntity createRole(RoleEntity role);
    PageResult<RoleEntity> listRoles(RoleEntity query, int page, int size);
}
```

```java
// RoleServiceImpl.java
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    // 实现业务逻辑
}
```

**3. 定义 Controller（network-web）**

```java
// RoleController.java
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public BaseResponse<RoleEntity> create(@Valid @RequestBody RoleCreateRequest request) {
        // ...
    }
}
```

**4. 定义请求 DTO（network-web）**

```java
// RoleCreateRequest.java
@Data
@Schema(description = "Role create request")
public class RoleCreateRequest {
    @NotBlank
    @Schema(description = "Role name")
    private String name;
}
```

整个过程只需新建文件，无需修改现有代码 —— 体现了 **开闭原则** 和 **低耦合**。
