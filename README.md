# SpringCloud

搭建SpringCloud，集成Nacos、SpringGateway、SpringSecurity、Dubbo远程调用等组件；前端采用React

## 项目目录结构

```
├── auth-service                 --授权认证服务
├── common-service               --公共服务包，包括工具包，公共Model
├── example-client               --客户端示例
├── example-resource             --资源服务器实例
├── gateway-service              --网关服务
├── user-center              	 --用户中心
│   ├── user-center-api          --api
│   ├── user-center-server  	 --服务端
│   └── pom.xml
├── website              		 --前端
│   ├── login      				 --登录服务前端页面
│   └── pom.xml
├── docs                 --文档及资源文件
├── readme.md            --readme文档入口
└── pom.xml             
```

## 基础服务

| 服务     | 实现                    | 进度 | 备注                                                   |
| -------- | ----------------------- | ---- | ------------------------------------------------------ |
| 注册中心 | Nacos                   | ✅    |                                                        |
| 配置中心 | Nacos                   | ✅    |                                                        |
| 网关     | SpringCloud Gateway     | ✅    |                                                        |
| 权限     | SpringSecurity OAuth2.0 | ✅    | 支持系统鉴权和第三方授权码鉴权，授权申请中心还在开发中 |
| 存储     | Redis                   | ing  | 加入Redis锁                                            |

## 更新日志

### 2020.12.25

- 加入Redis分布式锁实现

### 2020.12.24

- 新增授权码登录方式

