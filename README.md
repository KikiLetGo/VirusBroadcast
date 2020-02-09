# VirusBroadcast
一个基于java的模拟仿真程序，由于启动的时候时间仓促，数据不足，所以模型和推演过程过于简单，如果有好的想法或者能提供相关数据支持的朋友请提issues。
如果您也是一名java程序员，可以直接修改并给我提交pr，我之前已经启动每日疫情数据的每日抓取工作，希望在疫情结束后有机会通过这些精准的的数据做一个复盘。

2020.2.6:
病毒变异过程是一个不断适应的过程，可以尝试简单的DNN对病毒进行建模，已经开始着手实施。

## Maven 用法

```sh
# 编译
mvn compile
# 运行
mvn exec:java -Dexec.mainClass="com.github.KikiLetGo.VirusBroadcast"

# 打包
mvn package
# 复制 jar 包 target/VirusBroadcast-1.0-SNAPSHOT.jar 即可单独执行
cp target/VirusBroadcast-1.0-SNAPSHOT.jar ~/
java -jar ~/VirusBroadcast-1.0-SNAPSHOT.jar
```