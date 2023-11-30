package com.simple.recorder

import android.os.Bundle
import android.content.pm.PackageManager
import android.Manifest
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.simple.recorder.ui.theme.TheSimpleRecorderTheme

class MainActivity : ComponentActivity() {

    //用于实时表示是否在录制
    private val recording = mutableStateOf(false)

    //创建Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //界面创建
        setContent {
            TheSimpleRecorderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(name = "nhcsbc", isChecked = recording, buttonFunction = :: myButtonFunction)
                    //另一种写法：
                    //Greeting("Android"){myButtonFunction(it)}
                }
            }
        }
        //权限检查与申请
        //checkAndRequestPermission()
    }



    private fun myButtonFunction(checked:Boolean) {
        if(checked){
            //checkAndRequestPermission()这句话在MutableState的成员函数中使用存在问题
            Toast.makeText(this, "开始录制", Toast.LENGTH_LONG).show()
            /*if(!checkAndRequestPermission()){
                recording.value = false
            }*/
        } else {
            Toast.makeText(this, "结束录制", Toast.LENGTH_LONG).show()
        }
    }

    //权限检查与申请
    private fun checkAndRequestPermission(): Boolean {

        //定义直接请求权限的请求
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) { //用户允许
                    Toast.makeText(this, "可以录音了捏", Toast.LENGTH_LONG).show()
                } else { //用户拒绝
                    Toast.makeText(this, "奶奶滴，为什么不让我录音！！", Toast.LENGTH_LONG).show()
                }
            }

        return when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 已获得权限
                true
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.RECORD_AUDIO) -> {
                // 已被用户始终拒绝权限
                Toast.makeText(this, "This app requires audio recording permission.", Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                // 直接请求权限
                requestPermissionLauncher.launch(
                    Manifest.permission.RECORD_AUDIO)
                false
            }
        }
    }



}

@Composable
fun Greeting(name: String, isChecked : MutableState<Boolean>, modifier: Modifier = Modifier, buttonFunction: (Boolean) -> Unit) {
    Column {
        Text(
            text = name,
            modifier = modifier
        )

        //var isChecked by remember { mutableStateOf(false) }
        FilledIconToggleButton(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it ; buttonFunction(it) }
        ) {
            if (isChecked.value) {
                Icon(Icons.Filled.Lock, contentDescription = "Localized description")
            } else {
                Icon(Icons.Outlined.Lock, contentDescription = "Localized description")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheSimpleRecorderTheme {
        //Greeting("Android")
    }
}