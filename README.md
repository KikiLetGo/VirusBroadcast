# 简介

简易的病毒扩散和控制模拟。 2019-nCov
源码来自于中国抗病毒战友： https://github.com/KikiLetGo/VirusBroadcast
本人只是进行简单的打包，让普通人更简单的运行

# 如何编译运行：
```bash
mvn clean package && java -jar target/dummy-simulation-2019ncov-0.0.1.jar
```

# 参数定制
```bash
java -jar target/dummy-simulation-2019ncov-0.0.1.jar -h
```
e.g.
```
usage: 2019-nCov
 -b,--bed_count <arg>        医院床位
 -h,--help                   帮助信息
 -i,--infected_count <arg>   初始的感染人数
 -r,--receive_time <arg>     医院收治响应时间
 -s,--shadow_time <arg>      潜伏时间
 -t,--transfer_rate <arg>    传播率
 -u,--unstable_rate <arg>    流动意向平均值
```