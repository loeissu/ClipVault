# ClipVault 汉化版

<p align="left">
  <img src="store_assets/icon_512.png" alt="ClipVault" width="96" height="96">
</p>

**Android 本地优先剪贴板管理器（中文汉化版）。** 捕获、整理、搜索并恢复你复制的一切内容 — 不依赖云端。

> 本仓库是 [bilboo00/ClipVault](https://github.com/bilboo00/ClipVault) 的中文汉化 fork，基于上游 **v1.2.1**。功能逻辑与上游一致，界面文案已本地化为简体中文。

[![Upstream](https://img.shields.io/badge/upstream-bilboo00%2FClipVault-blue?style=flat-square)](https://github.com/bilboo00/ClipVault)
[![Base](https://img.shields.io/badge/base-v1.2.1-informational?style=flat-square)](https://github.com/bilboo00/ClipVault/releases/tag/v1.2.1)
[![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android%208.0%2B-3DDC84?style=flat-square&logo=android)](https://github.com/bilboo00/ClipVault)

---

## 为什么选择 ClipVault？

多数剪贴板管理器把数据锁在订阅后面、追踪你，或把文本上传到云端。ClipVault 将**全部数据保存在本机**的私有 SQLite 数据库中，并用现代 Material 3 界面帮你整理、转换，以及锁定敏感内容。

## 功能

### 捕获与历史
- 前台监控服务自动捕获剪贴板
- 对全部复制过的条目进行全文搜索
- 置顶重要条目，防止自动清理
- 智能识别内容类型（链接、邮件、电话、验证码、JSON）
- 重复检测，保持历史整洁

### 整理
- **标签** — 10 色色板，快速归类
- **合集** — 将相关条目打包成命名集合
- **备注** — 为任意条目附加 Markdown 备注
- **临时条目** — 按时间或使用次数自动过期

### 效率
- **文本转换** — 多种内置转换（大写、首字母大写、去空白、URL/Base64 编解码、JSON 格式化等），带实时预览
- **粘贴队列** — 存放多条片段并按顺序粘贴
- **片段** — 可复用文本快捷方式，支持关键字展开
- **导出** — 历史可导出为 CSV、Markdown 或纯文本

### 集成
- **增强链接预览** — URL 的 OpenGraph 元数据
- **分享到 ClipVault** — 通过系统分享面板从任意应用捕获文本
- **深链接** — `clipvault://` 与 `https://clipvault.app` URI
- **悬浮气泡** — 任意界面快速访问
- **摇一摇打开** 手势
- **桌面小组件** 与 **快捷设置磁贴**
- **生物识别锁定** — 用指纹 / 面容保护敏感条目

### 设计
- Material 3，支持动态取色与 AMOLED 纯黑
- 流畅动效与微交互
- 针对不同操作调校的触感反馈

## 系统要求

- **Android 8.0（API 26）及以上**
- 从源码构建需要 **Java 21**

## 下载

请到本仓库 **Releases** 下载汉化版 APK：

- **[v1.2.1-zh](https://github.com/loeissu/ClipVault/releases/tag/v1.2.1-zh)**  
  - `ClipVault-zh-v1.2.1-zh.apk`（固定签名，汉化版之间可覆盖安装）  
  - `app-release.apk`（同构建产物的通用文件名）

上游原版：[bilboo00/ClipVault Releases](https://github.com/bilboo00/ClipVault/releases)。

由 GitHub Actions 工作流 `Build ZH release APK` 自动打包；也可在 Actions 页手动 **Run workflow**。

## 从源码构建

```bash
git clone https://github.com/loeissu/ClipVault.git
cd ClipVault
git checkout zh/main
./gradlew :app:assembleRelease
```

## 汉化说明

| 范围 | 状态 |
|------|------|
| 底部导航、首页、搜索、设置、详情 | 已汉化 |
| 引导页、合集 / 标签 / 片段、统计 | 已汉化 |
| 通知、快捷磁贴、无障碍说明、悬浮气泡提示 | 已汉化 |
| 文本转换、类型徽章、相对时间、错误提示 | 已汉化 |
| SQL / Room 注解 / 函数名 / 日志标签 | 保持英文（避免破坏编译） |
| 品牌名 ClipVault、技术名词 URL/JSON/Base64/OTP | 保持原文 |

**术语对照（审校后统一）：**

| 英文 | 中文 | 说明 |
|------|------|------|
| clip | 条目 | 剪贴板历史中的一条记录 |
| pin / pinned | 置顶 | 不译作「固定」 |
| collections | 合集 | 相关条目的文件夹式分组 |
| tags | 标签 | 跨类型标记 |
| snippets | 片段 | 可复用文本模板 |
| paste queue | 粘贴队列 | 按顺序粘贴 |
| mask sensitive content | 隐藏敏感内容 | 通知/搜索中不显示正文 |
| auto-delete after | 自动删除时间 | 下拉选择保留时长 |
| Organized | 归类 | 详情页展示所属标签/合集 |

系统字符串在 `app/src/main/res/values/strings.xml`；Compose 文案在对应 `*Screen.kt`。重新应用（仅替换完整字符串字面量）：

```bash
python tools/apply-zh.py
python tools/apply-zh-fix.py
```

## 致谢

- 上游项目：[bilboo00/ClipVault](https://github.com/bilboo00/ClipVault)（MIT）
- 技术栈：Kotlin · Jetpack Compose · Material 3 · Room

## License

MIT，与上游一致。
