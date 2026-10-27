package com.flowphotos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flowphotos.app.ui.gallery.GalleryScreen
import com.flowphotos.app.ui.theme.FlowBackground
import com.flowphotos.app.ui.theme.FlowPhotosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowPhotosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = FlowBackground,
                ) {
                    GalleryScreen()
                }
            }
        }
    }
}
