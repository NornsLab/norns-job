# Norns-Job RoadMap

## norns-job-core

- 简化API设计，舍弃JobContext。直接使用 Context 代替 JobContext泛型边界
- Plugin 不继承 Service 接口，因为 Plugin 初始化基本依赖构造函数，继承后构造函数与 Service 函数中 context 出现重复
- Task 依赖运行数据参数封装为 Map 类型，减少对Config第三方接口强依赖
- Plugin 初始化使用封装类 Collection<PluginConfigSpec<?>> 进行依赖配置参数约定，初始化时采用 Configuration 封装配置参数
- 反射时，系统默认载入指定包下 plugin 映射为 map<plugin-name,class>

## norns-job-spark


## norns-job-flink

