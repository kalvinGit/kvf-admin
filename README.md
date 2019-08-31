# kvf-admin
kvf-admin是一套快速开发框架、脚手架、后台管理系统、权限系统，上手简单，拿来即用。为广大开发者去除大部分重复繁锁的代码工作，让开发者拥有更多的时间陪恋人、家人和朋友。
* 后端采用spring boot、mybatis(已集成mybatis-plus增强插件，开发更迅速，可查看官方文档了解更多：[mybatis-plus](https://baomidou.gitee.io/mybatis-plus-doc/#/quick-start))、shiro框架
* 前端采用layui作为UI框架，实现90%的移动端自适应，支持主题更换
* 提供代码生成器([wiki使用文档](https://github.com/kalvinGit/kvf-admin/wiki/kvf%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90%E5%99%A8%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3))，只需编写20%左右的代码，剩下全部自动生成；支持一键及批量功能模块生成，并支持一定程度上的自定义配置并生成代码，相对比较灵活

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
* 通过git/gitee下载源码(推荐使用git，因为gitee不是实时更新的)
* 创建数据库：kvf_admin【数据库编码为utf8mb4】，并执行sql/kvf_admin.sql创建表和初始化系统基础数据
* 修改开发环境配置文件application-dev.yml，配置数据库账号和密码
* 开发工具idea或eclipse还需要安装lombok插件，否则会提示找不到实体类的的get/set方法
* 运行KvfAdminApplication.java，启动项目【kvf-admin】
* idea启动访问：http://localhost/【一般idea都会自动去掉项目名】【这里使用80端口】
* eclipse启动访问：http://localhost/kvf-admin【这里使用80端口】
* 账号密码：admin/123456

### 项目演示
* 演示地址：http://kvfadmin.kalvinbg.cn
* 账号密码：test/123456

### 系统效果图展示

![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin.png)
![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin1.png)
![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin2.png)
![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin3.png)
![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin4.png)

### 更新日志
#### 2019-08-18
* 新增定时任务管理（可在yml配置是否开启）
* 增加登录验证码开关（可在yml配置，为了方便开发不用输入验证码登录）
* sql脚本更新
* 优化代码生成器
* 修复了若干个bug
#### 2019-08-11
* 新增字典管理
* 优化代码生成器字段类型转换
* 修复代码生成器生成代码包路径不正确bug
* 删除多余依赖包
* 修复了一些其它bug

#### 2019-07-21
* 修复代码生成器生成日期控件没有实例化
* 修复代码生成器生成的查询字段名称没有转成坨峰命名
* 修复代码生成器生成mapperxml时，表达式缺少半边中括号
* 优化部分功能代码
* 新增logback日志配置

### 敬请期待
* 集成UEditor
* 日程管理
* 集成activity工作流引擎
* vue-admin版本

### 交流反馈
* github仓库：https://github.com/kalvinGit/kvf-admin
* gitee仓库：https://gitee.com/kalvinmy/kvf-admin
* 作者QQ：1481397688
* 如需关注项目最新动态，请Watch、Star项目，同时也是对项目最好的支持

