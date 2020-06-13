<p align="center">
    <img src="http://cloud.kalvinbg.cn/image/kvf-admin-logo.png" alt="kvf-admin-logo">
</p>
<p align="center">
  <a href="https://github.com/kalvinGit/kvf-admin/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/mashape/apistatus.svg" alt="license">
  </a>
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/springboot-v2.2.4-green" alt="springboot">
  </a>
  <a href="https://mp.baomidou.com">
    <img src="https://img.shields.io/badge/mybatis--plus-v3.3.0-blue" alt="mybatis-plus">
  </a>
  <a href="https://github.com/kalvinGit/kvf-admin/wiki/kvf%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90%E5%99%A8%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3" rel="nofollow">
    <img src="https://img.shields.io/badge/code--generator-wiki-orange" alt="code-generator">
  </a>
  <a href="https://www.layui.com/doc/">
    <img src="https://img.shields.io/badge/layui-v2.5.6-brightgreen" alt="layui">
  </a>
  <a href="https://www.hutool.cn/">
    <img src="https://img.shields.io/badge/hutool--all-v4.5.1%20-yellow" alt="hutool">
  </a>
  <a href="https://www.activiti.org/">
    <img src="https://img.shields.io/badge/activiti-v6.0.0-lightgrey" alt="activiti6">
  </a>
</p>

# kvf-admin
kvf-admin是一套快速开发框架、脚手架、后台管理系统、权限系统，上手简单，拿来即用。为广大开发者去除大部分重复繁锁的代码工作，让开发者拥有更多的时间陪恋人、家人和朋友。<br>**技术交流群：214768328**
<a href="http://cloud.kalvinbg.cn/image/kvf_code.png" target="_blank">
   <img src="https://img.shields.io/badge/%E6%8A%80%E6%9C%AF%E4%BA%A4%E6%B5%81%E7%BE%A4-%E4%BA%8C%E7%BB%B4%E7%A0%81-blue" alt="二维码">
