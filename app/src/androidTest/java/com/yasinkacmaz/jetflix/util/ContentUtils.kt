package com.yasinkacmaz.jetflix.util

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme

fun ComposeContentTestRule.setTestContent(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) = setContent {
    JetflixTheme(isDarkTheme = isDarkTheme) {
        Surface {
            content()
        }
    }
}
