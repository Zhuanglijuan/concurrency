## 并发编程
1. 项目初始化
2. 线程安全性
	1. 原子性-Atomic包
		1. AtomicXXX：CAS、Unsafe.compareAndSwapInt
		2. AtomicLong、LongAdder
		3. AtomicReference、AtomicReferenceFieldUpdater
		4. AtomicStampReference:CAS的ABA问题

		
	2. 原子性-synchronized
		1. 修饰代码块：大括号括起来的代码，作用于调用的对象
		2. 修饰方法：整个方法，作用于调用的对象
		3. 修饰静态方法：整个静态方法，作用于所有对象
		4. 修饰类：括号括起来的部分，作用于所有对象。
		5. JMM关于synchronized的两条规定
			1. 线程解锁前，必须把共享变量的最新值刷新到主内存
			2. 线程加锁时，将清空工作内存中共享变量的值，从而使用共享变量时需要从主内存中重新读取最新的值
			3. 注意：加锁与解锁是同一把锁

	3. 原子性-对比

		1. synchronized：不可中断锁，适合竞争不激烈，可读性好
		2. Lock：可中断锁，多样化同步，竞争激烈时能维持常态
		3. Atomic：竞争激烈时能维持常态，比Lock性能好；只能同步一个值

	3. 可见性
		1. 导致共享变量在线程间不可见的原因

			1. 线程交叉执行
			2. 重排序结合线程交叉执行
			3. 共享变量更新后的值没有在工作内存与主内存间及时更新

	4. 可见性-volatile
		1. 通过加入内 存屏障和禁止重排序优化来实现
		2. 对volatile变量写操作时，会在写操作后加入一条store屏障指令，将本地内存中的共享变量值刷新到主内存
		3. 对volatile变量读操作时，会在读操作签加入一条load屏障指令，从主内存中读取共享变量

		
	5. 有序性-happens-before原则
		1. 程序次序规则：一个线程内，按照代码顺序，书写在前面的操作先行发生于书写在后面的操作
		2. 锁定规则：一个unlock操作先行发生于后面对同一锁的lock操作
		3. volatile变量规则：对一个变量的写操作先行发生与后面对这个变量的读操作
		4. 传递规则：如果操作A先行发生于操作B，而操作B又先行发生于操作C，则可以得出操作A先行发生于操作C
		5. 线程启动规则：Thread对象的start()方法先行发生于此线程的每一个动作
		6. 线程中断规则：对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中到事件的发生
		7. 线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行
		8. 对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始

3. 安全发布对象
	1. 发布与逸出
		1. 发布对象：使一个对象能够被当前范围之外的代码所使用
		2. 对象逸出：一种错误的发布。当一个对象还没有构造完成时，就使它被其他线程所见

	2. 安全发布对象
		1. 在静态初始化函数中初始化一个对象引用
		2. 将对象引用保存到volatile类型域或者AtomicReference对象中
		3. 将对象的引用保存到某个正确构造对象的final类型域中
		4. 将对象的引用保存到一个由锁保护的域中

4. 线程安全策略
	1. 不可变对象
		1. 不可变对象需要满足的条件
			1. 对象创建以后其状态就不能修改
			2. 对象所有域都是final类型
			3. 对象是正确创建的(在对象创建期间，this引用没有逸出

		2. final关键字：类、方法、变量

			1. 修饰类：不能被继承
			2. 修饰方法：1、锁定方法不被继承类修改；2、效率（注意：一个类的private方法会被隐式的被指定为final方法）
			3. 修饰变量：基本数据类型变量、引用类型变量

		3. Collections.unmodifiableXXX:Collection、List、Set、Map
		4. Guava：ImmutableXXX：Collection、List、Set、Map

	2. 线程封闭

		1. Ad-hoc线程封闭：程序控制实现，最糟糕，忽略
		2. 堆栈封闭：局部变量，无并发问题
		3. ThreadLocal线程封闭：特别好的封闭方法

	3. 线程不安全的类与写法
		1. StringBuilder -> StringBuffer
		2. SimpleDateFormat -> JodaTime
		3. ArrayList,HashSet,HashMap等Collections
		4. 先检查再执行：if(condition(a)){handle(a);}

	3. 线程安全-同步容器
		1. ArrayList -> Vector,Stack
		2. HashMap -> HashTable(key、value不能为null)
		3. Collections.synchronizedXXX(List、Set、Map)