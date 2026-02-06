package com.alfabank.homework.courseproject.presentation.ui.filterScreen.dateFilter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun QuickFilterChipSimple(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
//    Surface(
//        modifier = Modifier
//            .clip(RoundedCornerShape(16.dp))
//            .clickable(onClick = onClick),
//        color = if (selected) MaterialTheme.colorScheme.primaryContainer
//        else MaterialTheme.colorScheme.surfaceVariant,
//        border = BorderStroke(
//            width = 1.dp,
//            color = if (selected) MaterialTheme.colorScheme.primary
//            else MaterialTheme.colorScheme.outline
//        )
//    ) {
//        Text(
//            text = text,
//            style = MaterialTheme.typography.bodyMedium,
//            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
//            else MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
//        )
//    }

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline,
            borderWidth = 1.dp,
            enabled = true,
            selected = selected
        ),
        shape = RoundedCornerShape(16.dp)
    )
}


