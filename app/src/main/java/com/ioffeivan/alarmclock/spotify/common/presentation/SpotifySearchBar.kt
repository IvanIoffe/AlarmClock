package com.ioffeivan.alarmclock.spotify.common.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.ioffeivan.alarmclock.core.presentation.ClearTextIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifySearchBar(
    isActive: MutableState<Boolean>,
    query: MutableState<String>,
    placeholderText: String,
    onSearch: (String) -> Unit,
    onLeadingIconClick: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        query = query.value,
        onQueryChange = {
            query.value = it
        },
        onSearch = {
            isActive.value = false
            onSearch(it)
        },
        active = isActive.value,
        onActiveChange = {
            isActive.value = it
        },
        placeholder = {
            Text(text = placeholderText)
        },
        leadingIcon = {
            IconButton(
                onClick = {
                    if (isActive.value)
                        isActive.value = false
                    else
                        onLeadingIconClick()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            ClearTextIconButton(
                modifier = Modifier,
                isVisible = query.value.isNotEmpty(),
                onClick = {
                    if (!isFocused) focusRequester.requestFocus()
                    query.value = ""
                }
            )
        },
    ) {}
}