package com.xinzy.compose.wan.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.IntDef
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.xinzy.compose.wan.ui.theme.ComposeWanTheme

class UserActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = intent.getIntExtra("type", UserUiType.Login.type)

        val uiType = UserUiType.from(type)
        setContent {
            ComposeWanTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    UserScreen(
                        type = uiType,
                        activity = this
                    )
                }
            }
        }
    }


    companion object {

        fun start(context: Context, type: UserUiType) {
            val intent = Intent(context, UserActivity::class.java).also {
                it.putExtra("type", type.type)
            }
            context.startActivity(intent)
        }
    }

}