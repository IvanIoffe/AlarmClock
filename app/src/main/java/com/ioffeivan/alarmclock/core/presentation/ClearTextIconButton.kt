package com.ioffeivan.alarmclock.core.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ioffeivan.alarmclock.R

@Composable
fun ClearTextIconButton(
    modifier: Modifier,
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    if (isVisible) {
        IconButton(
            modifier = modifier
                .size(24.dp),
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_cancel),
                contentDescription = null,
            )
        }
    }
}