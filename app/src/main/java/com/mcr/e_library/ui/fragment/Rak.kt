package com.mcr.e_library.ui.fragment


import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mcr.e_library.R
import com.mcr.e_library.apiInterface.bukuAPI
import com.mcr.e_library.model.UserBookModel
import com.mcr.e_library.ui.myCustomUI
import com.mcr.e_library.util.API
import com.mcr.e_library.util.ColorPalette
import org.json.JSONObject
import qrcode.QRCode
import qrcode.color.Colors
import retrofit2.Call
import retrofit2.Response

class Rak(private val context: Context) {
    private val myCustomUI = myCustomUI()
    private val cp:ColorPalette = ColorPalette()
    private var keyword:MutableState<String> = mutableStateOf("")
    private var kategori: MutableState<String> = mutableStateOf("Semua")
    private val kategories: List<String> = listOf("Semua","Pending","Dipinjam","Dikembalikan")
    private val btnColor: List<Color> = listOf(cp.LightBlue,Color.Yellow, Color.Green,cp.LightBlue)
    var dataBuku: MutableList<UserBookModel.BookModels> = mutableListOf()
    private var filteredDataBuku: MutableList<UserBookModel.BookModels> = mutableListOf()
    private var isRefresh: MutableState<Boolean> = mutableStateOf(false)
    private var selectedBook: MutableState<UserBookModel.BookModels> = mutableStateOf(UserBookModel.BookModels())
    private var showQR:MutableState<Boolean> = mutableStateOf(false)

    fun generateQR(dataBuku:UserBookModel.BookModels):ByteArray{
        val jsonObject = JSONObject()
        jsonObject.put("tipe","peminjaman")
        jsonObject.put("id_buku",dataBuku.id_buku)
        jsonObject.put("id_peminjaman",dataBuku.id_peminjaman)
        val QRCode:QRCode = QRCode.ofSquares().withColor(Colors.BLACK).build(jsonObject.toString())
        return QRCode.render().getBytes()
    }

    fun fetchData() {
        isRefresh.value = true
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
                            dataBuku.clear()
                            dataBuku.addAll(it.dataBuku)

                        }
                    }
                }catch (e:Error){
                    Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                }
                isRefresh.value = false

            }

            override fun onFailure(call: Call<UserBookModel>, t: Throwable) {
                isRefresh.value = false
                Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show()
            }

        })

    }

    @Composable
    fun RakMainView(){
        ConstraintLayout {
            val (qr,content) = createRefs()
            Column (
                modifier = Modifier
                    .padding(10.dp)
                    .constrainAs(content) {
                        top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start);end.linkTo(parent.end)
                    }
            ){
                myCustomUI.MysearchBar(modifiers = Modifier.padding(0.dp), actions = {

                })
                KategoriList()
                ListRakView()
            }

            QrCodeView(
                Modifier
                    .fillMaxSize()
                    .constrainAs(qr) {
                        top.linkTo(parent.top); bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start); end.linkTo(parent.end)
                    })
        }

    }

    @Composable
    fun KategoriList(){
        LazyRow(
            modifier = Modifier.padding(top=10.dp),
            content = {
                items(
                    count = kategories.size,
                    itemContent = {
                        Button(
                            modifier = Modifier.padding(end = 5.dp),
                            colors = ButtonDefaults.buttonColors(if(kategories[it]!=kategori.value)ColorPalette().LightBlue else ColorPalette().Pink),
                            shape = RoundedCornerShape(50,50,50,50),
                            onClick = { kategori.value = kategories[it] }
                        ) {
                            Text(text = kategories[it], fontSize = 14.sp, color = Color.White)
                        }
                    }
                )
            }
        )
    }

    @Composable
    fun ListRakView(){
        if(isRefresh.value) kategori.value = kategori.value

        filteredDataBuku.clear()
        filteredDataBuku.addAll(FilterByCategory(kategori.value))

        LazyColumn(
            modifier = Modifier.padding(5.dp),
            content = {
                items(
                    count = filteredDataBuku.size,
                    itemContent = {
                        myCustomUI.UserbookListView(
                            icons = R.drawable.baseline_qr_code_scanner_36,
                            enabled = if(getKategori(filteredDataBuku[it])==2) true else false,
                            btnColor = btnColor[getKategori(filteredDataBuku[it])],
                            text = kategories[getKategori(filteredDataBuku[it])],
                            dataBuku = filteredDataBuku[it],
                            iconClick = {
                                showQR.value = true
                                selectedBook.value = filteredDataBuku[it]
                            },
                            onClickAction = {

                            }
                        )
                    }
                )
            }
        )
    }

    @Composable
    fun QrCodeView(modifier: Modifier){
        val imageData:ByteArray = generateQR(selectedBook.value)
        AnimatedVisibility(
            modifier = modifier
                .background(cp.Dark25)
                .clickable(true, null, null, {  }),
            visible = showQR.value,
            enter = fadeIn(),
            exit =  fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Image(modifier = Modifier.padding(25.dp),bitmap = BitmapFactory.decodeByteArray(imageData,0,imageData.size).asImageBitmap(), contentDescription = null )
                Button(
                    modifier = Modifier.padding(20.dp),
                    onClick = { showQR.value = false },
                    colors = ButtonDefaults.buttonColors(cp.LightBlue)
                ) {
                    Text(text = "Close",color = Color.White)
                }
            }
        }

    }

    private fun getKategori(it:UserBookModel.BookModels):Int{
        if (it.id_pengembalian =="") return 3
        else if (it.status=="disetujui") return 2
        else if ( it.status=="pending") return 1
        return 0
    }

    private fun FilterByCategory(kategori:String):java.util.ArrayList<UserBookModel.BookModels>{
        val filteredBook:ArrayList<UserBookModel.BookModels> = ArrayList()
        var judul: String
        dataBuku.forEach {
            if(kategori==kategories[getKategori(it)] || kategori == kategories[0]){
                judul = it.judul.toLowerCase()
                if(judul.contains(myCustomUI.keyword.value.toLowerCase())){
                    filteredBook.add(it)
                }
            }
        }
        return filteredBook
    }

}