package com.mcr.e_library.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.ktx.ExperimentGlideFlows
import com.mcr.e_library.R
import com.mcr.e_library.apiInterface.bukuAPI
import com.mcr.e_library.apiInterface.kehadiranAPI
import com.mcr.e_library.model.BookModel
import com.mcr.e_library.model.KunjunganAPIModel
import com.mcr.e_library.model.KunjunganModel
import com.mcr.e_library.model.UserBookModel
import com.mcr.e_library.model.UserBookModel.BookModels
import com.mcr.e_library.ui.myCustomUI
import com.mcr.e_library.ui.theme.ELibraryTheme
import com.mcr.e_library.util.API
import com.mcr.e_library.util.ColorPalette
import retrofit2.Call
import retrofit2.Response

class Profile(private val context: Context) : ComponentActivity() {
    private var showProfileArea:MutableState<Boolean> = mutableStateOf(false)
    private var showImage:MutableState<Boolean> = mutableStateOf(false)
    private val CP = ColorPalette()
    private val API = API().serverUrl
    private val imageSize = 125
    private var dataPinjaman:MutableList<BookModels> = mutableListOf()
    private var dataKunjungan:MutableList<KunjunganModel> = mutableListOf()
    private var sP: SharedPreferences = context.getSharedPreferences("mcrs_pref",Context.MODE_PRIVATE)

