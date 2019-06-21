# kvf-admin
kvf-admin是一套快速开发框架、后台管理系统、权限系统，上手简单，拿来即用。
* 后端采用spring boot、mybatis(集成mybatis-plus扩展插件)、shiro框架
* 前端采用layui作为UI框架，实现90%的移动端自适应
* 提供代码生成器， 支持一键及批量功能模块生成，并支持一定程度上的自定义配置并生成代码，相对比较灵活
* 目前只支持mysql数据库

### 项目结构树
````
kvf-admin
│
│ pom.xml maven依赖管理pom文件
│  
├─sql
│      kvf_sys.sql  项目初始化数据表及基础数据sql脚本
│      
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─kalvin
    │  │          └─kvf
    │  │              │  KvfAdminApplication.java   项目启动类
    │  │              │  
    │  │              ├─common  通用模块
    │  │              └─modules 功能模块
    │  │                  ├─generator   代码生成器模块
    │  │                  └─sys 系统模块（核心）
    │  └─resources
    │      │  application.yml   spring boot 配置文件
    │      │  ehcache.xml   ehcache缓存配置文件
    │      │  
    │      ├─mapper mybatis mapper文件
    │      ├─static 静态资料
    │      └─templates  模板
    │          │  403.html  403页面
    │          │  home.html 系统首页页面
    │          │  index.html   主页
    │          │  login.html   登录页
    │          │  
    │          ├─common 通用模板
    │          │      base.html
    │          │      sys_tpl.html
    │          │      
    │          ├─generator  生成器模板
    │          │          
    │          └─sys    系统页面模板
    │                  
    └─test  单元测试块

````

### 软件需求
* jdk8+
* mysql5.5+

### 本地部署
* 通过git下载源码
* 创建数据库：kvf-admin【数据库编码为utf8mb4】，并执行sql/kvf-admin.sql创建表和初始化系统基础数据
* 修改开发环境配置文件application-dev.yml，配置数据库账号和密码
* 开发工具idea或eclipse还需要安装lombok插件，否则会提示找不到实体类的的get/set方法
* 运行KvfAdminApplication.java，启动项目【kvf-admin】
* idea启动访问：http://localhost/【一般idea都会自动去掉项目名】【这里使用80端口】
* eclipse启动访问：http://localhost/kvf-admin【这里使用80端口】
* 账号密码：admin/123456

### 项目演示
* 演示地址：晚点会放上来，请稍候...
* 账号密码：admin/123456

### 系统效果图展示


### 交流反馈
* github仓库：https://github.com/kalvinGit/kvf-admin
* gitee仓库：
* 官方QQ群：214768328
* 作者QQ：1481397688
* 如需关注项目最新动态，请Watch、Star项目，同时也是对项目最好的支持


### 捐赠支持
项目的发展离不开你的支持，感谢！<br>

