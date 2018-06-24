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

	3. 原子性-对比

		1. synchronized：不可中断锁，适合竞争不激烈，可读性好
		2. Lock：可中断锁，多样化同步，竞争激烈时能维持常态
		3. Atomic：竞争激烈时能维持常态，比Lock性能好；只能同步一个值