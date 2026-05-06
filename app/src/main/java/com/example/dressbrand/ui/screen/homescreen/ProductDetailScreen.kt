package com.example.dressbrand.ui.screen.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dressbrand.data.CartItem
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import com.example.dressbrand.data.Product
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.data.room.entity.CartProduct
import com.example.dressbrand.ui.theme.BelligoesFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product : Product,
    cartItems: MutableList<CartItem>,
    onBack:() -> Unit
) {
    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
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
        }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            val context =LocalContext.current
            val scope = rememberCoroutineScope()
            val cartDao= DatabaseProvider.getDatabase(context).CartDao()
            var showCartDialog by remember { mutableStateOf(false) }

            var selectedSize by remember { mutableStateOf("") }

            var quantity by remember { mutableStateOf(1) }
            val sizeStock = mapOf(
                "XS" to product.sizeXS,
                "S" to product.sizeS,
                "M" to product.sizeM,
                "L" to product.sizeL,
                "XL" to product.sizeXL
            )
            Text(product.productName)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                AsyncImage(
                    model = product.imageUrl,

                    contentDescription =
                        product.productName,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .padding(16.dp),

                    contentScale =
                        ContentScale.Fit
                )

            }
            Text("₹${product.price}")
            Text("Rating:${product.rating}")
            Text("Sizes")

            LazyRow(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sizeStock.toList()) { (size, stock) ->
                    SizeButton(
                        size = size,
                        stock = stock
                    )

                }
            }
            Button(
                onClick = {
                    showCartDialog = true
                },
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
            ) {
                Text("Add To cart")
            }
            if(showCartDialog){
                AlertDialog(
                    onDismissRequest = {
                        showCartDialog=false
                    },
                    title = {
                        Text("Add to Cart")
                    },
                    text = {
                        Column {
                            Text(product.productName)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {

                                    AsyncImage(

                                        model = product.imageUrl,

                                        contentDescription =
                                            product.productName,

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp),

                                        contentScale =
                                            ContentScale.Fit

                                    )

                                }


                                Text("₹${product.price}")
                                Text("Rating:${product.rating}")
                                Text("Select Size:")
                            }
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                items(sizeStock.keys.toList()) { size ->

                                    val stock = sizeStock[size] ?: 0

                                    Button(
                                        enabled = stock > 0,

                                        onClick = {

                                            selectedSize = size

                                            if(quantity > stock){
                                                quantity = stock
                                            }
                                        }

                                    ) {

                                        Text(

                                            if(stock == 0)

                                                "$size Out"

                                            else if(selectedSize == size)

                                                "✓ $size"

                                            else

                                                size
                                        )

                                    }

                                }

                            }
                            Text(
                                text = "Selected Size : $selectedSize",
                                modifier = Modifier.padding(top = 12.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),

                                horizontalArrangement =
                                    Arrangement.Center,

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Button(onClick = {
                                    if (quantity>1){
                                        quantity--
                                    }
                                }) {Text("-") }
                                Text(
                                    text = quantity.toString()
                                )
                                Button(
                                    onClick = {

                                        if(
                                            quantity <
                                            (sizeStock[selectedSize] ?: 1)
                                        ){
                                            quantity++
                                        }

                                    }
                                ){
                                    Text("+")
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            if(selectedSize.isNotEmpty()){

                                val existingItem =
                                    cartItems.find {

                                        it.productId ==
                                                product.productId &&

                                                it.selectedSize ==
                                                selectedSize
                                    }

                                if(existingItem != null){

                                    existingItem.quantity +=
                                        quantity

                                }
                                else {

                                    cartItems.add(

                                        CartItem(
                                            productId = product.productId,
                                            productName = product.productName,
                                            imageUrl = product.imageUrl,
                                            price = product.price,
                                            quantity = quantity,
                                            selectedSize = selectedSize
                                        )

                                    )

                                    scope.launch {

                                        cartDao.insertCartItem(

                                            CartProduct(
                                                productId = product.productId,
                                                productName = product.productName,
                                                imageUrl = product.imageUrl,
                                                price = product.price,
                                                quantity = quantity,
                                                selectedSize = selectedSize
                                            )

                                        )

                                    }

                                }

                                showCartDialog = false
                            }
                        }){
                            Text("Confirm")
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun SizeButton(
    size:String,
    stock:Int
){


    Card(
        modifier = Modifier.padding(4.dp)
    ) {

        Text(
            text =
                if(stock == 0)
                    "$size (Out)"
                else
                    "$size : $stock",

            modifier = Modifier
                .padding(12.dp)
        )
    }
}