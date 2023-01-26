# fastipcdemo
快速搭建进程间通信（含demo示例）

# 服务端：

```
UserManager.getDefault().setUserInfo(User("ming", 25))
FastIPC.register(UserManager::class.java)

```

同时在服务端需要注册一个IPCService的实例，这里用的是IPCService01

```
<service
    android:name=".UserService"
    android:enabled="true"
    android:exported="true" />
<service android:name="com.lay.fastipc.service.FastIPCService01"
    android:exported="true"
    android:enabled="true">
    <intent-filter>
        <action android:name="android.intent.action.GET_USER_INFO"/>
    </intent-filter>
</service>
```
# 客户端：

调用connect方法，需要绑定服务端的服务，传入包名和action
```
FastIPC.connect(
    this,
    "com.lay.fastipcdemo",
    "android.intent.action.GET_USER_INFO",
    IPCService01::class.java
)
```

首先获取IUserManager的实例，注意这里要和服务端注册的UserManager2是同一个ServiceId，而且**接口、javabean需要存放在与服务端一样的文件夹下，详情见demo**。
```
val userManager = IPC.getInstanceWithName(
    IPCService01::class.java,
    IUserManager::class.java,
    "getDefault",
    null
)
val info = userManager?.getUserInfo()
```