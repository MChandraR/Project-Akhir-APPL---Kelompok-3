package com.mcr.e_library

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcr.e_library.apiInterface.loginAPI
import com.mcr.e_library.model.LoginModel
import com.mcr.e_library.model.UserModel
import com.mcr.e_library.ui.theme.ELibraryTheme
import com.mcr.e_library.util.API
import com.mcr.e_library.util.ColorPalette
import retrofit2.Call
import retrofit2.Response

class LoginForm : ComponentActivity() {
    private val context: Context = this
    private lateinit var spEditor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ELibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginView()
                }
            }
        }
        spEditor = getSharedPreferences("mcrs_pref",Context.MODE_PRIVATE).edit()

    }

    fun saveUserData(userData:UserModel.UserData){
        spEditor.putString("user_id",userData.user_id)
        spEditor.putString("username",userData.username)
        spEditor.putString("password",userData.password)
        spEditor.putString("nama",userData.nama)
        spEditor.putString("level",userData.level)
        spEditor.putString("id_anggota",userData.id_anggota)
        spEditor.apply()
    }

    private fun login(context: Context, username:String, password:String) {
            val aPI = API()
            val clientAPI = aPI.getClientAPI()
            val loginAPI = clientAPI.create(loginAPI::class.java)
            val response = loginAPI.login(LoginModel(username,password))
            response.enqueue(object:retrofit2.Callback<UserModel>{
                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {
                    try {
                        if(response.body()!=null){
                            response.body()?.let {
                                if (it.status=="sukses") {
                                    Toast.makeText(
                                        context,
                                        "Login berhasil\n" + it.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    saveUserData(it.data)
                                    context.startActivity(
                                        Intent(
                                            this@LoginForm,
                                            MainMenu::class.java
                                        )
                                    )
                                }else
                                    Toast.makeText(context,"Login gagal\n" + it.message,Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch (e:Error){
                        Toast.makeText(context,e.toString(), Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Toast.makeText(context,t.toString(), Toast.LENGTH_SHORT).show()
                }

            })

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginView(){
        val cp  = ColorPalette()
        var usernameInput by remember {
            mutableStateOf("")
        }
        var passwordInput by remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier.padding(bottom = 50.dp, end = 50.dp, start = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                text = "Login", color = Color.Black ,
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily =  FontFamily.SansSerif
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Username", color = Color.Black ,
                textAlign = TextAlign.Start,
                fontSize = 14.sp
            )
            myTextField(
                value = usernameInput ,
                onValueChange = {usernameInput = it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = cp.Invisible,
                    focusedIndicatorColor = cp.LightBlue,
                    unfocusedIndicatorColor = cp.Gray,
                    disabledIndicatorColor = cp.Invisible,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(25, 25, 25, 25),
                modifier  = Modifier
                    .padding(bottom = 25.dp)
                    .background(Color.White),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.baseline_person_outline_24), contentDescription =  "Username")
                },
                placeholder = {
                    PlaceHolder("Masukkan username")
                }
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Password", color = Color.Black ,
                textAlign = TextAlign.Start,
                fontSize = 14.sp
            )
            myTextField(
                value = passwordInput ,
                onValueChange = {passwordInput = it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = cp.Invisible,
                    focusedIndicatorColor = cp.LightBlue,
                    unfocusedIndicatorColor = cp.Gray,
                    disabledIndicatorColor = cp.Invisible,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(25, 25, 25, 25),
                modifier  = Modifier
                    .padding(bottom = 10.dp)
                    .background(Color.White),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.baseline_lock_open_24), contentDescription = "Password")
                },
                placeholder = {
                    PlaceHolder("Masukkan password")
                }
            )
            Text(modifier = Modifier
                .clickable { context.startActivity(Intent(this@LoginForm, MainMenu::class.java)) }
                .fillMaxWidth()
                .padding(bottom = 30.dp, top = 5.dp),text = "Lupa password ? ",  fontSize = 14.sp, textAlign = TextAlign.End, color = cp.Gray)
            Button(
                modifier = Modifier.wrapContentWidth(),
                colors = ButtonDefaults.buttonColors(cp.Invisible),
                contentPadding = PaddingValues(0.dp),
                onClick = { login(context,usernameInput,passwordInput)}
            ) {
                Box(
                    modifier = Modifier
                        .background(cp.largeRadialGradient)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 50.dp, vertical = 15.dp),
                        text = "Login",
                        color = Color.White
                    )
                }
            }
            //Text(text = "Belum punya akun ? \nSIGN UP", modifier = Modifier.fillMaxWidth().padding(vertical = 50.dp), textAlign = TextAlign.Center, fontSize = 14.sp)
        }
    }

}


@Composable
fun PlaceHolder(text:String="", color:Color = ColorPalette().Gray){
    Text(text = text, color = color, fontSize = 14.sp)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myTextField(
    maxLines:Int=1,
    fontColor:Color=Color.Black,
    leadingIcon:  @Composable (() -> Unit)? = null,
    trailingIcon:  @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    value:String = "",
    modifier:Modifier,
    onValueChange: (String) -> Unit,
    colors:androidx.compose.material3.TextFieldColors = TextFieldDefaults.textFieldColors(Color.White),
    shape: Shape = TextFieldDefaults.filledShape,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keywordOptions: KeyboardOptions = KeyboardOptions.Default
    ){
    BasicTextField(
        value = value,
        modifier = modifier
            .defaultMinSize(
                minHeight = 14.dp
            )
            .fillMaxWidth()
            .padding(all = 0.dp),
        onValueChange = onValueChange,
        enabled = true,
        readOnly = false,
        cursorBrush = SolidColor(ColorPalette().LightBlue),
        visualTransformation = VisualTransformation.None,
        keyboardOptions = keywordOptions,
        keyboardActions = keyboardActions,
        interactionSource = remember { MutableInteractionSource() },
        singleLine = true,
        maxLines = maxLines,
        textStyle = androidx.compose.ui.text.TextStyle(color=fontColor),
        decorationBox = {
            TextFieldDefaults.TextFieldDecorationBox(
                contentPadding = PaddingValues(0.dp) ,
                value = value,
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                supportingText = supportingText,
                shape = shape,
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                colors = colors
            )
        }

    )
}



