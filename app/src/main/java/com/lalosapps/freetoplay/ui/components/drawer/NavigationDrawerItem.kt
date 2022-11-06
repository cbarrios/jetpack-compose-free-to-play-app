package com.lalosapps.freetoplay.ui.components.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerItem(
    icon: ImageVector,
    iconColor: Color,
    text: String,
    textStyle: TextStyle,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp)

    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = icon,
            contentDescription = icon.name,
            tint = iconColor,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            fontWeight = FontWeight.Bold,
        )
    }
}