# Banner Carousel SDK

Banner Carousel SDK 提供一个基于 `ViewPager2` 的五卡轮播效果，支持中间卡片放大、左右卡片缩小露出、可选的顶部状态徽标与底部信息栏。SDK 以 `Builder` 方式初始化，方便集成到任意应用中。

## 功能特性
- 视差式的卡片轮播，中间卡片放大 1.8x，其余卡片缩放至正常尺寸。
- 支持无限循环滚动与预加载，保持 5 个可见元素。
- 可配置顶部完结/连载徽标、底部评分与观看人数区域。
- 对外暴露点击回调，返回位置与 `BannerModel` 数据。
- 通过 `maven-publish` 预设，可生成 `1.0.0` 版本 AAR 并推送到私有仓库（JCenter 已停止服务，建议改用 MavenCentral 或私有 Maven 仓库）。

## 快速集成

1. **在 settings.gradle.kts 中引入仓库（假设同步至 Maven 仓库）**
   ```kotlin
   dependencyResolutionManagement {
       repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
       repositories {
           google()
           mavenCentral()
           maven("https://your.repo.com/maven") // 根据实际仓库修改
       }
   }
   ```

2. **添加依赖**
   ```kotlin
   dependencies {
       implementation("com.liselfcreator:banner-carousel-sdk:1.0.0")
   }
   ```

3. **准备数据**
   ```kotlin
   val banners = listOf(
       BannerModel(
           imgUrl = "https://example.com/cover1.jpg",
           status = BannerStatus.COMPLETED,
           score = "4.8",
           views = "12.4k"
       ),
       BannerModel(
           imgUrl = "https://example.com/cover2.jpg",
           status = BannerStatus.ONGOING,
           score = "4.5",
           views = "30.2k"
       )
   )
   ```

4. **初始化轮播**
   ```kotlin
   BannerCarousel.with(viewPager2)
       .setData(banners)
       .showStatus(true)   // 控制顶部完结徽标
       .showBottom(true)   // 控制底部评分/观看人数
       .setOnItemClickListener { position, model ->
           // 处理点击事件
       }
       .build()
   ```

5. **监听页码变化（可选，用于同步其他 UI）**
   ```kotlin
   viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
       override fun onPageSelected(position: Int) {
           val realIndex = ((position % banners.size) + banners.size) % banners.size
           val model = banners[realIndex]
           // 同步背景图、标题等
       }
   })
   ```

## Builder 参数说明
- `setData(List<BannerModel>)`：必填，轮播数据列表。
- `showStatus(Boolean)`：是否显示顶部完结/连载徽标，默认 `true`。
- `showBottom(Boolean)`：是否显示底部评分与观看人数，默认 `true`。
- `setOnItemClickListener((Int, BannerModel) -> Unit)`：点击回调，可选。

## BannerModel 字段
| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `imgUrl` | `String` | 封面图片地址 |
| `status` | `BannerStatus` | `COMPLETED` 或 `ONGOING` |
| `score` | `String?` | 评分字符串，可为空 |
| `views` | `String?` | 观看人数字符串，可为空 |

## 发布说明
项目内已提供 `banner-carousel-sdk` library module，并开启 `maven-publish`：

```bash
./gradlew :banner-carousel-sdk:publishReleasePublicationToMavenLocal
```

如需推送到远程仓库，请在 `banner-carousel-sdk/build.gradle.kts` 的 `publishing.repositories` 块中添加目标仓库与凭证。**注意：JCenter 自 2021 年起已停止对新包开放上传，建议改用 MavenCentral、私有 Maven（如 Nexus、Artifactory）或 GitHub Packages。**

## Demo
`app` 模块提供演示工程，展示 Builder 调用与大图同步预览的实现，可直接运行体验效果。

