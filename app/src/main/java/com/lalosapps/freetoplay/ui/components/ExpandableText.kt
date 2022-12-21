package com.lalosapps.freetoplay.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.lalosapps.freetoplay.R

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    visibleLines: Int = 3,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    softWrap: Boolean = true,
    style: TextStyle = LocalTextStyle.current,
    buttonColor: Color = MaterialTheme.colors.primary
) {
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
        }
    }
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(buildAnnotatedString { append(text) }) }

    val textLayoutResult = textLayoutResultState.value
    val context = LocalContext.current

    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded -> {
                finalText = buildAnnotatedString {
                    append(text)
                    withStyle(
                        SpanStyle(
                            color = buttonColor,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(context.getString(R.string.show_less))
                    }
                }
            }
            textLayoutResult.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult.getLineEnd(lineIndex = visibleLines - 1)
                val showMoreString = context.getString(R.string.show_more)
                val adjustedText = text
                    .substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreString.length)
                    .dropLastWhile { it == ' ' || it == '.' }

                finalText = buildAnnotatedString {
                    append(adjustedText)
                    append("...")
                    withStyle(
                        SpanStyle(
                            color = buttonColor,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(showMoreString)
                    }
                }

                isClickable = true
            }
        }
    }

    Text(
        text = finalText,
        maxLines = if (isExpanded) Int.MAX_VALUE else visibleLines,
        onTextLayout = { textLayoutResultState.value = it },
        modifier = modifier
            .testTag("ExpandableText")
            .clickable(enabled = isClickable) { isExpanded = !isExpanded }
            .animateContentSize(),
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        softWrap = softWrap,
        style = style,
        color = textColor
    )
}