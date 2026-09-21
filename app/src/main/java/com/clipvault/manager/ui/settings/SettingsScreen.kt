package com.clipvault.manager.ui.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BubbleChart
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clipvault.manager.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private data class IconAccent(val bg: Color, val tint: Color)

private val Indigo = IconAccent(Color(0xFFEEF2FF), Color(0xFF4F46E5))
private val Amber = IconAccent(Color(0xFFFEF3C7), Color(0xFFD97706))
private val Blue = IconAccent(Color(0xFFDBEAFE), Color(0xFF2563EB))
private val Violet = IconAccent(Color(0xFFEDE9FE), Color(0xFF7C3AED))
private val Green = IconAccent(Color(0xFFDCFCE7), Color(0xFF16A34A))
private val Pink = IconAccent(Color(0xFFFCE7F3), Color(0xFFDB2777))
private val Slate = IconAccent(Color(0xFFF1F5F9), Color(0xFF475569))
private val Red = IconAccent(Color(0xFFFEE2E2), Color(0xFFDC2626))
private val Purple = IconAccent(Color(0xFFF3E8FF), Color(0xFF9333EA))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showClearDialog by remember { mutableStateOf(false) }
    var showBubbleDialog by remember { mutableStateOf(false) }
    var showRetentionSheet by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showDuplicatesDialog by remember { mutableStateOf(false) }
    var showExportFormatSheet by remember { mutableStateOf(false) }
    var selectedExportFormat by remember { mutableStateOf(com.clipvault.manager.data.export.ExportFormat.JSON) }
    var exportMessage by remember { mutableStateOf<String?>(null) }
    var isExporting by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("*/*")
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                isExporting = true
                try {
                    val clips = viewModel.exportAllClips()
                    val content = com.clipvault.manager.data.export.ClipExporter.export(clips, selectedExportFormat)
                    withContext(Dispatchers.IO) {
                        context.contentResolver.openOutputStream(uri)?.use { stream ->
                            stream.write(content.toByteArray())
                        }
                    }
                    exportMessage = "已将 ${clips.size} 条导出为 ${selectedExportFormat.extension.uppercase()}"
                } catch (e: Exception) {
                    exportMessage = "导出失败：${e.message}"
                } finally {
                    isExporting = false
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                try {
                    val clips = withContext(Dispatchers.IO) {
                        val raw = context.contentResolver.openInputStream(uri)?.use { stream ->
                            stream.bufferedReader().readText()
                        } ?: throw Exception("无法读取文件")
                        if (raw.trimStart().startsWith("[")) {
                            com.clipvault.manager.data.export.ClipJsonExporter.importFromJson(raw)
                        } else if (raw.trimStart().startsWith("{")) {
                            com.clipvault.manager.data.export.ClipJsonExporter.importFromJson(raw)
                        } else {
                            throw Exception("导入仅支持本应用导出的 JSON 文件。")
                        }
                    }
                    val count = viewModel.importClips(clips)
                    exportMessage = "已导入 $count 条"
                } catch (e: Exception) {
                    exportMessage = "导入失败：${e.message}"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
        ) {
            // ── Monitoring ──────────────────────────────────────────────
            item { SectionHeader("监控") }
            item {
                SettingsCard {
                    ToggleRow(
                        accent = Indigo,
                        icon = Icons.Outlined.ContentPaste,
                        title = "剪贴板监控",
                        subtitle = "保存你在使用手机时复制的全部内容。",
                        checked = state.monitoringEnabled,
                        onCheckedChange = viewModel::setMonitoring
                    )
                }
            }

            // ── Privacy ─────────────────────────────────────────────
            item { SectionHeader("隐私") }
            item {
                SettingsCard {
                    ToggleRow(
                        accent = Red,
                        icon = Icons.Outlined.VisibilityOff,
                        title = "隐藏敏感内容",
                        subtitle = "在通知、小组件与搜索结果中隐藏条目正文。",
                        checked = state.maskSensitiveContent,
                        onCheckedChange = viewModel::setMaskSensitiveContent
                    )
                    HorizontalDivider()
                    ToggleRow(
                        accent = Purple,
                        icon = Icons.Outlined.Fingerprint,
                        title = "打开时需要生物识别",
                        subtitle = "使用生物识别或设备凭据锁定应用。",
                        checked = state.requireBiometric,
                        onCheckedChange = viewModel::setRequireBiometric
                    )
                }
            }

            // ── Quick access ────────────────────────────────────────────
            item { SectionHeader("快捷访问") }
            item {
                SettingsCard {
                    ToggleRow(
                        accent = Amber,
                        icon = Icons.Outlined.BubbleChart,
                        title = "悬浮气泡",
                        subtitle = bubbleSubtitle(context),
                        checked = state.bubbleEnabled,
                        onCheckedChange = { requested ->
                            if (requested && !canDrawOverlays(context)) {
                                showBubbleDialog = true
                            } else {
                                viewModel.setBubbleEnabled(requested)
                            }
                        }
                    )
                    SettingsDivider()
                    ChevronRow(
                        accent = Blue,
                        icon = Icons.Outlined.Widgets,
                        title = "快捷设置磁贴",
                        subtitle = "下拉通知栏 → 点编辑 → 拖到快捷栏。"
                    )
                    SettingsDivider()
                    ChevronRow(
                        accent = Violet,
                        icon = Icons.Outlined.AccessibilityNew,
                        title = "无障碍服务",
                        subtitle = "可选。用于在任意应用中捕获复制内容。",
                        onClick = { runCatching { context.startActivity(viewModel.accessibilitySettingsIntent()) } }
                    )
                }
            }

            // ── Organize ─────────────────────────────────────────────────
            item { SectionHeader("整理") }
            item {
                SettingsCard {
                    ChevronRow(
                        accent = Indigo,
                        icon = Icons.Outlined.Sell,
                        title = "标签",
                        subtitle = "用自定义标签整理条目。",
                        onClick = { onNavigate(com.clipvault.manager.ui.nav.Route.Tags.path) }
                    )
                    SettingsDivider()
                    ChevronRow(
                        accent = Violet,
                        icon = Icons.Outlined.Folder,
                        title = "合集",
                        subtitle = "把相关条目归入合集文件夹。",
                        onClick = { onNavigate(com.clipvault.manager.ui.nav.Route.Collections.path) }
                    )
                }
            }

            // ── Cleanup ─────────────────────────────────────────────────
            item { SectionHeader("清理") }
            item {
                SettingsCard {
                    ChevronRow(
                        accent = Green,
                        icon = Icons.Outlined.AutoAwesome,
                        title = "自动删除条目",
                        subtitle = retentionLabel(state.retentionDays),
                        onClick = { showRetentionSheet = true }
                    )
                    SettingsDivider()
                    ChevronRow(
                        accent = Pink,
                        icon = Icons.Outlined.Palette,
                        title = "主题",
                        subtitle = themeLabel(state.themeMode),
                        onClick = { showThemeSheet = true }
                    )
                    SettingsDivider()
                    ChevronRow(
                        accent = Green,
                        icon = Icons.Outlined.ContentCopy,
                        title = "查找重复项",
                        subtitle = "合并内容相同的条目，保持历史整洁。",
                        onClick = {
                            showDuplicatesDialog = true
                        }
                    )
                }
            }

            // ── Data ──────────────────────────────────────────────────
            item { SectionHeader("数据") }
            item {
                SettingsCard {
                    ChevronRow(
                        accent = Blue,
                        icon = Icons.Outlined.FileUpload,
                        title = "导出历史",
                        subtitle = "把全部条目导出为 ${selectedExportFormat.extension.uppercase()}。",
                        onClick = {
                            if (!isExporting) showExportFormatSheet = true
                        }
                    )
                    if (isExporting) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "导出中…",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    SettingsDivider()
                    ChevronRow(
                        accent = Green,
                        icon = Icons.Outlined.FileDownload,
                        title = "导入历史",
                        subtitle = "从 JSON 备份恢复条目。",
                        onClick = { importLauncher.launch(arrayOf("application/json")) }
                    )
                    if (exportMessage != null) {
                        Text(
                            text = exportMessage!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // ── About ───────────────────────────────────────────────────
            item { SectionHeader("关于") }
            item {
                SettingsCard {
                    // Debug: appears only when the in-app crash reporter has
                    // captured something. Tap copies the newest stack trace
                    // so it can be pasted straight into a chat.
                    var crashReports by remember {
                        mutableStateOf(com.clipvault.manager.util.CrashReporter.pendingReports(context))
                    }
                    LaunchedEffect(Unit) {
                        crashReports = com.clipvault.manager.util.CrashReporter.pendingReports(context)
                    }
                    if (crashReports.isNotEmpty()) {
                        ChevronRow(
                            accent = Red,
                            icon = Icons.Outlined.BugReport,
                            title = "调试崩溃日志（${crashReports.size}）",
                            subtitle = "点按可复制最新的崩溃堆栈。",
                            onClick = {
                                val text = com.clipvault.manager.util.CrashReporter.latestReportText(context)
                                    ?: "没有报告内容"
                                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE)
                                    as android.content.ClipboardManager
                                cm.setPrimaryClip(
                                    android.content.ClipData.newPlainText("ClipVault 崩溃日志", text.take(90_000))
                                )
                                android.widget.Toast.makeText(
                                    context,
                                    "已复制最新崩溃日志 — 可粘贴给开发者",
                                    android.widget.Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                        SettingsDivider()
                    }
                    ChevronRow(
                        accent = Slate,
                        icon = Icons.Outlined.Info,
                        title = "剪贴板管理器",
                        subtitle = "版本 ${BuildConfig.VERSION_NAME}",
                        onClick = { showAboutDialog = true }
                    )
                    SettingsDivider()
                    ChevronRow(
                        accent = Amber,
                        icon = Icons.Outlined.RestartAlt,
                        title = "重新运行设置引导",
                        subtitle = "再走一遍新手引导流程。",
                        onClick = { viewModel.resetOnboarding() }
                    )
                    SettingsDivider()
                    ChevronRow(
                        accent = Red,
                        icon = Icons.Outlined.DeleteForever,
                        title = "删除全部条目",
                        subtitle = "删除全部已保存条目，置顶项会保留。",
                        destructive = true,
                        onClick = { showClearDialog = true }
                    )
                }
            }

            // ── Footer ──────────────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "所有数据仅保存在本机",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "无账号 · 无追踪 · 不上云",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // ── Dialogs ─────────────────────────────────────────────────────

    if (showBubbleDialog) {
        AlertDialog(
            onDismissRequest = { showBubbleDialog = false },
            icon = { Icon(Icons.Outlined.BubbleChart, contentDescription = null, tint = Amber.tint) },
            title = { Text("悬浮窗权限") },
            text = { Text("要显示悬浮气泡，Android 需要「显示在其他应用上层」权限。") },
            confirmButton = {
                TextButton(onClick = {
                    runCatching { context.startActivity(viewModel.overlayPermissionIntent()) }
                    showBubbleDialog = false
                }) { Text("授权") }
            },
            dismissButton = {
                TextButton(onClick = { showBubbleDialog = false }) { Text("取消") }
            }
        )
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            icon = { Icon(Icons.Outlined.DeleteForever, contentDescription = null, tint = Red.tint) },
            title = { Text("删除全部条目？") },
            text = { Text("将从历史中永久删除全部已保存条目。") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAll()
                    showClearDialog = false
                }) { Text("删除") }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("取消") }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            icon = { Icon(Icons.Outlined.Info, contentDescription = null, tint = Slate.tint) },
            title = { Text("剪贴板管理器") },
            text = {
                Column {
                    Text("版本 ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "简洁的本地剪贴板历史应用。复制内容全部保存在本机 — 无账号、不上云、无追踪。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = { TextButton(onClick = { showAboutDialog = false }) { Text("完成") } }
        )
    }

    if (showDuplicatesDialog) {
        val duplicates by viewModel.duplicates.collectAsStateWithLifecycle()
        LaunchedEffect(showDuplicatesDialog) {
            if (showDuplicatesDialog) viewModel.refreshDuplicates()
        }
        AlertDialog(
            onDismissRequest = { showDuplicatesDialog = false },
            icon = { Icon(Icons.Outlined.ContentCopy, contentDescription = null, tint = Green.tint) },
            title = { Text("查找重复项") },
            text = {
                if (duplicates.isEmpty()) {
                    Text("未发现重复条目，历史很干净。")
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            "每一组内容相同。合并时保留最新条目（置顶优先），并把标签、合集与使用次数并入该条目。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(12.dp))
                        duplicates.forEach { group ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = group.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${group.count} 次复制",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                TextButton(onClick = {
                                    viewModel.mergeDuplicate(group.keepId, group.content)
                                }) { Text("合并") }
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showDuplicatesDialog = false }) { Text("完成") } }
        )
    }

    if (showRetentionSheet) {
        RetentionPickerSheet(
            selected = state.retentionDays,
            onSelect = { viewModel.setRetention(it); showRetentionSheet = false },
            onDismiss = { showRetentionSheet = false }
        )
    }

    if (showThemeSheet) {
        ThemePickerSheet(
            selected = state.themeMode,
            onSelect = { viewModel.setTheme(it); showThemeSheet = false },
            onDismiss = { showThemeSheet = false }
        )
    }

    if (showExportFormatSheet) {
        ExportFormatPickerSheet(
            selected = selectedExportFormat,
            onDismiss = { showExportFormatSheet = false },
            onSelect = { format ->
                if (isExporting) return@ExportFormatPickerSheet
                selectedExportFormat = format
                showExportFormatSheet = false
                val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.US)
                    .format(java.util.Date())
                exportLauncher.launch("clipvault_export_$timestamp.${format.extension}")
            }
        )
    }
}

// ── Layout primitives ─────────────────────────────────────────────

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.2.sp
        ),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(content = content)
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 64.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        thickness = 1.dp
    )
}

@Composable
private fun ToggleRow(
    accent: IconAccent,
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    // Use `toggleable` on the row and let the Switch react purely to the
    // hoisted `checked` state. The previous `.clickable { onCheckedChange(!checked) }`
    // combined with the Switch's own `onCheckedChange` could fire twice on a
    // single tap (row click → toggle, then Switch click → toggle again),
    // which both flipped the switch back and emitted two DataStore writes
    // per tap — the visible cause of the "stuttery" feel on every row.
    Row(
        modifier = Modifier
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBadge(accent, icon)
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun ChevronRow(
    accent: IconAccent,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    destructive: Boolean = false
) {
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = clickableModifier
            .semantics { role = Role.Button }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBadge(accent, icon)
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (destructive) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun IconBadge(accent: IconAccent, icon: ImageVector) {
    // Tint-at-alpha background adapts to light/dark/AMOLED schemes (no hardcoded pastels).
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(accent.tint.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent.tint,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ── Bottom sheets ──────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RetentionPickerSheet(
    selected: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                "自动删除时间",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 4.dp)
            )
            Text(
                "较旧的条目会被自动移除。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
            )
            listOf(0, 7, 30, 90, 365).forEach { days ->
                SheetOption(
                    label = when (days) {
                        0 -> "永不"
                        365 -> "1 年"
                        else -> "$days 天"
                    },
                    selected = selected == days,
                    onClick = { onSelect(days) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemePickerSheet(
    selected: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                "主题",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 4.dp)
            )
            Text(
                "选择你喜欢的外观。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
            )
            listOf(
                0 to "跟随系统",
                1 to "浅色",
                2 to "深色",
                3 to "AMOLED 纯黑"
            ).forEach { (value, label) ->
                SheetOption(
                    label = label,
                    selected = selected == value,
                    onClick = { onSelect(value) }
                )
            }
        }
    }
}

@Composable
private fun SheetOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .semantics { role = Role.Button }
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
        AnimatedVisibility(visible = selected, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

// ── Helpers ────────────────────────────────────────────────────────

@Composable
private fun bubbleSubtitle(context: Context): String {
    val overlayGranted = canDrawOverlays(context)
    return if (overlayGranted) "点按气泡即可保存当前剪贴板。"
    else "点按以授予悬浮窗权限。"
}

private fun canDrawOverlays(context: Context): Boolean =
    Settings.canDrawOverlays(context)

private fun retentionLabel(days: Int): String = when (days) {
    0 -> "永不"
    7 -> "7 天后"
    30 -> "30 天后"
    90 -> "90 天后"
    365 -> "1 年后"
    else -> "$days 天后"
}

private fun themeLabel(mode: Int): String = when (mode) {
    0 -> "跟随系统"
    1 -> "浅色"
    2 -> "深色"
    3 -> "AMOLED 纯黑"
    else -> "跟随系统"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExportFormatPickerSheet(
    selected: com.clipvault.manager.data.export.ExportFormat,
    onDismiss: () -> Unit,
    onSelect: (com.clipvault.manager.data.export.ExportFormat) -> Unit
) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                "导出格式",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 4.dp)
            )
            Text(
                "选择导出文件使用的格式。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
            )
            com.clipvault.manager.data.export.ExportFormat.entries.forEach { format ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(format) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        format.extension.uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (selected == format) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selected == format) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    AnimatedVisibility(visible = selected == format, enter = fadeIn(), exit = fadeOut()) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
    }
}