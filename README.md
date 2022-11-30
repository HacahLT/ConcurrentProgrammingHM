# ConcurrentProgrammingHM

并发编程代码

...

## 线程池

线程池知识再深入，并手写一个线程池。池的概念是为了复用资源，避免过度消耗。

![image-20221130150806308](https://raw.githubusercontent.com/HacahLT/ImageShack/main/blogImg2/image-20221130150806308.png)

### 手写线程池实现

分两步，一步是把阻塞队列实现，用于存放执行任务，之后才是线程池内部实现。

大体流程就是线程池可以添加任务，线程池使用线程执行这些任务。

#### 阻塞队列实现

获取存储，相关的超时方法。

#### 线程池实现

需求分析：线程池维护多个线程，执行传入的任务。 能指定线程大小，线程空闲时存活时间。指定满队列后策略。

设计：

* 内部维护Worker池，每一个worker都是执行者。

* 维护一个queue，queue用于存储超过线程数量的任务，等待执行的任务。

* 线程空闲时按照超过时间回收线程。

* 使用策略模式，在创建线程池时传入策略，用于如果线程满了，而且任务队列也满了，执行策略，比如等待有以下策略：

    * ```
    1. 一直等
    2. 带超时等
    3.让调用者放弃任务执行
    4.让调用者抛出异常
    5.让调用者自己执行任务
    ```

代码位置：https://github.com/HacahLT/ConcurrentProgrammingHM/tree/main/src/main/java/com/hacah/app/threadpool







