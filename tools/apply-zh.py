#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""ClipVault 汉化：仅替换完整字符串字面量，译文已人工审校。

原则：
1. 只替换 "完整英文" → "完整中文"，绝不改代码标识符/SQL/注解。
2. 不翻译品牌名 ClipVault、API 名、路径。
3. UI 术语按 Android 剪贴板场景统一（条目/合集/片段/标签/置顶）。
"""

from __future__ import annotations

import pathlib

ROOT = pathlib.Path(__file__).resolve().parent.parent
JAVA = ROOT / "app/src/main/java/com/clipvault/manager"
STRINGS = ROOT / "app/src/main/res/values/strings.xml"

# key：源码中完整字符串（不含外层引号）；value：精准中文
MAP: dict[str, str] = {
    # —— 底部导航 ——
    "Home": "首页",
    "Search": "搜索",
    "Snippets": "片段",
    "Stats": "统计",
    "Settings": "设置",
    # —— 通用按钮/标签 ——
    "Back": "返回",
    "Cancel": "取消",
    "OK": "确定",
    "Save": "保存",
    "Delete": "删除",
    "Edit": "编辑",
    "Copy": "复制",
    "Close": "关闭",
    "Done": "完成",
    "Retry": "重试",
    "Undo": "撤销",
    "Create": "创建",
    "Clear": "清除",
    "Add": "添加",
    "Replace": "替换",
    "Insert": "插入",
    "Name": "名称",
    "Color": "颜色",
    "Title": "标题",
    "Content": "内容",
    "Grant": "授权",
    "Continue": "继续",
    "Get started": "开始使用",
    "Open settings": "打开设置",
    "Enable accessibility": "启用无障碍",
    "Grant permission": "授予权限",
    "Select all": "全选",
    "Deselect all": "取消全选",
    "Save now": "立即保存",
    "Merge": "合并",
    "Unlock": "解锁",
    "Queue": "队列",
    "Transform": "转换",
    "Organize": "整理",
    "Favorite": "收藏",
    "Favorites": "收藏",
    "Pinned": "置顶",
    "Unpinned": "已取消置顶",
    "Locked": "已锁定",
    "Copied": "已复制",
    "Loading…": "加载中…",
    "Loading...": "加载中...",
    "No matches": "无匹配结果",
    "Nothing copied yet": "还没有复制内容",
    "Never": "永不",
    "Unlimited": "不限",
    "Notes": "备注",
    "Source": "来源",
    "Organized": "归类",
    "Expires": "过期",
    "Characters": "字符数",
    "Words": "词数",
    "Yesterday": "昨天",
    "Older": "更早",
    "All": "全部",
    "Clipboard": "剪贴板",
    "Unavailable": "不可用",
    "Statistics": "统计",
    "Storage": "存储",
    "Recent": "最近",
    "just now": "刚刚",
    # —— 锁屏 / 生物识别 ——
    "Vault locked": "已锁定",
    "Unlock to view your saved clips": "解锁后查看已保存的条目",
    "Biometrics not available": "当前无法使用生物识别",
    "Unlock ClipVault": "解锁 ClipVault",
    "Authenticate to access your clipboard": "验证身份后访问剪贴板",
    # —— 首页 ——
    "Saved to clipboard history": "已保存到剪贴板历史",
    "Already in history": "已在历史中",
    "Clip deleted": "条目已删除",
    "Pinned to top": "已置顶",
    "Clipboard monitoring paused — existing entries are safe": "剪贴板监控已暂停 — 现有条目不受影响",
    "Clipboard monitoring resumed": "剪贴板监控已恢复",
    "Clear all history?": "清空全部历史？",
    "This removes every saved clip (pinned items are kept).": "将删除全部已保存条目（置顶项会保留）。",
    "Paste queue": "粘贴队列",
    "Pause monitoring": "暂停监控",
    "Resume monitoring": "恢复监控",
    "Exit selection": "退出多选",
    "Locked clip — unlock it to copy": "条目已锁定 — 解锁后才能复制",
    "Copy code": "复制验证码",
    "This Week": "本周",
    "Copy text anywhere — it shows up here.\nShake to clear history · long-press to multi-select.":
        "在任意处复制文本，都会出现在这里。\n摇一摇可清空历史 · 长按可多选。",
    # —— 搜索 ——
    "Find in history": "在历史中查找",
    "Search your clipboard history": "搜索剪贴板历史",
    "Type to find anything you've copied. Matches are highlighted.":
        "输入关键词查找你复制过的内容，匹配项会高亮显示。",
    # —— 合集 Collections ——
    "New collection": "新建合集",
    "Edit collection": "编辑合集",
    "Delete collection?": "删除合集？",
    "Clips will stay in your history.": "其中的条目仍会保留在历史中。",
    "No collections yet": "还没有合集",
    "Collections group related clips into folders.": "合集用于把相关条目归到同一文件夹。",
    "Create your first collection": "创建你的第一个合集",
    # —— 片段 Snippets（可复用文本模板）——
    "New snippet": "新建片段",
    "Edit snippet": "编辑片段",
    "Search snippets": "搜索片段",
    "Clear search": "清除搜索",
    "No snippets yet": "还没有片段",
    "Save reusable text — emails, addresses, replies.":
        "保存可重复使用的文本，例如邮件、地址、常用回复。",
    "Delete snippet?": "删除片段？",
    # —— 标签 Tags ——
    "New tag": "新建标签",
    "Edit tag": "编辑标签",
    "Delete tag?": "删除标签？",
    "No tags yet": "还没有标签",
    "Tags let you organize clips across types and dates.":
        "标签可跨类型、跨日期整理条目。",
    "Create your first tag": "创建你的第一个标签",
    # —— 条目详情 ——
    "It no longer exists in your history.": "该条目已不在历史中。",
    "Use limit": "使用次数上限",
    "Delete clip?": "删除条目？",
    "This clip will be removed from your history. You can undo right after deleting.":
        "该条目将从历史中移除。删除后可立即撤销。",
    "Authenticate to remove this clip's lock.": "验证身份以解除该条目的锁定。",
    "Authenticate to lock this clip's notes.": "验证身份以锁定该条目的备注。",
    "Authenticate to view this clip's content.": "验证身份以查看该条目内容。",
    "Locked clip": "条目已锁定",
    "Add context or reminders.": "补充说明或提醒。",
    "Pick a transformation and copy or replace.": "选择一种转换方式，然后复制或替换。",
    "5 minutes": "5 分钟",
    "30 minutes": "30 分钟",
    "1 hour": "1 小时",
    "12 hours": "12 小时",
    "24 hours": "24 小时",
    "7 days": "7 天",
    "Auto-delete": "自动删除",
    "The clip is removed after the chosen time. Pinned clips are never auto-deleted.":
        "所选时间过后将删除该条目。置顶条目不会自动删除。",
    "The clip is removed after being copied this many times. Pinned clips are never auto-deleted.":
        "复制达到所选次数后将删除该条目。置顶条目不会自动删除。",
    "1 use": "使用 1 次",
    "2 uses": "使用 2 次",
    "3 uses": "使用 3 次",
    "5 uses": "使用 5 次",
    "10 uses": "使用 10 次",
    "Edit clip": "编辑条目",
    # —— 文本转换 ——
    "Strip HTML formatting": "去除 HTML 格式",
    "Convert to uppercase": "全部转换为大写",
    "Convert to title case": "每个单词首字母大写",
    "Normalize whitespace": "规范化空白字符",
    "Keep raw text": "保留原始文本",
    "Pretty print JSON": "格式化 JSON",
    "Strip HTML tags": "去除 HTML 标签",
    "Keep raw URL": "保留原始 URL",
    "Raw": "原文",
    "Pretty": "格式化",
    "Strip": "去除",
    "Title Case": "每个单词首字母大写",
    "Sentence case": "句首字母大写",
    "Trim Whitespace": "去掉首尾空白",
    "Remove Line Breaks": "删除换行",
    "Add Line Breaks": "添加换行",
    "URL Encode": "URL 编码",
    "URL Decode": "URL 解码",
    "Base64 Encode": "Base64 编码",
    "Base64 Decode": "Base64 解码",
    "Format JSON": "格式化 JSON",
    "Minify JSON": "压缩 JSON",
    # —— 内容类型徽章 ——
    "Email": "邮箱",
    "Phone": "电话",
    "Code": "验证码",
    "Number": "数字",
    "Text": "文本",
    "Wallet": "钱包地址",
    "Image": "图片",
    # —— 行内预览 ——
    "Call": "拨打",
    "Open": "打开",
    # —— 整理面板 / 粘贴队列 ——
    "Assign tags and collections to this clip.": "为该条目分配标签与合集。",
    "No tags yet. Create them from Settings → Tags.": "还没有标签。可在「设置 → 标签」中创建。",
    "No collections yet. Create them from Settings → Collections.":
        "还没有合集。可在「设置 → 合集」中创建。",
    "Queue is empty": "队列为空",
    "Add clips from the detail screen with the Queue button.":
        "在详情页点击「队列」按钮，可把条目加入队列。",
    "Copy this clip": "复制此条",
    "Move up": "上移",
    "Move down": "下移",
    "Remove from queue": "移出队列",
    "Copy next": "复制下一条",
    "next to paste": "待粘贴",
    # —— 设置 ——
    "Clipboard monitoring": "剪贴板监控",
    "Save everything you copy while using your phone.":
        "保存你在使用手机时复制的全部内容。",
    "Mask sensitive content": "隐藏敏感内容",
    "Hide clip text in notifications, widget, and search.":
        "在通知、小组件与搜索结果中隐藏条目正文。",
    "Require biometric to open": "打开时需要生物识别",
    "Quick access": "快捷访问",
    "Floating bubble": "悬浮气泡",
    "Quick Settings tile": "快捷设置磁贴",
    "Pull down the shade → edit → drag to the bar.":
        "下拉通知栏 → 点编辑 → 拖到快捷栏。",
    "Accessibility service": "无障碍服务",
    "Optional. Capture copies in any app.":
        "可选。用于在任意应用中捕获复制内容。",
    "Organize clips with custom labels.": "用自定义标签整理条目。",
    "Group related clips into folders.": "把相关条目归入合集文件夹。",
    "Auto-delete clips": "自动删除条目",
    "Find duplicates": "查找重复项",
    "Merge identical clips to keep history tidy.": "合并内容相同的条目，保持历史整洁。",
    "Export history": "导出历史",
    "Exporting…": "导出中…",
    "Exporting...": "导出中...",
    "Import history": "导入历史",
    "Restore clips from a JSON backup.": "从 JSON 备份恢复条目。",
    "Tap to copy the newest stack trace.": "点按可复制最新的崩溃堆栈。",
    "No report content": "没有报告内容",
    "ClipVault crash log": "ClipVault 崩溃日志",
    "Newest crash log copied — paste it to the developer":
        "已复制最新崩溃日志 — 可粘贴给开发者",
    "Clipboard Manager": "剪贴板管理器",
    "Replay setup guide": "重新运行设置引导",
    "Walk through the onboarding flow again.": "再走一遍新手引导流程。",
    "Delete all clips": "删除全部条目",
    "Removes every saved entry. Pinned items are kept.": "删除全部已保存条目，置顶项会保留。",
    "All data stays on your device": "所有数据仅保存在本机",
    "No accounts · No tracking · No cloud": "无账号 · 无追踪 · 不上云",
    "Could not read file": "无法读取文件",
    "Only JSON exports are supported for import.": "导入仅支持本应用导出的 JSON 文件。",
    "Monitoring": "监控",
    "Privacy": "隐私",
    "Cleanup": "清理",
    "Theme": "主题",
    "Data": "数据",
    "About": "关于",
    "Lock the app behind biometric / device credential.":
        "使用生物识别或设备凭据锁定应用。",
    "Overlay permission": "悬浮窗权限",
    "Delete all clips?": "删除全部条目？",
    "This permanently removes every saved clip from your history.":
        "将从历史中永久删除全部已保存条目。",
    "A simple clipboard history app. Everything you copy is saved locally — no account, no cloud, no tracking.":
        "简洁的本地剪贴板历史应用。复制内容全部保存在本机 — 无账号、不上云、无追踪。",
    "No duplicate clips found — nice and tidy.": "未发现重复条目，历史很干净。",
    "Auto-delete after": "自动删除时间",
    "Older clips are removed automatically.": "较旧的条目会被自动移除。",
    "1 year": "1 年",
    "Pick the look that suits you.": "选择你喜欢的外观。",
    "Follow system": "跟随系统",
    "Light": "浅色",
    "Dark": "深色",
    "AMOLED Black": "AMOLED 纯黑",
    "Tap the bubble to save the current clipboard.": "点按气泡即可保存当前剪贴板。",
    "Tap to grant the overlay permission.": "点按以授予悬浮窗权限。",
    "After 30 days": "30 天后",
    "After 90 days": "90 天后",
    "After 1 year": "1 年后",
    "Export format": "导出格式",
    "Pick how to encode your export.": "选择导出文件使用的格式。",
    "Each group shares the same content. Merging keeps the newest clip (pinned first) and folds tags, collections and use counts into it.":
        "每一组内容相同。合并时保留最新条目（置顶优先），并把标签、合集与使用次数并入该条目。",
    # —— 新手引导 ——
    "Never lose a copy again": "复制内容永不丢失",
    "Clipboard Manager saves everything you copy so you can find and re-paste it later — even days after you copied it.":
        "剪贴板管理器会保存你复制的全部内容，方便日后查找并再次粘贴 — 哪怕是几天前复制的。",
    "All data stays on your phone. No accounts, no cloud, no tracking.":
        "所有数据都保存在你的手机上。无账号、不上云、无追踪。",
    "Already saving copies": "已开始保存复制内容",
    "A small notification runs while you use your phone. It captures everything you copy into your history list.":
        "使用手机时会有一条常驻通知，把复制的内容写入历史列表。",
    "You can pause it any time from the notification or Settings.":
        "可随时在通知或设置中暂停该功能。",
    "Accessibility enabled": "无障碍已启用",
    "Accessibility not enabled": "无障碍未启用",
    "Capture copies in the background": "在后台捕获复制内容",
    "Android 10+ blocks background clipboard access for privacy. ":
        "Android 10 及以上出于隐私保护会限制后台读取剪贴板。 ",
    "Enabling this optional Accessibility service lets the app detect when you copy ":
        "启用这项可选的无障碍服务后，应用才能感知你何时复制，",
    "in any other app — even when it's not open.":
        "即使是在其它应用中、或本应用未打开时。",
    "Open settings": "打开设置",
    "Enable accessibility": "启用无障碍",
    "Optional — you can skip and enable later in Settings.":
        "可选 — 可先跳过，稍后在设置中启用。",
    "Quick access shortcuts": "快捷访问入口",
    "Two optional ways to grab a copy from anywhere:":
        "以下两种可选方式，可让你在任意界面快速保存复制内容：",
    "A small draggable bubble that saves the current clipboard when you tap it.":
        "可拖动的小气泡；点按即可保存当前剪贴板。",
    "Overlay permission granted": "已授予悬浮窗权限",
    "Overlay permission needed": "需要悬浮窗权限",
    "Grant permission": "授予权限",
    "Pull down the notification shade, tap the pencil icon, then drag ":
        "下拉通知栏，点铅笔图标，然后把 ",
    "\"Clipboard history\" onto the bar.":
        "「剪贴板历史」拖到快捷栏。",
    "Manual setup": "需手动设置",
    "Both are optional. You can enable them later from Settings.":
        "两者均为可选，之后可在设置中开启。",
    "Get started": "开始使用",
    "Granted": "已授权",
    "Running": "运行中",
    "Paused": "已暂停",
    # —— 悬浮气泡 / 后台服务提示 ——
    "Clipboard is empty": "剪贴板为空",
    "Already saved": "已保存过",
    "Cannot read clipboard right now": "暂时无法读取剪贴板",
    "Could not save": "无法保存",
    "Failed to add bubble overlay": "无法添加悬浮气泡",
    "Image saved": "图片已保存",
    # —— 系统字符串 resources ——
    "Clipboard Manager is active": "剪贴板管理器运行中",
    "Waiting for copied text…": "正在等待复制的文本…",
    "Pause": "暂停",
    "Resume": "继续",
    "Stop": "停止",
    "Clipboard monitor": "剪贴板监控",
    "Keeps clipboard history in sync while the app is in use.":
        "应用使用期间持续同步维护剪贴板历史。",
    "Clipboard bubble is active": "剪贴板气泡运行中",
    "Tap the floating bubble to save the current clipboard.":
        "点按悬浮气泡即可保存当前剪贴板。",
    "Controls the floating clipboard bubble overlay.":
        "用于控制剪贴板悬浮气泡叠层。",
    "Clipboard bubble": "剪贴板气泡",
    "Clipboard history": "剪贴板历史",
    "Recent clipboard items": "最近的剪贴板条目",
    "Detects when you copy text in other apps and saves it to your clipboard history. No data leaves your device.":
        "检测你在其它应用中复制的文本，并保存到剪贴板历史。数据不会离开本机。",
    # —— 含转义引号 / 模板（源码字面量）——
    "To show the floating bubble, Android requires the \"draw over other apps\" permission.":
        "要显示悬浮气泡，Android 需要「显示在其他应用上层」权限。",
    "Nothing in your history matches \"${state.query}\".":
        "历史中没有与「${state.query}」匹配的内容。",
    "Nothing found for \"${state.query}\".":
        "没有找到与「${state.query}」相关的内容。",
    "${event.clips.size} clips deleted": "已删除 ${event.clips.size} 条",
    "$selectedCount selected": "已选中 $selectedCount 项",
    "Clip: ${clip.preview}": "条目：${clip.preview}",
    "Image clip, copied ${formatTime(clip.createdAt)}":
        "图片条目，复制于 ${formatTime(clip.createdAt)}",
    "Imported $count clips": "已导入 $count 条",
    "Export failed: ${e.message}": "导出失败：${e.message}",
    "Import failed: ${e.message}": "导入失败：${e.message}",
    "Authentication failed: $msg": "身份验证失败：$msg",
    "Version ${BuildConfig.VERSION_NAME}": "版本 ${BuildConfig.VERSION_NAME}",
    "Debug crash logs (${crashReports.size})": "调试崩溃日志（${crashReports.size}）",
    "$current of $total": "$current / $total",
    "Next to paste: #${currentIndex.coerceIn(0, items.size - 1) + 1} of ${items.size}":
        "下一条粘贴：第 ${currentIndex.coerceIn(0, items.size - 1) + 1} / ${items.size} 条",
    "Exported ${clips.size} clips as ${selectedExportFormat.extension.uppercase()}":
        "已将 ${clips.size} 条导出为 ${selectedExportFormat.extension.uppercase()}",
    "Save all clips as ${selectedExportFormat.extension.uppercase()}.":
        "把全部条目导出为 ${selectedExportFormat.extension.uppercase()}。",
    "${minutes}m": "${minutes} 分",
    "${minutes / 60}h": "${minutes / 60} 小时",
    "${minutes / (24 * 60)}d": "${minutes / (24 * 60)} 天",
    "⏳ ${formatRemaining(clip.expiresAt!!)}": "⏳ ${formatRemaining(clip.expiresAt!!)}后删除",
    "in ${formatRemaining(it, now)}": "${formatRemaining(it, now)}后",
    "${clip.useCount}/${clip.useLimit} uses": "${clip.useCount}/${clip.useLimit} 次",
    "Copy text anywhere — it shows up here.\\nShake to clear history · long-press to multi-select.":
        "在任意处复制文本，都会出现在这里。\\n摇一摇可清空历史 · 长按可多选。",
    "\"Clipboard history\" onto the bar.": "「剪贴板历史」拖到快捷栏。",
    # 时间格式（若源码为独立字面量）
    "m ago": " 分钟前",
    "h ago": " 小时前",
    "d ago": " 天前",
    "🔒 Locked": "🔒 已锁定",
}

# 源码中带引号的完整替换（含引号）
QUOTED_EXTRA: list[tuple[str, str]] = [
    (
        '"Copy text anywhere — it shows up here.\\nShake to clear history · long-press to multi-select."',
        '"在任意处复制文本，都会出现在这里。\\n摇一摇可清空历史 · 长按可多选。"',
    ),
    (
        'text = "Android 10+ blocks background clipboard access for privacy. " +\n'
        '                "Enabling this optional Accessibility service lets the app detect when you copy " +\n'
        '                "in any other app — even when it\'s not open.",',
        'text = "Android 10 及以上出于隐私保护会限制后台读取剪贴板。 " +\n'
        '                "启用这项可选的无障碍服务后，应用才能感知你何时复制，" +\n'
        '                "即使是在其它应用中、或本应用未打开时。",',
    ),
    (
        'description = "Pull down the notification shade, tap the pencil icon, then drag " +\n'
        '                "\\"Clipboard history\\" onto the bar.",',
        'description = "下拉通知栏，点铅笔图标，然后把 " +\n'
        '                "「剪贴板历史」拖到快捷栏。",',
    ),
]

# 允许扫描的相对路径（避开 data/dao、SQL、依赖注入）
ALLOW = (
    "ui/",
    "app/",
    "service/",
    "widget/",
    "util/DateFormatters.kt",
    "util/CrashReporter.kt",
    "domain/TextTransformer.kt",
    "domain/model/Clip.kt",
    "data/security/BiometricManager.kt",
)
DENY = (
    "data/local",
    "data/repository",
    "data/export",
    "data/preferences",
    "di/",
    "domain/PasteQueue",
)


def iter_target_files() -> list[pathlib.Path]:
    files: list[pathlib.Path] = []
    for path in JAVA.rglob("*.kt"):
        rel = str(path.relative_to(JAVA)).replace("\\", "/")
        if any(d in rel for d in DENY):
            continue
        if any(rel.startswith(a) or rel == a for a in ALLOW):
            files.append(path)
    return files


def patch_literal(text: str, en: str, zh: str) -> tuple[str, int]:
    n = 0
    # 1) 带引号完整字面量
    en_q = f'"{en}"'
    zh_q = f'"{zh}"'
    if en_q in text:
        n += text.count(en_q)
        text = text.replace(en_q, zh_q)
    # 2) 字符串拼接片段：源码里是 "...English "（带结尾空格时 MAP 里已含）
    # 仅当英文含空格或标点、且以引号形式出现在拼接中
    if n == 0 and (" " in en or en.endswith((".", "?", "!", "…", ":", "。"))):
        en_frag = f'"{en}"'
        if en_frag in text:
            n += text.count(en_frag)
            text = text.replace(en_frag, f'"{zh}"')
    return text, n


def main() -> None:
    total = 0
    for path in iter_target_files():
        text = path.read_text(encoding="utf-8")
        orig = text
        file_n = 0
        for en_q, zh_q in QUOTED_EXTRA:
            if en_q in text:
                c = text.count(en_q)
                text = text.replace(en_q, zh_q)
                file_n += c
        # 长 key 优先，避免短词误伤完整句（仍只替换完整 "key"）
        for en, zh in sorted(MAP.items(), key=lambda kv: -len(kv[0])):
            text, n = patch_literal(text, en, zh)
            file_n += n
        if text != orig:
            path.write_text(text, encoding="utf-8", newline="\n")
            rel = path.relative_to(ROOT)
            print(f"[{file_n}] {rel}")
            total += file_n

    if STRINGS.exists():
        text = STRINGS.read_text(encoding="utf-8")
        orig = text
        for en, zh in sorted(MAP.items(), key=lambda kv: -len(kv[0])):
            text = text.replace(f">{en}<", f">{zh}<")
        if text != orig:
            STRINGS.write_text(text, encoding="utf-8", newline="\n")
            print("[xml] strings.xml")
            total += 1
    print("TOTAL", total)


if __name__ == "__main__":
    main()
