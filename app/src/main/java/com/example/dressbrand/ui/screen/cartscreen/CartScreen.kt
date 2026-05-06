package com.example.dressbrand.ui.screen.cartscreen

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dressbrand.data.CartItem
import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.data.room.entity.PurchaseHistory
import com.example.dressbrand.ui.theme.BabyPink
import com.example.dressbrand.ui.theme.BelligoesFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onHomeClick:() -> Unit,
    onProfileClick:() -> Unit,
    onProductClick:() -> Unit,
    cartItems: MutableList<CartItem>,
    paymentLauncher:
    ActivityResultLauncher<Intent>
){
    val purchaseGroupId = System.currentTimeMillis()
    val context = LocalContext.current
    var showCheckoutDialog by remember {

        mutableStateOf(false)

    }

    val totalAmount = cartItems.sumOf {

        it.price * it.quantity

    }
    Scaffold(
        floatingActionButton = {

            FloatingActionButton(

                onClick = {
                    showCheckoutDialog=true
                }

            ) {

                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Buy"
                )

            }

        },
        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Dress brand",
                            fontFamily = BelligoesFont,
                            fontSize = 32.sp
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onHomeClick()
                    },
                    label = {
                        Text("Home")
                    },
                    icon = {Icon(Icons.Default.Home,"home") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    label = {
                        Text("Cart")
                    },
                    icon = {Icon(Icons.Default.ShoppingCart,"cart") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onProfileClick()
                    },
                    label = {
                        Text("Profile")
                    },
                    icon = {Icon(Icons.Default.Person,"profile")}
                )
            }
        }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

            Text("Cart")

            cartItems.forEach {item ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = BabyPink
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        AsyncImage(
                            model = item.imageUrl,

                            contentDescription =
                                item.productName,

                            modifier = Modifier
                                .height(100.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Product ${item.productId}"
                            )


                            Text(
                                "Size: ${item.selectedSize}"
                            )

                            Text(
                                "Qty: ${item.quantity}"
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                IconButton(
                                    onClick = {

                                        if(item.quantity > 1){
                                            item.quantity--
                                        }

                                    }
                                ){
                                    Text("-")
                                }

                                Text(
                                    item.quantity.toString()
                                )

                                IconButton(
                                    onClick = {

                                        item.quantity++

                                    }
                                ){
                                    Text("+")
                                }

                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            cartItems.remove(item)
                        }) {Icon(Icons.Default.Delete, contentDescription ="delete" ) }
                    }
                }


            }

        }
        if(showCheckoutDialog){
            AlertDialog(
                onDismissRequest = {
                    showCheckoutDialog=false
                },
                title = {
                    Text("Order Summary")
                },
                text = {
                    Column {
                        cartItems.forEach { item ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.productName)
                                    Text("Rs.${item.price}")
                                }
                                IconButton(onClick = {
                                    cartItems.remove(item)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "delete")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Total Rs.${totalAmount}")
                    }
                },
                confirmButton = {
                    val context = LocalContext.current
                    Button(onClick = {
                        val itemToSave =cartItems.toList()
                        CoroutineScope(Dispatchers.IO).launch{
                            val purchaseDao = DatabaseProvider
                                .getDatabase(context)
                                .PurchaseHistoryDao()
                            val cartDao= DatabaseProvider
                                .getDatabase(context)
                                .CartDao()

                            itemToSave.forEach { item ->
                                purchaseDao.insertPurchase(
                                    PurchaseHistory(
                                        productId = item.productId,
                                        purchaseGroupId = purchaseGroupId,
                                        productName = item.productName,
                                        quantity = item.quantity,
                                        size = item.selectedSize,
                                        totalPaid = item.quantity * item.price
                                    )
                                )
                            }
                            cartDao.clearCart()
                        }

                        cartItems.clear()
                        showCheckoutDialog=false
                    }) {
                        Text("Buy")
                    }
                }
            )
        }

    }
    }

