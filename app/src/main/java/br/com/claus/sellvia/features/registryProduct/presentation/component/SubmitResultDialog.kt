package br.com.claus.sellvia.features.registryProduct.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.claus.sellvia.features.registryProduct.presentation.SubmitResult

@Composable
fun SubmitResultDialog(
    result: SubmitResult,
    onDismiss: () -> Unit,
    onGoToList: () -> Unit,
    onRegisterNew: () -> Unit,
) {
    val isSuccess = result is SubmitResult.Success
    val iconColor = if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val bgColor = iconColor.copy(alpha = 0.10f)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Ícone animado
                AnimatedVisibility(
                    visible = true,
                    enter = scaleIn(spring(Spring.DampingRatioMediumBouncy)) + fadeIn()
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(bgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSuccess) Icons.Outlined.Check else Icons.Outlined.Close,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = if (isSuccess) "Produto salvo!" else "Erro ao salvar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = if (isSuccess)
                        "O produto foi cadastrado com sucesso."
                    else
                        (result as SubmitResult.Error).message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(28.dp))

                if (isSuccess) {
                    // Dois botões lado a lado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onGoToList,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("Ver lista", fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick = onRegisterNew,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("Novo produto", fontWeight = FontWeight.SemiBold)
                        }
                    }
                } else {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Fechar", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}