package br.com.satsolucoes.fieldflow.presentation.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray100
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray400
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray800

// Cores de status
val SyncSuccessGreen = Color(0xFF22C55E)
val SyncErrorRed = Color(0xFFEF4444)
val SyncPendingYellow = Color(0xFFF59E0B)

@Composable
fun SyncSummaryChip(
    count: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun SyncSummaryRow(
    pendentes: Int,
    sincronizados: Int,
    comErro: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SyncSummaryChip(
            count = pendentes,
            label = "Pendentes",
            color = SyncPendingYellow,
            modifier = Modifier.weight(1f)
        )
        SyncSummaryChip(
            count = sincronizados,
            label = "Enviados",
            color = SyncSuccessGreen,
            modifier = Modifier.weight(1f)
        )
        SyncSummaryChip(
            count = comErro,
            label = "Erros",
            color = SyncErrorRed,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SyncStatusIcon(
    status: SyncStatus,
    modifier: Modifier = Modifier
) {
    val (icon, color) = when (status) {
        SyncStatus.PENDING -> Icons.Default.Schedule to SyncPendingYellow
        SyncStatus.SYNCED -> Icons.Default.CheckCircle to SyncSuccessGreen
        SyncStatus.ERROR -> Icons.Default.Error to SyncErrorRed
    }

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SyncStatusBadge(
    status: SyncStatus,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (status) {
        SyncStatus.PENDING -> "Pendente" to SyncPendingYellow
        SyncStatus.SYNCED -> "Enviado" to SyncSuccessGreen
        SyncStatus.ERROR -> "Erro" to SyncErrorRed
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun SyncItemCard(
    materialNome: String,
    unidadeFormatada: String,
    dataFormatada: String,
    observacao: String,
    status: SyncStatus,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Gray100),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            SyncStatusIcon(status = status)

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = materialNome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    SyncStatusBadge(status = status)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = unidadeFormatada,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray800
                    )
                    Text(
                        text = "•",
                        color = Gray400
                    )
                    Text(
                        text = dataFormatada,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray400
                    )
                }

                // Observação (se houver)
                if (observacao.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = observacao,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray400,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

