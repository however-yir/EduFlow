# EduFlow

🔥 A Spring Boot teaching management backend based on MyBatis, MySQL, and JWT authentication.  
🚀 Built for course management, member administration, enrollment workflows, and approval processes.  
⭐ Supports login interception, course selection, withdrawal, auditing, and education data operations.

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

在 [application.properties](/Users/liuzhuoran/Documents/Playground/EduFlow/teaching-manager-bk/src/main/resources/application.properties) 中配置：

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
- JWT 密钥定义在 [JwtUtil.java](/Users/liuzhuoran/Documents/Playground/EduFlow/teaching-manager-bk/src/main/java/group/teachingmanagerbk/utils/JwtUtil.java)

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

## 10. 设计与实现思路

`EduFlow` 的实现重点放在“教务流程后端化”而不是页面展示上，整体采用“先公共能力、后业务流程”的推进顺序：

1. 先完成登录、JWT 鉴权和拦截器，明确接口访问边界；
2. 再完成教师、学生、院系、课程等基础数据模块；
3. 最后补选课、退课、课程申请和审核等流程型接口，让系统具备完整的教务业务闭环。

这种分阶段方式有两个直接好处：

- 公共能力先稳定，后续业务扩展时改动范围更可控；
- 即使前端尚未补齐，后端接口也已经具备独立联调和验证条件。

## 11. 关键难点与优化方向

### 11.1 关键难点

项目的难点主要不在单个 CRUD 接口，而在于多个流程之间的一致性：

- 登录态、接口权限和业务角色要保持统一；
- 课程管理、选课和审核流程之间要有清晰边界；
- 后端接口设计既要方便当前验证，也要为后续前端接入保留稳定结构。

### 11.2 当前处理方式

为此，当前实现采用了几项明确的约束：

- 使用 JWT 和拦截器统一处理登录态，而不是把鉴权逻辑分散到每个接口中；
- 按 `controller -> service -> mapper` 分层拆分课程、成员、申请、审核等逻辑，降低模块耦合；
- 将课程相关操作按“新增 / 修改 / 删除 / 申请 / 审核 / 选课”进行职责划分，避免流程膨胀后难以维护；
- 提前处理跨域与基础拦截逻辑，减少后续联调阶段的环境类问题。

### 11.3 后续优化方向

如果继续演进，优先级较高的方向包括：

- 增加数据库初始化脚本与迁移工具，降低部署和接手成本；
- 增加更细粒度的角色权限控制，而不是只依赖“已登录”判断；
- 补统一异常处理、接口文档和测试用例，提升工程完整度；
- 补齐前端工程，形成完整的课程管理演示闭环。

## 12.1 贡献建议

欢迎通过 Issue / PR 提交：

- SQL 初始化脚本与演示数据
- 前端工程补齐与联调说明
- 鉴权与权限粒度优化
- 自动化测试与 CI 配置

## 12.2 许可说明

本仓库采用 MIT License，详见 [LICENSE](/Users/liuzhuoran/Documents/Playground/EduFlow/LICENSE)。

## 13. 简历改造清单

- 改造追踪见 [docs/resume-upgrade-checklist.md](/Users/liuzhuoran/Documents/Playground/EduFlow/docs/resume-upgrade-checklist.md)
- 开发环境模板见 [.env.example](/Users/liuzhuoran/Documents/Playground/EduFlow/.env.example)
- 本地数据库快速启动见 [docker-compose.dev.yml](/Users/liuzhuoran/Documents/Playground/EduFlow/docker-compose.dev.yml)
- CI 配置见 [.github/workflows/ci.yml](/Users/liuzhuoran/Documents/Playground/EduFlow/.github/workflows/ci.yml)

本轮已落地：

- 选课状态限制（仅可选/待选）
- 选课时间冲突校验
- 两条核心规则的单元测试
