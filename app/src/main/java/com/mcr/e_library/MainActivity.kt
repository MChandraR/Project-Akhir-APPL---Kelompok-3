package com.mcr.e_library

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcr.e_library.ui.theme.ELibraryTheme

class MainActivity : ComponentActivity() {
    val mainMenu:Class<MainMenu> = MainMenu::class.java
    val loginForm:Class<LoginForm> = LoginForm::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
        goToHome()
    }

    private fun goToHome(){
        val handler = Handler(Looper.getMainLooper())
        val runnable = kotlinx.coroutines.Runnable {
            startActivity(Intent(this,loginForm))
        }
        handler.postDelayed(runnable,1000)
    }


}



@Composable
fun Main(){
    ELibraryTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(
                    animationSpec =tween(
                        easing = EaseIn,
                        durationMillis = 500),
                    initialAlpha = 0f
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 250)
                ))
            {
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.elib_icon),
                        contentDescription = "App Icon",
                        contentScale = ContentScale.Crop,
                        modifier =  Modifier.size(100.dp)
                    )
                    Title()
                }
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier) {
    Text(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        text = "E-Library",
        modifier = modifier
    )
}

@Preview(showBackground = false)
@Composable
fun GreetingPreview() {
    Main()
}








