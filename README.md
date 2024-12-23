# EduFlow

> 面向教务场景的课程与成员管理系统后端项目，基于 Spring Boot + MyBatis + MySQL 实现，包含登录鉴权（JWT）、课程管理、选课流程与审核流程等核心能力。

## 1. 项目定位

`EduFlow` 用于教学管理业务的后端支撑，当前仓库以后端为主：

- 后端代码位于 `teaching-manager-bk`
- 前端目录 `teaching-manager-ui/Pc/teaching-manager-pc-ui` 当前仅保留目录骨架（未包含完整前端源码）

## 2. 已实现能力（后端）

- 登录与登录校验（JWT）
- 用户密码修改
- 教师/学生/院系信息管理
- 课程管理（新增、更新、删除、状态切换）
- 学生选课、退课、已选课程查询
- 课程申请与课程审核流程
- CORS 与登录拦截器

## 3. 技术栈

- Java 17
- Spring Boot 3.1.5
- MyBatis 3.x
- MySQL 8.x
- JWT（`io.jsonwebtoken`）
- Lombok / Fastjson

## 4. 项目结构

```text
EduFlow
├── teaching-manager-bk/                        # Spring Boot 后端
│   ├── src/main/java/group/teachingmanagerbk
│   │   ├── controller/                         # Login/Course/Member/... 控制器
│   │   ├── service/                            # 业务服务层
│   │   ├── mapper/                             # MyBatis Mapper
│   │   ├── interceptor/                        # 登录拦截器
│   │   └── utils/                              # JWT 与统一返回结构
│   └── src/main/resources/application.properties
└── teaching-manager-ui/Pc/teaching-manager-pc-ui  # 前端目录占位
```

## 5. 本地启动（后端）

### 5.1 环境

- JDK 17
- Maven 3.8+
- MySQL 8.x

### 5.2 数据库配置

在 [application.properties](/Users/liuzhuoran/Documents/Playground/readme-batch/EduFlow/teaching-manager-bk/src/main/resources/application.properties) 中配置：

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

默认数据库名为：`teaching-manager`。

说明：仓库未附带 `.sql` 初始化文件，需按业务模型自行建表或补充迁移脚本。

### 5.3 运行

```bash
cd teaching-manager-bk
mvn spring-boot:run
```

## 6. 鉴权机制说明

- `/login`、`/check/login` 默认放行
- 其他接口走登录拦截，需在 `Authorization` 头中携带 JWT
- JWT 密钥定义在 [JwtUtil.java](/Users/liuzhuoran/Documents/Playground/readme-batch/EduFlow/teaching-manager-bk/src/main/java/group/teachingmanagerbk/utils/JwtUtil.java)

## 7. 关键接口（示例）

- 登录：`POST /login`
- 成员管理：`/teachers`、`/students`、`/departments`
- 课程管理：`/insert/course`、`/update/course`、`/delete/course`
- 选课流程：`/student/select/course`、`/exit/course`
- 课程申请：`/apply/add/course`、`/all/application`
- 审核流程：`/wait/examination`、`/course/examination`

## 8. 当前仓库状态

- 后端代码完整度高，可直接作为 API 项目运行
- 前端目录目前为空壳结构，建议补齐前端工程后联调

## 9. 开发建议

- 使用环境变量管理数据库与 JWT 配置
- 增加 Flyway/Liquibase 管理数据库迁移
- 增加统一异常处理与接口文档（OpenAPI）

## 12.1 贡献建议

欢迎通过 Issue / PR 提交：

- SQL 初始化脚本与演示数据
- 前端工程补齐与联调说明
- 鉴权与权限粒度优化
- 自动化测试与 CI 配置

## 12.2 许可说明

本仓库采用 MIT License，详见 [LICENSE](/Users/liuzhuoran/Documents/Playground/readme-batch/EduFlow/LICENSE)。
