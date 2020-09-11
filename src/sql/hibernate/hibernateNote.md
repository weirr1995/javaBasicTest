##1.Hibernate基本步骤
- 1.1.引入jar包
- 1.2.编写hibernate.cfg.xml文件
- 1.3.创建持久化类，实现Serializable接口
- 1.4.为持久化类编写.hbm.xml映射文件
- 1.5.中节点引用.hbm.xml文件
- 1.6.Java代码运行

``` java
Configuration conf = new Configuration().configure();
SessionFactory sf = conf.buildSessionFactory();
Session ses = sf.getCurrentSession();
Transaction tx = session.beginTransaction();
ses.save(entity);
tx.commit();//tx.rollback();
ses.close();
```

##2.按主键查询
Session.get(class,Serializable)，如果没有返回null，立即加载
Session.load(class,Serializable)，延迟加载的按主键查询，找不到报异常

主键id生成策略：
- 1 assigned：程序负责生成id值，并赋给持久化对象，
- 2 increment：Hibernate自动从该表该列查找最大值，并且+1
- 3 identity：适合SqlServer、MySQL
- 4 sequence：适合oracle、db2等大型数据库
- 5 native：自动。hibernate根据数据库自行决定采用何种策略
- 6

##3.Session的增、删、改
- save():对象必须没有id，执行insert语句
- saveOrUpdate():对象有id就执行update()，否则执行save()
- update():对象必须有id，执行update语句
- delete()：
- evict()：把持久化对象从session缓存中驱逐出去
- close()：关闭session
- clear()：清空session缓存中的所有对象
- flush()：刷新，把缓存中的瞬时、脏数据都提交到数据库

##4.Hibernate中java对象的三种状态
- 4.1.瞬时状态:与数据库没有关联的对象。比如新创建的实体对象、被delete()后的对象
- 4.2.持久状态:与数据库同步的对象。比如执行get()、load()、save()、update()、saveOrUpdate()后的对象
- 4.3.游离状态:曾持久但已经脱管的对象。比如执行evict()、close()、clear()后的对象

##5.脏检查和刷新缓存机制
- flush()刷新缓存时
- commit()提交事务时

##6.更新数据的方法
+ dynamic-update只更新发生变化的属性值
+ merge():游离对象覆盖掉与Session缓存/数据库中的数据，实现更新；如果传入瞬时对象则copy并insert
    + 有id的游离对象，先select，再update
    + 有id的游离对象（数据库中无此id），先select，再insert

- 1 如果session中存在相同持久化标识(identifier)的实例，用用户给出的对象的状态覆盖旧有的持久实例
- 2 如果session没有相应的持久实例，则尝试从数据库中加载，或copy为持久化实例
- 3 最后返回该持久实例
- 4 用户给出的这个对象没有被关联到session上，它依旧是脱管的

##7.merge与saveOrUpdate区别：merge会返回一个新的持久化对象，传入的仍旧是托管的