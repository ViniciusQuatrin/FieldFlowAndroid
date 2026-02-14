package br.com.satsolucoes.fieldflow.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Black
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray100
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray300
import br.com.satsolucoes.fieldflow.presentation.ui.theme.White

enum class BottomNavItem(val icon: ImageVector) {
    MATERIALS(Icons.Filled.Apps),
    SYNC(Icons.Filled.Sync)
}

@Composable
fun BottomNavBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier,
    showFab: Boolean = false,
    onFabClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = White,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .background(White)
                .padding(top = 1.dp)
                .background(Gray100)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 48.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItemButton(
                    item = BottomNavItem.MATERIALS,
                    isSelected = selectedItem == BottomNavItem.MATERIALS,
                    onClick = { onItemSelected(BottomNavItem.MATERIALS) }
                )

                // FAB central (ou espaço vazio)
                if (showFab) {
                    FloatingActionButton(
                        onClick = onFabClick,
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        containerColor = Black,
                        contentColor = White,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirmar consumos",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    // Espaço vazio para manter alinhamento
                    Box(modifier = Modifier.size(56.dp))
                }

                NavItemButton(
                    item = BottomNavItem.SYNC,
                    isSelected = selectedItem == BottomNavItem.SYNC,
                    onClick = { onItemSelected(BottomNavItem.SYNC) }
                )
            }
        }
    }
}

@Composable
private fun NavItemButton(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.name,
            tint = if (isSelected) Black else Gray300,
            modifier = Modifier.size(26.dp)
        )
    }
}




