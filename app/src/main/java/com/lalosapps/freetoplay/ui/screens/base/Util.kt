package com.lalosapps.freetoplay.ui.screens.base

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

val IconName = SemanticsPropertyKey<String>("IconName")
var SemanticsPropertyReceiver.iconName by IconName