#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""ClipVault 汉化补丁：仅处理剩余用户可见文案，全部为完整字符串字面量。"""

from __future__ import annotations

import pathlib

ROOT = pathlib.Path(__file__).resolve().parent.parent
JAVA = ROOT / "app/src/main/java/com/clipvault/manager"

# 精确 (旧字面量片段含引号, 新字面量含引号)
REPLACEMENTS: list[tuple[str, str]] = [
    # Search
    (
        r'"Nothing in your history matches \"${state.query}\"."',
        r'"历史中没有与「${state.query}」匹配的内容。"',
    ),
    # Settings overlay permission
    (
        r'"To show the floating bubble, Android requires the \"draw over other apps\" permission."',
        r'"要显示悬浮气泡，Android 需要「显示在其他应用上层」权限。"',
    ),
    # Settings nav
    ('title = "Tags",', 'title = "标签",'),
    ('title = "Collections",', 'title = "合集",'),
    # Screens top bar
    ('Text("Tags")', 'Text("标签")'),
    ('Text("Collections")', 'Text("合集")'),
    ('SectionLabel("Tags")', 'SectionLabel("标签")'),
    ('SectionLabel("Collections")', 'SectionLabel("合集")'),
    # Stats
    ('label = "Total clips",', 'label = "条目总数",'),
    ('label = "Today",', 'label = "今天",'),
    ('label = "This week",', 'label = "本周",'),
    ('"By type",', '"按类型",'),
    # Home
    (
        r'snackbarHostState.show("${event.count} clip${if (event.count == 1) \"\" else \"s\"} updated")',
        r'snackbarHostState.show("已更新 ${event.count} 条")',
    ),
    (
        r'text = "$count saved · ${if (monitoringActive) \"live\" else \"paused\"}",',
        r'text = "已保存 $count 条 · ${if (monitoringActive) \"监控中\" else \"已暂停\"}",',
    ),
    ('contentDescription = "Pin",', 'contentDescription = "置顶",'),
    ('"Copy code · $code",', '"复制验证码 · $code",'),
    ('clip.createdAt >= startOfToday -> "Today"', 'clip.createdAt >= startOfToday -> "今天"'),
    # Detail
    ('Text("Go back")', 'Text("返回")'),
    ('label = "Share",', 'label = "分享",'),
    ('"Unlock clip" to "验证身份以解除该条目的锁定。"', '"解锁条目" to "验证身份以解除该条目的锁定。"'),
    ('"Lock clip" to "验证身份以锁定该条目的备注。"', '"锁定条目" to "验证身份以锁定该条目的备注。"'),
    # Multi-select / swipe
    ('contentDescription = "Pin")', 'contentDescription = "置顶")'),
    ('"Pin",', '"置顶",'),
    # Tags / Collections usage
    (
        r'text = "$usageCount clip${if (usageCount == 1) \"\" else \"s\"}",',
        r'text = "$usageCount 条",',
    ),
    # Settings duplicates / retention
    ('text = "${group.count} copies",', 'text = "${group.count} 次复制",'),
    ('else -> "$days days"', 'else -> "$days 天"'),
    ('7 -> "After 7 days"', '7 -> "7 天后"'),
    ('else -> "After $days days"', 'else -> "$days 天后"'),
    # DateFormatters
    ('"${diff / MINUTE_MS}m ago"', '"${diff / MINUTE_MS} 分钟前"'),
    ('"${diff / HOUR_MS}h ago"', '"${diff / HOUR_MS} 小时前"'),
    ('"${diff / DAY_MS}d ago"', '"${diff / DAY_MS} 天前"'),
    # TextTransformer errors
    ('"Failed to encode URL: ${e.message}"', '"URL 编码失败：${e.message}"'),
    ('"Failed to decode URL: ${e.message}"', '"URL 解码失败：${e.message}"'),
    ('"Failed to encode Base64: ${e.message}"', '"Base64 编码失败：${e.message}"'),
    ('"Failed to decode Base64: ${e.message}"', '"Base64 解码失败：${e.message}"'),
    ('"Invalid JSON: ${e.message}"', '"JSON 无效：${e.message}"'),
    # Snippets delete / tags delete (exact source forms)
    (
        r'"${snippet.title}\" will be removed."',
        r'"「${snippet.title}」将被移除。"',
    ),
    (
        r'"${snippet.title}" will be removed."',
        r'"「${snippet.title}」将被移除。"',
    ),
    # 源码中转义引号形式
    (
        r'"Nothing found for \"${state.query}\"."',
        r'"没有找到与「${state.query}」相关的内容。"',
    ),
    (
        r'"\"${snippet.title}\" will be removed."',
        r'"「${snippet.title}」将被移除。"',
    ),
    (
        r'"Remove the \"${tag.name}\" tag. Clips will be untagged but kept."',
        r'"移除标签「${tag.name}」。条目会取消该标签，但不会删除。"',
    ),
    (
        r'"Remove \"${collection.name}\". Clips will stay in your history."',
        r'"移除合集「${collection.name}」。其中的条目仍会保留在历史中。"',
    ),
    (
        r'text = "${snippet.useCount} use${if (snippet.useCount == 1) \"\" else \"s\"}",',
        r'text = "已使用 ${snippet.useCount} 次",',
    ),
]

# 按文件内容精确匹配的更大块（读入后整段替换）
FILE_BLOCKS: list[tuple[str, str, str]] = [
    # rel_path, old, new
    (
        "ui/tags/TagsScreen.kt",
        r'text = "$usageCount clip${if (usageCount == 1) "" else "s"}",',
        r'text = "$usageCount 条",',
    ),
    (
        "ui/collections/CollectionsScreen.kt",
        r'text = "$usageCount clip${if (usageCount == 1) "" else "s"}",',
        r'text = "$usageCount 条",',
    ),
]


def main() -> None:
    total = 0
    # 全局精确替换（带引号的完整 token）
    for path in JAVA.rglob("*.kt"):
        rel = str(path.relative_to(JAVA)).replace("\\", "/")
        if any(x in rel for x in ("data/local", "data/repository", "data/export", "data/preferences", "di/")):
            continue
        text = path.read_text(encoding="utf-8")
        orig = text
        for old, new in REPLACEMENTS:
            if old in text:
                text = text.replace(old, new)
        for rel_b, old, new in FILE_BLOCKS:
            if rel.replace("\\", "/") == rel_b and old in text:
                text = text.replace(old, new)
        if text != orig:
            path.write_text(text, encoding="utf-8", newline="\n")
            print("updated", rel)
            total += 1
    print("files", total)


if __name__ == "__main__":
    main()
