package com.mcr.e_library

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mcr.e_library.apiInterface.bukuAPI
import com.mcr.e_library.model.BookModel
import com.mcr.e_library.ui.fragment.pairText
import com.mcr.e_library.ui.theme.ELibraryTheme
import com.mcr.e_library.util.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeBackup : ComponentActivity() {
    var bookData:ArrayList<BookModel> = ArrayList()
    val API:String = API().serverUrl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ELibraryTheme {
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview3() {
        ELibraryTheme {
        }
    }

//    @OptIn(ExperimentalFoundationApi::class)
//    @Composable
//    public fun HomeView(context: Context) {
//        var API = API()
//        var ClientAPI = API.getClientAPI()
//        var BookAPI = ClientAPI.create(bukuAPI::class.java)
//
//        val data:ArrayList<BookModel> = ArrayList()
//        var dataCount by remember {
//            mutableStateOf(data.count())
//        }
//        data.add(BookModel("Kalkulus"))
//        data.add(BookModel("Tutorial Java"))
//        data.add(BookModel("Aljabar.L"))
//        data.add(BookModel("Komik"))
//        dataCount = data.count()
//        BookAPI.getBuku().enqueue(/* callback = */ object: Callback<ArrayList<BookModel>> {
//            override fun onResponse(
//                call: Call<ArrayList<BookModel>>,
//                response: Response<ArrayList<BookModel>>
//            ) {
//                Toast.makeText(context,response.body()!!.get(0).judul,Toast.LENGTH_SHORT).show()
//                for(buku in response.body()!!){
//                    data.add(buku)
//                }
//
//                dataCount = data.count()
//
//            }
//
//            override fun onFailure(call: Call<ArrayList<BookModel>>, t: Throwable) {
//                Toast.makeText(context,t.toString()!!,Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        ELibraryTheme {
//            Surface(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(all = 10.dp),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                LazyColumn() {
//                    stickyHeader() {
//                        KategoriHeader(kategori = "Terbaru\n")
//                    }
//                    item {
//                        Column(Modifier.padding(top = 5.dp, bottom = 10.dp)) {
//                            LazyRow{
//                                items(dataCount) {
//                                    BookCard(title = data.get(it).judul)
//                                }
//                            }
//                        }
//                    }
//                    stickyHeader {
//                        KategoriHeader(kategori = "Komik\n")
//                    }
//                    item{
//                        Column(Modifier.padding(top = 5.dp, bottom = 10.dp)) {
//                            BookList()
//                        }
//                    }
//                    stickyHeader {
//                        KategoriHeader(kategori = "Majalah")
//                    }
//                    item{
//                        Column(Modifier.padding(top = 5.dp, bottom = 10.dp)) {
//                            BookList()
//                        }
//                    }
//                    stickyHeader {
//                        KategoriHeader(kategori = "Skripsi")
//                    }
//                    item{
//                        Column(Modifier.padding(top = 5.dp, bottom = 10.dp)) {
//                            BookList()
//                        }
//                    }
//                }
//
//            }
//        }
//    }
}
//
//val home = Home()
//
//@Composable
//fun NewBookList(){
//
//}
//
//@Composable
//fun KategoriHeader(kategori:String){
//    Text(
//        modifier = Modifier
//            .background(Color.White)
//            .fillMaxWidth(1f),
//        text = kategori, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//}
//
//@Composable
//fun BookList(){
//    var data:ArrayList<BookModel> = ArrayList()
//    data.add(BookModel("Bruh"))
//    data.add(BookModel("Bruh"))
//    data.add(BookModel("Bruh"))
//    data.add(BookModel("Bruh"))
//    LazyRow {
//        items(data.count()){
//            BookCard(title = data.get(it).judul)
//        }
//    }
//}
//
//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun BookCard(title: String, height: Dp = 210.dp) {
//    var titles by remember {
//        mutableStateOf( title + "\n\n")
//    }
//
//    Card(
//        modifier = Modifier
//            .padding(all = 5.dp)
//            .heightIn(max = height + 60.dp)
//            .width(125.dp)
//    ) {
//        ConstraintLayout {
//            val (button, column, img, fav) = createRefs()
//
//            GlideImage(
//                model = home.API + "img/buku/$title.png",
//                contentDescription = title,
//                modifier = Modifier
//                    .constrainAs(img) {
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                        top.linkTo(parent.top)
//                    }
//                    .fillMaxWidth()
//                    .height(180.dp)
//            ) {
//                it.centerCrop()
//            }
//
//            IconButton(
//                modifier = Modifier.constrainAs(fav) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                },
//                onClick = { /*TODO*/ }
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.baseline_star_24),
//                    tint = Color.Yellow,
//                    contentDescription = "Star"
//                )
//            }
//
//            Column(
//                modifier = Modifier
//                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 5.dp)
//                    .constrainAs(column) {
//                        top.linkTo(img.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    },
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    lineHeight = 12.sp,
//                    text = titles,
//                    textAlign = TextAlign.Center,
//                    fontSize = 12.sp,
//                    maxLines = 2,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Row(horizontalArrangement = Arrangement.Center) {
//                    Button(
//                        onClick = { /*TODO*/ },
//                    ) {
//                        Text(text = "Pinjam", color = Color.White, fontSize = 9.sp)
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//
//

