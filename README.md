# EduFlow | 教务管理系统

EduFlow 是一款基于 Spring Boot 开发的教务管理系统，旨在为高校或教育机构提供简洁高效的教务信息管理解决方案。系统涵盖课程管理、教师信息管理、学生选课、成绩录入等核心功能，助力数字化教务管理流程。

## ✨ 项目特点

- 基于 Spring Boot 构建，结构清晰，易于扩展
- 支持学生/教师/管理员三角色权限控制
- 提供课程管理、选课管理、成绩管理等模块
- 前后端分离，界面简洁美观（可集成 Vue）
- 使用 MySQL 数据库，支持主流部署环境

## 🏗️ 技术栈

- **后端框架**：Spring Boot 3.x  
- **持久层框架**：MyBatis 3.x  
- **数据库**：MySQL 8.x  
- **项目管理**：Maven 4.x  
- **前端建议**：Vue.js + Element UI（可选）

## 📁 项目结构

EduFlow
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.eduflow
│   │   │       ├── controller
│   │   │       ├── service
│   │   │       ├── mapper
│   │   │       ├── entity
│   │   │       └── EduFlowApplication.java
│   │   └── resources
│   │       ├── application.yml
│   │       └── mapper/*.xml
└── pom.xml

## ⚙️ 功能模块

- [x] 教师信息管理  
- [x] 学生信息管理  
- [x] 课程信息管理  
- [x] 学生选课管理  
- [x] 成绩录入与查询  
- [x] 角色权限控制（管理员/教师/学生）

## 🚀 快速开始

1. 克隆项目到本地：
   ```bash
   git clone https://github.com/your-username/EduFlow.git

	2.	导入 IDEA，配置 MySQL 数据源，初始化数据库
	3.	启动 EduFlowApplication.java
	4.	登录默认账户（配置文件中可设置）

📄 许可证

本项目采用 MIT License 开源，欢迎使用与二次开发。
