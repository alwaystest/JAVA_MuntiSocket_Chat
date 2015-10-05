# README

---

此项目是学校安排的大四企业实习课程的小结。综合利用了学过的JAVA的API和特性，写出一个C/S架构的简易聊天程序，功能简陋，还有许多待完善之处。

### 实现顺序
1. 使用Properties类生成配置文件。
2. 编写Socket和ServerSocket实现并验证C/S能够互联，发送和接收。
3. 提取Socket类。写出MultiSocket，在子线程中循环接收处理发过来的数据包。期间使用ServerSocket发送字符串测试Client是否能够正常接收处理。
4. 编写底层传输的数据类MultiDatagramPacket。包含C/S之间交流的所有语义。
5. 尝试使用ObjectIn(Out)putStream来发送和接收数据包对象。
6. 构造MultiServerSocket来处理每个接入请求，分发数据包。
7. 编写MultiSocketEvent，Listerner,Adapter,Support类，实现Event分发。将针对事件的处理交给添加监听器的类。提高重用性。
8. 修改Socket里面处理接入，处理包的地方，将其交给Support，由Support判断发生的事件，并将其交给对应的监听器处理。
9. 关联图形化界面。


### 收获总结

1. **接口：**之前书上读到过接口的定义，但是一直不能够理解，之前接触过的概念总是按照接口这个语义来解释，只要知道某个类实现了这个接口，就可以直接调用接口里面定义的方法，而不用管里面的具体实现。比如猫叫啊狗叫啊都属于动物叫什么的。这样子讲的确说明了接口的作用，但是我始终不知道什么时候应该定义一个接口。这次实习中，我的疑惑终于得到了解答。老师这么说：*“接口是不相关类的共同特性的抽象集合。”*这样我就了然了，比如Comparable接口定义了compareTo方法，只要实现这个接口，对象就可以比较了。无论对象是不是动物，只要需要比较对象，就实现Comparable接口。至于怎么比较，那就是compareTo方法定义的事情了。
2. 使用了学校里面不怎么教的输入输出流。
3. 自己手动实现了一次事件监听器分发处理。使用了观察者模式，受益颇丰。（MultiSocketSupport）
4. 服务器和客户端配置采用动态加载。解除了硬编码的限制。（Properties）
5. 使用TCP协议来实现可靠的数据传输。

### 使用到的特性

- 多进程下的进程安全。有多线程共享的资源要注意使用线程安全的实现。
- 接口 适配器 GUI Socket 守护进程
- 观察者模式（事件监听器实现 事件分发 处理）
- 工厂模式（生产Packets，保证产生的包是预期的，不会出现异常）
- 输入输出流，包装，对象序列化（传输）

### 零零星星的关键词

- 多态 运行期绑定
- 常量群接口（网上有说不建议这么用的，建议使用Enum代替） 标志型接口（比如Clone）
- StringBuilder 非线程安全 StringBuffer 线程安全
```
String str = new String("haha");//直接创建对象，返回对象的引用，对象不放入heap
String str = "haha";//先检查heap，使用equals()，如果已存在，返回对象的引用，如果没有，创建该对象，把该对象放入heap，返回对象的引用。
```
- JDK1.7以上 catch语句可以一句捕获多个并列级别的Exception。用竖线分割。
- JDK1.7以上 try...with语法。eg:`try(Autocloseable)`会在`finally`中自动close对象。
- 泛型 多个参数 注意二义性。
- 泛型通配符
- 泛型方法
- `JButton.setMnemonic('s')`设定快捷键，第一次出现的快捷键对应的字母会有下划线提示。ALT+S触发。
- 自定义输入输出流继承FilterOut(In)putStream，可以定义输出的位置，而继承FileOut(In)putStream没那么方便了。FilterOutputStream的其余`write(...)`调用`write(int x)`方法，修改一个就可以了。
- transient 关键词表示无需序列化。
- static变量默认不会被序列化。 Enternalizable extends Serializable
- `System.setOut(PrintStream)`可以修改输出位置。
- InputStreamReader 桥接。
- `thread.setDeamon(true)` 守护线程
- Thread.interrupt() 改变标志位。
- ObjectInputStream 特殊，C/S获取输入输出流的时候需要反向获取才能对接上。