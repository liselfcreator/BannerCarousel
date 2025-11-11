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

4. **布局中引用自定义 View**
   ```xml
   <com.liselfcreator.bannercarousel.BannerCarouselView
       android:id="@+id/banner_carousel"
       android:layout_width="match_parent"
       android:layout_height="wrap_content" />
   ```

5. **在页面中配置**
   ```kotlin
   bannerCarouselView.setup {
       setData(banners)
       showStatus(true)     // 顶部状态徽标
       showBottom(true)     // 底部评分/阅读信息
       setItemSpacingDp(17) // 卡片间距，默认 17dp
       setEdgeVisibleFraction(0.2f)  // 首尾露出比例，默认 0.2
       setCenterScaleFactor(1.8f)    // 中心卡放大倍数，默认 1.8
       setItemAspectRatio("3:4")     // 卡片宽高比，默认 3:4
       setImageLoader { imageView, url, placeholder ->
           Glide.with(imageView)
               .load(url)
               .placeholder(placeholder)
               .into(imageView)
       }
       setOnPageChangeListener { index, model ->
           // 页面切换回调，可同步背景、标题等
       }
       setOnItemClickListener { position, model ->
           // 处理点击事件
       }
   }
   ```

## Builder 参数说明
- `setData(List<BannerModel>)`：必填，轮播数据列表。
- `showStatus(Boolean)`：是否显示顶部完结/连载徽标，默认 `true`。
- `showBottom(Boolean)`：是否显示底部评分与观看人数，默认 `true`。
- `setItemSpacingDp(Int)`：卡片间距（dp），默认 `17`。
- `setEdgeVisibleFraction(Float)`：首尾可见宽度占正常卡片宽度的比例，默认 `0.2f`。
- `setCenterScaleFactor(Float)`：中心卡片放大倍数，默认 `1.8f`。
- `setItemAspectRatio(String)`：卡片宽高比，格式如 `"3:4"`，默认 `3:4`。
- `setImageLoader(BannerImageLoader)`：图片加载回调，必须由宿主提供。
- `setOnItemClickListener((Int, BannerModel) -> Unit)`：点击回调，可选。
- `setOnPageChangeListener((Int, BannerModel) -> Unit)`：页面切换回调，可选。

| 方法 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `setData` | `List<BannerModel>` | — | 必填，轮播数据列表 |
| `showStatus` | `Boolean` | `true` | 是否显示顶部完结/连载徽标 |
| `showBottom` | `Boolean` | `true` | 是否显示底部评分与观看人数 |
| `setItemSpacingDp` | `Int` | `17` | 卡片间距（dp） |
| `setEdgeVisibleFraction` | `Float` | `0.2f` | 首尾可见宽度 / 正常卡片宽度 |
| `setCenterScaleFactor` | `Float` | `1.8f` | 中心卡片放大倍数 |
| `setItemAspectRatio` | `String` | `3:4` | 卡片宽高比（宽:高） |
| `setImageLoader` | `BannerImageLoader` | — | 必填，负责加载封面图 |
| `setOnItemClickListener` | `(Int, BannerModel) -> Unit` | `null` | 卡片点击回调 |
| `setOnPageChangeListener` | `(Int, BannerModel) -> Unit` | `null` | 页面切换回调 |

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

