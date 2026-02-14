package br.com.satsolucoes.fieldflow.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.satsolucoes.fieldflow.data.remote.config.ApiConfig
import br.com.satsolucoes.fieldflow.data.remote.config.ApiMode

/**
 * Componente dropdown para selecionar o modo de API (Fake ou Servidor).
 */
@Composable
fun ApiModeSelector(
    modifier: Modifier = Modifier,
    onModeChanged: () -> Unit = {}
) {
    val currentMode by ApiConfig.apiMode.collectAsState()
    val currentUrl by ApiConfig.serverUrl.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var showUrlDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = if (currentMode == ApiMode.FAKE) Icons.Filled.CloudOff else Icons.Filled.Cloud,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when (currentMode) {
                    ApiMode.FAKE -> "Modo Offline (Dados Mockados)"
                    ApiMode.SERVER -> "Servidor: ${currentUrl.removePrefix("http://").removeSuffix("/")}"
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Expandir"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.CloudOff,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Modo Offline")
                            Text(
                                text = "Usa dados mockados locais",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                onClick = {
                    ApiConfig.setApiMode(ApiMode.FAKE)
                    expanded = false
                    onModeChanged()
                }
            )

            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Cloud,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Servidor Web")
                            Text(
                                text = "Conecta à API real",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                onClick = {
                    ApiConfig.setApiMode(ApiMode.SERVER)
                    expanded = false
                    showUrlDialog = true
                }
            )
        }
    }

    // Dialog para configurar URL do servidor
    if (showUrlDialog) {
        ServerUrlDialog(
            currentUrl = currentUrl,
            onDismiss = { showUrlDialog = false },
            onConfirm = { newUrl ->
                ApiConfig.setServerUrl(newUrl)
                showUrlDialog = false
                onModeChanged()
            }
        )
    }
}

@Composable
private fun ServerUrlDialog(
    currentUrl: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var url by remember { mutableStateOf(currentUrl) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Configurar Servidor") },
        text = {
            Column {
                Text(
                    text = "Digite o endereço do servidor (ex: http://192.168.1.100:8081)",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL do Servidor") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "💡 No emulador Android, use 10.0.2.2 para acessar o localhost do computador",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(url) }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

