package com.mcr.e_library.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.mcr.e_library.MainMenu
import com.mcr.e_library.R
import com.mcr.e_library.apiInterface.bukuAPI
import com.mcr.e_library.model.BookModel
import com.mcr.e_library.model.SearchModel
import com.mcr.e_library.myTextField
import com.mcr.e_library.PlaceHolder
import com.mcr.e_library.apiInterface.peminjamanAPI
import com.mcr.e_library.model.PeminjamanModel
import com.mcr.e_library.ui.myCustomUI
import com.mcr.e_library.ui.theme.ELibraryTheme
import com.mcr.e_library.util.API
import com.mcr.e_library.util.ColorPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import kotlin.math.absoluteValue

class Home(private var context: Context, private var mainMenu: MainMenu) : ViewModel() {
    private val cp = ColorPalette()
    private var key = 0
    private val myCustomUI:myCustomUI = myCustomUI()
    private lateinit var sp:SharedPreferences

    //Peminjaman
    private var reqPeminjaman:MutableState<Boolean> = mutableStateOf(false)

    //Pencarian
    private var showResult:MutableState<Boolean> = mutableStateOf(false)
    private var resultCount:MutableState<Int> = mutableIntStateOf(0)
    private var searchResult:MutableState<ArrayList<BookModel>> = mutableStateOf(ArrayList<BookModel>())

    private val api:String = API().serverUrl
    private var data: MutableState<ArrayList<BookModel>> = mutableStateOf(ArrayList())
    private var dataTemp: MutableState<ArrayList<BookModel>> = mutableStateOf(ArrayList())
    private var dataCount: MutableState<Int> = mutableIntStateOf(0)
    private var pagerState: PagerState = PagerState()
    private val showDesc:MutableState<Boolean> = mutableStateOf(false)
    private val showImage:MutableState<Boolean> = mutableStateOf(false)
    private val homeScroll:ScrollState = ScrollState(0)
    private var targetBook:MutableState<BookModel> = mutableStateOf(
        BookModel("B0000000001","Judul Buku","-",0,"-","-")
    )


    init {
        sp = context.getSharedPreferences("mcrs_pref",Context.MODE_PRIVATE)
        val policy  = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        dataTemp.value.add(BookModel("B000000011","Naruto Shippuuden Vol.12","Andrea",2023,"Erlangga","Umum"))
        dataTemp.value.add(BookModel("B000000022","Naruto Shippuuden Vol.13","Andrea",2023,"Erlangga","Umum"))
        dataTemp.value.add(BookModel("B000000033","Perahu Kertas","Andrea",2023,"Erlangga","Umum"))
        dataTemp.value.add(BookModel("B000000044","Komik","Andrea",2023,"Erlangga","Umum"))
//        dataTemp.value.add(BookModel("B000000055","Bruh","Andrea",2023,"Erlangga","Umum"))
//        dataTemp.value.add(BookModel("B000000066","Bruh","Andrea",2023,"Erlangga","Umum"))
//        dataTemp.value.add(BookModel("B000000077","Bruh","Andrea",2023,"Erlangga","Umum"))
//        dataTemp.value.add(BookModel("B000000088","Bruh","Andrea",2023,"Erlangga","Umum"))
        dataCount.value = data.value.count()
        fetchData(context)
    }

    private fun updateSearchResult(data: ArrayList<BookModel>){
        searchResult.value = data
        resultCount.value = data.size
        showResult.value = true
    }

