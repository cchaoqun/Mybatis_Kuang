# Mybatis

环境:

- JDK1.8
- Mysql5.7 8.0
- maven 3.6.1
- IDEA

回顾:

- JDBC
- Mysql
- Java基础
- Maven
- Junit

官网

```
https://mybatis.org/mybatis-3/zh/index.html
```

## 什么是Mybatis?

- MyBatis 是一款优秀的**持久层框架，**
- 它支持自定义 SQL、存储过程以及高级映射。
- MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
- MyBatis 本是apache的一个开源项目ibatis, 2010年这个项目由apache 迁移到了google code，并且改名为MyBatis 。
- 2013年11月迁移到**Github** .

## 如何获得Mybatis

- maven仓库

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.2</version>
</dependency>
```

- Mybatis官方文档 : http://www.mybatis.org/mybatis-3/zh/index.html
- GitHub : https://github.com/mybatis/mybatis-3

## 持久化

### 数据持久化

- 持久化就是将程序的数据在持久状态和瞬时状态转化的过程
- 内存: 断电即失
- 数据库(jdbc), io文件持久化
- 生活: 冷藏 罐头

### 为什么需要持久化?

- 有一些对象, 不能让他丢掉
- 内存太贵了



## 持久层

- Dao层, Service层, Controller层
  - 完成持久化工作的代码块
  - 层是界限十分明显的

## 为什么需要Mybatis?

- 帮助程序猿将数据存入到数据库中
- 方便
- 传统的JDBC代码复杂, 简化, 框架
- 不用Mybatis也可以, 更容易上手. **技术没有高低之分**
- 优点:
  - 简单易学：本身就很小且简单。没有任何第三方依赖，最简单安装只要两个jar文件+配置几个sql映射文件易于学习，易于使用，通过文档和源代码，可以比较完全的掌握它的设计思路和实现。
  - 灵活：mybatis不会对应用程序或者数据库的现有设计强加任何影响。 sql写在xml里，便于统一管理和优化。通过sql语句可以满足操作数据库的所有需求。
  - 解除sql与程序代码的耦合：通过提供DAO层，将业务逻辑和数据访问逻辑分离，使系统的设计更清晰，更易维护，更易单元测试。sql和代码的分离，提高了可维护性。
  - 提供映射标签，支持对象与数据库的orm字段关系映射
  - 提供对象关系映射标签，支持对象关系组建维护
  - 提供xml标签，支持编写动态sql。
- 最重要的一点: 使用的人多
- Spring SpringMVC SpringBoot

# 第一个Mybatis程序

## 思路:搭建环境-->导入Mybatis-->编写代码-->测试

## 搭建环境

### 搭建数据库

```mysql
CREATE DATABASE `Mybatis`;

USE `Mybatis`;

