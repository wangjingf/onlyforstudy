使用状态机实现如下简单功能：
1. 空调有两个按钮： power及cool
2. 有三种状态： 运行(running)、关闭(closing)、制冷(cooling)
3. 正常 -> power -> 关闭
    正常 -> cool -> 制冷
   关闭 -> power -> 打开
   制冷->power ->关闭
   