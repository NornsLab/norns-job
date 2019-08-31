# norns-job(SNAPSHOT)
![JDK](https://img.shields.io/badge/JDK-1.8-brightgreen.svg?style=flat-square)
![Scala](https://img.shields.io/badge/Scala-2.12.8-brightgreen.svg?style=flat-square)
![Gradle](https://img.shields.io/badge/Gradle-5.6.1-brightgreen.svg?style=flat-square)
[![CodeFactor](https://www.codefactor.io/repository/github/gourderwa/norns-job/badge)](https://www.codefactor.io/repository/github/gourderwa/norns-job)
# 为什么使用
norns-job 一个快速开发大数据任务工具
- 大数据开发项目经常面临重复性变量初始化，外部配置管理复杂
- 任务数量日益变多，相同类型任务自由组合为单个任务执行
- 任务依赖关系变化，修改配置文件即可
- ETL 快速开发
- 任务运行状态监控
- 公司应用数量日益变多，支持自动化上线
- 任务需要按不同应用不同日期执行
- 应用分优先级执行
- 重跑数据指定或者跳过具体任务

简单示例
```scala
// 默认开发
object HelloWorld {
   
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("HelloWorld")
      .setMaster("local[1]")
    val spark = SparkSession.builder.config(sparkConf).getOrCreate
    spark.sql("select * from db")
    spark.stop()
  }

}
```
```scala
// 使用 norns-job 开发
class SingleSparkJobExample extends SparkJob {

  override def run(): Unit = {
    sql("select * from db")
  }

}
```
# 项目结构
## 模块说明
|模块|说明|
|:---|:---|
|[norns-job-core](./norns-job-core)|核心功能接口封装|
|[norns-job-spark(开发中)](./norns-job-spark)|spark-job|
|[norns-job-flink(开发中)](./norns-job-flink)|flink-job|
|[norns-job-examples](./norns-job-examples)|项目示例|

# 计划
- 配置内容支持动态替换context#data

# 联系
* QQ 群：348527861
* email：gourderwa@163.com

# License
Copyright [2019] [NornsLab]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
