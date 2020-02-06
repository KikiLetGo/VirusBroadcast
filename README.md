# 瘟疫传播模拟程序 Plague Spread Simulation Program

## 简介 Introduction

这是由 Bruce Young 制作的用于模拟2019-nCoV传播并在B站上告诫人们要呆在家里的模拟程序。

This is a simulator created by Bruce Young to simulate spread of 2019-nCoV 
and tell everyone on BiliBili why it's safer to stay home while the coronavirus is breaking out.  


代码已经过修改，如想要查看原始版本代码，请转到https://github.com/KikiLetGo/VirusBroadcast 。

The code has been modified. If you want to see the original code, please go to https://github.com/KikiLetGo/VirusBroadcast.


**武汉加油！中国加油！💪**


特别感谢 Bruce Young 以及他的“Ele实验室”带来如此有教育意义的程序。

Specially thank Bruce Young and his "Ele shiyanshi" (Hungry Lab) for releasing such an educative program.

[原始视频链接 Original Video Link](https://www.bilibili.com/video/av86478875?spm_id_from=333.5.b_6c6966655f6461696c79.18)

|||
| --: | :-- |
| 原始库 | [KikiLetGo/VirusBroadcast](https://github.com/KikiLetGo/VirusBroadcast) |
| 使用语言 | Java |
| 原始开发者 | Bruce Young |

## 使用方法 Usage

1. 直接打开可执行JAR包
2. 通过Shell打开
	```bash
		cd <当前目录>
		java -jar VirusBroadcast.jar <参数>
	```

	| 可用命令 ||
	| --: | :-- |
	| -h, -? | 获取帮助信息 |
	| -o, --original | 自定义初始感染数量 |
	| -b, --broad-rate | 自定义传播率（0 ~ 1的小数） |
	| -s, --shadow | 自定义潜伏时间 |
	| -r, --receive | 自定义医院收治时间 |
	| -c, --bed-count | 自定义床位 |
	| -m, --move-u | 自定义流动意向平均值（-0.99 ~ 0.99） |
	| -d, --safe-dist | 自定义安全距离 |
	| -p, --population | 自定义城市人口（建议在5000左右） |
