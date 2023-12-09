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

        val type = intent.getIntExtra("type", TYPE_LOGIN)

        setContent {
            ComposeWanTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    UserScreen(
                        type = type,
                        activity = this
                    )
                }
            }
        }
    }


    companion object {

        const val TYPE_LOGIN = 1
        const val TYPE_REGISTER = 2

        @IntDef(value = [TYPE_LOGIN, TYPE_REGISTER])
        @Retention(AnnotationRetention.SOURCE)
        @Target(AnnotationTarget.VALUE_PARAMETER)
        annotation class UserType

        fun start(context: Context, @UserType type: Int) {
            val intent = Intent(context, UserActivity::class.java).also {
                it.putExtra("type", type)
            }
            context.startActivity(intent)
        }
    }

}