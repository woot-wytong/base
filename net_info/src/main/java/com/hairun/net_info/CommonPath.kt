package com.hairun.net_info

import android.os.Environment
import com.blankj.utilcode.util.Utils

object CommonPath {

    @JvmStatic
    fun getDocumentPath(): String {
        val file = Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (file != null) {
            return file.path
        } else {
            return ""
        }
    }

}