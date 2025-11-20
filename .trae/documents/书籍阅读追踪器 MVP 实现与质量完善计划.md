## 要求覆盖确认

* Kotlin + Jetpack Compose：已使用 Compose 与 Material3（例如 `app/src/main/java/com/example/bookreadingtracker/ui/screens/DiscoverScreen.kt:24-67`、`app/src/main/java/com/example/bookreadingtracker/ui/theme/Theme.kt:11-15`）。

* 3–5 页面 + Navigation：首页、发现、书架、详情、设置、分析共 6 页；基于 Navigation Compose（`app/src/main/java/com/example/bookreadingtracker/ui/nav/AppNavGraph.kt:23-49`，路由定义 `app/src/main/java/com/example/bookreadingtracker/ui/nav/Destinations.kt:2-10`）。

* 架构 ViewModel + Repository + Hilt：示例 ViewModel（`ui/screens/DiscoverViewModel.kt:17-38`、`ui/screens/ShelfViewModel.kt:9-15`），仓库接口与实现（`data/repository/BookRepository.kt:6-11`、`data/repository/DefaultBookRepository.kt:14-49`），依赖注入（`di/AppModule.kt:21-56`、`MyApp.kt:6-7`、`MainActivity.kt:19-20`）。

* Room（实体/DAO/Database）：`BookEntity.kt:6-13`、`UserBookEntity.kt:6-14`；`BookDao.kt:6-19`、`UserBookDao.kt:11-30`；`AppDatabase.kt:12-20`。

* 网络请求：Google Books API（`data/remote/BookApi.kt:7-9`），Retrofit/Hilt 配置（`di/AppModule.kt:29-38`）。

* 离线缓存 + 错误处理（强烈推荐项）：搜索结果入库缓存（`DefaultBookRepository.kt:20-26`）；错误提示基础已在发现页（`DiscoverViewModel.kt:31-36`），将统一到 Snackbar 并补全 Network‑Bound 策略与自动刷新。

* 测试（必须）：目前示例测试文件在 `app/src/androidTest/...ExampleInstrumentedTest.kt` 与 `app/src/test/...ExampleUnitTest.kt`；将补齐核心仓库/用例单测、DAO 仪器测试与 Compose UI 测试。

结论：所有“必须”项已具备基础实现，推荐项将按计划强化，满足课程高分要求。

## 待修正问题

* Gradle 依赖重复与版本不一致（`app/build.gradle.kts:29-69`），包括重复 `hilt` 插件声明与 `navigation-compose`、`coil` 多版本。

* 详情页 ViewModel 导入路径不匹配：`ui/screens/BookDetailScreen.kt:19` 指向 `ui.viewmodels`，实际位于 `ui.screens/BookDetailViewModel.kt:12-17`。

* 深色主题仅会话态：`MainActivity.kt:25-33` 使用 `rememberSaveable`，需持久化到 `DataStore`。

## 实施计划（分阶段）

### 阶段 1：构建与基础清理

1. 清理 `build.gradle.kts` 重复插件与库，统一版本（遵循 Compose BOM、统一 `navigation-compose` 与 `coil`、保留一个 Hilt 配置）。
2. 修正 `BookDetailScreen` 的 ViewModel 导入；优化底部导航点击的栈策略（单例路由去重）。

### 阶段 2：主题持久化

1. 集成 `DataStore` 持久化暗色开关；在 `MainActivity` 通过 Flow 收集并写入。
2. 在 `Settings` 切换时保存偏好，应用全局主题。

### 阶段 3：笔记模块（占位 → 可用）

1. 增加 `NoteDao` 并在 `AppDatabase` 暴露；仓库新增 `observeNotes/upsertNote/deleteNote`。
2. 用例实现 `UpsertNote`、`DeleteNote`、`GetNotesForBook`；详情页提供列表与编辑对话框。

### 阶段 4：阅读记录与进度

1. 增加 `ReadingSessionDao` 与仓库接口实现；
2. 用例实现 `LogReading`、`GetReadingSessions`、`UpdateProgress`；详情页增加记录入口与进度条。

### 阶段 5：基础分析

1. `GetAnalytics` 汇总总时长/近 7 日趋势等；
2. `AnalyticsScreen` 卡片与趋势占位图展示。

### 阶段 6：离线缓存与自动刷新

1. Network‑Bound‑Resource：详情优先 Room，网络成功后刷新 Room，统一错误处理为 Snackbar。
2. `WorkManager` 周期刷新书架书目元数据（Wi‑Fi+充电条件，指数退避重试）。

### 阶段 7：测试覆盖

* 单元测试：仓库与用例（MockWebServer + In‑Memory Room）、映射函数。

* 仪器测试：DAO 一致性与事务；

* Compose UI 测试：搜索→详情→加入/移除书架→笔记增删改→记录阅读主流程。

### 阶段 8：UI 打磨与文档

* MD3 组件与可访问性、加载/空/错误态骨架；

* README 产品文档与 Git 分支策略补充（语义化提交前缀、PR 模版）。

## 首批交付目标（确认后立即执行）

* 完成阶段 1 与阶段 2：可编译稳定的依赖配置、修正导入与导航、主题偏好持久化。

* 验证：构建通过、主题切换持久化、导航栈正确；提交基础测试用例（单元+UI）以回归主路径。

