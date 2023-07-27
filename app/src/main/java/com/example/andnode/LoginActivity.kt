package com.example.andnode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.text.SimpleDateFormat

class LoginActivity : AppCompatActivity() {

    lateinit var etId: EditText
    lateinit var etPw: EditText
    lateinit var btnLogin: Button

    lateinit var reqQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etId = findViewById(R.id.etId)//
        etPw = findViewById(R.id.etPw)
        btnLogin = findViewById(R.id.btnLogin)

        reqQueue = Volley.newRequestQueue(this@LoginActivity)

        btnLogin.setOnClickListener{
            val inputId = etId.text.toString()
            val inputPw = etPw.text.toString()

            //object: 무명객체 생성 -> 인터페이스를 구현하고자 하는 일회성의 구현 객체 생성시 사용
            val request = object: StringRequest(
                Request.Method.POST,  //요청메서드
                "http://192.168.255.65:8888/login",  //요청 경로 Manifest - android:usesCleartextTraffic="true" 추가!
                // localhost안댐...
                { response ->  //요청, 응답 성공 시 호출될 리스너 객체
                    Log.d("response", response.toString())
                    //resultText.text = response /
                },
                {
                        error -> //요청, 응답 실패 시 오류 발생시 호출될 리스너 객체
                    Log.d("Error", error.toString())
                    Toast.makeText(this, "error발생", Toast.LENGTH_SHORT).show()
                }
            ){ //요청 파라미터를 담기위한 getParams 메서드 override! (POST 방식으로 요청할 경우 사용!)
                override fun getParams(): MutableMap<String, String> {
                    //MutableMap : 가변형
                    val params:MutableMap<String, String> = HashMap<String, String>()

                    val am:AndMember = AndMember(inputId, inputPw, null, null)
                    params.put("AndMember", Gson().toJson(am))
                    return params
                }
            }
            // 여러번 요청할 경우 캐시가 누적됨, 이전 결과가 있더라도 새로 요청하여 응답 보여주고 싶은 경우
            request.setShouldCache(false)

            //request 정보를 request queue에 추가해줘야만 쓰레드를 생성해 서버로 요청 및 응답 받을 수 있음
            reqQueue.add(request)
        }
    }
}