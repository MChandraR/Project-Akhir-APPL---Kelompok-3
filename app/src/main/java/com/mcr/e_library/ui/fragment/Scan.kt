package com.mcr.e_library.ui.fragment

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.QRResult.QRSuccess
import io.github.g00fy2.quickie.ScanQRCode

class Scan(private val context: Context) {

    @Composable
    fun scanMainView(){
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            GetQRCodeExample()
        }
    }

    @Composable
    fun GetQRCodeExample() {
        val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result ->
            var res:String
            var status:String
            when(result){
                is QRSuccess -> {
                    status = "success"
                    res = result.content.rawValue!!
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
                    res = "Operasi dibatalkan !"
                }
            }
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
        }

        Button(onClick = { scanQrCodeLauncher.launch(null) }) {
            Text(text = "Scan")
        }

    }
}