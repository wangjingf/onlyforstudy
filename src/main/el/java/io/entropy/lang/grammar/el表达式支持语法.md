# EL表达式
* EL表达式是一种类函数式语言，只规定了少数运算符，尽量减少复杂的语法结构，尽可能的通过函数来统一所有运算过程。所有表达式都有返回值，因此很容易进行组合。与xpl语言集成时，加入了编译期执行以及标签调用功能，从而具备了元编程能力。
* 函数和普通变量处于同一个变量空间，便于翻译为javascript

## 与javascript区别
* 没有switch语句，只有switch函数
* 没有undefined, 只有null
* 没有类和prototype
* 没有===操作
* == 判断不会自动将数字转化为字符串。对于两个数字比较，如果类型不一致会试图转换为同样类型后进行比较。
* null参与数学运算时不会自动转换为0。
* 没有NaN概念，减少前台显示或者内部处理时需要判断的情况。
* 只支持i++/i--, 不支持++x和--x。
* 空指针不会报错，null.f()会返回null
* 非法的数学运算返回null,而不是NaN
* EL表达式中关键字只有function, var, null, true, false, return, continue, break, import, new, for, of, if, do, while这几个，变量命名限制较少。
* a && b 这种布尔表达式的值必然是boolean类型，而不是a或者b的类型
* 以$为前缀的变量名保留为系统变量名，所有自定义变量不能以$为前缀。
* lambda表达式使用lambda关键字，即形式为 lambda x, y=> x + y, 而不是 （x,y) => x+y

## 变量域限制
* 变量、函数名、参数名三者之间不能重名
* 不允许重复定义同名变量、函数或者参数
* 变量定义强制要求初始化。不能是var x, 而必须是var x= null;
* 通过闭包引用的变量不能被修改. 这一点主要是为了便于翻译到java实现。

## 注释
* 单行注释形式
```js
// 这里是注释
```

* 多行注释形式
```js
/**
 这里是多行
 注释
 */
```

## 基本运算规则
- `+-*/`四则运算。对于+操作，如果左右两侧任意一个元素为CharSequence类型，则执行字符串相加运算，对于null会转换为字符串'null'。
- `&&,||, !` 布尔逻辑运算，运算具有短路特性，即如果发现后续运算不会改变结果，则后续表达式不会被执行。例如
```js
false && x = 1   // x=1这个赋值语句不会被执行
```
- `>=,>, ==,<, <=, !=` 二元比较运算
- 求余  x % 2
- `>>, >>=, <<, |, &, ^, |=, &=, ^=` bit operation

## 内置常量
* null 
* true
* false

## 语言关键字
* function 用于函数定义
* lambda 用于lambda表达式定义
* var  用于变量声明
* break
* continue
* return
* do
* import

## 字面量
* ""或者''都可以表示字符串，内部使用\转义，按照一般java语言转义规则进行。
* 没有new String()对象
* 后缀D表示double类型， 例如3D
* 后缀L表示long类型，例如3L
* 后缀F表示float类型，例如3F

## 赋值语句
* 简单赋值 x = y
* 设置属性 entity.x = y
* 设置动态属性 entity[propName] = y
* 数组下标赋值  list[2] = y, 如果是List类型而且数组下标越界，则会试图将List扩展到足够大小，然后再赋值。
* 复合属性自动创建： entity.x.y = z 相当于调用 entity.makeX().y = z 
1. 如果entity上存在makeXX函数，则使用makeXX函数来返回属性值。约定make系列函数的语义为如果为空，则自动创建一个。
2. 如果entity.x返回null, 则entity.x.y = z时会抛异常。

注意: 以$为前缀的变量约定为系统级变量，在EL表达式中不允许对系统变量赋值。

## 空值校验
token解析时要求变量名对应的变量在上下文环境中必须存在，可以是null。可以在变量名后加?表示允许该变量不存在。这一机制主要是为了防止编程过程中拼写错误。
```
  y = o.x;
  y = o?.x;
```
上述第一个表达式中，如果o不存在，会抛出`expr.err_undefined_token`异常。

## 属性读取
* 静态属性 o.a.b().c 当对象指针为null时，属性读取总是返回null
* 动态属性读取  o[propName]
* 数组下标  list[3] 如果下标越界，直接返回null, 而不是抛出异常
* 属性名按照Java Bean规范确定，如果没有对应的get/set方法，但是具有public的Field, 则直接存取field
```java
  class MyClass{
     public int fldA;
     
     private String fldB;
     
     public String getFieldB(){
        return fldB;
     }
  }
  // 在EL表达式中可以访问属性 fldA和fieldB
```

