# 常量
1. true|false|null
2. 数字d表示double类型、long表示long类型、float表示浮点类型
3. 字符串 "" 或者'' ,\代表转义

# 空值校验
```
  y = o.x;
  y = o?.x;
```
上述第一个表达式中，如果o不存在，会抛出`expr.err_undefined_token`异常。

# 表达式
* 加减乘除+-/* 
* 赋值 +=、-=、*=、/=、=
* == 、>、<、>=、<=判断
* `&&,||,!`
* 求余%
* a.b运算
* 三元表达式 a?b:c
* 函数 f(a,b)
# 关键字
* var
* function 
* break
* continue
* return 
* do
* while
* for
* import