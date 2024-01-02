package com.mcr.e_library.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mcr.e_library.PlaceHolder
import com.mcr.e_library.R
import com.mcr.e_library.model.BookModel
import com.mcr.e_library.model.PeminjamanModel
import com.mcr.e_library.model.UserBookModel
import com.mcr.e_library.myTextField
import com.mcr.e_library.ui.fragment.pairText
import com.mcr.e_library.util.API
import com.mcr.e_library.util.ColorPalette

class myCustomUI {
    private val cp = ColorPalette()
    var keyword:MutableState<String> = mutableStateOf("")
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun bookListView(dataBuku:BookModel, onClickAction: () -> Unit){
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Column(modifier = Modifier.weight(.35f)) {
                GlideImage(
                    model = API().getUrl() + "img/buku/${dataBuku.id_buku}.png",
                    contentDescription = dataBuku.judul!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    it.centerCrop()
                }
            }
            Column(modifier = Modifier
                .weight(.65f)
                .height(180.dp)
                .padding(start = 10.dp)){
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (judul,detail,button) = createRefs()
                    Text(text = dataBuku.judul, lineHeight = 20.sp, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.constrainAs(judul){
                            start.linkTo(parent.start)
                        }
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(detail) {
                                start.linkTo(parent.start);top.linkTo(judul.bottom)
                            }
                    ){
                        Text(modifier = Modifier.padding(bottom=10.dp),text = dataBuku.kategori, lineHeight = 14.sp, fontSize = 15.sp)
                        pairText(param1 = "Pengarang", param2 = dataBuku.pengarang, .4f)
                        pairText(param1 = "Penerbit", param2 = dataBuku.penerbit,.4f)
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(ColorPalette().LightBlue),
                        shape = RoundedCornerShape(50,50,50,50),
                        onClick = onClickAction,
                        modifier = Modifier.constrainAs(button){
                            bottom.linkTo(parent.bottom); end.linkTo(parent.end)
                        }
                    ) {
                        Text(text = "Pinjam", color = Color.White)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun UserbookListView(
        icons: Int,
        enabled:Boolean=true,
        btnColor: Color=ColorPalette().LightBlue,
        text:String,
        dataBuku:UserBookModel.BookModels,
        iconClick: () -> Unit,
        onClickAction: () -> Unit){
        Row(modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Column(modifier = Modifier.weight(.35f)) {
                GlideImage(
                    model = API().getUrl() + "img/buku/${dataBuku.id_buku}.png",
                    contentDescription = dataBuku.judul!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    it.centerCrop()
                }
            }
            Column(modifier = Modifier
                .weight(.65f)
                .height(180.dp)
                .padding(start = 10.dp)){
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (judul,detail,button) = createRefs()
                    Text(text = dataBuku.judul, lineHeight = 20.sp, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.constrainAs(judul){
                            start.linkTo(parent.start)
                        }
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(detail) {
                                start.linkTo(parent.start);top.linkTo(judul.bottom)
                            }
                    ){
                        Text(modifier = Modifier.padding(bottom=10.dp),text = dataBuku.kategori, lineHeight = 14.sp, fontSize = 15.sp)
                        pairText(param1 = "Pengarang", param2 = dataBuku.pengarang, .4f)
                        pairText(param1 = "Penerbit", param2 = dataBuku.penerbit,.4f)
                        pairText(param1 = "Thn.Terbit", param2 = dataBuku.tahun_terbit.toString(),.4f)
                    }
                    Row(
                        modifier = Modifier.constrainAs(button){
                            bottom.linkTo(parent.bottom); end.linkTo(parent.end)
                        },horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = cp.LightBlue,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .clickable(onClick = iconClick),
                            painter = painterResource(id = icons ) , contentDescription = null
                            )
                        Button(
                            enabled = enabled ,
                            colors = ButtonDefaults.buttonColors(btnColor),
                            shape = RoundedCornerShape(50,50,50,50),
                            onClick = onClickAction,
                        ) {
                            Text(text = text, color = Color.White)
                        }
                    }

                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MysearchBar(modifiers:Modifier,actions: () -> Unit){
        val focusManager: FocusManager = LocalFocusManager.current
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = {-50}),
            exit = slideOutVertically(targetOffsetY = {-50})
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (bar, btn) = createRefs()

                myTextField(
                    fontColor = Color.White,
                    value = keyword.value,
                    onValueChange = {
                        keyword.value = it
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
                            actions
                        }
                    )
                )

                IconButton(
                    modifier = Modifier.constrainAs(btn) {
                        end.linkTo(parent.end,5.dp);top.linkTo(parent.top,5.dp);bottom.linkTo(parent.bottom)
                    },
                    onClick = {
                        keyword.value= ""
                        focusManager.clearFocus(true)
                    }
                ) {
                    Image(imageVector = Icons.Default.Clear, contentDescription = null)
                }

            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun inputField(label:String="Input",input:MutableState<String>,modifiers: Modifier,onClickAction: () -> Unit){
        Box(modifier = modifiers
            .clip(RoundedCornerShape(15))
            .background(Color.Transparent)
            .padding(25.dp) ){
            Column( modifier = Modifier
                .padding(25.dp)
                .clip(RoundedCornerShape(10))
                .background(Color.White),horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = label,modifier= Modifier
                    .padding(top = 25.dp)
                    .fillMaxWidth(),textAlign = TextAlign.Center, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
                myTextField(modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 35.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(15)),
                    onValueChange = {
                        input.value = it
                    },
                    maxLines = 2,
                    value = input.value,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = cp.Invisible,
                        focusedIndicatorColor = cp.LightBlue,
                        unfocusedIndicatorColor = cp.Gray,
                        disabledIndicatorColor = cp.Invisible,
                        containerColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(painter = painterResource(id = R.drawable.baseline_edit_24), contentDescription = null)
                    }
                )
                Button(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .clip(RoundedCornerShape(25)),
                    colors = ButtonDefaults.buttonColors(cp.LightBlue),
                    onClick = onClickAction) {
                    Text(text = "Oke")
                }
            }
        }
    }

    @Composable
    fun confirmDialog(peminjamanModel: MutableState<PeminjamanModel>,modifiers: Modifier, onClickAction: () -> Unit){
        Box(modifier = modifiers
            .clip(RoundedCornerShape(15))
            .background(Color.Transparent)
            .padding(25.dp) ){
            Column( modifier = Modifier
                .padding(25.dp)
                .clip(RoundedCornerShape(10))
                .background(Color.White),horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Apakah anda yakin ingin memproses data ini ?",modifier= Modifier
                    .padding(top = 25.dp)
                    .fillMaxWidth(),textAlign = TextAlign.Center, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
                pairText(param1 = "ID Peminjaman ", param2 = peminjamanModel.value.id_peminjaman,.4f)
                pairText(param1 = "ID Peminjaman ", param2 = peminjamanModel.value.id_anggota,.4f)
                pairText(param1 = "ID Peminjaman ", param2 = peminjamanModel.value.id_buku,.4f)
                pairText(param1 = "ID Peminjaman ", param2 = peminjamanModel.value.status,.4f)
                Button(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .clip(RoundedCornerShape(25)),
                    colors = ButtonDefaults.buttonColors(cp.LightBlue),
                    onClick = onClickAction) {
                    Text(text = if (peminjamanModel.value.status=="pending") "Terima" else "Kembalikan")
                }
            }
        }
    }
}