    @Composable
    fun ProfileMainView(){
        ELibraryTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                ConstraintLayout {
                    val ( foreground, logout) = createRefs()

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()) {
                        Column(modifier = Modifier
                            .background(CP.largeRadialGradient)
                            .fillMaxWidth()
                            .weight(0.3f)){}
                        Column(modifier = Modifier
                            .background(CP.SoftGray)
                            .fillMaxWidth()
                            .weight(0.7f)){}
                    }

                    IconButton(
                        modifier = Modifier.constrainAs(logout){
                            top.linkTo(parent.top,10.dp); end.linkTo(parent.end,10.dp)
                        },
                        onClick = { Toast.makeText(context, "Fitur belum tersedia !", Toast.LENGTH_SHORT).show()}
                    ) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(id = R.drawable.baseline_logout_24),
                            contentDescription = "Logout Icon" ,
                            )
                    }


                    profileArea(
                        Modifier
                            .padding(all = 20.dp)
                            .constrainAs(foreground) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start); end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom, 110.dp)
                            })
                }

            }
        }

        showProfileArea.value = true
        Handler(Looper.myLooper()!!).postDelayed({
            showImage.value = true
        },250)
    }


    @Composable
    fun profileArea(modifier:Modifier){
        AnimatedVisibility(
            visible = showProfileArea.value,
            enter = slideInVertically(initialOffsetY = {(imageSize*2).toInt()}, animationSpec = tween(500))
        ) {
            ConstraintLayout(modifier = modifier) {
                val (profilePicture,body) = createRefs()
                profileBody(
                    Modifier
                        .constrainAs(body) {
                            top.linkTo(parent.top, (imageSize * 1.2).dp)
                            start.linkTo(parent.start); end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(bottom = (150 + (imageSize / 2)).dp)

                )

                AnimatedVisibility(
                    visible = showImage.value,
                    modifier = Modifier.constrainAs(profilePicture) {
                        top.linkTo(parent.top, margin = (imageSize * 0.7).dp)
                        start.linkTo(parent.start); end.linkTo(parent.end)
                    },
                    enter = fadeIn(initialAlpha = 0.0f, animationSpec = tween(500,   50,  easing = EaseIn))
                ) {
                    circleImage(
                        model = API + "img/users/profile/${sP.getString("user_id","")!!}.png",
                        Modifier
                            .clip(shape = CircleShape)
                            .height(imageSize.dp)
                            .width(imageSize.dp)

                    )
                }
            }
        }
    }

    @Composable
    fun profileBody(modifier: Modifier){
        Card(modifier = modifier, colors = CardDefaults.cardColors(Color.White)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = (imageSize * 0.5).dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = sP.getString("nama","")!!, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = CP.DarkBlue, fontSize = 18.sp)
                Text(text = "Anggota", textAlign = TextAlign.Center,  color = CP.DarkBlue, fontSize = 14.sp)
                Spacer(Modifier.size(20.dp))
                userInfoCard(Color.White,CP.DarkBlue, Modifier
                    .padding(all = 1.dp)
                    .fillMaxWidth()
                )
                listPeminjaman()
                listKunjungan()
            }
        }
    }

    @Composable
    fun listKunjungan(){
        Column {
            Text(modifier = Modifier
                .padding(top = 25.dp, bottom = 15.dp)
                .fillMaxWidth(),text= "Daftar Kunjungan", fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign =  TextAlign.Start)
            LazyColumn(
                state = rememberLazyListState() ,
                modifier = Modifier.padding(bottom=10.dp).clip(RoundedCornerShape(5)),
                content = {
                    items(
                        count = dataKunjungan.size,
                        itemContent = {
                            Row(
                                Modifier
                                    .background(ColorPalette().Dark10)
                                    .padding(vertical = 5.dp)) {
                                Text(modifier=Modifier.padding(start=3.dp),text = ((it+1).toString()+"         ").substring(0,3))
                                pairText(param1 = dataKunjungan[it].keterangan, param2 = dataKunjungan[it].waktu_kunjungan.substring(0,19),.5f )
                            }
                        }
                    )
                }
            )

        }
    }

    fun fetchData() {
        val sP:SharedPreferences = context.getSharedPreferences("mcrs_pref",Context.MODE_PRIVATE)
        val aPI = API()
        val clientAPI = aPI.getClientAPI()
        val bookAPI = clientAPI.create(bukuAPI::class.java)
        val response = bookAPI.getUserBuku(UserBookModel(sP.getString("id_anggota","")!!))
        response.enqueue(object:retrofit2.Callback<UserBookModel>{
            override fun onResponse(
                call: Call<UserBookModel>,
                response: Response<UserBookModel>
            ) {
                try {
                    if(response.body()!=null){
                        response.body()?.let {
                            dataPinjaman.clear()
                            dataPinjaman.addAll(it.dataBuku)

                        }
                    }
                }catch (e:Error){
                    Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<UserBookModel>, t: Throwable) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun fetchDataKunjungan() {
        val sP:SharedPreferences = context.getSharedPreferences("mcrs_pref",Context.MODE_PRIVATE)
        val aPI = API()
        val clientAPI = aPI.getClientAPI()
        val bookAPI = clientAPI.create(kehadiranAPI::class.java)
        val response = bookAPI.getDataKunjungan(KunjunganAPIModel(sP.getString("id_anggota","")!!))
        response.enqueue(object:retrofit2.Callback<KunjunganAPIModel>{
            override fun onResponse(
                call: Call<KunjunganAPIModel>,
                response: Response<KunjunganAPIModel>
            ) {
                try {
                    if(response.body()!=null){
                        response.body()?.let {
                            dataKunjungan.clear()
                            dataKunjungan.addAll(it.data)

                        }
                    }
                }catch (e:Error){
                    Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<KunjunganAPIModel>, t: Throwable) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun copyData(datalist: MutableList<BookModels>){
        dataPinjaman.clear()
        dataPinjaman.addAll(datalist)
        if (datalist.size <1){
            fetchData()
        }
    }

    @Composable
    fun listPeminjaman(){
        Text(modifier = Modifier
            .padding(top = 25.dp, bottom = 15.dp)
            .fillMaxWidth(),text= "Buku yang dipinjam", fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign =  TextAlign.Start)
        LazyRow(Modifier.fillMaxWidth()){
            items(
                count = dataPinjaman.size,
                itemContent = {
                    BookCard(BookModel(
                        dataPinjaman[it].id_buku,
                        dataPinjaman[it].judul,
                        dataPinjaman[it].pengarang,
                        dataPinjaman[it].tahun_terbit,
                        dataPinjaman[it].penerbit,
                        dataPinjaman[it].kategori
                    ));
                }
            )
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun BookCard(buku:BookModel, height: Dp = 210.dp) {
        Card(
            modifier = Modifier
                .padding(all = 5.dp)
                .heightIn(max = height)
                .width(100.dp)
        ) {
            ConstraintLayout {
                val (column, img, fav) = createRefs()
                GlideImage(
                    model = API + "img/buku/${buku.id_buku}.png",
                    contentDescription = buku.judul!!,
                    modifier = Modifier
                        .constrainAs(img) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    it.centerCrop()
                }
                IconButton(
                    modifier = Modifier.constrainAs(fav) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_star_24),
                        tint = Color.Yellow,
                        contentDescription = "Star"
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(column) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .background(CP.Dark50)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.size(5.dp))
                    Text(
                        color = Color.White,
                        lineHeight = 12.sp,
                        text = buku.judul!!+"\n\n",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = {  },
                        ) {
                            Text(text = "Pinjam", color = Color.White, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }

    fun clearStatus(){
        this.showProfileArea.value = false
        this.showImage.value = false
    }

    @Composable
    fun userInfoCard(bg:Color, color:Color, modifier: Modifier){
        Card(
            colors = CardDefaults.cardColors(color)
        ) {
            Card(colors = CardDefaults.cardColors(bg), modifier = modifier) {
                Row(
                    Modifier
                        .padding(all = 10.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Column(Modifier.weight(0.33f)) {
                        Text(text = "Buku dibaca",fontSize = 12.sp, fontWeight = FontWeight.Bold,color = color, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Text(text = dataPinjaman.size.toString(),fontSize = 11.sp,color = color, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                    Column(Modifier.weight(0.34f)) {
                        Text(text = "Kunjungan", fontSize = 12.sp,fontWeight = FontWeight.Bold,color = color, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Text(text = "10", fontSize = 11.sp,color = color, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                    Column(Modifier.weight(0.33f)) {
                        Text(text = "ID Anggota", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Text(text = sP.getString("id_anggota","")!!, fontSize = 11.sp,  color = color, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalGlideComposeApi::class, ExperimentGlideFlows::class)
@Composable
fun circleImage(model:Any, modifier: Modifier){
    GlideImage(
        modifier = modifier,
        model = model,
        contentDescription = "Profile Picture"
    ){
        it.centerCrop()
    }
}





