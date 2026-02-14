package br.com.satsolucoes.fieldflow.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Black
import br.com.satsolucoes.fieldflow.presentation.ui.theme.White

@Composable
fun QuantityButton(
    icon: ImageVector,
    onClick: () -> Unit,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    if (isPrimary) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.size(width = 80.dp, height = 64.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Black,
                contentColor = White
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.size(width = 80.dp, height = 64.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, Black),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Black
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

