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

	4. 线程安全-并发容器 J.U.C
		1. ArrayList -> CopyOnWriteArrayList(适合读多写少的场景）
		2. HashSet、TreeSet -> CopyOnWriteArraySet、ConcurrentSkipListSet
		3. HashMap、TreeMap -> ConcurrentHashMap、ConCurrentSkipListMap(1. key是有序的。2、支持更高的并发。）

	5. 安全共享对象策略 - 总结
		1. 线程限制：一个被线程限制的对象，由线程独占，并且只能被占有它的线程修改
		2. 共享只读：一个共享只读的对象，在没有额外同步的情况下，可以被多个线程并发访问，但任何线程都不能修改它
		3. 线程安全对象：一个线程安全的对象或者容器，在内部通过同步机制来保证线程安全，所以其他线程无需额外的同步就可以通过公共接口随意访问它。
		4. 被守护对象：被守护对象只能通过获取特定的锁来访问

5. J.U.C之AQS
	1. 介绍
		1. AbstractQueuedSynchronizer - AQS
			1. 使用Node实现FIFO队列，可以用于构建锁或者其他同步装置的基础框架
			2. 利用了一个int类型表示状态
			3. 使用方法是继承
			4. 子类通过继承并通过实现它的方法管理其状态{acquire和release}的方法操纵状态
			5. 可以同时实现排它锁和共享锁模式（独占、共享）

	2. 同步组件
		1. CountDownLatch
		2. Semaphore
		3. CyClicBarrier
		4. ReentrantLock与锁
			1. ReentrantLock（可重入锁）和synchronized区别
				1. 可重入性
				2. 锁的实现 synchronized关键字依赖JVM实现的，而ReentrantLock依赖JDK实现的，可阅读源码。
				3. 性能的区别 synchronized自从引入偏向锁、轻量锁，也就是自旋锁后。两者性能差不多了，官方更建议synchronized
				4. 功能区别
					1. synchronized的使用比较方便简洁，并且是由编译器保证锁的加锁和释放的。而ReentrantLock需要我们手动声明来加锁和释放锁。为了避免手工释放锁造成的死锁，所以最好是在finally里中释放锁
					2. 锁的细粒度和灵活度

			2. ReentrantLock独有的功能
				1. 可指定是公平所还是非公平锁
				2. 提供了一个Condition类，可以分组唤醒需要唤醒的线程
				3. 提供能够中断等待锁的线程机制，lock.lockInterruptibly()

6. J.U.C组件扩展
	1. FutureTask
		1. Callable与Runnable接口对比
		2. Future接口
		3. FutureTask类

	2. Fork/Jion框架
	3. BlockingQueue
	
		1. ArrayBlockingQueue :有界的阻塞队列，内部实现是数组，有边界的意思是容量是有限的，在其初始化的时候必须制定指定容量大小。以先进先出的方式存储数据，最先插入的对象是尾部，最先移除的对象是头部。
		2. DelayQueue ：阻塞的是内部元素，里面的元素必须实现一个接口，J.U.C内的Delay接口，此接口继承了Comparable接口，因为DelayQueue需要进行排序，一般情况下按照元素过期时间的优先级进行排序，应用场景：定时关闭连接、缓存对象、超时处理等
		3. LinkedBlockingQueue：此队列大小配置可以选择，如果初始化时指定大小则有边界，若不指定则无边界。内部实现是链表。以先进先出的方式存储数据。
		4. PriorityBlockingQueue：无边界带优先级的队列，允许插入null，所有插入此队列的对象必须实现Comparable接口。可以获得迭代器iterator，但是不保证按照优先级顺序进行迭代。
		5. SynchronousQueue：仅容纳一个元素，当一个线程插入一个元素后就会被阻塞，除非这个元素被另一个线程消费，因此又称它为同步队列，一个无界非缓存队列即不存储元素，放入元素后只能等待取走元素后才能放入。
		6. 小结：BlockingQueue不仅实现了队列所具有的基本功能，同时在多线程环境下还管理多线程间的自动等待、唤醒功能，从而使得开发者可以忽略这些细节，关注更高级的功能，