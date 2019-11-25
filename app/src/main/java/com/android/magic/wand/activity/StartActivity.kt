package com.android.magic.wand.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.magic.wand.Constants.Config
import com.android.magic.wand.R
import com.android.magic.wand.utils.PermissionUtil
import com.android.magic.wand.utils.UpdateLog
import com.android.magic.wand.view.BlurringView2
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class StartActivity : AppCompatActivity() {

    private val TAG = "UpdateController"

    private var mAppUpdateManager: AppUpdateManager? = null
    private val REQUEST_CODE_UPDATE = 9102
    private var mBlurringView: BlurringView2? = null
    private var mImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        mImageView = findViewById(R.id.iv_blur)
        mBlurringView = findViewById(R.id.blurring_view)

        if (mBlurringView != null) {
            mBlurringView!!.setBlurredView(mImageView)
            mBlurringView!!.invalidate()
        }

        if (!PermissionUtil.requirePermissions(this)) {
            init()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtil.PERMISSIONS_CODE_APK_INFO) {
            if (PermissionUtil.checkPermissions(this)) {
                Toast.makeText(
                    this, "not permission, then finish!",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            } else {
                init()
            }
        }
    }

    private fun init() {
        try {
            val type: Int = AppUpdateType.FLEXIBLE

            //AppUpdateType.IMMEDIATE

            mAppUpdateManager = AppUpdateManagerFactory.create(this)
            val appUpdateInfo = mAppUpdateManager!!.appUpdateInfo
            appUpdateInfo.addOnCompleteListener { task ->
                UpdateLog.d(TAG, "toUpdateInApp onComplete")
                if (task.isSuccessful) {
                    // 监听成功，不一定检测到更新
                    val it = task.result
                    val available = it.updateAvailability()
                    UpdateLog.d(TAG, "toUpdateInApp onComplete:$available")
                    if (available == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(type)) { // 检测到更新可用且支持即时更新
                        try {

                            UpdateLog.d(TAG, "toUpdateInApp check success")
                            mAppUpdateManager!!.startUpdateFlowForResult(it, type, this, REQUEST_CODE_UPDATE)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        UpdateLog.d(TAG, "toUpdateInApp check error 1")

                    }
                } else {
                    UpdateLog.d(TAG, "toUpdateInApp check error 2 " + task.exception)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!PermissionUtil.requirePermissions(this)) {
            val appUpdateInfo = mAppUpdateManager!!.appUpdateInfo
            appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                UpdateLog.w(TAG, "onResume onSuccess")
                try {
                    val status = appUpdateInfo.installStatus()
                    UpdateLog.d(TAG, "onResume onSuccess:$status")
                    if (status == InstallStatus.DOWNLOADED) {
                        // 只有后台下载才会有这个通知
                        mAppUpdateManager!!.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            REQUEST_CODE_UPDATE
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UpdateLog.w(TAG, "onActivityResult:$requestCode,$resultCode")
        if (requestCode == REQUEST_CODE_UPDATE) run {
            when (resultCode) {
                Activity.RESULT_OK -> UpdateLog.w(TAG, "update in app success")
                Activity.RESULT_CANCELED -> UpdateLog.w(TAG, "update in app, user cancel")
                else -> UpdateLog.w(TAG, "update in app, error")
            }
        }
    }

    fun onViewClick(view: View?) {
        when (view!!.id) {
            R.id.bubble -> startAc(Config.BUBBLE_POP)
            R.id.circle -> startAc(Config.CIRCLE)
            R.id.clean -> startAc(Config.CLEAN)
            R.id.scroll -> startAc(ScrollingActivity::class.java)
            R.id.round -> startAc(RoundActivity::class.java)
        }
    }

    private fun startAc(function: String) {
        try {
            val intent = Intent(this, FuncActivity::class.java)
            intent.putExtra("function", function)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun startAc(cls: Class<*>) {
        try {
            val intent = Intent(this, cls)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

}
