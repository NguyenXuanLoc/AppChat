package com.example.fcm.common.ext

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.appchat.common.Constant
import ctx


fun AppCompatActivity.addFragment(containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().add(containerId, fragment, fragment.TAG).commit()
}

fun AppCompatActivity.replaceFragment(containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(containerId, fragment, fragment.TAG).commit()
}

fun AppCompatActivity.toast(notify: String?) {
    Toast.makeText(this, notify.toString(), Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.openActivity(
    clz: Class<*>, bundle: Bundle? = null, keyBundle: String? = null, clearStack: Boolean = false,
    enterAnim: Int? = null, exitAnim: Int? = null, flags: IntArray? = null
) {
    val intent = Intent(ctx, clz)
    if (flags?.isNotEmpty() == true) {
        for (flag in flags) {
            intent.addFlags(flag)
        }
    }
    if (clearStack) {
        setResult(Activity.RESULT_CANCELED)
        finishAffinity()
    }
    if (bundle != null) {
        intent.putExtra(keyBundle, bundle)
//        intent.putExtras(bundle)
    }
    startActivity(intent)
    enterAnim?.also { enter ->
        exitAnim?.also { exit ->
            overridePendingTransition(enter, exit)
        }
    }
}

fun AppCompatActivity.openActivityForResult(
    clz: Class<*>,
    requestCode: Int,
    bundle: Bundle? = null, keyBundle: String? = null,
    enterAnim: Int? = null,
    exitAnim: Int? = null
) {
    val intent = Intent(ctx, clz)
    if (bundle != null) {
        intent.putExtra(keyBundle, bundle)
    }
    startActivityForResult(intent, requestCode)
    enterAnim?.also { enter ->
        exitAnim?.also { exit ->
            overridePendingTransition(enter, exit)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun AppCompatActivity.closeActivity(enterAnim: Int? = null, exitAnim: Int? = null) {
    finishAfterTransition()
    enterAnim?.also { enter ->
        exitAnim?.also { exit ->
            overridePendingTransition(enter, exit)
        }
    }
}

fun AppCompatActivity.closeActivityAndClearStack() {
    setResult(Activity.RESULT_CANCELED)
    finishAffinity()
}