## 局部变量声明
```
x = 1;

y = 1;

f = function(){
  var x = 2;
  y = 2;
  var notExists = 3;
}

f();

$.assertTrue(x == 1);
$.assertTrue(y == 2);
$.assertTrue(!$scope.containsLocalValue('notExists'));

```
通过var方式声明的变量是局部变量，出了当前执行区域，它的值会被自动恢复。
* var声明时必须执行初始化，例如可以是`var x =1, y=2;` 或者 `var x=1;` 而不能是`var x;`
* var声明不能与此前已经使用过的变量名冲突。例如以下情况都是错误的
````
  x = 1;
  var x = 2; // 与已经使用的变量名冲突
  
  var y = 3;
  var y = 4; // 与已经定义的变量名冲突
  
  for(var y of list){ // 与已经定义的变量名冲突
  }
````
* for和using的变量定义进行了特殊处理，多个for语句可以使用同样的var
````
  for(var i=0;i<10;i++){
  }
  
  for(var i=1;i<5;i++){
  }
````

## 函数调用
* 静态函数  f(a,b)
* 对象函数  o.f(a,b) 当对象为null时，对象函数调用会直接返回null,而不会去执行a和b表达式。
* 函数定义 function(arg1,arg2){ 函数体 }, 其类型为IEvalFunction, 它支持类似js的bind操作。
```
   f = function(x){
   }
   
   $.assertTrue(instance_of(f, 'io.entropy.expr.IEvalFunction'));
   
   var g = f.bind(o,2);
  
   g();
   
```
* lambda表达式与function的区别在于lambda表达式不会具有this指针和arguments变量。
````
  $A(c).each(lambda vs => vs.current + 1);
  $A(c).reduce(v, lambda (v, vs) => v + vs.current);
  $A(c).map(lambda vs => { return vs.current + 2});
````

* IFunction类型的变量可以作为函数被调用，调用时只能有一个参数, 例如 func(arg1)
* IEvalReference类型的变量可以通过eval函数来执行
```js
   // 第三个参数为scope，值可以是local或者share。
   eval(expr, {x:arg1,y:arg2}, "local") // 在expr内部可以访问参数x和y. 
```
* 在EL表达式中函数会自动转型为Callable, Runnable, IProcessor, IFilter, ITransformer, Comparator等接口



## 类型转换
* 通过V函数实现类型转换
```
   V(x).toInt(defaultValue)
   V(x).toDouble()
```

## 类似JSON的基本数据结构
* Map
```js
{
 a: 1,
 'b': 2 
}
```

* List
```js
  a = [];
  b = [1,2];
```

## 通过函数实现的基本数据结构
* Map
```js
Map() //空Map
Map('a',1,'b',2) // 通过参数配对来表达key-value结构
```
Map函数与{}机制的区别在于，M函数的key可以不是字符串。而{}语法只支持字符串Key

* Set
```js
  Set();  // 空集合
  Set(a); // 一个元素的集合
  Set(a,b); // 多个元素的集合
  Set(a,a); // 最终产生的集合里只包含一个元素a
```

## 复杂语句
所有的语句同时也是表达式，作为表达式，它们的返回值是null

### 判断语句
```
 if(cond) { 
   body
 }else if(cond){
   body
 }else{
   body
 }
```

### 循环语句
```
  for(init;cond;post){
      body
  }
  
  for(var varName of collection){
     body
  }
```

### using语句
类似于C#中的using语句，自动调用AutoCloseable接口的close方法
```
  using(var fs = openFile(), anotherIo = openFile()){
    ...
  }
```

## 通过函数实现的运算结构

* 判断结构  
```js
if(testExpr, trueExpr)
if(testExpr, trueExpr, falseExpr)
if(testExpr, trueExpr, testExpr2,trueExpr2, defaultExpr) 
switch(varExpr, v1, expr1, v2,expr2, defaultExpr)
```

* 循环结构 
```js
$A(c).each(function(vs){ 
        ...
     }).map(function(vs){
        ...
     }) 
```
参数vs的类型为LoopVarStatus。  
| 函数名        | 说明 |  
| ------------ | ---- |  
| vs.index     | 当前循环下标，从0开始 |   
| vs.hasNext() | 返回是否还有后续元素 |  
| vs.current   | 返回当前元素 |
| vs.value   |   与vs.current相同，返回当前元素 |
| vs.isFirst() | 判断是否第一个元素 |  
| vs.isLast()  | 判断是否最后一个元素 |  
| vs.count     | 当前循环次数，从1开始 |  
| vs.skipAll() | 跳过后续所有元素，退出循环。注意当前步骤并不会被中断，是下次判断vs.hasNext()时返回false。 |

## 函数定义
通过javascript function的方式实现函数定义。语法形式为 `function(参数列表){ 函数体 }`

```java
  f = function(x){ return x + 1 };
  f = function(x,y){ return x + 2 };
```

## 装载bean
* $$beanName可以直接访问IoC容器管理的bean, 缺省情况下, 缺省情况下会调用 BeanContainer.instance().getBean(beanName)
* 判断bean是否存在: $beans.containsBean("xx")
* 根据类名创建对象: $beans.newObject(className)
* 根据类名获得Class： $beans.getStatic(className)
 对于Class对象，调用它的对象方法和属性时实际访问的是静态方法及静态field
