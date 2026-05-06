package com.example.dressbrand

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import com.example.dressbrand.navigationbar.AppNavigation
import com.example.dressbrand.ui.screen.homescreen.HomeScreen
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dressbrand.ui.theme.DressBrandTheme

class MainActivity : ComponentActivity() {
    lateinit var paymentLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        paymentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){result ->
            if (result.resultCode == RESULT_OK){

            }
        }
        setTheme(R.style.Theme_DressBrand)
        setContent {
            DressBrandTheme {
                AppNavigation(paymentLauncher)
            }
        }
    }
}