CREATE TABLE `user`(
	`id` INT(20) PRIMARY KEY NOT NULL,
	`name` VARCHAR(30) DEFAULT NULL,
	`pwd` VARCHAR(30) DEFAULT NULL
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `user` (`id`, `name`, `pwd`) VALUES
(1,'name1', 'pwd1'),
(2,'name2', 'pwd2'),
(3,'name3', 'pwd3');	
```

### 新建项目

1. 新建一个Maven项目

2. 删除src 目录, 让其成为父工程

3. 导入maven依赖

   ```xml
   <!--导入依赖-->
       <dependencies>
           <!--Mysql驱动-->
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>8.0.23</version>
           </dependency>
           <!--Mybatis-->
           <dependency>
               <groupId>org.mybatis</groupId>
               <artifactId>mybatis</artifactId>
               <version>3.5.2</version>
           </dependency>
           <!--Junit-->
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>4.12</version>
           </dependency>
   
       </dependencies>
   ```

   

### 创建一个模块

- 编写Mybatis核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--Configuration核心配置文件-->
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?serverTimezone=UTC&amp;useSSL=true&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="123"/>
            </dataSource>
        </environment>
    </environments>
    <!--每一个Mapper.xml都需要在Mybatis核心配置文件中注册!-->
    <mappers>
        <mapper resource="UserMapper.xml"/>
    </mappers>
</configuration>
```



- 编写Mybatis工具类

```java
//sqlSessionFactory -->sqlSession
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static{
        InputStream inputStream = null;
        try {
            //使用Mybatis第一步: 获取sqlSession对象
            String resource = "mybatis-config.xml";
            inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
    // SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句。例如：

    public static SqlSession getSession(){
        return sqlSessionFactory.openSession();
    }
}
```



### 编写代码

- 实体类

```java
public class User {
    private int id;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

```



- Dao接口

```java
public interface UserDao {
    List<User> getUserList();
}
```



- 接口实现类由原来的UserDaoImpl转变为一个Mapper配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace=绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.kuang.dao.UserDao">
    <!--select查询语句-->
    <select id="getUserList" resultType="com.kuang.pojo.User">
        select * from mybatis.user
    </select>


</mapper>
```



## 测试

### 注意点

MapperRegistry 在核心配置文件中注册mappers

```xml
<!--每一个Mapper.xml都需要在Mybatis核心配置文件中注册!-->
<mappers>
    <mapper resource="UserMapper.xml"/>
</mappers>
```



- junit测试

```java
public class UserDaoTest {

    @Test
    public void getUserList() {
        //获取SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //方式一: getMapper
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserList();

        //方式二
//        List<User> userList = sqlSession.selectList("com.kuang.dao.UserDao.getUserList");
        for(User user:userList){
            System.out.println(user);
        }

        //关闭sqlSession
        sqlSession.close();

    }
}
```

- 可能会遇到的问题

  1. 配置文件没有注册

  2. 绑定接口错误

  3. 方法名不对

  4. 返回类型错误

  5. Maven导出资源问题

     1. ```xml
        <!--在build中配置resource, 来防止我们资源导出失败的问题-->
            <build>
                <resources>
                    <resource>
                        <directory>src/main/java</directory>
                        <includes>
                            <include>**/*.properties</include>
                            <include>**/*.xml</include>
                        </includes>
                        <filtering>false</filtering>
                    </resource>
                    <resource>
                        <directory>src/main/resources</directory>
                        <includes>
                            <include>**/*.properties</include>
                            <include>**/*.xml</include>
                        </includes>
                        <filtering>false</filtering>
                    </resource>
                </resources>
            </build>
        ```



# CRUD

## namespace

namespace中的包名和Dao/Mapper接口的包名一致

## 操作流程

参数解释

- id: 就是对应的namespace中的方法名
- resultType: sql 语句执行的返回值
- parameterType: 参数类型

### 编写接口

```java
public interface UserMapper {
    //查询全部用户
    List<User> getUserList();

    //根据id查询用户
    User getUserById(int id);

    //insert一个用户
    int addUser(User user);

    //update
    int updateUser(User user);

    //delete
    int deleteUserById(int id);
    
}
```



### 编写Mapper中的SQL语句(Select, Update, Add, Delete)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace=绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.kuang.dao.UserMapper">
    <!--select查询语句-->
    <select id="getUserList" resultType="com.kuang.pojo.User">
        select * from mybatis.user
    </select>

    <select id="getUserById" parameterType="int" resultType="com.kuang.pojo.User">
        select * from user where id = #{id}
    </select>

    <!--插入-->
    <!--对象中的属性可以直接取出-->
    <insert id="addUser" parameterType="com.kuang.pojo.User">
        insert into mybatis.user(id,name,pwd) values (#{id}, #{name}, #{pwd});
    </insert>

    <!--更新-->
    <update id="updateUser" parameterType="com.kuang.pojo.User">
        update mybatis.user set `name`=#{name}, `pwd`=#{pwd} where `id`=#{id};
    </update>

    <!--删除-->
    <delete id="deleteUserById" parameterType="int">
        delete from mybatis.user where `id`=#{id};
    </delete>

</mapper>
```



### 测试

注意点:

- 增删改需要提交事务

```java
public class UserMapperTest {
    @Test
    public void getUserList() {
        //获取SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //方式一: getMapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();
        //方式二
//        List<User> userList = sqlSession.selectList("com.kuang.dao.UserDao.getUserList");
        for(User user:userList){
            System.out.println(user);
        }
        //关闭sqlSession
        sqlSession.close();
    }
    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById(2);
        System.out.println(user);

        sqlSession.close();
    }

    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.addUser(new User(6,"name4", "pwd4"));
        if(res>0){
            System.out.println("插入成功");
        }
        //提交事务
        sqlSession.commit();

        sqlSession.close();
    }

    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.updateUser(new User(5, "name555", "pwd555"));
        if(res>0){
            System.out.println("更新成功");
        }
        sqlSession.commit();


        sqlSession.close();
    }

    @Test
    public void deleteUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.deleteUserById(5);
        if(res>0){
            System.out.println("删除成功");
        }
        sqlSession.commit();
        sqlSession.close();
    }
}
```



## 分析错误

- 标签不要匹配错
- resource绑定mapper需要使用路径(com/kuang/...)
- 程序配置文件必须符合规范
- NullPointerException没有注册到资源
- maven资源没有导出



## 万能Map

假设我们的实体类, 或者数据库中的表, 字段或者参数过多, 我们应当考虑使用Map

```java
//万能Map
    int addUser2(Map<String, Object> map);
```

```xml
<!--map
    传递map的key
    -->
    <insert id="addUser2" parameterType="map">
        insert into mybatis.user(id,name,pwd) values (#{userId}, #{userName}, #{userPwd});
    </insert>
```

```java
@Test
public void addUser2(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();

    HashMap<String, Object> map = new HashMap<>();
    map.put("userId",5);
    map.put("userName","name555");
    map.put("userPwd","pwd555");
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    mapper.addUser2(map);

    sqlSession.commit();
    sqlSession.close();

}
```

- Map传递参数, 直接在SQL中取出key即可          [parameterType="map"]
- 对象传递参数, 直接在SQL中取出对象属性即可  [parameterType="map"]
- 只有一个基本类型参数的情况下, 可以直接在SQL中取到
- 多个参数用Map, 或者注解

## 思考题

### 模糊查询?

1. java代码执行的时候, 传递通配符%%

   1. ```java
      List<User> userList = mapper.getUserLike("%李%");
      ```

      

2. 在sql拼接中使用通配符

   1. ```xml
      select * from mybatis.user where name like "%"#{value}"%";
      ```



# 配置解析

## 核心配置文件

- mybatis-config.xml

- MyBatis 的配置文件包含了会深深影响 MyBatis 行为的设置和属性信息。

  ```
  configuration（配置）
  properties（属性）
  settings（设置）
  typeAliases（类型别名）
  typeHandlers（类型处理器）
  objectFactory（对象工厂）
  plugins（插件）
  environments（环境配置）
  environment（环境变量）
  transactionManager（事务管理器）
  dataSource（数据源）
  databaseIdProvider（数据库厂商标识）
  mappers（映射器）
  <!-- 注意元素节点的顺序！顺序不对会报错 -->
  ```



## 环境配置(environments)

### environments元素

```xml
<environments default="development">
 <environment id="development">
   <transactionManager type="JDBC">
     <property name="..." value="..."/>
   </transactionManager>
   <dataSource type="POOLED">
     <property name="driver" value="${driver}"/>
     <property name="url" value="${url}"/>
     <property name="username" value="${username}"/>
     <property name="password" value="${password}"/>
   </dataSource>
 </environment>
</environments>
```

- 配置MyBatis的多套运行环境，将SQL映射到多个不同的数据库上，必须指定其中一个为默认运行环境（通过default指定）

- 子元素节点：**environment**

- - dataSource 元素使用标准的 JDBC 数据源接口来配置 JDBC 连接对象的资源。

  - 数据源是必须配置的。

  - 有三种内建的数据源类型

    ```
    type="[UNPOOLED|POOLED|JNDI]"）
    ```

  - unpooled：这个数据源的实现只是每次被请求时打开和关闭连接。

  - **pooled**：这种数据源的实现利用“池”的概念将 JDBC 连接对象组织起来 , 这是一种使得并发 Web 应用快速响应请求的流行处理方式。

  - jndi：这个数据源的实现是为了能在如 Spring 或应用服务器这类容器中使用，容器可以集中或在外部配置数据源，然后放置一个 JNDI 上下文的引用。

  - 数据源也有很多第三方的实现，比如dbcp，c3p0，druid等等....

## 属性(properties)

### 配置文件的属性标签必须按照顺序来写

![image-20210524161611634](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524161611634.png)

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。

### 编写一个配置文件

db.properties

```xml
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?serverTimezone=UTC&useSSL=true&useUnicode=true&characterEncoding=UTF-8
username=root
password=123
```

### 在核心配置文件中引入

```xml
<!--引入外部配置文件-->
<properties resource="db.properties">
    <property name="username" value="root"/>
    <property name="password" value="1231"/>
</properties>
```

- 可以直接引入外部文件
- 可以在其中增加一些属性
- 如果properties标签中的property标签与外部文件中的字段相同, 优先使用外部配置文件的值



## 类型别名(TypeAlias)

### 方式一

- 类型别名是为 Java 类型设置一个短的名字。
- 它只和 XML 配置有关，存在的意义仅在于用来减少类完全限定名的冗余。

```xml
<!--可以给实体类起别名-->
    <typeAliases>
        <typeAlias type="com.kuang.pojo.User" alias="User"/>
    </typeAliases>
```

### 方式二

- 也可以指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean，比如：
- 扫描实体类的包, 他的默认别名就是这个类的类名, 首字母小写

```xml
<typeAliases>
    <package name="com.kuang.pojo"/>
</typeAliases>
```

### 总结

- 实体类较少, 使用第一种

- 实体类较多, 使用第二种

- 第一种可以自定义别名, 第二种不行, 如果必须改, 必须在实体类上增加注解

  - ```java
    @Alias("user")
    public class User {
    ```



## 设置

这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。 下表描述了设置中各项设置的含义、默认值等。

![image-20210524164451961](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524164451961.png)

![image-20210524164435802](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524164435802.png)

## 其他配置

- [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)
- [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)
- [plugins（插件）]



## 映射器(mapper)

MapRegistry: 注册绑定我们的Mapper文件

### 方式一

```xml
<!--每一个Mapper.xml都需要在Mybatis核心配置文件中注册!-->
<mappers>
    <mapper resource="com/kuang/dao/UserMapper.xml"/>
</mappers>
```

### 方式二: 使用class文件绑定注册

```xml
<mappers>
    <mapper class="com.kuang.dao.UserMapper"/>
</mappers>
```

#### 注意点

- 接口和他的Mapper配置文件必须同名
- 接口和他的Mapper配置文件必须在同一个包下

### 方式三:使用扫描包进行注入绑定

```xml
<mappers>
    <package name="com.kuang.dao"/>
</mappers>
```

#### 注意点

- 接口和他的Mapper配置文件必须同名
- 接口和他的Mapper配置文件必须在同一个包下

### 练习

- 将数据库配置文件外部引入
- 实体类别名
- 保证UserMapper接口和UserMapper.xml放在同名包下



## 声明周期和作用域

作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的并发问题。

<img src="C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524170056728.png" alt="image-20210524170056728" style="zoom:50%;" />

### **SqlSessionFacoryBuilder**

- 这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了
- 局部变量

### SqlSessionFacory

- 可以类比于数据库连接池
- SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。
-  SqlSessionFactory 的最佳作用域是应用作用域
- 最简单的就是使用单例模式或者静态单例模式

### SqlSession

- 连接到连接池的一个请求

- 每个线程都应该有它自己的 SqlSession 实例。
- SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域
- 用完之后需要赶紧关闭, 否则资源被占用

<img src="C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524170416342.png" alt="image-20210524170416342" style="zoom:50%;" />

- 这里面的每一个Mapper都代表一个具体的业务



# 解决属性名和字段名不一致的问题

## 数据库字段

![image-20210524170531664](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524170531664.png)

## 拷贝之前项目新建,测试实体类字段不一致的情况

```java
public class User {
    private int id;
    private String name;
    private String password;
```

## 测试出现问题

![image-20210524171324008](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524171324008.png)

```xml
select * from mybatis.user where id = #{id}
select id,name,pwd from mybatis.user where id=#{id}
```

## 解决方法

### 起别名

```xml
<select id="getUserById" parameterType="int" resultType="com.kuang.pojo.User">
    select id,name,pwd as password from mybatis.user where id=#{id}
</select>
```

### ResultMap

结果集映射

```
数据库:	id	name	pwd
实体类:	id	name	password
```

```xml
<!--结果集映射-->
<resultMap id="UserMap" type="User">
<!--column数据库中的字段名, property实体类的属性名-->
<result column="id" property="id"/>
<result column="name" property="name"/>
<result column="pwd" property="password"/>
</resultMap>

<select id="getUserById" parameterType="int" resultMap="UserMap">
select * from mybatis.user where id = #{id}
</select>
```

- `resultMap` 元素是 MyBatis 中最重要最强大的元素。
- ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。



# 日志

## 日志工厂

- 如果一个数据库操作, 出现了异常, 我们需要排错, 日志是最好的助手
- 曾经: syso Debug
- 现在:日志工厂

![image-20210524180016536](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524180016536.png)

- SLF4J
- LOG4J [掌握]
- LOG4J2
- JDK_LOGGING
- COMMONS_LOGGING
- STDOUT_LOGGING [掌握]
- NO_LOGGING

### **标准日志实现**

指定 MyBatis 应该使用哪个日志记录实现。如果此设置不存在，则会自动发现日志记录实现。

```xml
<settings>
       <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```

测试，可以看到控制台有大量的输出！我们可以通过这些输出来判断程序到底哪里出了Bug

![image-20210524180523165](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524180523165.png)



## LOG4J

### 什么是LOG4J

- Log4j是Apache的一个开源项目
- 通过使用Log4j，我们可以控制日志信息输送的目的地：控制台，文本，GUI组件....
- 我们也可以控制每一条日志的输出格式；
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。

1. 先导入LOG4J的包

   ```xml
   <dependency>
       <groupId>log4j</groupId>
       <artifactId>log4j</artifactId>
       <version>1.2.17</version>
   </dependency>
   ```

   

2. log4j.properties

   ```xml
   #将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
   log4j.rootLogger=DEBUG,console,file
   
   #控制台输出的相关设置
   log4j.appender.console = org.apache.log4j.ConsoleAppender
   log4j.appender.console.Target = System.out
   log4j.appender.console.Threshold=DEBUG
   log4j.appender.console.layout = org.apache.log4j.PatternLayout
   log4j.appender.console.layout.ConversionPattern=[%c]-%m%n
   
   #文件输出的相关设置
   log4j.appender.file = org.apache.log4j.RollingFileAppender
   log4j.appender.file.File=./log/kuang.log
   log4j.appender.file.MaxFileSize=10mb
   log4j.appender.file.Threshold=DEBUG
   log4j.appender.file.layout=org.apache.log4j.PatternLayout
   log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n
   
   #日志输出级别
   log4j.logger.org.mybatis=DEBUG
   log4j.logger.java.sql=DEBUG
   log4j.logger.java.sql.Statement=DEBUG
   log4j.logger.java.sql.ResultSet=DEBUG
   log4j.logger.java.sql.PreparedStatement=DEBUG
   ```

   

3. 配置log4j为日志的实现

   ```xml
   <settings>
   	<setting name="logImpl" value="LOG4J"/>
   </settings>
   ```

4. Log4j的使用, 运行之前的查询

   1. ![image-20210524181824828](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524181824828.png)

### 简单使用

1. 在要使用Log4j的类中, 导入包 

   1. ```
      import org.apache.log4j.Logger;
      ```

2. 生成日志对象, 参数为当前类的class

   1. ```java
      static Logger logger = Logger.getLogger(UserMapperTest.class);
      ```

3. 日志级别

   1. ```java
      @Test
      public void testLog4J(){
          logger.info("info:进入了testLog4J");
          logger.debug("debug:进入了testLog4J");
          logger.error("error:进入了testLog4J");
      }
      ```



# 分页

## 思考,为什么要分页?

- 减少数据的处理量

## 使用Limit分页

```sql
Select * from user limit startIndex, pageSize;
Select * from user limit n; #[0,n]
```



## 使用Mybatis实现分页

1. 接口

   ```java
   //分页
   List<User> getUserByLimit(Map<String, Integer> map);
   ```

2. Mapper.xml

   ```xml
   <select id="getUserByLimit" parameterType="map" resultType="User" resultMap="UserMap">
       select * from mybatis.user limit #{startIndex}, #{pageSize};
   </select>
   ```

3. 测试

   ```java
   @Test
   public void getUserByLimit(){
       SqlSession sqlSession = MybatisUtils.getSqlSession();
   
       HashMap<String, Integer> map = new HashMap<String, Integer>();
       map.put("startIndex",1);
       map.put("pageSize",2);
   
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       List<User> userList = mapper.getUserByLimit(map);
   
       System.out.println(userList);
   
   
       sqlSession.close();
   }
   ```



## RowBounds分页

1. 不再使用SQL实现分页

   1. 接口

      ```java
      //分页2
      List<User> getUserByRowBounds();
      ```

   2. mapper.xml

      ```xml
      <!--分页2-->
      <select id="getUserByRowBounds" resultMap="UserMap">
          select * from mybatis.user;
      </select>
      ```

   3. 测试

      ```java
      @Test
      public void getUserByRowBounds(){
          SqlSession sqlSession = MybatisUtils.getSqlSession();
      
          //RowBounds实现
          RowBounds rowBounds = new RowBounds(1, 2);
      
          //通过java代码层面实现分页
          List<User> userList = sqlSession.selectList("com.kuang.dao.UserMapper.getUserByRowBounds", null, rowBounds);
          for(User user:userList){
          System.out.println(user);
          }
          sqlSession.close();
      }
      ```

## 分页插件

![image-20210524202305859](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524202305859.png)

# 使用注解开发

## 面向接口编程

- 大家之前都学过面向对象编程，也学习过接口，但在真正的开发中，很多时候我们会选择面向接口编程
- **根本原因 :  解耦 , 可拓展 , 提高复用 , 分层开发中 , 上层不用管具体的实现 , 大家都遵守共同的标准 , 使得开发变得容易 , 规范性更好**
- 在一个面向对象的系统中，系统的各种功能是由许许多多的不同对象协作完成的。在这种情况下，各个对象内部是如何实现自己的,对系统设计人员来讲就不那么重要了；
- 而各个对象之间的协作关系则成为系统设计的关键。小到不同类之间的通信，大到各模块之间的交互，在系统设计之初都是要着重考虑的，这也是系统设计的主要工作内容。面向接口编程就是指按照这种思想来编程。



### **关于接口的理解**

- 接口从更深层次的理解，应是定义（规范，约束）与实现（名实分离的原则）的分离。

- 接口的本身反映了系统设计人员对系统的抽象理解。

- 接口应有两类：

- - 第一类是对一个个体的抽象，它可对应为一个抽象体(abstract class)；
  - 第二类是对一个个体某一方面的抽象，即形成一个抽象面（interface）；

- 一个体有可能有多个抽象面。抽象体与抽象面是有区别的。



### **三个面向区别**

- 面向对象是指，我们考虑问题时，以对象为单位，考虑它的属性及方法 .
- 面向过程是指，我们考虑问题时，以一个具体的流程（事务过程）为单位，考虑它的实现 .
- 接口设计与非接口设计是针对复用技术而言的，与面向对象（过程）不是一个问题.更多的体现就是对系统整体的架构



## 使用注解开发

1. 注解在接口上实现

   ```java
   @Select("select * from user;")
   List<User> getUsers();
   ```

2. 需要在核心配置文件中绑定接口

   ```xml
   <!--绑定接口-->
   <mappers>
       <mapper class="com.kuang.dao.UserMapper"/>
   </mappers>
   ```

3. 测试

   ```java
   @Test
   public void getUsers(){
       SqlSession sqlSession = MybatisUtils.getSqlSession();
   
       //底层主要应用反射
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       List<User> users = mapper.getUsers();
   
       System.out.println(users);
   
       sqlSession.close();
   
   
   }
   ```

   

- 本质: 反射机制实现
- 底层: 动态代理

## Mybatis详细执行流程

![image-20210524204813922](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524204813922.png)







## CRUD

1. 我们可以在工具类创建的时候实现自动提交事务

   ```java
   public static SqlSession getSqlSession(){
       //true 代表自动提交事务 aucommit=true
       return sqlSessionFactory.openSession(true);
   }
   ```

2. 编写接口, 增加注解

   ```java
   public interface UserMapper {
       //查询所有
       @Select("select * from user;")
       List<User> getUsers();
   
       //根据id查询
       //方法存在多个参数,所有的参数都需加上 @Param("")
       @Select("select * from user where id=#{id};")
       User getUserById(@Param("id") int id, @Param("name") String name);
   
   
       //添加
       @Insert("insert into user(id,name,pwd) values(#{id}, #{name}, #{password})")
       int addUser(User user);
   
       //更新
       @Update("update user set name=#{name},pwd=#{password} where id=#{id};")
       int updateUser(User user);
   
       //删除
       @Delete("delete from user where id = #{uid};")
       int deleteUser(@Param("uid")int id);
   }
   ```

   

3. 测试类

4. 注意: 必须要将接口注册到核心配置文件

   ```xml
   <!--绑定接口-->
   <mappers>
       <mapper class="com.kuang.dao.UserMapper"/>
   </mappers>
   ```

   

## 关于@Param()注解

- 基本类型的参数或者String类型, 需要加上
- 引用类型不需要加
- 如果只有一个基本类型的话, 可以忽略, 但是建议加上
- 我们在SQL中引用的就是我们这里的@Param()照片那个设定的属性名



# Lombok

```java
Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.
Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.
```

- java library
- plugs
- build tools
- with one annotation

## 使用步骤

1. 在IDEA安装Lombok插件

2. 在项目中带入lombok的jar包

   ```
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <version>1.18.10</version>
   </dependency>
   ```

3. 注释解析

   ```
   @Getter and @Setter
   @FieldNameConstants
   @ToString
   @EqualsAndHashCode
   @AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
   @Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
   @Data
   @Builder
   @SuperBuilder
   @Singular
   @Delegate
   @Value
   @Accessors
   @Wither
   @With
   @SneakyThrows
   @val
   @var
   experimental @var
   @UtilityClass
   Lombok config system
   ```

   说明:

   ```
   @Data: 无参构造, get set  toString hashcode equals
   @AllArgsConstructor
   @NoArgsConstructor
   @EqualsAndHashCode
   @ToString
   Getter
   ```

   

# 多对一的处理

1. 示例

   - 多个学生, 对应一个老师
   - 对于学生这边, **关联**, 多个学生, 关联一个老师 [多对一]
   - 对于老师而言, **集合**, 一个老师有很多学生[一对多]

2. sql

   ```sql
   CREATE TABLE `teacher` (
     `id` INT(10) NOT NULL,
     `name` VARCHAR(30) DEFAULT NULL,
     PRIMARY KEY (`id`)
   ) ENGINE=INNODB DEFAULT CHARSET=utf8
   
   INSERT INTO teacher(`id`, `name`) VALUES (1, '秦老师'); 
   
   CREATE TABLE `student` (
     `id` INT(10) NOT NULL,
     `name` VARCHAR(30) DEFAULT NULL,
     `tid` INT(10) DEFAULT NULL,
     PRIMARY KEY (`id`),
     KEY `fktid` (`tid`),
     CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
   ) ENGINE=INNODB DEFAULT CHARSET=utf8
   
   INSERT INTO `student` (`id`, `name`, `tid`) VALUES (1, '小明', 1); 
   INSERT INTO `student` (`id`, `name`, `tid`) VALUES (2, '小红', 1); 
   INSERT INTO `student` (`id`, `name`, `tid`) VALUES (3, '小张', 1); 
   INSERT INTO `student` (`id`, `name`, `tid`) VALUES (4, '小李', 1); 
   INSERT INTO `student` (`id`, `name`, `tid`) VALUES (5, '小王', 1);
   ```

   ![image-20210524235822371](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210524235822371.png)



## 测试环境搭建

1. 导入lombok

   ```
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <version>1.18.10</version>
   </dependency>
   ```

2. 新建实体类Teacher Student

   ```java
   @Data
   public class Teacher {
       private int id;
       private String name;
   }
   
   @Data
   public class Student {
       private int id;
       private String name;
       //学生需要关联一个学生
       private Teacher taecher;
   }
   ```

   

3. 新建Mapper接口

   ![image-20210525002759149](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210525002759149.png)

4. 建立Mapper.xml文件

   ![image-20210525002736050](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210525002736050.png)

5. 在核心配置文件中绑定注册我们的Mapper接口或者文件[方式很多]

   ```xml
   <mappers>
       <mapper class="com.kuang.dao.TeacherMapper"/>
       <mapper class="com.kuang.dao.StudentMapper"/>
   </mappers>
   ```

   

6. 测试查询是否能够成功

   ```java
   @Test
   public void getTeacher(){
       SqlSession sqlSession = MybatisUtils.getSqlSession();
       TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
       Teacher teacher = mapper.getTeacher(1);
       System.out.println(teacher);
       sqlSession.close();
   }
   ```



## 按查询嵌套处理

```xml
<!--
    需求：获取所有学生及对应老师的信息
    思路：
     1. 获取所有学生的信息
     2. 根据获取的学生信息的老师ID->获取该老师的信息
     3. 思考问题，这样学生的结果集中应该包含老师，该如何处理呢，数据库中我们一般使用关联查询？
         1. 做一个结果集映射：StudentTeacher
         2. StudentTeacher结果集的类型为 Student
         3. 学生中老师的属性为teacher，对应数据库中为tid。
            多个 [1,...）学生关联一个老师=> 一对一，一对多
         4. 查看官网找到：association – 一个复杂类型的关联；使用它来处理关联查询
     -->
<select id="getStudent" resultMap="StudentTeacher">
    select * from student;
</select>
<resultMap id="StudentTeacher" type="Student">

    <!--association关联属性 property属性名 javaType属性类型 column在多的一方的表中的列名-->
    <association property="taecher" column="tid" javaType="Teacher" select="getTeacher"/>
</resultMap>
<!--
    这里传递过来的id，只有一个属性的时候，下面可以写任何值
    association中column多参数配置：
       column="{key=value,key=value}"
       其实就是键值对的形式，key是传给下个sql的取值名称，value是片段一中sql查询的字段名。
    -->
<select id="getTeacher" resultType="Teacher">
    select * from teacher where id =#{id};
</select>
```

## 按照结果嵌套处理

```xml
<!--
按查询结果嵌套处理
思路：
   1. 直接查询出结果，进行结果集的映射
-->
<select id="getStudent2" resultMap="StudentTeacher2">
	select s.id sid, s.name sname, t.name tname
	from student s, teacher t
	where s.tid = t.id;
</select>
<resultMap id="StudentTeacher2" type="Student">
	<result property="id" column="sid"/>
	<result property="name" column="sname"/>
    <!--关联对象property 关联对象在Student实体类中的属性-->
	<association property="teacher" javaType="Teacher">
		<result property="name" column="tname"/>
	</association>
</resultMap>
```



## 回顾MySql 多对一查询方式

- 子查询
- 联表查询



# 一对多处理

一对多的理解：

- 一个老师拥有多个学生
- 如果对于老师这边，就是一个一对多的现象，即从一个老师下面拥有一群学生（集合）！

## 环境搭建

1. 环境搭建 和刚才一样

2. 实体类

   ```java
   @Data
   public class Student {
       private int id;
       private String name;
       //学生需要关联一个学生
       private int tid;
   }
   @Data
   public class Teacher {
       private int id;
       private String name;
       private List<Student> students;
   }
   ```

## 按照结果嵌套处理

```xml
<!--按结果嵌套查询
   思路:
       1. 从学生表和老师表中查出学生id，学生姓名，老师姓名
       2. 对查询出来的操作做结果集映射
           1. 集合的话，使用collection！
               JavaType和ofType都是用来指定对象类型的
               JavaType是用来指定pojo中属性的类型
               ofType指定的是映射到list集合属性中pojo的类型。
   -->
<select id="getTeacher" resultMap="TeacherStudent">
    select s.id sid, s.name sname, t.name tname, t.id tid
    from student s, teacher t
    where s.tid = t.id and t.id = #{tid}
</select>
<resultMap id="TeacherStudent" type="Teacher">
    <result property="id" column="tid"/>
    <result property="name" column="tname"/>
    <!--复杂的属性, 我们需要单独处理, 对象association 集合collection
        javaType = "" 指向属性的类型
        集合中的泛型信息, 我们使用ofType获取
        -->
    <collection property="students" ofType="Student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
        <result property="tid" column="tid"/>
    </collection>
</resultMap>
```







## 按照查询嵌套处理

```xml
<!--按照查询嵌套处理-->
<select id="getTeacher2" resultMap="TeacherStudent2">
    select * from teacher where id = #{tid}
</select>
<resultMap id="TeacherStudent2" type="Teacher">
    <collection property="students" javaType="ArrayList" ofType="Student" column="id" select="getStudentByTeacherId"/>
</resultMap>

<select id="getStudentByTeacherId" resultType="Student">
    select * from student where tid = #{tid};
</select>
```



## 小结

1. 关联-association
2. 集合-collection
3. 所以association是用于一对一和多对一，而collection是用于一对多的关系
4. JavaType和ofType都是用来指定对象类型的
   1. JavaType是用来指定pojo中属性的类型
   2. ofType指定的是映射到list集合属性中pojo的类型。

## **注意说明：**

1、保证SQL的可读性，尽量通俗易懂

2、根据实际要求，尽量编写性能更高的SQL语句

3、注意属性名和字段不一致的问题

4、注意一对多和多对一 中：字段和属性对应的问题

5、尽量使用Log4j，通过日志来查看自己的错误





# 动态SQL

**动态SQL指的是根据不同的查询条件 , 生成不同的Sql语句.**

```
官网描述：
MyBatis 的强大特性之一便是它的动态 SQL。如果你有使用 JDBC 或其它类似框架的经验，你就能体会到根据不同条件拼接 SQL 语句的痛苦。例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL 这一特性可以彻底摆脱这种痛苦。
虽然在以前使用动态 SQL 并非一件易事，但正是 MyBatis 提供了可以被用在任意 SQL 映射语句中的强大的动态 SQL 语言得以改进这种情形。
动态 SQL 元素和 JSTL 或基于类似 XML 的文本处理器相似。在 MyBatis 之前的版本中，有很多元素需要花时间了解。MyBatis 3 大大精简了元素种类，现在只需学习原来一半的元素便可。MyBatis 采用功能强大的基于 OGNL 的表达式来淘汰其它大部分元素。

  - if
  - choose (when, otherwise)
  - trim (where, set)
  - foreach
```

## 搭建环境

### sql

```sql
CREATE TABLE `blog` (
`id` varchar(50) NOT NULL COMMENT '博客id',
`title` varchar(100) NOT NULL COMMENT '博客标题',
`author` varchar(30) NOT NULL COMMENT '博客作者',
`create_time` datetime NOT NULL COMMENT '创建时间',
`views` int(30) NOT NULL COMMENT '浏览量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

### 创建新的基础工程

![image-20210525164059165](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210525164059165.png)



### IDUtils工具类

```javA
public class IDUtils {
    //生成随机的id
    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    @Test
    public void testGetId(){
        System.out.println(getId());
        System.out.println(getId());
        System.out.println(getId());
    }
}
```

### 实体类

```java
public class Blog {
   private String id;
   private String title;
   private String author;
   private Date createTime;
   private int views;
   //set，get....
}
```

### 编写Mapper接口及xml文件

```java
public interface BlogMapper {}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kuang.dao.BlogMapper">

    <insert id="addBlog" parameterType="blog">
        insert into mybatis.blog(id,title,author, create_time,views)
        values(#{id},#{title},#{author},#{createTime},#{views});
    </insert>


</mapper>
```

### mybatis核心配置文件，下划线驼峰自动转换

```xml
<settings>
    <!--标准的日志工厂的实现-->
    <setting name="logImpl" value="STDOUT_LOGGING"/>
    <!--是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。-->
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>

<mappers>
    <mapper resource="com/kuang/dao/BlogMapper.xml"/>
</mappers>
```



### 插入初始数据

1. 编写接口

   ```java
   //新增一个博客
   int addBlog(Blog blog);
   ```

2. sql配置文件

   ```xml
   <insert id="addBlog" parameterType="blog">
     insert into blog (id, title, author, create_time, views)
     values (#{id},#{title},#{author},#{createTime},#{views});
   </insert>
   ```

3. 初始化博客方法

   ```java
    @Test
   public void addBlogTest() {
       SqlSession sqlSession = MybatisUtils.getSqlSession();
       BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
       Blog blog = new Blog();
       blog.setId(IDUtils.getId());
       blog.setTitle("Mybatis");
       blog.setAuthor("狂神说");
       blog.setCreateTime(new Date());
       blog.setViews(9999);
   
       mapper.addBlog(blog);
   
       blog.setId(IDUtils.getId());
       blog.setTitle("Java");
       mapper.addBlog(blog);
   
       blog.setId(IDUtils.getId());
       blog.setTitle("Spring");
       mapper.addBlog(blog);
   
       blog.setId(IDUtils.getId());
       blog.setTitle("微服务");
       mapper.addBlog(blog);
   
       sqlSession.close();
   
   }
   ```

   

![image-20210525161935164](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210525161935164.png)



## if

```xml
<select id="queryBlogIF" parameterType="map" resultType="blog">
    select * from blog where 1=1
    <if test="title != null">
        and title = #{title}
    </if>
    <if test="author != null">
        and author = #{author}
    </if>
</select>
```

## choose (when, otherwise)

有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

```xml
<select id="queryBlogChoose" parameterType="map" resultType="blog">
    select * from blog
    <where>
        <choose>
            <when test="title!=null">
                title = #{title}
            </when>
            <when test="author!=null">
                author = #{author}
            </when>
            <otherwise>
                views = #{views}
            </otherwise>
        </choose>
    </where>
</select>
```



## where

```xml
<!--IF 查询-->
<select id="queryBlogIF" parameterType="map" resultType="blog">
    select * from blog
    <where>
        <if test="title != null">
            title = #{title}
        </if>
        <if test="author != null">
            and author = #{author}
        </if>
    </where>
</select>
```



## trim Set

```xml
<!--Set更新-->
<update id="updateBlog" parameterType="map" >
    update blog
    <set>
        <if test="title != null">
            title = #{title},
        </if>
        <if test="author != null">
            author = #{author}
        </if>
    </set>
    where id = #{id};
</update>
```



## SQL片段

有时候可能某个 sql 语句我们用的特别多，为了增加代码的重用性，简化代码，我们需要将这些代码抽取出来，然后使用时直接调用。

1. 使用sql标签抽取公共的部分

   ```xml
   <!--sql片段-->
   <sql id="if-title-author">
       <if test="title != null">
           title = #{title}
       </if>
       <if test="author != null">
           and author = #{author}
       </if>
   </sql>
   ```

2. 在需要使用的地方使用include标签引用即可

   ```xml
   <!--IF 查询-->
   <select id="queryBlogIF" parameterType="map" resultType="blog">
       select * from blog
       <where>
           <include refid="if-title-author"/>
       </where>
   </select>
   ```

3. 注意:

   - 最好基于单表来定义SQL片段
   - 不要存在where标签



## foreach

*foreach* 元素的功能非常强大，它允许你指定一个集合，声明可以在元素体内使用的集合项（item）和索引（index）变量。它也允许你指定开头与结尾的字符串以及集合项迭代之间的分隔符。这个元素也不会错误地添加多余的分隔符，看它多智能！

**提示** 你可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象作为集合参数传递给 *foreach*。当使用可迭代对象或者数组时，index 是当前迭代的序号，item 的值是本次迭代获取到的元素。当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。

将数据库中前三个数据的id修改为1,2,3；

需求：我们需要查询 blog 表中 id 分别为1,2,3的博客信息

![image-20210525180318385](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210525180318385.png)

1. 编写接口

   ```java
   List<Blog> queryBlogForeach(Map map);
   ```

2. 编写SQL语句

   ```xml
    <!--foreach 查询-->
   <select id="queryBlogForeach" parameterType="map" resultType="blog">
       select * from blog
       <where>
           <!--
          collection:指定输入对象中的集合属性
          item:每次遍历生成的对象
          open:开始遍历时的拼接字符串
          close:结束时拼接的字符串
          separator:遍历对象之间需要拼接的字符串
          select * from blog where 1=1 and (id=1 or id=2 or id=3)
        	-->
           <foreach collection="ids" item="id" open="and (" separator="or" close=")">
               id = #{id}
           </foreach>
       </where>
   </select>
   ```

3. 测试

   ```java
   @Test
   public void testQueryBlogForeach(){
      SqlSession session = MybatisUtils.getSession();
      BlogMapper mapper = session.getMapper(BlogMapper.class);
   
      HashMap map = new HashMap();
      List<Integer> ids = new ArrayList<Integer>();
      ids.add(1);
      ids.add(2);
      ids.add(3);
      map.put("ids",ids);
   
      List<Blog> blogs = mapper.queryBlogForeach(map);
   
      System.out.println(blogs);
   
      session.close();
   }
   ```



## 动态SQL 总结

==其实动态 sql 语句的编写往往就是一个拼接的问题，为了保证拼接准确，我们最好首先要写原生的 sql 语句出来，然后在通过 mybatis 动态sql 对照着改，防止出错。多在实践中使用才是熟练掌握它的技巧。==



# 缓存

## 简介

1. 什么是缓存 [ Cache ]？
   - 存在内存中的临时数据。
   - 将用户经常查询的数据放在缓存（内存）中，用户去查询数据就不用从磁盘上(关系型数据库数据文件)查询，从缓存中查询，从而提高查询效率，解决了高并发系统的性能问题。\
2. 为什么使用缓存？
   - 减少和数据库的交互次数，减少系统开销，提高系统效率。
3. 什么样的数据能使用缓存？
   - 经常查询并且不经常改变的数据。[可以使用缓存]



## Mybatis缓存

- MyBatis包含一个非常强大的查询缓存特性，它可以非常方便地定制和配置缓存。缓存可以极大的提升查询效率。

- MyBatis系统中默认定义了两级缓存：**一级缓存**和**二级缓存**

- - 默认情况下，只有一级缓存开启。（SqlSession级别的缓存，也称为本地缓存）
  - 二级缓存需要手动开启和配置，他是基于namespace级别的缓存。
  - 为了提高扩展性，MyBatis定义了缓存接口Cache。我们可以通过实现Cache接口来自定义二级缓存



## 一级缓存

一级缓存也叫本地缓存：

- 与数据库同一次会话期间查询到的数据会放在本地缓存中。
- 以后如果需要获取相同的数据，直接从缓存中拿，没必须再去查询数据库；

1. 测试
   1. 开启日志
   2. 测试在一个Session中查询两次相同的记录
   3. 查看日志输出
   4. ![image-20210525184630416](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210525184630416.png)

### 缓存失效

1. 查询不同的东西
2. 增删改操作, 可能会改变原来的数据 所以必定会刷新缓存
   1. ![image-20210525185449734](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210525185449734.png)
3. 查询不同的Mapper.xml
4. 手动清理缓存 

### 小结

一级缓存默认开启, 只有在一次sqlSession中有效, 也就是拿到连接到关闭连接这个时间段

一级缓存就是于一个Map

## 二级缓存

- 二级缓存也叫全局缓存，一级缓存作用域太低了，所以诞生了二级缓存

- 基于namespace级别的缓存，一个名称空间，对应一个二级缓存；

- 工作机制

- - 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中；
  - 如果当前会话关闭了，这个会话对应的一级缓存就没了；但是我们想要的是，会话关闭了，一级缓存中的数据被保存到二级缓存中；
  - 新的会话查询信息，就可以从二级缓存中获取内容；
  - 不同的mapper查出的数据会放在自己对应的缓存（map）中



### 步骤

1. 开启全局缓存

   ```xml
   <!--显示开启全局缓存-->
   <setting name="cacheEnabled" value="true"/>
   ```

2. 在要使用二级缓存中的Mapper中开启

   ```
   <!--在当前Mapper.xml中使用二级缓存-->
   可以无参
   <cache/>
   也可以自定义参数
   <cache 	eviction="FIFO"
           flushInterval="60000"
           size="512"
           readOnly="true"/>
   ```

3. 测试

   1. 问题: 我们需要将实体类序列化!否则会报错

   2. ```
      Caused by: java.io.NotSerializableException: com.kuang.pojo.User
      ```

### 小结

- 只要开启了二级缓存，我们在同一个Mapper中的查询，可以在二级缓存中拿到数据
- 查出的数据都会被默认先放在一级缓存中
- 只有会话提交或者关闭以后，一级缓存中的数据才会转到二级缓存中

## 缓存原理

1. 先查看二级缓存 找到直接返回
2. 二级缓存没找到 继续找一级缓存
3. 一级缓存没有 数据库查询

![image-20210525193803448](C:\Users\Chaoq\AppData\Roaming\Typora\typora-user-images\image-20210525193803448.png)



## 自定义缓存-ehcache

Ehcache是一种广泛使用的java分布式缓存，用于通用缓存；

要在应用程序中使用Ehcache，需要引入依赖的jar包

```xml
<!-- https://mvnrepository.com/artifact/org.mybatis.caches/mybatis-ehcache -->
<dependency>
   <groupId>org.mybatis.caches</groupId>
   <artifactId>mybatis-ehcache</artifactId>
   <version>1.1.0</version>
</dependency>
```

在mapper.xml中使用对应的缓存即可

```xml
<mapper namespace = “org.acme.FooMapper” >
   <cache type = “org.mybatis.caches.ehcache.EhcacheCache” />
</mapper>
```

编写ehcache.xml文件，如果在加载时未找到/ehcache.xml资源或出现问题，则将使用默认配置。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
        updateCheck="false">
   <!--
      diskStore：为缓存路径，ehcache分为内存和磁盘两级，此属性定义磁盘的缓存位置。参数解释如下：
      user.home – 用户主目录
      user.dir – 用户当前工作目录
      java.io.tmpdir – 默认临时文件路径
    -->
   <diskStore path="./tmpdir/Tmp_EhCache"/>
   
   <defaultCache
           eternal="false"
           maxElementsInMemory="10000"
           overflowToDisk="false"
           diskPersistent="false"
           timeToIdleSeconds="1800"
           timeToLiveSeconds="259200"
           memoryStoreEvictionPolicy="LRU"/>

   <cache
           name="cloud_user"
           eternal="false"
           maxElementsInMemory="5000"
           overflowToDisk="false"
           diskPersistent="false"
           timeToIdleSeconds="1800"
           timeToLiveSeconds="1800"
           memoryStoreEvictionPolicy="LRU"/>
   <!--
      defaultCache：默认缓存策略，当ehcache找不到定义的缓存时，则使用这个缓存策略。只能定义一个。
    -->
   <!--
     name:缓存名称。
     maxElementsInMemory:缓存最大数目
     maxElementsOnDisk：硬盘最大缓存个数。
     eternal:对象是否永久有效，一但设置了，timeout将不起作用。
     overflowToDisk:是否保存到磁盘，当系统当机时
     timeToIdleSeconds:设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
     timeToLiveSeconds:设置对象在失效前允许存活时间（单位：秒）。最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，也就是对象存活时间无穷大。
     diskPersistent：是否缓存虚拟机重启期数据 Whether the disk store persists between restarts of the Virtual Machine. The default value is false.
     diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。
     diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒。
     memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。
     clearOnFlush：内存数量最大时是否清除。
     memoryStoreEvictionPolicy:可选策略有：LRU（最近最少使用，默认策略）、FIFO（先进先出）、LFU（最少访问次数）。
     FIFO，first in first out，这个是大家最熟的，先进先出。
     LFU， Less Frequently Used，就是上面例子中使用的策略，直白一点就是讲一直以来最少被使用的。如上面所讲，缓存的元素有一个hit属性，hit值最小的将会被清出缓存。
     LRU，Least Recently Used，最近最少使用的，缓存的元素有一个时间戳，当缓存容量满了，而又需要腾出地方来缓存新的元素的时候，那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存。
  -->

</ehcache>
```





































