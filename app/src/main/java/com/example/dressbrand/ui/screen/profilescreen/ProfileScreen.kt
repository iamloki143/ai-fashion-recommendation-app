package com.example.dressbrand.ui.screen.profilescreen

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dressbrand.R
import com.example.dressbrand.data.room.DatabaseProvider
import com.example.dressbrand.data.room.entity.PurchaseHistory
import com.example.dressbrand.ui.theme.BelligoesFont
import com.example.dressbrand.ui.theme.DeepObsidian
import com.example.dressbrand.ui.theme.DimSteel
import com.example.dressbrand.ui.theme.ElevatedSurface
import com.example.dressbrand.ui.theme.IvoryWhite
import com.example.dressbrand.ui.theme.LuxeGold
import com.example.dressbrand.ui.theme.RichCharcoal
import com.example.dressbrand.ui.theme.SilverMist
import com.example.dressbrand.ui.theme.SubtleBorder
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onCartClick: () -> Unit
) {

    Scaffold(

        containerColor = DeepObsidian,

        topBar = {

            TopAppBar(

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepObsidian,
                    titleContentColor = IvoryWhite
                ),

                title = {

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "DRESS BRAND",
                            fontFamily = BelligoesFont,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Light,
                            color = IvoryWhite,
                            letterSpacing = 6.sp
                        )

                    }

                },

                modifier = Modifier.border(
                    width = 0.5.dp,
                    color = LuxeGold.copy(alpha = 0.4f)
                )

            )

        },

        bottomBar = {

            NavigationBar(

                containerColor = RichCharcoal,

                modifier = Modifier.border(
                    width = 0.5.dp,
                    color = LuxeGold.copy(alpha = 0.4f)
                )

            ) {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onHomeClick()
                    },
                    label = {
                        Text(
                            "Home",
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    },
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold,
                        selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel,
                        unselectedTextColor = DimSteel
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onCartClick()
                    },
                    label = {
                        Text(
                            "Cart",
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    },
                    icon = {
                        Icon(
                            Icons.Default.ShoppingCart,
                            null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold,
                        selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel,
                        unselectedTextColor = DimSteel
                    )
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    label = {
                        Text(
                            "Profile",
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LuxeGold,
                        selectedTextColor = LuxeGold,
                        indicatorColor = ElevatedSurface,
                        unselectedIconColor = DimSteel,
                        unselectedTextColor = DimSteel
                    )
                )

            }

        }

    ) { padding ->

        val context = LocalContext.current

        val prefs = context.getSharedPreferences(
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
            ) { uri ->

                if (uri != null) {

                    profileImageUri = uri.toString()

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

        LaunchedEffect(true) {

            while (true) {

                purchases =
                    purchaseDao.getAllPurchases()

                delay(1000)

            }

        }

        Column(

            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(DeepObsidian)
                .padding(horizontal = 20.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Box(

                modifier = Modifier
                    .size(140.dp)
                    .border(
                        width = 2.dp,
                        color = LuxeGold,
                        shape = CircleShape
                    )
                    .padding(4.dp)

            ) {

                if (profileImageUri != null) {

                    AsyncImage(

                        model = profileImageUri,

                        contentDescription = "Profile",

                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable {
                                showProfileDialog = true
                            },

                        contentScale = ContentScale.Crop

                    )

                } else {

                    Image(

                        painter = painterResource(
                            R.drawable.profile_pic
                        ),

                        contentDescription = "Profile",

                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable {
                                showProfileDialog = true
                            },

                        contentScale = ContentScale.Crop

                    )

                }

            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = userName,
                color = IvoryWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = contact,
                color = SilverMist,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "PURCHASE HISTORY",
                    color = IvoryWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "${purchases.size} items",
                    color = SilverMist,
                    fontSize = 11.sp
                )

            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        RichCharcoal,
                        RoundedCornerShape(16.dp)
                    )
                    .border(
                        0.5.dp,
                        SubtleBorder,
                        RoundedCornerShape(16.dp)
                    )

            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    if (purchases.isEmpty()) {

                        Text(
                            text = "No purchases yet",
                            color = SilverMist,
                            fontSize = 13.sp
                        )

                    } else {

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                        ) {

                            val groupedOrders =
                                purchases.groupBy {
                                    it.purchaseGroupId
                                }

                            items(groupedOrders.toList()) { (orderId, itemList) ->

                                val totalPrice =
                                    itemList.sumOf {
                                        it.totalPaid
                                    }

                                Box(

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .background(
                                            ElevatedSurface,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .border(
                                            0.5.dp,
                                            SubtleBorder,
                                            RoundedCornerShape(14.dp)
                                        )

                                ) {

                                    Column(
                                        modifier = Modifier.padding(14.dp)
                                    ) {

                                        Text(
                                            text = "ORDER #$orderId",
                                            color = LuxeGold,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )

                                        Spacer(
                                            modifier = Modifier.height(12.dp)
                                        )

                                        itemList.forEach { item ->

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),

                                                horizontalArrangement =
                                                    Arrangement.SpaceBetween
                                            ) {

                                                Text(
                                                    text = item.productName,
                                                    color = IvoryWhite,
                                                    fontSize = 13.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )

                                                Spacer(
                                                    modifier = Modifier.width(12.dp)
                                                )

                                                Text(
                                                    text = "₹${item.totalPaid}",
                                                    color = LuxeGold,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )

                                            }

                                        }

                                        Spacer(
                                            modifier = Modifier.height(10.dp)
                                        )

                                        Divider(
                                            color = SubtleBorder,
                                            thickness = 0.5.dp
                                        )

                                        Spacer(
                                            modifier = Modifier.height(10.dp)
                                        )

                                        Text(
                                            text = "TOTAL  ₹$totalPrice",
                                            color = IvoryWhite,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                    }

                                }

                            }

                        }

                    }

                }

            }

            if (showProfileDialog) {

                AlertDialog(

                    onDismissRequest = {
                        showProfileDialog = false
                    },

                    containerColor = RichCharcoal,

                    shape = RoundedCornerShape(18.dp),

                    title = {

                        Column {

                            Text(
                                text = "EDIT PROFILE",
                                color = IvoryWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 3.sp
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Divider(
                                color = LuxeGold,
                                thickness = 0.8.dp
                            )

                        }

                    },

                    text = {

                        Column(

                            horizontalAlignment =
                                Alignment.CenterHorizontally

                        ) {

                            if (profileImageUri != null) {

                                AsyncImage(

                                    model = profileImageUri,

                                    contentDescription = null,

                                    modifier = Modifier
                                        .size(160.dp)
                                        .clip(CircleShape),

                                    contentScale = ContentScale.Crop

                                )

                            } else {

                                Image(

                                    painter = painterResource(
                                        R.drawable.profile_pic
                                    ),

                                    contentDescription = null,

                                    modifier = Modifier
                                        .size(160.dp)
                                        .clip(CircleShape),

                                    contentScale = ContentScale.Crop

                                )

                            }

                            Spacer(
                                modifier = Modifier.height(18.dp)
                            )

                            OutlinedTextField(

                                value = userName,

                                onValueChange = {
                                    userName = it
                                },

                                label = {
                                    Text("Username")
                                },

                                singleLine = true,

                                shape = RoundedCornerShape(12.dp),

                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LuxeGold,
                                    unfocusedBorderColor = SubtleBorder,
                                    focusedTextColor = IvoryWhite,
                                    unfocusedTextColor = IvoryWhite,
                                    cursorColor = LuxeGold,
                                    focusedContainerColor = ElevatedSurface,
                                    unfocusedContainerColor = ElevatedSurface
                                )

                            )

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            OutlinedTextField(

                                value = contact,

                                onValueChange = {
                                    contact = it
                                },

                                label = {
                                    Text("Contact")
                                },

                                singleLine = true,

                                shape = RoundedCornerShape(12.dp),

                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LuxeGold,
                                    unfocusedBorderColor = SubtleBorder,
                                    focusedTextColor = IvoryWhite,
                                    unfocusedTextColor = IvoryWhite,
                                    cursorColor = LuxeGold,
                                    focusedContainerColor = ElevatedSurface,
                                    unfocusedContainerColor = ElevatedSurface
                                )

                            )

                            Spacer(
                                modifier = Modifier.height(18.dp)
                            )

                            Button(

                                onClick = {

                                    launcher.launch(
                                        "image/*"
                                    )

                                },

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LuxeGold,
                                    contentColor = DeepObsidian
                                ),

                                shape = RoundedCornerShape(10.dp)

                            ) {

                                Text(
                                    text = "CHANGE PHOTO",
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                            }

                        }

                    },

                    confirmButton = {

                        Button(

                            onClick = {

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

                                showProfileDialog = false

                            },

                            colors = ButtonDefaults.buttonColors(
                                containerColor = LuxeGold,
                                contentColor = DeepObsidian
                            ),

                            shape = RoundedCornerShape(10.dp),

                            modifier = Modifier.fillMaxWidth()

                        ) {

                            Text(
                                text = "SAVE",
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )

                        }

                    }

                )

            }

        }

    }

}