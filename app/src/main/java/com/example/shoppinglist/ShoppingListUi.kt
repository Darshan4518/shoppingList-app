package com.example.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListUi(modifier: Modifier) {
    var productsList by remember { mutableStateOf(listOf<Product>()) }
    var newProduct by remember { mutableStateOf("") }
    var newQuantity by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Shopping List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn {
                items(productsList) {
                    item->
                    ProductItem(
                        items = item,

                        delete = {
                            productsList = productsList.filter { it.id != item.id }
                        }
                    )
                }
            }

        }

        if (showDialog) {
            ProductDialog(
                productName = newProduct,
                productQuantity = newQuantity.toIntOrNull() ?: 1,
                onDismiss = { showDialog = false },
                onNameChange = { newProduct = it },
                onQuantityChange = { newQuantity = it },
                onSave = {
                    productsList = productsList + Product(
                        id = productsList.size + 1,
                        name = newProduct,
                        quantity = newQuantity.toIntOrNull() ?: 1,
                    )
                    newProduct = ""
                    newQuantity = ""
                    showDialog = false
                }

            )
        }

    }
}

@Composable
fun ProductDialog(
    productName: String,
    productQuantity: Int,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onSave: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Product") },
        text = {
            Column {
                OutlinedTextField(
                    value = productName,
                    onValueChange = onNameChange,
                    label = { Text(text = "Name") }
                )
                OutlinedTextField(
                    value = productQuantity.toString(),
                    onValueChange = onQuantityChange,
                    label = { Text(text = "Quantity") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text(text = "Save")
            }
        },

        )
}

@Composable
fun ProductItem(
    items: Product,
    delete: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(text = items.name, style = MaterialTheme.typography.titleLarge)
                Text(text = "Quantity : ${items.quantity}")
            }
            Column {

                IconButton(onClick = delete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }

}