</a>
* 后端采用spring boot、mybatis(已集成mybatis-plus增强插件，开发更迅速，可查看官方文档了解更多：[mybatis-plus](https://mp.baomidou.com/))、shiro框架
* 前端采用layui作为UI框架，实现90%的移动端自适应，支持主题更换
* 提供代码生成器([wiki使用文档](https://github.com/kalvinGit/kvf-admin/wiki/kvf%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90%E5%99%A8%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3))，只需编写20%左右的代码，剩下全部自动生成；支持一键及批量功能模块生成，并支持一定程度上的自定义配置并生成代码，相对比较灵活
#### 基础框架功能模块
* [x] 用户管理
* [x] 部门管理
* [x] 菜单管理
* [x] 角色管理
* [x] 字典管理
* [x] 操作日志
* [x] 代码生成
* [x] 组件管理
## kvf-admin-activiti（工作流OA版本）
kvf-admin-activiti是基于kvf-admin脚手架集成了工作流引擎（activiti6），并封装了核心工作流程（OA）功能模块。如下：
目前工作流（OA）模块还处于初始阶段，代码也比较粗糙，后续再不断完善优化。<br>
备注：工作流版本在【activiti】分支
* [x] 核心API（支持启动流程、提交任务、驳回、驳回任意环节、驳回首环节、撤回、挂起/激活流程等API）
* [x] 流程管理（支持流程在线设计器、发布/部署、挂起、激活、导出/导入、配置表单、启动、删除）
* [x] 表单管理（支持快速表单在线设计器、增/删/改/查、预览表单）
* [x] 我的流程（发起流程申请）
* [x] 我的待办（支持快速办理、查看任务表单办理、历史审批意见、流程实时流转图等）
* [x] 我的已办（支持撤回功能）
* [x] 我的申请（查看所有当前用户申请过的流程情况）
* [ ] 下一步计划，期待大家的反馈意见！

### 更新日志
[👳👉‍点我点我点我](https://github.com/kalvinGit/kvf-admin/wiki/%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97)

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
* mysql5.7+

### 所用技术
#### 前端
* jQuery 
* [layui v2.5.6](https://www.layui.com/doc/) (UI框架)

#### 后端
* spring boot v2.2.4.RELEASE
* Mybatis
* [Mybatis-plus v3.3.0](https://mp.baomidou.com/guide/wrapper.html#abstractwrapper) (mybatis增强插件，无侵入。非常强大的插件，除了联表操作，几乎都可以使用它的sql条件构造器完成)
* Shiro v1.4.0
* Druid v1.1.21
* ehcache
* redis
* [hutool-all v4.5.1](https://hutool.cn/docs/#/) (java通用工具类，此包几乎包括了所有常用的工具方法，你也可以按需引入相应工具模块包)

### 项目特点
* 非常精简且轻量级的权限系统，代码简洁易懂，无论学习还是项目中应用，都是非常简单易上手的项目
* 拥有界面配置化代码生成器，支持一键生成及简单自定义配置生成代码
* 自动过滤输入的非法字符串，防止XSS攻击
* 使用ehcache + redis作为缓存，对需要加入缓存的方法上添加@Cacheable注解即可（你也可以使用redisTemplate添加获取缓存），提升系统运行速度
* 支持日志记录，可在需要加入日志操作记录的controller方法上添加@Log("业务操作备注")即可完成日志记录
* 系统全局统一异常处理，所有异常信息统一处理返回R对象，前端处理提示信息更方便
* 支持工作流（OA）功能
* 完美支持多种部署方式（jar、tomcat、docker等）

### 本地部署
* 通过git/gitee下载源码(推荐使用git，因为gitee不是实时更新的)，若是工作流OA版本，请clone activiti分支
* 创建数据库：执行sql/kvf_admin.sql脚本创建数据库及表并初始化系统基础数据，若是工作流OA版本需要额外执行sql/kvf_admin_activiti.sql脚本
* 修改开发环境配置文件application-dev.yml，配置数据库账号和密码
* 开发工具idea或eclipse还需要安装lombok插件，否则会提示找不到实体类的的get/set方法
* 运行KvfAdminApplication.java，启动项目【kvf-admin】
* idea启动访问：http://localhost/【一般idea都会自动去掉项目名】【这里使用80端口】
* eclipse启动访问：http://localhost/kvf-admin【这里使用80端口】
* 账号密码：admin/123456


### linux部署
注意：以下三种方式部署前，记得初始化数据库哦
#### 打包
###### 开发环境(dev)：
```
mvn package -P dev
```
###### 测试环境(test)：
```
mvn package -P test -Dmaven.test.skip=true
```
###### 生产环境(prod)：
```
mvn package -P prod -Dmaven.test.skip=true
```
#### jar包方式部署
项目已解决以jar包运行的情况下，无法读取文件等各种问题，所以放心使用

###### 运行
```
nohup java -jar kvf-admin.jar &
```
#### tomcat部署
打包前先修改pom.xml的打包方式为war
```
<packaging>war</packaging>
```
##### 运行
##### 把war包拷贝到tomcat的webapps目录下，然后进入bin目录执行：
```
./startup.sh
```

### docker部署
前提：安装docker及docker-compose<br>

##### 进入kvf-admin目录，在已打包(上面打包步骤)的情况下，构建docker镜像
```
mvn docker:build
```
##### 进入./docker-compose目录
修改相应docker-compose配置（可选）
```
vim docker-compose.yml
vim .env
```
##### 运行（启动所有镜像）
```
docker-compose up -d
```
##### 或者启动指定镜像
```
docker-compose up -d kvf-admin
```


### 项目演示
* 演示地址：http://kvfadmin.kalvinbg.cn
* 账号密码：test/123456

### 系统效果图展示

![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin.png)
![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin1.png)
![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin2.png)
![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin3.png)
![系统效果图](http://cloud.kalvinbg.cn/image/kvf-admin4.png)

### 开发指南
* 前端通用配置js【kconfig.js】
* 前端通用工具js【kcommon.js】
* 前端静态文件引用统一管理配置【base.html】，在需要引用里面的配置的页面上引用即可，如引用通用的css：`<link th:replace="common/base::static"/>`
* 后端自定义日志注解@Log("业务操作说明")[com.kalvin.kvf.common.annotation.Log]，在需要加入日志的controller方法上加这个注解即可
* 缓存使用[点我](https://my.oschina.net/sdlvzg/blog/1608871)：对需要加入缓存的方法上添加`@Cacheable(value="cache_name")`注解即可，同时需要在对应的方法上加上更新或删除缓存注解`@CacheEvict(value = "cache_name", allEntries = true)`;也可以使用redisTemplate添加删除更新缓存
* 代码生成器使用文档[点我](https://github.com/kalvinGit/kvf-admin/wiki/kvf%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90%E5%99%A8%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3)
* Spring上下文工具【SpringContextKit.java】，可使用它手动获取指定bean。如`IUserService userService = SpringContextKit.getBean(IUserService.class);`
* 自定义异常处理类【KvfException.java】，可用于业务层【service】抛出业务异常，如：`throw new KvfException("不存在的任务ID");` ，前端可接收到这个提示信息
* 统一接口返回数据封装类【R.java】，可用于控制层【controller】返回成功或失败等数据。如`R.ok(data); 或 R.fail("验证码不正确");`
* 开发环境【dev】默认关闭登录验证码，若需要开启验证码登录可在application-dev.yml配置开启
* 通用文件上传接口：CommonController->fileUpload

### 敬请期待
* vue-admin版本

### 常见问题
**1.有些人访问报错：`org.apache.ibatis.binding.BindingException: Parameter 'xxx' not found,Available parameters are [0, 1, param1, param2]`**

为什么会出现部分人报错，有些却不报错呢？
答案参考这篇分析文章：[点我](https://blog.csdn.net/u011821334/article/details/101763001)

####解决方案：
* 方案一：
可能是使用低版本的idea，因为低版本的idea默认是没有加上-parameters选项的，需要手动加上；（上面的分析文章有详细说明）
或者升级idea版本

* 方案二：
在mapper的方法参数上加上@Param注解


**2.mybatisPlus自带的crud方法默认会根据实体类字段驼峰自动转下划线匹配数据表字段，如果不需要自动转下划线该如何配置**
#### 全局配置：
可在配置项`map-underscore-to-camel-case`配置：
配置为`true`时，mybatisplus会根据实体类字段驼峰自动转下划线匹配数据表字段如：myColumn(实体字段) -> my_column(表字段)<br>
`map-underscore-to-camel-case:true`<br>
同理，配置为`false`时：myColumn(实体字段) -> myColumn(表字段)<br>
`map-underscore-to-camel-case:false`
![全局配置](https://images.gitee.com/uploads/images/2020/0404/110427_012b323d_1235987.png "微信截图_20200404110317.png")

#### 局部配置：
如果只是部分表或字段需要，可在实体类的字段上配置@TableField(value="表字段")注解指定数据表字段名称如：

```
@TableField(value = "myColumn")
private String myColumn;
```




### 交流反馈
* github仓库：https://github.com/kalvinGit/kvf-admin
* gitee仓库：https://gitee.com/kalvinmy/kvf-admin
* 如需关注项目最新动态，请Watch、Star项目，同时也是对项目最好的支持
* 交流群：214768328
<p>
    <img width="150" src="http://cloud.kalvinbg.cn/image/kvf_code.png" alt="技术交流群">
</p>