//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun DescCard(modifier: Modifier){
//    AnimatedVisibility(
//        enter = scaleIn(initialScale = 0.3f),
//        exit = scaleOut(),
//        visible = showDesc.value,
//        modifier = modifier,
//    ) {
//        Card(
//            Modifier
//                .padding(bottom = 50.dp, top = 20.dp, start = 15.dp, end = 15.dp)
//                .shadow(elevation = Dp.Infinity, clip = true, ambientColor = cp.Dark25)
//                .offset(y = -(20).dp)
//        ) {
//            Column(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(all = 20.dp)
//                    .fillMaxHeight(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Top) {
//                Text(
//                    modifier = Modifier.padding(top=30.dp, bottom = 20.dp),
//                    text = targetBook.value.judul,
//                    textAlign = TextAlign.Center,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold)
//                //Gambar Buku
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .height(250.dp),
//                    horizontalArrangement = Arrangement.Center) {
//                    Column(Modifier.weight(0.2f)){}
//                    Column(modifier = Modifier
//                        .weight(0.6f)
//                        .clip(RoundedCornerShape(15.dp))) {
//                        GlideImage(model = api+"img/buku/"+targetBook.value.id_buku+".png", contentDescription = "Cover Buku"){it.centerCrop()}
//                    }
//                    Column(Modifier.weight(0.2f)){}
//
//                }
//
//                //Detail Buku
//                Text(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp, bottom = 10.dp),color = Color.Black, text = "Detail", fontWeight = FontWeight.Bold)
//                Column{
//                    pairText("Kategori",targetBook.value.kategori)
//                    pairText("Pengarang",targetBook.value.pengarang)
//                    pairText("Penerbit",targetBook.value.penerbit)
//                    pairText("Thn.Terbit",targetBook.value.tahun_terbit.toString())
//                }
//                //Deskripsi
//                Text(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp, bottom = 10.dp),color = Color.Black, text = "Deskripsi", fontWeight = FontWeight.Bold)
//                Text(modifier = Modifier
//                    .fillMaxWidth()
//                    .heightIn(max = (14 * 12).dp)
//                    .verticalScroll(rememberScrollState())
//                    ,text = "Ini adalah area deskripsi buku , test 1 2 3 sdfkldsjfsdfkjsdlkfjsdklfjsdkljfsdkjfdfdflsjkdflkjdfahsidfhuasdnfaniunhvuifynuoryf9dggyagbyfgyuasdgynuifahnuisdfhumahuisdfhmiosdfhunasdhnfsdhnfhasdfnasdmfhoasdhfashudfahumsdfmasxumfasumdfmsdfasdfmasdfjiasjmidfajmisdfjmasvhuscvgnufgbnuadgnusfgnuasdgnufgnusdfhnuioasdhuifasdhnuifhnuiasdhnuiofanuisdfanuiosdfnuiosoenfnauinshuodivnhushnuenurnusehnuahnudvhnuahnusdfhnunusadnufhnuse ufhuhughnuerghnurnuhgernugnuergnuweghu greguerhnugrhnug rger ug huehurgerhuighuerighuierg hueiur hgeruihgruioh egriehgerhuiog erguieh ger",fontSize=14.sp, textAlign = TextAlign.Justify)
//
//                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp)){
//                    Button( onClick = { }){
//                        Text(text = "Konfirmasi", color = Color.White)
//                    }
//                    Spacer(modifier = Modifier.size(16.dp))
//                    Button(onClick = { showDesc.value = false ; mainMenu.showBar.value = true}){
//                        Text(text = "Close", color = Color.White)
//                    }
//                }
//
//            }
//        }
//    }
//}
