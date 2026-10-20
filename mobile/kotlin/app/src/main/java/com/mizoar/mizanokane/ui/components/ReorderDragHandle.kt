package com.mizoar.mizanokane.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.ui.theme.MizanTheme

/**
 * Komponen bersama untuk pegangan geser urutan (≡) berbasis API foundation Compose.
 * Dipakai oleh Kelola Dompet dan Kelola Kategori.
 */
@Composable
fun ReorderDragHandle(
    isDragging: Boolean,
    onDragStart: () -> Unit,
    onDrag: (dragAmountY: Float) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    modifier: Modifier = Modifier,
    key: Any? = Unit
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .pointerInput(key) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount.y)
                    },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragCancel() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "≡",
            style = MaterialTheme.typography.titleLarge.copy(
                color = if (isDragging) MizanTheme.colors.accent else MizanTheme.colors.textSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        )
    }
}