```
  var MyClass = $beans.getStatic("test.MyClass");
  MyClass.myStaticFunction();
  
  var x = MyClass.MY_STATIC_FIELD;
```

## 表达式序列
* ExprSequence会自动将最后一个子句的返回值作为整个表达式的返回值。
* XLang.parseExprSequence解析得到的是ExprSequence。这主要是为了便于实现类似defaultExpr/exportExpr这样的表达式配置。
```
  <!-- 在om模型中配置defaultExpr时，表达式的最后一行执行结果会作为返回值 -->
  <defaultExpr> now() </defaultExpr>
  
  <c:script>
     if(x > 0){
       return x + 3;
     }
     // 如果此前没有执行return, 则最后一句表达式执行结果会作为return值
    x + 2
  </c:script>
```


## 内置对象和函数
* 内置对象参见 `ide_config/sys_objects.xml`
* 内置函数参见 `ide_config/sys_functions.xml`

## 变量解析
1. 首先检查是否以$为前缀的系统变量，如果是，则尝试从EvalRegistry中获取。
2. 如果未找到，则在$scope管理的variables中查找变量
3. 如果未找到，则在$scope.parentScope中查找

## 调试支持
系统内置了$对象，它对应于EvalGlobal类，通过它提供了断言、日志、断点等调试手段

* 断言
```js
$.assertTrue(myVar != null);
$.assertTrue(myVar != null, "myVar 变量不能为空:data={}", data);
$.assertEquals(x, y);
v = $.notNull(myVar, "myVar变量不能为null");
v = $.notEmpty(myVar, "myVar不能为空串");
```

*日志
```js
$.info("some data = {}", someData);
$.debug("other info={}", other);
o.f($.v(myVar,"myValue")); // 会打印出myValue=xxx
```


*变量位置跟踪
EL表达式执行过程中会自动跟踪设置变量时的源码位置，
在EL表达式中可以通过$scope.getLocalVariable("xxx").getSourceLocation()来得到对应变量xxx的源码位置。或者通过$scope.displayString来获取所有变量信息

## 安全性设计
* 不允许调用Class对象上的方法
* 系统函数和系统变量不允许被上下文环境中的变量覆盖
* Object类上的方法只允许调用equals/hashCode/toString, 其他方法不允许调用。

## 字符串扩展函数
* String类型的对象被自动扩展了大量函数，它们对应于StringHelper类上的静态帮助方法
```js
 str.$escapeXml() // 对应调用StringHelper.escapeXml(str)
```
为了避免与对象原本存在的方法冲突，新增的扩展函数都以$为前缀

## 扩展
* 通过EvalRegistry可以注册全局对象，全局对象名必须以$为前缀，例如 $Math对应MathHelper类，提供数学帮助函数
* 通过EvalRegistry可以注册全局函数
* 通过XClass.registerHelper(MyClass.class, MyHelper.class)这种方式可以为指定类型的对象增加函数。String扩展函数就是采用这种形式实现的。
   registerHelper函数会自动把MyHelper类中的所有满足条件的静态函数（第一个参数要求是MyClass类型或者是其基类类型）注册为MyClass类的对象函数。
  为了避免与对象原本存在的方法冲突，新增的扩展函数都以$为前缀
* 对象上可以实现IHookReader, IHookWriter, IHookMaker, IHookFunctionProvider等接口，当根据反射未找到某个属性或者方法时会调用这里的函数来处理，这一机制类似ruby语言中的method_missing机制。
  
## xpl语言中的EL表达式
* 以`#`为开始的表达式部分在编译期运行
```js
   f(#(2+3))相当于直接写f(5)
   
   #f() + 3 相当于在编译期调用f()得到返回值, 在运行期再和3加在一起
```  
* 利用编译期运行机制可以实现常量引用校验
```js
   #const.MY_TYPE  // 在编译期const实际上就是一个XConstants类型的普通对象，只是从它获取未定义的值时，它会抛出异常
```


* 以`namespace:`为开始的表达式部分表示调用xpl标签
```js
my:MyTag(x=1)  // 相当于调用标签 <my:MyTag x="1" />
my:MyTag(x=x,y=y) // 相当于调用标签 <my:MyTag x="${x}" y="${y}" />

my:MyTag(x=1){    // {}内为子节点内容，子节点如果有内容采用 名称=值的形式。值只能是字符串，数字或者布尔类型。
  subTagName=true
  subTagName2="sss"
  subTagName3=3.3
  subTagName4{
    subSubTag
    subSubTag3 = 'abc'
    subSubTag2=true
  }
  subTagName5(attr="1"){

  }

  subTagName6(attr=1) = `
     很长的多行文本，
     采用``转义
  `
}
```
节点内容如果是多行字符串，可以使用`来表示。

* 通过import语法可以在EL表达式中引入标签库
```
  import "/xapp/wf/_xlib/wf.xlib"
  import my_namespace from "/xapp/wf/_xlib/wf.xlib"
```
import语法会自动被翻译为 <cp:import lib="libPath" namespace="namespace" />标签调用
