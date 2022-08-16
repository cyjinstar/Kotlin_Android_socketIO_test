package com.testing.ktio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.*
import java.net.*

class MainActivity : AppCompatActivity()  {

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var button : Button
    lateinit var editText : EditText
    lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        textView = findViewById(R.id.textView)

        button.setOnClickListener {
            ClientThread().start()
        }
    }

    inner class ClientThread : Thread() {
        override fun run() {
            val hostName = "192.168.48.59"
            val port = 7777

            try {
                val socket = Socket(hostName, port)

                val outputStream = ObjectOutputStream(socket.getOutputStream())
                outputStream.writeObject(editText.text.toString())
                outputStream.flush()

                val inputStream = ObjectInputStream(socket.getInputStream())
                val input = inputStream.readObject() as String

                handler.post {
                    textView.append("$input\n")
                }
            } catch (e: SocketException) {
                handler.post {
                    Toast.makeText(applicationContext, "소켓 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}