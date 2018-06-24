## 并发编程
1. 项目初始化
2. 线程安全
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

		
	