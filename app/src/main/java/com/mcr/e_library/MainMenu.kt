@file:JvmName("MainMenuKt")

package com.mcr.e_library

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mcr.e_library.apiInterface.bukuAPI
import com.mcr.e_library.apiInterface.kehadiranAPI
import com.mcr.e_library.apiInterface.peminjamanAPI
import com.mcr.e_library.model.KunjunganModel
import com.mcr.e_library.model.PeminjamanModel
import com.mcr.e_library.model.UserBookModel
import com.mcr.e_library.ui.fragment.Home
import com.mcr.e_library.ui.fragment.Profile
import com.mcr.e_library.ui.fragment.Rak
import com.mcr.e_library.ui.fragment.Scan
import com.mcr.e_library.ui.fragment.Settings
import com.mcr.e_library.ui.myCustomUI
import com.mcr.e_library.ui.theme.ELibraryTheme
import com.mcr.e_library.util.API
import com.mcr.e_library.util.ColorPalette
import com.mcr.e_library.util.Menu
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class MainMenu : ComponentActivity() {
    private val items = listOf(Menu.Home, Menu.Rak,Menu.Empty, Menu.Setting,Menu.Profile)
    private val cp = ColorPalette()
    private var totalBack:Int = 0
    private var isCD:Boolean = false
    private val CAMERA_REQ:Int = 303

    lateinit var sP: SharedPreferences

    private var input:MutableState<String> = mutableStateOf("")
    private var targetPeminjaman:MutableState<PeminjamanModel> = mutableStateOf(PeminjamanModel("",""))


    //Deklarasi Kelas untuk View
    private lateinit var home:Home
    private lateinit var rak: Rak
    private lateinit  var profile:Profile
    private lateinit var scan: Scan
    private lateinit var  settings: Settings
    private var showInputField:MutableState<Boolean> = mutableStateOf(false)
    private var showConfirmation:MutableState<Boolean> = mutableStateOf(false)

    //Fungsi Composable pada kelas yang dideklarasikan
    private lateinit var rakView: @Composable () -> Unit
    private lateinit var  profView: @Composable () -> Unit
    private lateinit var  homeView: @Composable () -> Unit
    private lateinit var scanView: @Composable () -> Unit
    private lateinit var settingsView: @Composable () -> Unit

    private var selectedScreen:MutableState<String> =  mutableStateOf(Menu.Home.route)
    var showBar:MutableState<Boolean> = mutableStateOf(false)
    private val align = FabPosition.Center


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotNavBar()
        }
        Handler(Looper.getMainLooper()!!).postDelayed({
            showBar.value = true
        },1000)

        sP = this.getSharedPreferences("mcrs_pref", Context.MODE_PRIVATE)

        home =  Home(this,this)
        homeView = { home.HomeView() }
        profile = Profile(this)
        profView = { profile.ProfileMainView()}
        rak = Rak(this)
        rakView = {rak.RakMainView()}
        scan = Scan(this)
        scanView = {scan.scanMainView()}
        settings = Settings()
        settingsView = {settings.settingsMainView()}
        setupPermissions()
    }

    fun beginCountDown(){
        totalBack += 1
        if(totalBack==1) {
            Toast.makeText(this,"Tekan sekali lagi untuk keluar !",Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                totalBack = 0
            },3000)
        }
        if(totalBack>1) finishAffinity()
        isCD = true
        Handler(Looper.getMainLooper()).postDelayed({
            isCD = false
        },500)

    }

    @Deprecated("Deprecated in Java", ReplaceWith("this.finishAffinity()"))
    override fun onBackPressed() {
        if(!isCD) beginCountDown()
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview2() {
        ELibraryTheme {
            BotNavBar()
        }
    }

    @Composable
    fun BotNavBar() {
        Scaffold(
            bottomBar = {
                MybottomNavBar()
            },
            isFloatingActionButtonDocked = true,
            floatingActionButton = {
                FAB()
            },
            floatingActionButtonPosition = align

        ){ innerPadding ->
            val pad = innerPadding
            when(selectedScreen.value) {
                "home" -> {
                    profile.clearStatus()
                    homeView()
                }
                "profile" ->{
                    profView()
                    profile.fetchDataKunjungan()
                    profile.copyData(rak.dataBuku)
                }
                "rak" -> {
                    profile.clearStatus()
                    rakView()
                    rak.fetchData()
                }
                "settings" -> {
                    profile.clearStatus()
                    settingsView()
                }
            }
            ConstraintLayout(modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()) {
                val (inputField) = createRefs()


                AnimatedVisibility(
                    modifier = Modifier.constrainAs(inputField){
                        top.linkTo(parent.top);start.linkTo(parent.start);end.linkTo(parent.end);bottom.linkTo(parent.bottom)
                    },
                    visible = showInputField.value ,
                    enter = fadeIn(),exit = fadeOut()
                ) {
                    myCustomUI().inputField(
                        label = "Masukkan keterangan",
                        modifiers = Modifier,
                        input = input,
                        onClickAction = {
                            reqAddKunjungan(
                                KunjunganModel(
                                    sP.getString("id_anggota","")!!,
                                    sP.getString("level","")!!,
                                    input.value
                                )
                            )
                            showInputField.value = false
                        }
                    );
                }

                AnimatedVisibility(
                    modifier = Modifier.constrainAs(inputField){
                        top.linkTo(parent.top);start.linkTo(parent.start);end.linkTo(parent.end);bottom.linkTo(parent.bottom)
                    },
                    visible = showConfirmation.value ,
                    enter = fadeIn(),exit = fadeOut()
                ) {
                    myCustomUI().confirmDialog(
                        peminjamanModel = targetPeminjaman,
                        modifiers = Modifier,
                        onClickAction = {
                            processDataPeminjaman()
                        }
                    );
                }

            }

        }
    }

    @Composable
    fun MybottomNavBar(){
        AnimatedVisibility(
            visible = showBar.value,
            enter = scaleIn(initialScale = 0f)
        ) {
            BottomAppBar(
                backgroundColor = cp.Invisible,
                modifier = Modifier
                    .height(75.dp)
                ,
                cutoutShape = CircleShape,
                elevation = 0.dp
            ) {
                BottomNavigation(
                    backgroundColor = cp.Invisible,
                    modifier = Modifier
                        .padding(12.dp, 0.dp, 12.dp, 12.dp)
                        .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                        .height(100.dp)
                        .background(cp.largeRadialGradient),
                    elevation = 0.dp
                ) {
                    items.forEach { it ->
                        var isIcon = false
                        BottomNavigationItem(
                            icon = {
                                it.icon?.let {
                                    isIcon = true
                                    Icon(
                                        imageVector = it,
                                        contentDescription = "",
                                        modifier = Modifier.size(30.dp),
                                        tint = Color.White

                                    )
                                }
                            },
                            label = {
                                it.title?.let {
                                    var text = ""
                                    if(isIcon) text = it
                                    Text(
                                        text = text,
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }
                            },
                            selected = it.title == selectedScreen.value,
                            onClick = {
                                if(isIcon)selectedScreen.value = it.route
                            }
                        )
                    }
                }
            }
        }

    }

    //Aksi yang dilakukan setelah scan QR code berhasil
    fun scanAction(data:JSONObject){
        if(sP.getString("level","")!! == "admin"){
            targetPeminjaman.value.id_peminjaman = data.getString("id_peminjaman")
            reqDataPeminjaman()
            showConfirmation.value =  true
        }else{
            if(data.getString("tipe")=="kunjungan"){
                showInputField.value = true
            }
        }
    }

    @Composable
    fun FAB() {
        val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
            var res:String
            var showMsg:Boolean = true
            var status:String
            when(result){
                is QRResult.QRSuccess -> {
                    status = "success"
                    res = result.content.rawValue!!
                    showMsg = false
                    scanAction(JSONObject(res))
                }
                is QRResult.QRError ->{
                    status = "error"
                    res = "Error "
                }
                is QRResult.QRMissingPermission -> {
                    status = "error"
                    res = "Izin kamera tidak diberikan !"
                }
                is QRResult.QRUserCanceled -> {
                    status = "error"
                    showMsg = false
                    res = "Operasi dibatalkan !"
                }

            }
            if(showMsg) Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
        }
        AnimatedVisibility(
            visible = showBar.value,
            enter = scaleIn(initialScale = 0f)
        ) {
            FloatingActionButton(
                modifier = Modifier.size(60.dp),
                onClick = {
                    scanQrCodeLauncher.launch(null)
                },
                shape = CircleShape,
                contentColor = Color.White,
                containerColor = cp.LightBlue
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                    contentDescription = "float_icon"
                )

            }
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun reqAddKunjungan(data:KunjunganModel) {
        val aPI = API()
        val clientAPI = aPI.getClientAPI()
        val bookAPI = clientAPI.create(kehadiranAPI::class.java)
        val response = bookAPI.addKunjungan(data)
        response.enqueue(object:retrofit2.Callback<KunjunganModel>{
            override fun onResponse(
                call: Call<KunjunganModel>,
                response: Response<KunjunganModel>
            ) {
                try {
                    if(response.body()!=null){
                        response.body()?.let {
                            Toast.makeText(this@MainMenu,it.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e:Error){
                    //Toast.makeText(this@MainMenu,e.toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<KunjunganModel>, t: Throwable) {
                //Toast.makeText(this@MainMenu,t.toString(),Toast.LENGTH_SHORT).show()
            }

        })


    }

    fun reqDataPeminjaman() {
        val aPI = API()
        val clientAPI = aPI.getClientAPI()
        val bookAPI = clientAPI.create(peminjamanAPI::class.java)
        val response = bookAPI.getPeminjaman(targetPeminjaman.value)
        response.enqueue(object:retrofit2.Callback<PeminjamanModel>{
            override fun onResponse(
                call: Call<PeminjamanModel>,
                response: Response<PeminjamanModel>
            ) {
                try {
                    if(response.body()!=null){
                        response.body()?.let {
                            targetPeminjaman.value = it
                            showConfirmation.value = true
                            Toast.makeText(this@MainMenu,it.toString(),Toast.LENGTH_SHORT).show()

                        }
                    }
                }catch (e:Error){
                    Toast.makeText(this@MainMenu,e.toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PeminjamanModel>, t: Throwable) {
                Toast.makeText(this@MainMenu,t.toString(),Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun processDataPeminjaman() {
        val aPI = API()
        val clientAPI = aPI.getClientAPI()
        val bookAPI = clientAPI.create(peminjamanAPI::class.java)
        val response = bookAPI.prosesPeminjaman(targetPeminjaman.value)
        response.enqueue(object:retrofit2.Callback<PeminjamanModel>{
            override fun onResponse(
                call: Call<PeminjamanModel>,
                response: Response<PeminjamanModel>
            ) {
                try {
                    if(response.body()!=null){
                        response.body()?.let {
                            Toast.makeText(this@MainMenu,it.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e:Error){
                    //Toast.makeText(this@MainMenu,e.toString(),Toast.LENGTH_SHORT).show()
                }

                showConfirmation.value = false
            }

            override fun onFailure(call: Call<PeminjamanModel>, t: Throwable) {
                //Toast.makeText(this@MainMenu,t.toString(),Toast.LENGTH_SHORT).show()
                showConfirmation.value = false
            }

        })

    }
}






