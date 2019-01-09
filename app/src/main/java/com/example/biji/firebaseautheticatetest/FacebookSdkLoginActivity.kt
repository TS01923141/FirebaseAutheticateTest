package com.example.biji.firebaseautheticatetest

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.Logger

class FacebookSdkLoginActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton

    @JvmField
    val PERMISSIONS_REQUEST_CODE = 1248

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_sdk_login)

        init()

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        Toast.makeText(this,
                "login status: $isLoggedIn", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }

//    private fun checkPremission(){
//        Log.d(TAG, "test")
//        var internetPermission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.INTERNET)
//
//        Toast.makeText(this, "internetPermission: $internetPermission",
//                Toast.LENGTH_LONG)
//
//        Log.d(TAG, "internetPermission: $internetPermission")
//
//        if (internetPermission == PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(this,
////                    String[] {Manifest.permission.INTERNET},
//                    arrayOf(Manifest.permission.INTERNET),
//                    PERMISSIONS_REQUEST_CODE)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when(requestCode){
//            PERMISSIONS_REQUEST_CODE ->
//                    if (grantResults.isNotEmpty()){
//                        var InternetPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
//                        if (InternetPermission){
//                            init()
//                        }else{
//                            checkPremission()
//                        }
//                    }
//        }
//    }

    private fun init() {
        //init facebook login
        callbackManager = CallbackManager.Factory.create()

        //初始化方式1. 初始化button
//        loginButton = findViewById<View>(R.id.login_button) as LoginButton
//        loginButton.setReadPermissions("email")
//        // If using in a fragment
////        loginButton.setFragment(this)
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult) {
//                // App code
//                Toast.makeText(this@MainActivity,
//                        "login succeed: " + loginResult.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onCancel() {
//                // App code
//            }
//
//            override fun onError(exception: FacebookException) {
//                // App code
//            }
//        })

        //初始化方式2. 初始化CallbackManager
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        Toast.makeText(this@FacebookSdkLoginActivity,
                                "login succeed: " + loginResult.accessToken, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "login succeed")


                        val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                            try {
                                //here is the data that you want
                                Logger.d("FBLOGIN_JSON_RES", `object`.toString())
                                Log.d(TAG, `object`.toString())

                                if (`object`.has("id")) {
//                                    handleSignInResultFacebook(`object`)
                                } else {
//                                    Logger.e("FBLOGIN_FAILD", `object`.toString())
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
//                                dismissDialogLogin()
                            }
                        }

                        //包入你想要得到的資料 送出request
                        val parameters = Bundle()
//                        parameters.putString("fields", "id,name,link,email")
                        parameters.putString("fields", "name,email,id,picture.type(large)")
                        request.parameters = parameters
                        request.executeAsync()

                    }

                    override fun onCancel() {
                        // App code
                        Log.d(TAG, "login cancel")
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                        Log.d(TAG, "login error:" + exception.message)
                    }
                })
    }
}
