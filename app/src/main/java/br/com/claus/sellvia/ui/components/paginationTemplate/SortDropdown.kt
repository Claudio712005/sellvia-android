package br.com.claus.sellvia.ui.components.paginationTemplate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.claus.sellvia.ui.components.paginationTemplate.models.SortOption
import kotlin.collections.forEach

@Composable
fun SortDropdown(
    sortOptions: List<SortOption>,
    selectedSort: SortOption,
    onSortChange: (SortOption) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        InputChip(
            selected = true,
            onClick = { if(enabled) expanded = true },
            label = { Text(selectedSort.label) },
            leadingIcon = {
                Icon(
                    imageVector = selectedSort.icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, null)
            },
            enabled = enabled
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sortOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    leadingIcon = { Icon(option.icon, null) },
                    onClick = {
                        onSortChange(option)
                        expanded = false
                    },
                    trailingIcon = if(option == selectedSort) {
                        { Icon(Icons.Default.Check, null) }
                    } else null
                )
            }
        }
    }
}