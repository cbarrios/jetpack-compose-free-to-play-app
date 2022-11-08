package com.lalosapps.freetoplay.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onQueryClear: () -> Unit,
    onSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = "Search", color = MaterialTheme.colors.onBackground)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearch()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        shape = MaterialTheme.shapes.small,
        trailingIcon = {
            IconButton(onClick = onQueryClear) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        }
    )
}