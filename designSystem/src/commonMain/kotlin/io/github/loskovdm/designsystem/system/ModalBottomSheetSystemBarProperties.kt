package io.github.loskovdm.designsystem.system

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun rememberNavigationChromeModalBottomSheetProperties(): ModalBottomSheetProperties
