package com.ljb.bens.util

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.ljb.downloadx.Const.context
import java.io.File


class ApkUtil {

    companion object {
        fun install(apk: File) {
            try { //这里有文件流的读写，需要处理一下异常
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //如果SDK版本 =24，即：Build.VERSION.SDK_INT  = 24
                    val packageName = context.applicationContext.packageName
                    val authority = StringBuilder(packageName).append(".provider").toString()
                    var uri = FileProvider.getUriForFile(context, authority, apk)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                } else {
                    var uri = Uri.fromFile(apk)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getApkSize(storagePath: String?): Long {
            if (storagePath.isNullOrEmpty()) return 0
            var apkFile = File(storagePath)
            if (apkFile.exists() && apkFile.isFile) {
                return apkFile.length()
            }
            return 0
        }


    }
}