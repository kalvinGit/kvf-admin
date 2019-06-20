# kvf-admin
kvf-admin快速开发框架、后台管理系统


### 项目结构树
````
kvf-admin
│
│ pom.xml maven依赖管理pom文件
│  
├─sql
│      kvf_sys.sql  项目初始化数据表及基础数据
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
