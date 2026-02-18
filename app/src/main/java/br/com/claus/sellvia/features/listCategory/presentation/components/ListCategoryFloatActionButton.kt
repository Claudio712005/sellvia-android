package br.com.claus.sellvia.features.listCategory.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun ListCategoryFloatActionButton(openModal: () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(220)) +
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(dampingRatio = 0.7f)
                ),
        exit = fadeOut(animationSpec = tween(150)) +
                scaleOut(targetScale = 0.8f)
    ) {
        FloatingActionButton(
            onClick = openModal,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar categoria"
            )
        }
    }
}