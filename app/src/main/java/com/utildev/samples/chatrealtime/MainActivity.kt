package com.utildev.samples.chatrealtime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {

    private lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            socket = IO.socket("http://192.168.100.23:3000")
        } catch (e: URISyntaxException) {
        }

        socket.connect()

        socket.on("server_register_result", onServerRegisterResult())

        actMain_btRegister.setOnClickListener {
            socket.emit("client_register", actMain_etName.text.toString())
            actMain_etName.setText("")
        }

    }

    private fun onServerRegisterResult(): Emitter.Listener = object : Emitter.Listener {
        override fun call(vararg args: Any?) {
            runOnUiThread(Runnable {
                val result = args[0] as JSONObject
                val message: String
                try {
                    message = result.getString("message")
                } catch (e: JSONException) {
                    return@Runnable
                }
                Toast.makeText(
                    this@MainActivity,
                    if (message == "true") "Register success" else "Register fail",
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
        socket.off("server_register_result", onServerRegisterResult())
    }
}