    private fun fetchData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            val aPI = API()
            val clientAPI = aPI.getClientAPI()
            val bookAPI = clientAPI.create(bukuAPI::class.java)
            val response = bookAPI.getBuku()
            response.enqueue(object:retrofit2.Callback<ArrayList<BookModel>>{
                override fun onResponse(
                    call: Call<ArrayList<BookModel>>,
                    response: Response<ArrayList<BookModel>>
                ) {
                    try {
                        if(response.body()!=null){
                            response.body()?.let {
                                data.value.addAll(it)
                                dataCount.value = data.value.count()
                            }
                        }
                    }catch (e:Error){
                        Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ArrayList<BookModel>>, t: Throwable) {
                   Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun pinjamBuku(context: Context, idBuku:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val aPI = API()
            val clientAPI = aPI.getClientAPI()
            val bookAPI = clientAPI.create(peminjamanAPI::class.java)
            val response = bookAPI.reqPeminjaman(PeminjamanModel(sp.getString("id_anggota","")!!, idBuku))
            response.enqueue(object:retrofit2.Callback<PeminjamanModel>{
                override fun onResponse(
                    call: Call<PeminjamanModel>,
                    response: Response<PeminjamanModel>
                ) {
                    try {
                        if(response.body()!=null){
                            response.body()?.let {
                                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch (e:Error){
                        Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                    }
                    reqPeminjaman.value = false
                    showDesc.value = false
                }

                override fun onFailure(call: Call<PeminjamanModel>, t: Throwable) {
                    reqPeminjaman.value = false
                    showDesc.value = false
                    Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun fetchDatabyKey(context: Context, key:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val aPI = API()
            val clientAPI = aPI.getClientAPI()
            val bookAPI = clientAPI.create(bukuAPI::class.java)
            val response = bookAPI.searchBuku(SearchModel(key))
            response.enqueue(object:retrofit2.Callback<ArrayList<BookModel>>{
                override fun onResponse(
                    call: Call<ArrayList<BookModel>>,
                    response: Response<ArrayList<BookModel>>
                ) {
                    try {
                        if(response.body()!=null){
                            response.body()?.let {
                                updateSearchResult(it)
                            }
                        }
                    }catch (e:Error){
                        Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ArrayList<BookModel>>, t: Throwable) {
                    Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    @Composable
    fun HomeView() {
        ELibraryTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                color = MaterialTheme.colorScheme.background

            ) {
                ConstraintLayout {
                    val (search,main,desc,searchResult) = createRefs()

                    MainView(modifier = Modifier
                        .verticalScroll(homeScroll)
                        .constrainAs(main) {
                            top.linkTo(parent.top)
                        }
                    )

                    searchBar(modifiers = Modifier
                        .padding(top = 10.dp)
                        .background(Color.Transparent)
                        .constrainAs(search) {
                            top.linkTo(parent.top)
                        }
                    )

                    searchResultArea(
                        modifier = Modifier
                            .padding(top = 70.dp)
                            .fillMaxSize()
                            .background(Color.White)
                            .constrainAs(searchResult) {
                                top.linkTo(
                                    search.bottom,
                                    margin = 100.dp
                                );bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start);end.linkTo(parent.end)
                            }
                    )

                    DescCard(modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 10.dp)
                        .constrainAs(desc) {
                            top.linkTo(parent.top); bottom.linkTo(parent.bottom); start.linkTo(
                            parent.start
                        ); end.linkTo(parent.end)
                        }
                    )

                }

            }
        }
    }
    
    @Composable
    fun MainView(modifier: Modifier){
        Column(
            modifier = modifier
        ) {
            Carousel()
            Text(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(1f),
                text = "Terbaru", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Column(Modifier.padding(top = 5.dp, bottom = 10.dp)) {
                LazyRow{
                    items(
                        count = dataCount.value,
                        key = {
                            data.value[it].id_buku
                        },
                        itemContent = {
                            BookCard(buku = data.value[it])
                        }
                    )
                }
            }
            BookList("Komik")
            BookList("Majalah")
            BookList("Skripsi")
            Box(modifier = Modifier.height(80.dp))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun searchBar(modifiers:Modifier){
        var keyword by remember {
            mutableStateOf("")
        }
        val focusManager:FocusManager = LocalFocusManager.current
        AnimatedVisibility(
            visible = !homeScroll.isScrollInProgress,
            enter = slideInVertically(initialOffsetY = {-50}),
            exit = slideOutVertically(targetOffsetY = {-50})
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (bar, btn) = createRefs()

                myTextField(
                    fontColor = Color.White,
                    value = keyword,
                    onValueChange = {
                        keyword = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        selectionColors = TextSelectionColors(
                            handleColor = Color.White,
                            backgroundColor = Color.White
                        ),
                        disabledTextColor = cp.Invisible,
                        focusedIndicatorColor = cp.LightBlue,
                        focusedTrailingIconColor = Color.White,
                        unfocusedIndicatorColor = cp.Invisible,
                        disabledIndicatorColor = cp.Invisible,
                        containerColor = ColorPalette().Dark25,
                        textColor = Color.Blue,
                        placeholderColor = Color.White
                    ),
                    shape = RoundedCornerShape(50, 50, 50, 50),
                    modifier = modifiers.constrainAs(bar) {
                        top.linkTo(parent.top);end.linkTo(parent.end);start.linkTo(parent.start);bottom.linkTo(
                        parent.bottom
                    )
                    },
                    leadingIcon = {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "Password"
                        )
                    },
                    placeholder = {
                        PlaceHolder("Cari buku")
                    },
                    keywordOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            fetchDatabyKey(context,keyword)

                        }
                    )
                )

                IconButton(
                    modifier = Modifier.constrainAs(btn) {
                        end.linkTo(parent.end,5.dp);top.linkTo(parent.top,5.dp);bottom.linkTo(parent.bottom)
                    },
                    onClick = {
                        keyword = ""
                        focusManager.clearFocus(true)
                        showResult.value = false
                    }
                ) {
                    Image(imageVector = Icons.Default.Clear, contentDescription = null)
                }

            }
        }

    }

    fun showImage(){
        Handler(Looper.getMainLooper()).postDelayed({
            showImage.value = true
        },500)
    }

    fun hideImage(){
        showImage.value = false
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun DescCard(modifier: Modifier){
        AnimatedVisibility(
            enter = scaleIn(initialScale = 0.3f),
            exit = scaleOut(),
            visible = showDesc.value,
            modifier = modifier.background(Color.Transparent)
        ) {
            Card(
                colors = CardDefaults.cardColors(Color.Transparent),
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(bottom = 50.dp, top = 20.dp, start = 15.dp, end = 15.dp)
                    .offset(y = -(20).dp)
                    ) {
                ConstraintLayout(
                    Modifier
                        .background(Color.Transparent)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    val (image,foot,back,content) = createRefs()

                    Column(
                        Modifier
                            .constrainAs(back){
                                top.linkTo(parent.top)
                            }
                    ) {
                        Column(modifier = Modifier
                            .weight(0.28f)
                            .fillMaxSize()
                        ) {}

                        Column(modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .background(cp.SoftDarkGray)
                            .weight(0.72f)
                            .fillMaxSize()
                        ){}
                    }

                    //Gambar Buku
                    Box(
                        Modifier
                            .background(Color.Transparent)
                            .fillMaxSize()
                            .constrainAs(image) {
                                top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start); end.linkTo(parent.end)
                            }
                    ) {
                        this@Card.AnimatedVisibility(
                            visible = showImage.value,
                            enter = slideInVertically(initialOffsetY = {it}),
                            exit = slideOutVertically(targetOffsetY = {it})
                        ) {
                            Column {
                                Column(modifier = Modifier
                                    .background(Color.Transparent)
                                    .padding(horizontal = 25.dp)
                                    .weight(0.5f)
                                    .fillMaxSize()
                                ) {
                                    GlideImage(model = api+"img/buku/"+targetBook.value.id_buku+".png", contentDescription = "Cover Buku"){it.centerCrop()}
                                }
                                Column(
                                    Modifier
                                        .weight(.5f)
                                        .fillMaxSize()) {}
                            }
                        }

                    }

                    Column(
                        Modifier
                            .fillMaxSize()
                            .constrainAs(content) {
                                top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start); end.linkTo(parent.end)
                            }
                    ) {
                        Column(modifier = Modifier
                            .weight(0.35f)
                            .fillMaxSize()
                        ) {}

                        Column(modifier = Modifier
                            .clip(RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
                            .background(Color.White)
                            .weight(0.65f)
                            .fillMaxSize()
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp, bottom = 10.dp),
                                style = TextStyle(shadow = Shadow(Color.White, blurRadius =  1f)),
                                text = targetBook.value.judul,
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                maxLines = 3,
                                fontWeight = FontWeight.ExtraBold)
                            //Detail Buku
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, bottom = 10.dp)
                                    .padding(horizontal = 20.dp),
                                color = Color.Black,
                                text = "Detail",
                                fontWeight = FontWeight.Bold
                            )
                            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                pairText("Kategori", targetBook.value.kategori)
                                pairText("Pengarang", targetBook.value.pengarang)
                                pairText("Penerbit", targetBook.value.penerbit)
                                pairText("Thn.Terbit", targetBook.value.tahun_terbit.toString())
                            }
                            //Deskripsi
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 20.dp, bottom = 10.dp),
                                color = Color.Black,
                                text = "Deskripsi",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .heightIn(max = (15 * 12).dp)
                                    .verticalScroll(rememberScrollState()),
                                text = "Ini adalah area deskripsi buku , test 1 2 3 sdfkldsjfsdfkjsdlkfjsdklfjsdkljfsdkjfdfdflsjkdflkjdfahsidfhuasdnfaniunhvuifynuoryf9dggyagbyfgyuasdgynuifahnuisdfhumahuisdfhmiosdfhunasdhnfsdhnfhasdfnasdmfhoasdhfashudfahumsdfmasxumfasumdfmsdfasdfmasdfjiasjmidfajmisdfjmasvhuscvgnufgbnuadgnusfgnuasdgnufgnusdfhnuioasdhuifasdhnuifhnuiasdhnuiofanuisdfanuiosdfnuiosoenfnauinshuodivnhushnuenurnusehnuahnudvhnuahnusdfhnunusadnufhnuse ufhuhughnuerghnurnuhgernugnuergnuweghu greguerhnugrhnug rger ug huehurgerhuighuerighuierg hueiur hgeruihgruioh egriehgerhuiog erguieh ger",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Justify
                            )


                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center, modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .constrainAs(foot){
                                bottom.linkTo(parent.bottom, 20.dp)
                                start.linkTo(parent.start);end.linkTo(parent.end)
                            }
                    ) {
                        Button(
                            enabled = !reqPeminjaman.value,
                            onClick = {
                                reqPeminjaman.value = true
                                pinjamBuku(context,targetBook.value.id_buku)
                            }
                        ) {
                            Text(text = "Konfirmasi", color = Color.White)
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Button(onClick = {
                            showImage.value = false;
                            showDesc.value = false; mainMenu.showBar.value = true
                        }) {
                            Text(text = "Close", color = Color.White)
                        }
                    }

                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun Carousel(){
        pagerState = rememberPagerState()
        if(!pagerState.interactionSource.collectIsDraggedAsState().value){
            LaunchedEffect(Unit) {
                while (true) {
                    delay(5000)
                    pagerState.animateScrollToPage(
                        page = (pagerState.currentPage + 1)%3,
                    )
                }
            }
        }
        com.google.accompanist.pager.HorizontalPager(count = 3, state =pagerState ) {
            Card(
                Modifier
                    .height(250.dp)
                    .padding(top = 70.dp, bottom = 30.dp)
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(it).absoluteValue
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            amount = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            amount = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                GlideImage(model = api + "img/banner/0" + (it+1) + ".png", contentDescription = "deskripsi"){
                    it.centerCrop()
                }
            }
        }
    }

    @Composable
    fun BookList(kategori:String){
        key += 1

        Column(Modifier.padding(top = 5.dp, bottom = 10.dp)) {
            Text(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(1f),
                text = kategori, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            LazyRow{
                items(
                    count = dataTemp.value.count(),
                    key = { dataTemp.value[it].id_buku },
                    itemContent = {
                        BookCard(dataTemp.value[it])
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun BookCard(buku:BookModel, height: Dp = 210.dp) {
        Card(
            modifier = Modifier
                .padding(all = 5.dp)
                .heightIn(max = height + 60.dp)
                .width(125.dp)
        ) {
            ConstraintLayout {
                val (column, img, fav) = createRefs()
                GlideImage(
                    model = api + "img/buku/${buku.id_buku}.png",
                    contentDescription = buku.judul,
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
                        .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 5.dp)
                        .constrainAs(column) {
                            top.linkTo(img.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        lineHeight = 12.sp,
                        text = buku.judul!!+"\n\n",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = {  hideImage();showImage();showDesc.value = true; targetBook.value = buku },
                        ) {
                            Text(text = "Pinjam", color = Color.White, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun searchResultArea(modifier: Modifier){
        AnimatedVisibility(
            modifier = modifier,
            visible = showResult.value,
            enter = slideInVertically(initialOffsetY = {it}),
            exit = slideOutVertically(targetOffsetY = {it})
        ) {
            Column {
                Text(modifier = Modifier.padding(top=20.dp, bottom = 30.dp),text="Menampilkan "+resultCount.value+" hasil pencarian", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    state = rememberLazyListState(),
                    content = {
                        items(
                            count = resultCount.value,
                            key = {
                                searchResult.value[it].id_buku
                            },
                            itemContent = {
                                myCustomUI.bookListView(searchResult.value[it], onClickAction = {
                                    showDesc.value = true
                                    targetBook.value = searchResult.value[it]
                                })
                            }
                        )
                        item { Box(modifier = Modifier.height(60.dp)) }
                    }
                )
            }
        }

    }
}

@Composable
fun pairText(param1:String, param2:String, fweight:Float=.3f){
    Row {
        Column(Modifier.weight(fweight)) {
            Text(text = param1,fontSize = 14.sp, color = Color.Black)
        }
        Column {
            Text(": ",fontSize = 14.sp, color = Color.Black)
        }
        Column(Modifier.weight(1f-fweight)) {
            Text(text = param2,fontSize = 14.sp, color = Color.Black)
        }
    }
}










