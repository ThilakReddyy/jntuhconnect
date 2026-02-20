package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun GentleAnimatedBottomBar() {
    val items = listOf(
        Icons.Default.Home,
        Icons.Default.Computer,
        Icons.Default.Notifications,
        Icons.Default.Work
    )

    var selectedIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier.fillMaxWidth().height(100.dp)
    ) {
        Surface(
            color = Color(0xFF1B1B23),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
        ) {
            BoxWithConstraints {
                val itemWidth = maxWidth / items.size

                // Animate semicircle’s horizontal position with gentle curve
                val indicatorCenterX by animateDpAsState(
                    targetValue = itemWidth * selectedIndex + itemWidth / 2,
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)
                    )
                )

                // Draw smooth white semicircle under selected icon
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radius = 30.dp.toPx()
                    val centerY = size.height + 50  // bottom edge
                    drawCircle(
                        color = Color.White,
                        radius = radius,
                        center = Offset(indicatorCenterX.toPx(), centerY)
                    )
                }

                // Icons Row
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items.forEachIndexed { index, icon ->
                        val isSelected = index == selectedIndex

                        val iconTint by animateColorAsState(
                            targetValue = if (isSelected) Color.White else Color.Gray,
                            animationSpec = tween(400, easing = LinearOutSlowInEasing)
                        )

                        val scale by animateFloatAsState(
                            targetValue = if (isSelected) 1.15f else 1f,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(itemWidth)
                                .padding(2.dp)
                                .clickable { selectedIndex = index }
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.scale(scale)
                            )
                        }
                    }
                }
            }
        }
        Text("Hello",color= MaterialTheme.colorScheme.primary)

    }
}

