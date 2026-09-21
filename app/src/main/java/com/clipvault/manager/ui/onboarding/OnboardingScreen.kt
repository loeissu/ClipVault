package com.clipvault.manager.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BubbleChart
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clipvault.manager.haptic.rememberHaptics

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val haptics = rememberHaptics()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            ProgressHeader(
                current = state.currentPage + 1,
                total = state.totalPages,
                onBack = {
                    haptics.light()
                    viewModel.previous()
                },
                showBack = state.currentPage > 0
            )

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(
                targetState = state.currentPage,
                transitionSpec = {
                    val direction = if (targetState > initialState) 1 else -1
                    (slideInHorizontally(
                        animationSpec = tween(400),
                        initialOffsetX = { it * direction / 4 }
                    ) + fadeIn(animationSpec = tween(400)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(400))) togetherWith
                        (slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { -it * direction / 4 }
                        ) + fadeOut(animationSpec = tween(300)) +
                            scaleOut(targetScale = 0.96f, animationSpec = tween(300)))
                },
                label = "onboarding-page"
            ) { page ->
                when (page) {
                    0 -> WelcomePage()
                    1 -> BackgroundServicePage(monitoringOn = state.monitoringOn)
                    2 -> AccessibilityPage(
                        granted = state.accessibilityGranted,
                        onEnable = {
                            haptics.medium()
                            viewModel.enableAccessibility()
                        }
                    )
                    3 -> BubbleAndTilePage(
                        overlayGranted = state.overlayGranted,
                        onGrantOverlay = {
                            haptics.medium()
                            viewModel.enableOverlay()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            BottomActions(
                isLastPage = state.currentPage == state.totalPages - 1,
                onNext = {
                    if (state.currentPage == state.totalPages - 1) {
                        haptics.success()
                        viewModel.finish()
                        onFinished()
                    } else {
                        haptics.light()
                        viewModel.next()
                    }
                }
            )
        }
    }
}

@Composable
private fun ProgressHeader(
    current: Int,
    total: Int,
    onBack: () -> Unit,
    showBack: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            TextButton(onClick = onBack) {
                Text("返回")
            }
        } else {
            Spacer(modifier = Modifier.width(64.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$current / $total",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    LinearProgressIndicator(
        progress = { current.toFloat() / total.toFloat() },
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp),
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

@Composable
private fun WelcomePage() {
    PageScaffold(
        icon = Icons.Outlined.ContentPaste,
        title = "复制内容永不丢失",
        body = "剪贴板管理器会保存你复制的全部内容，方便日后查找并再次粘贴 — 哪怕是几天前复制的。",
        footer = "所有数据都保存在你的手机上。无账号、不上云、无追踪。"
    )
}

@Composable
private fun BackgroundServicePage(monitoringOn: Boolean) {
    PageScaffold(
        icon = Icons.Outlined.ContentPaste,
        title = "已开始保存复制内容",
        body = "使用手机时会有一条常驻通知，把复制的内容写入历史列表。",
        footer = "可随时在通知或设置中暂停该功能。",
        statusBadge = if (monitoringOn) StatusBadge.Active else StatusBadge.Inactive
    )
}

@Composable
private fun AccessibilityPage(
    granted: Boolean,
    onEnable: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BigIcon(Icons.Filled.Accessibility)
        Spacer(modifier = Modifier.height(16.dp))
        StatusRow(
            label = if (granted) "无障碍已启用" else "无障碍未启用",
            isGranted = granted
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "在后台捕获复制内容",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Android 10 及以上出于隐私保护会限制后台读取剪贴板。 " +
                "启用这项可选的无障碍服务后，应用才能感知你何时复制，" +
                "即使是在其它应用中、或本应用未打开时。",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (granted) {
            OutlinedButton(onClick = onEnable, modifier = Modifier.fillMaxWidth()) {
                Text("打开设置")
            }
        } else {
            Button(
                onClick = onEnable,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("启用无障碍")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "可选 — 可先跳过，稍后在设置中启用。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BubbleAndTilePage(
    overlayGranted: Boolean,
    onGrantOverlay: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BigIcon(Icons.Outlined.BubbleChart)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "快捷访问入口",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "以下两种可选方式，可让你在任意界面快速保存复制内容：",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        ShortcutRow(
            icon = Icons.Outlined.BubbleChart,
            title = "悬浮气泡",
            description = "可拖动的小气泡；点按即可保存当前剪贴板。",
            status = if (overlayGranted) "已授予悬浮窗权限" else "需要悬浮窗权限",
            isGranted = overlayGranted,
            actionLabel = if (overlayGranted) "已授权" else "授予权限",
            onAction = if (!overlayGranted) onGrantOverlay else null
        )

        Spacer(modifier = Modifier.height(16.dp))

        ShortcutRow(
            icon = Icons.Filled.Settings,
            title = "快捷设置磁贴",
            description = "下拉通知栏，点铅笔图标，然后把 " +
                "「剪贴板历史」拖到快捷栏。",
            status = "需手动设置",
            isGranted = false,
            actionLabel = null,
            onAction = null
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "两者均为可选，之后可在设置中开启。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PageScaffold(
    icon: ImageVector,
    title: String,
    body: String,
    footer: String? = null,
    statusBadge: StatusBadge? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BigIcon(icon)
        Spacer(modifier = Modifier.height(16.dp))
        if (statusBadge != null) {
            StatusRow(
                label = if (statusBadge == StatusBadge.Active) "运行中" else "已暂停",
                isGranted = statusBadge == StatusBadge.Active
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (footer != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = footer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private enum class StatusBadge { Active, Inactive }

@Composable
private fun BigIcon(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun StatusRow(label: String, isGranted: Boolean) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isGranted)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = if (isGranted)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = if (isGranted)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ShortcutRow(
    icon: ImageVector,
    title: String,
    description: String,
    status: String,
    isGranted: Boolean,
    actionLabel: String?,
    onAction: (() -> Unit)?
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            StatusRow(label = status, isGranted = isGranted)
            if (actionLabel != null && onAction != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                    Text(actionLabel)
                }
            }
        }
    }
}

@Composable
private fun BottomActions(
    isLastPage: Boolean,
    onNext: () -> Unit
) {
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(if (isLastPage) "开始使用" else "继续")
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun Card(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Box(modifier = Modifier.padding(0.dp)) { content() }
    }
}