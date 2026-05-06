package com.example.dressbrand.ui.screen.profilescreen

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import coil.compose.AsyncImage
import com.example.dressbrand.R
import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.data.room.entity.PurchaseHistory
import com.example.dressbrand.ui.theme.BelligoesFont
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onHomeClick:()->Unit,
    onCartClick:()->Unit
){

    Scaffold(

        topBar = {

            TopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            MaterialTheme.colorScheme.primary,

                        titleContentColor =
                            Color.White
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

            NavigationBar(
                containerColor =
                    MaterialTheme.colorScheme.primary
            ){

                NavigationBarItem(
                    selected=false,
                    onClick={
                        onHomeClick()
                    },
                    label={
                        Text("Home")
                    },
                    icon={
                        Icon(
                            Icons.Default.Home,
                            null
                        )
                    }
                )

                NavigationBarItem(
                    selected=false,
                    onClick={
                        onCartClick()
                    },
                    label={
                        Text("Cart")
                    },
                    icon={
                        Icon(
                            Icons.Default.ShoppingCart,
                            null
                        )
                    }
                )

                NavigationBarItem(
                    selected=true,
                    onClick={},
                    label={
                        Text("Profile")
                    },
                    icon={
                        Icon(
                            Icons.Default.Person,
                            null
                        )
                    }
                )

            }

        }

    ){ padding ->

        val context =
            LocalContext.current

        val prefs =
            context.getSharedPreferences(
                "profile_data",
                Context.MODE_PRIVATE
            )

        var showProfileDialog by remember {

            mutableStateOf(false)

        }

        var userName by remember {

            mutableStateOf(
                prefs.getString(
                    "username",
                    "Lokesh"
                ) ?: "Lokesh"
            )

        }

        var contact by remember {

            mutableStateOf(
                prefs.getString(
                    "contact",
                    "9876543210"
                ) ?: "9876543210"
            )

        }

        var profileImageUri by remember {

            mutableStateOf<String?>(
                prefs.getString(
                    "profile_image",
                    null
                )
            )

        }

        val launcher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ){ uri ->

                if(uri!=null){

                    profileImageUri =
                        uri.toString()

                    prefs.edit()
                        .putString(
                            "profile_image",
                            uri.toString()
                        )
                        .apply()
                }

            }

        val purchaseDao =
            DatabaseProvider
                .getDatabase(context)
                .PurchaseHistoryDao()

        var purchases by remember {

            mutableStateOf(
                emptyList<PurchaseHistory>()
            )

        }

        LaunchedEffect(true){

            while (true){
                purchases = purchaseDao.getAllPurchases()
                delay(1000)
            }

        }

        Column(

            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ){

            Spacer(
                modifier=
                    Modifier.height(24.dp)
            )

            if(profileImageUri!=null){

                AsyncImage(

                    model =
                        profileImageUri,

                    contentDescription =
                        "Profile",

                    modifier =
                        Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable{
                                showProfileDialog=true
                            },

                    contentScale =
                        ContentScale.Crop

                )

            }
            else{

                Image(

                    painter=
                        painterResource(
                            R.drawable.profile_pic
                        ),

                    contentDescription=
                        "Profile",

                    modifier=
                        Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable{
                                showProfileDialog=true
                            },

                    contentScale=
                        ContentScale.Crop

                )

            }

            Spacer(
                modifier=
                    Modifier.height(12.dp)
            )

            Text(
                text=userName
            )

            Spacer(
                modifier=
                    Modifier.height(32.dp)
            )

            Text(
                "Purchase History"
            )

            Card(
                modifier=
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
            ){

                Column(
                    modifier=
                        Modifier.padding(16.dp)
                ){

                    if(
                        purchases.isEmpty()
                    ){

                        Text(
                            "No purchases yet"
                        )

                    }
                    else{
                        LazyColumn(
                            modifier = Modifier.padding(16.dp).fillMaxWidth().height(300.dp)
                        ) {
                            val groupOrders = purchases.groupBy { it.purchaseGroupId }

                            items(groupOrders.toList()){ (orderId, item) ->
                                val totalPrice  = item.sumOf{it.totalPaid}

                                Card(
                                    modifier= Modifier
                                            .fillMaxWidth()
                                            .padding(vertical=8.dp)
                                ){

                                    Column(
                                        modifier= Modifier.padding(12.dp)
                                    ){
                                        Text("Order ID :${orderId}")
                                        Spacer(modifier = Modifier.height(8.dp))

                                        item.forEach { item ->
                                            Row(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                                                Text(
                                                    item.productName,
                                                    modifier = Modifier.weight(1f),
                                                    maxLines = 1
                                                )
                                                Text("₹${item.totalPaid}")
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Total: ₹${totalPrice}")

                                    }

                                }

                            }
                        }

                    }

                }

            }

            if(showProfileDialog){

                AlertDialog(

                    onDismissRequest = {

                        showProfileDialog=false

                    },

                    title={
                        Text("Profile")
                    },

                    text={

                        Column(

                            horizontalAlignment=
                                Alignment.CenterHorizontally

                        ){

                            if(profileImageUri!=null){

                                AsyncImage(
                                    model=
                                        profileImageUri,

                                    contentDescription=
                                        null,

                                    modifier=
                                        Modifier
                                            .size(
                                                180.dp
                                            )
                                            .clip(
                                                CircleShape
                                            ),

                                    contentScale=
                                        ContentScale.Crop
                                )

                            }
                            else{

                                Image(
                                    painter=
                                        painterResource(
                                            R.drawable.profile_pic
                                        ),

                                    contentDescription=
                                        null,

                                    modifier=
                                        Modifier
                                            .size(
                                                180.dp
                                            )
                                            .clip(
                                                CircleShape
                                            ),

                                    contentScale=
                                        ContentScale.Crop
                                )

                            }

                            Spacer(
                                modifier=
                                    Modifier.height(
                                        16.dp
                                    )
                            )

                            OutlinedTextField(
                                value=
                                    userName,

                                onValueChange={
                                    userName=it
                                },

                                label={
                                    Text(
                                        "Username"
                                    )
                                }
                            )

                            Spacer(
                                modifier=
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            OutlinedTextField(
                                value=
                                    contact,

                                onValueChange={
                                    contact=it
                                },

                                label={
                                    Text(
                                        "Contact"
                                    )
                                }
                            )

                            Spacer(
                                modifier=
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            Button(

                                onClick={

                                    launcher.launch(
                                        "image/*"
                                    )

                                }

                            ){

                                Text(
                                    "Edit Profile Pic"
                                )

                            }

                        }

                    },

                    confirmButton={

                        Button(

                            onClick={

                                prefs.edit()
                                    .putString(
                                        "username",
                                        userName
                                    )
                                    .putString(
                                        "contact",
                                        contact
                                    )
                                    .apply()

                                showProfileDialog=false

                            }

                        ){

                            Text(
                                "Save"
                            )

                        }

                    }

                )

            }

        }

    }

}