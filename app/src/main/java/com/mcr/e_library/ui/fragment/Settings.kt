package com.mcr.e_library.ui.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcr.e_library.util.ColorPalette

class Settings {
    private var menu:ArrayList<String> = ArrayList(listOf("Tema","Bahasa"))

    @Composable
    fun settingsMainView(){
        Column(Modifier.background(Color.Blue)) {
            Box(modifier = Modifier
                .weight(.3f)
                .background(Color.White)
                .clip(RoundedCornerShape(0, 0, 50, 0))){
                Box(modifier = Modifier.background(ColorPalette().LightBlue)){
                    Text(
                        modifier= Modifier
                            .padding(all = 20.dp)
                            .fillMaxSize()
                            .background(ColorPalette().LightBlue),
                        text="Aplikasi Perpustakaan By Kelompok 2",
                        fontSize = 28.sp,
                        lineHeight = 36.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                }

            }

            Box(modifier = Modifier
                .weight(.7f)
                .background(ColorPalette().LightBlue)
                .clip(RoundedCornerShape(20, 0, 0, 0))){

                Box(modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(vertical = 25.dp)
                ){
                    LazyColumn(
                        modifier= Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        content = {
                            item { 
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp,horizontal = 40.dp),
                                    text = "Pengaturan",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold)
                            }
                            items(
                                count = menu.size,
                                itemContent = {
                                    Text(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp, horizontal = 40.dp),text = menu[it], fontWeight = FontWeight.Bold)
                                }
                            )
                        }
                    )
                }
            }
        }

    }
}