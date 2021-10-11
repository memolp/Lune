# Lune

这只是个人做的一个学习型项目，我会在自己的一些需要的地方使用，即不能保证性能，也不能保证稳定性，更别说BUG了。

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
    my_list[5] = 0  # 这样干就会变 [1,"a",3, 0, 0, 0] 会用当前值填充扩展的数据

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
    Parent = class("className")
    Parent.Name = "x"  # 创建属性 -- 这个算静态属性
    Parent.ctor = function(n)  # 函数声明 - 注意ctor为默认构造函数
    {
        this.Name = n  # 创建属性--这个才是实例属性
    }
    # 声明类，如果传入第二个参数，则表示继承
    Child = class("className", Parent)
    Child.ctor = function(n, c)
    {
        Parent.ctor(n)  # 调用父类的方法
        this.Cal = c
    }
    Child.test = function(){}

    c = Child("xxx", 1) # 新建实例对象，传入参数会自动调用ctor并传入
    c.test()
```


