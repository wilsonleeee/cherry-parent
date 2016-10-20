# cherry-parent

```
├── cherry-parent
│   ├── cherry-common  # 存放共用的东西(常用工具包等)
│   ├── cherry-member  # 会员域
│   │   ├── cherry-member-api  # 会员域开放的api接口
│   │   ├── cherry-member-biz  # 会员域接口的实现（只能操作会员域里的表，不可跨域）
│   ├── cherry-biz  # 涉及多个域的一些聚合业务
│   ├── cherry-web  # 后台web项目
│   │   ├── cherry-web-member  # 会员模块页面（按模块划分）
│   ├── cherry-job  # job作业
```

## 项目使用技术
* jdk 1.7
* jetty9
* struts2
* spring3
* ibatis
* cat监控
* sql server