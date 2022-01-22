# Lune

个人做的学习项目，主线版本严格按照读取Token-建立语法树-运行（先解释后运行）的模式，性能相对一般（while 1亿次累加略高于Python），用途目前作为Web服务器的模板语言解释器使用，Bug肯定是有的，而且思想感觉有时候想的不够，经验不足，整体代码的逻辑会比较逊。待主线稳定，有时间的时候分支将开启边解释边运行的模式，到时候估计性能会稍微好点，然后就是看能不能用C/C++来实现。至于VM虚拟机就免了，毕竟不是那块料。。。

### 语法介绍
1. Hello开始
```javascript
    # 这是注释
    param = "Hello Lune"  # 变量声明并赋值
    print(param)  # 函数调用-内部方法
```
2. 函数声明与调用
```javascript
    # 函数声明也是类似变量一样放在右侧（左侧形式目前不支持）
    test_fun = function(param1, param2)
    {
        # 需要指定return后才会有返回值，而不是返回最后一句的值
        return param1 + param2
    }
    # 函数调用
    res = test_fun(2, 4)
    # string.format 类似lua的方法，但是只支持%s（目前）
    print(string.format("res=%s", res))
```
3. 列表和字典
```javascript
    # 列表声明
    my_list = [1, 2, 3]
    # 字典的声明
    my_dict = {"A":1, "B":2}
    # 列表修改值
    my_list[1] = "a"
    # 注意这个特性被干了，防止my_list[index]index很大的情况，导致内存问题，性能问题。
    # my_list[5] = 0  # 这样干就会变 [1,"a",3, 0, 0, 0] 会用当前值填充扩展的数据

    # 字段的赋值
    my_dict["A"] = 3
    my_dict["C"] = 9 
    print(my_list[4], my_dict['C'])
```
4. 分支，循环
```javascript
    # if语句及分支
    i = 0
    if(i > 1)
    {

    }elif(i < 2)
    {

    }else
    {

    }
    # 遍历列表的值
    for(i in my_list)
    {

    }
    # 遍历字段的key和value
    for(k,v in my_dict)
    {

    }

    # while循环
    while(i > 1)
    {

    }
    # 其中for和while都支持break和continue
```
5. 类，与继承
```javascript
    # 类声明
    # 20220122修改， 针对函数的声明增加了self，移除this。这样做是可以区别成员方法和静态方法。
    # 类似Python的self的功能，但是静态方法不需要加装饰器说明（Lune语言没有装饰器）
    Parent = class("className")
    Parent.Name = "x"  # 创建属性 -- 这个算静态属性
    Parent.ctor = function(self, n)  # 函数声明 - 注意ctor为默认构造函数
    {
        self.Name = n  # 创建属性--这个才是实例属性
    }
    # 声明类，如果传入第二个参数，则表示继承
    Child = class("className", Parent)
    Child.ctor = function(self, n, c)
    {
        Parent.ctor(self, n)  # 调用父类的方法
        self.Cal = c
    }
    Child.test = function(self){}  # 这个有self，则为成员方法。
    Child.static_me = function() {} # 这个算静态方法，因为没有self

    c = Child("xxx", 1) # 新建实例对象，传入参数会自动调用ctor并传入
    c.test()
```


