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

7. 线程调度-线程池
	1. new  Thread弊端
		1. 每次new Thread新建对象，性能差
		2. 线程缺乏统一管理，可能无限制的新建线程，相互竞争，有可能占用过多系统资源导致死机或OOM
		3. 缺少更多功能，如更多执行、定期执行、线程中断

	2. 线程池的好处
		1. 重用存在的线程，减少对象创建、消亡的开销，性能佳
		2. 可有效控制最大并发线程数，提高系统资源利用率，同时可以避免过多资源竞争，避免阻塞
		3. 提供定时执行、定期执行、单线程、并发数控制等功能

	2. ThreadPoolExecutor
		1. corePoolSize：核心线程数量maximumPoolSize：线程最大线程数
		2. workQueue：阻塞队列，存储等待执行的任务，很重要，会对线程池运行过程产生重大影响
		3. keepAliveTime：线程没有任务执行时最多保持多久时间终止
		4. unit：keepAliveTime的时间单位threadFactory：线程工厂，用来创建线程
		5. rejectHandler：当拒绝处理任务时的策略
		6. execute():提交任务，交给线程池执行
		7. submit()：提交任务，能够够返回执行结果execute+Future
		8. shutdown():关闭线程池，等待任务都执行完
		9. shutdownNow():关闭线程池，不等待任务执行完
		10. getTaskCount():线程池已执行和未执行的任务总数
		11. getCompletedTaskCount():已完成的任务数量
		12. getPoolSize():线程池当前的线程数量
		13. getActiveCount():当前线程池中正在执行任务的线程数量

	3. Executor框架接口
		1. Executors.newCachedThreadPool
		2. Executors.newFixedThreadPool
		3. Executors.newScheduledThreadPool
		4. Executors.newSingleThreadExecutor

	4. 线程池-合理配置
		1. CPU密集型任务，就需要尽量压榨CPU，参考值可以设为NCPU+1
		2. IO密集型任务，参考值可以设置为2*NCPU
		
8. 多线程并发扩展
	1. 死锁-必要条件
		1. 互斥条件：进程对所分配到的资源不允许其他进程进行访问，若其他进程访问该资源，只能等待，直至占有该资源的进程使用完成后释放该资源

		2. 请求和保持条件：进程获得一定的资源之后，又对其他资源发出请求，但是该资源可能被其他进程占有，此事请求阻塞，但又对自己获得的资源保持不放

		3. 不可剥夺条件：是指进程已获得的资源，在未完成使用之前，不可被剥夺，只能在使用完后自己释放
		
		4. 环路等待条件：是指进程发生死锁后，必然存在一个进程--资源之间的环形链

	2. 多线程并发最佳实践
		1. 使用本地变量：应该总是使用本地变量，而不是创建一个类或实例变量，通常情况下，开发人员使用对象实例作为变量可以节省内存并可以重用，因为他们认为每次在方法中创建本地变量会消耗很多内存。
		2. 使用不可变类：不可变类比如String Integer等一旦创建，不再改变，不可变类可以降低代码中需要的同步数量。


		3. 最小化锁的作用域范围：S=1/(1-a+a/n)：任何在锁中的代码将不能被并发执行，如果你有5%代码在锁中，那么根据Amdahl's law，你的应用形象就不可能提高超过20倍，因为锁中这些代码只能顺序执行，降低锁的涵括范围，上锁和解锁之间的代码越少越好。
		4. 使用线程池的Executor，而不是直接new Thread：创建一个线程的代价是昂贵的，如果你要得到一个可伸缩的Java应用，你需要使用线程池，使用线程池管理线程。JDK提供了各种ThreadPool线程池和Executor。
		5. 宁可使用同步而不要使用线程的wait notify：从Java 1.5以后增加了需要同步工具如CycicBariier, CountDownLatch 和 Sempahore，你应当优先使用这些同步工具，而不是去思考如何使用线程的wait和notify，通过BlockingQueue实现生产-消费的设计比使用线程的wait和notify要好得多，也可以使用CountDownLatch实现多个线程的等待
		6. 使用BlockingQueue实现生产-消费模式：大部分并发问题都可以使用producer-consumer生产-消费设计实现，而BlockingQueue是最好的实现方式，堵塞的队列不只是可以处理单个生产单个消费，也可以处理多个生产和消费。
		7. 使用并发集合Collection而不是加了同步锁的集合：Java提供了 ConcurrentHashMap CopyOnWriteArrayList 和 CopyOnWriteArraySet以及BlockingQueue Deque and BlockingDeque五大并发集合，宁可使用这些集合，也不用使用Collections.synchronizedList之类加了同步锁的集合， CopyOnWriteArrayList 适合主要读很少写的场合，ConcurrentHashMap更是经常使用的并发集合
		8. 使用Semaphore创建有界：为了建立可靠的稳定的系统，对于数据库 文件系统和socket等资源必须有界bound，Semaphore是一个可以限制这些资源开销的选择，如果某个资源不可以，使用Semaphore可以最低代价堵塞线程等待。
		9. 宁可使用同步代码块，也不使用加同步的方法：使用synchronized 同步代码块只会锁定一个对象，而不会将当前整个方法锁定；如果更改共同的变量或类的字段，首先选择原子性变量，然后使用volatile。如果你需要互斥锁，可以考虑使用ReentrantLock 
		10. 避免使用静态变量：静态变量在并发执行环境会制造很多问题，如果你必须使用静态变量，让它称为final 常量，如果用来保存集合Collection，那么考虑使用只读集合。
		11. 宁可使用锁，而不是synchronized 同步关键字：Lock锁接口是非常强大，粒度比较细，对于读写操作有不同的锁，这样能够容易扩展伸缩，而synchronized不会自动释放锁，如果你使用lock()上锁，你可以使用unlock解锁。

	3. Spring与线程安全
		1. Spring bean：singleton、prototype
		2. 无状态对象
		