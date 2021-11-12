package com.will_d.yogadesignkt.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.will_d.yogadesignkt.Global
import com.will_d.yogadesignkt.R
import com.will_d.yogadesignkt.RetrofitHellper
import com.will_d.yogadesignkt.RetrofitService
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UserSettingActivity : AppCompatActivity() {

    val PERMISSION_EX_PHOTO :Int = 1018
    var isPermission :Boolean = false
    lateinit var activityResult : ActivityResultLauncher<Intent>
    lateinit var realPath : String

    lateinit var imgUri : Uri

    var isPhotoChange : Boolean = false

    val civ:CircleImageView by lazy { findViewById(R.id.civ) }
    val etNickName : EditText by lazy { findViewById(R.id.et_nickname) }


    //blur
    val pg : ProgressBar by lazy { findViewById(R.id.pg) }
    val blur: RelativeLayout by lazy { findViewById(R.id.blur) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)

        val permission : Array<String> = arrayOf<String>(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkSelfPermission(permission[0]) == PackageManager.PERMISSION_DENIED){
            requestPermissions(permission, PERMISSION_EX_PHOTO)
        }

        activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val intent : Intent? = it.data
                imgUri = (intent?.data) as Uri
                realPath = absolutelyPath(imgUri)
                Log.i("realPath", realPath)
                Glide.with(this@UserSettingActivity).load(imgUri).into(civ)

                isPhotoChange = true
            }else{
                isPhotoChange = false
            }
        }

    }

    fun clickUpdateImg(view: View) {
        if (isPermission){
            val intent : Intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }else{
            Toast.makeText(this, "사진 허용을 선택해주세요", Toast.LENGTH_SHORT).show()
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==PERMISSION_EX_PHOTO && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "외부저장소 접근 허용", Toast.LENGTH_SHORT).show()
            isPermission = true

        }else{
            Toast.makeText(this, "이미지 업로드 불가", Toast.LENGTH_SHORT).show()
            isPermission = false
        }

    }



    fun clickCancel(view: View) {
        val pref :SharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()
        editor.putBoolean("isLogin", false)
        editor.putBoolean("isFirstPhotoChecked", false)
        editor.putString("nickName", "")
        editor.putString("profileUrl", "")
        editor.putString("id", "")
        editor.commit()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun clickStart(view: View) {
        if (isPhotoChange){
            firebaseSaveData()
        }else{
            Toast.makeText(this, "사진을 선택해주세요", Toast.LENGTH_SHORT).show()
        }

    }

    val firebaseSaveData:()->Unit = {

        pg.visibility = View.VISIBLE
        blur.visibility = View.VISIBLE

        Global.nickName = etNickName.text.toString()  //***************************************************************

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyyMMddhhmmss")
        val fileName : String = "IMG_${sdf.format(Date())}.png"

        val firebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()
        val imgRef : StorageReference = firebaseStorage.getReference("profileImage/${fileName}")

        val uploadTask = imgRef.putFile(imgUri)
        uploadTask.addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener {

                Global.profileUrl = it.toString() //***************************************************************

                Log.i("profileImg", Global.profileUrl)


                val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()
                val profileRef : DatabaseReference = firebaseDatabase.getReference("profiles")
                profileRef.child(Global.nickName).setValue(Global.profileUrl)

                val pref : SharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)
                val editor : SharedPreferences.Editor = pref.edit()
                editor.putString("nickName", Global.nickName)
                editor.putString("profileUrl", Global.profileUrl)
                editor.commit()

                memberDataSendDB()
            }
        }


    }


    val memberDataSendDB : ()->Unit ={
        val retrofit : Retrofit = RetrofitHellper.getRetrofritScalars()
        val retrofitService : RetrofitService = retrofit.create(RetrofitService::class.java)

        val pref : SharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)
        val id :String? = pref.getString("id","")


        var filePart : MultipartBody.Part? = null
        if (realPath !=null){
            val file : File = File(realPath)
            val requestBody : RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            filePart = MultipartBody.Part.createFormData("img", file.name, requestBody)
        }

        val dataPart = mutableMapOf<String, String>()
        dataPart.put("crud", "UPDATE")
        dataPart.put("id", id as String)
        dataPart.put("nickName", etNickName.text.toString())

        val call : Call<String> = retrofitService.memberDataSendDB(dataPart, filePart)
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {

                pg.visibility = View.INVISIBLE
                blur.visibility = View.INVISIBLE

                Log.i("memberDataSendDB", response.body() as String)
                val pref:SharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)
                val editor : SharedPreferences.Editor = pref.edit()
                editor.putBoolean("isFirstPhotoChecked", true)
                editor.commit()

                startActivity(Intent(this@UserSettingActivity, MainActivity::class.java))
                this@UserSettingActivity.finish()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("memberDataSendDB", t.message as String)
            }

        })

    }


    // 절대경로 변환
    fun absolutelyPath(path: Uri): String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        var result = c.getString(index)

        return result
    }




}