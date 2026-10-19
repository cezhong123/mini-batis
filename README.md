# 手写迷你 MyBatis

从零实现一个极简 ORM：**SQL 写在注解上，接口不用实现类，结果集自动映射成对象。**

功能很简单，算是成一次动态代理，反射，jdbc相关的练习和学习记录
## 它能干什么

```java
UserMapper m = ProxyUser.getMapper(UserMapper.class);

List<User> users = m.findAll();
User u = m.findById(101);
int n = m.insertUser(new User(0, "测试乙", "13900000002", null));
System.out.println(u.getId());      // 数据库发的新号，插完就能拿到
```

`UserMapper` 是一个接口，**这个项目里没有任何类实现它**。

## 怎么跑

1. 建库 `shop_lab_d10`，表有个users就行了，其他表的操作也没实现
2. 把 `src/main/resources/db.properties.example` 复制成 `db.properties`，填上你的口令
3. `mvn compile exec:java -Dexec.mainClass=com.shop.Main`

## 结构

| 类                   | 干什么                       |
| ------------------- | ------------------------- |
| `ProxyUser`         | 核心。把一个没有实现类的接口，变成一个能调用的对象 |
| `Select` / `Insert` | 把一条 SQL 贴在方法上的注解          |
| `UserMapper`        | 接口。声明"能做什么"，没有任何实现        |
| `RowMapper`         | 结果集的一行 → 一个对象（反射完成）       |
| `SqlParser`         | `#{x}` → `?`，同时把占位符名字记下来  |
| `Database`          | 链接数据库                     |
| `User`              | 实体类                       |

## 走过的八步

| 步   | 做了什么                               | 学了什么                                |
| --- | ---------------------------------- | ----------------------------------- |
| 1   | 用裸 JDBC 查出一个用户并打印                  | 巩固了一下 JDBC 三件套：连接、语句、结果集            |
| 2   | 改成一个方法返回 `User` 对象                 | **从"打印"变成"交出一个结果"**                 |
| 3   | 加 `findAll()` 查多条                  |                                     |
| 4   | 用反射把"装对象"自动化                       | **代码里不再出现 `User` 这个字**换个类也能装，不过代理没写 |
| 5   | 把 SQL 写进 `@Select`，用反射读出来          | SQL 从 Java 代码里搬到了方法上                |
| 6   | `Proxy.newProxyInstance` 让接口自己变出实现 | 这个代理也没调对象的方法，所以invoke里就用自己方便些       |
| 7   | `#{id}` 换成 `?`，按位置填参数              | 方法参数自动变成 SQL 参数                     |
| 8   | 从泛型里推出实体类型                         |                                     |

## 遇到的一些问题

### 1. `record` 只有全参构造，`new User()` 编不过

- **报错**：`应为 4 个实参，但实际为 0 个`
- **我当时以为**：record 写起来短，用它就行
- **实际原因**：反射造对象靠 `getDeclaredConstructor().newInstance()`，而 record 没有无参构造、字段又是 `final`，`Field.set` 也改不了。

### 2. `type` 是接口，不是实体类

- **报错**：`NoSuchMethodException: com.shop.UserMapper.<init>()`
- **我当时以为**：`getMapper(Class<T> type)` 里的 `type` 就是"要装成什么类"
- **实际原因**：`type` 是 `UserMapper.class`（接口），而装对象要用 `User.class`。**一个 getMapper 里其实需要两个 Class**：一个是"要实现的接口"，一个是"要装成什么"。

### 3. `List<T>` 里的 `T` 和我想的不是一个东西

- **报错**：`不存在类型变量的实例，使 User 符合 T`
- **我当时以为**：`getMapper` 里的 `<T>` 就是实体类型
- **实际原因**：那个 `T` 是**接口**（`UserMapper`）。要装的是实体，得另外从 `List<User>` 的泛型里挖。

### 4. `setInt(1, args[0])` 把 `findAll()` 弄崩了

- **报错**：`NullPointerException` / `ArrayIndexOutOfBoundsException`
- **我当时以为**：反正只有一个参数，写死下标 0 就行
- **实际原因**：`findAll()` 没有参数，`args` 是 `null`。
  改成 `if (args != null)` + 循环 `setObject`——**框架代码永远选最宽的那个 API**，因为它不知道调用方会传什么。

### 5. `Matcher.group()` 没 `find()` 就报错

- **报错**：`IllegalStateException: No match found`
- **我当时以为**：编译好 pattern 就能直接 `group(1)`
- **实际原因**：`Matcher` 是**有状态的**：必须先 `find()` 让它前进到第一个匹配，`group()` 才取得到。


## 没做完的

- `@Update` / `@Delete`（现在只有 Select 和 Insert）
- 连接池（现在每次调用都开一条新连接）
- 下划线转驼峰（`created_at` → `createdAt`，现在字段名只能是下划线）
- 主键字段名硬编码成 `"id"`，多实体时要改成注解参数
- MyBatis 的 XML 方案（SQL 放文件里，支持动态拼条件）