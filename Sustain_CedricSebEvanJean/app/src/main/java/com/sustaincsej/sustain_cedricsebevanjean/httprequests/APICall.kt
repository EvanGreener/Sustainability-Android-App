package com.sustaincsej.sustain_cedricsebevanjean.httprequests

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * Make API Calls
 *
 * Example:
 * val jsonString = "{\"fromlatitude\":\"45\", \"fromlongitude\":\"-73\", \"tolatitude\":\"45.001\", \"tolongitude\":\"-73.001\", \"mode\":\"car\"}"
 * val request = APICall("https://jayaghgtracker.herokuapp.com/api/v1/tripinfo", "GET", "sect2team2@test", "1qazxsw2", jsonString)
 * val response = request.execute().get()
 *
 * Note: Wether it is a post or get, the format is the same. The class will take care of everything:)
 *
 * @author Jean Robatto (with help from Jeffrey Boisvert)
 */
class APICall(private var url: String, private val method: String, private val email: String, private val password: String, private var jsonString: String, private val gettingToken: Boolean = true) :
    AsyncTask<Void, Void, JSONObject>() {

    val connection: HttpURLConnection
    private var jwtToken: String = ""

    /**
     * Set token if needed, prepare the GET url if needed, open connection
     */
    init {
        if (gettingToken) setToken()
        addToken()
        if (method=="GET") {
            prepareGetUrl()
        }
        this.connection = URL(this.url).openConnection() as HttpURLConnection
    }

    /**
     * Sets the token by making a recursive call
     */
    private fun setToken() {
        val response = APICall(
            "https://jayaghgtracker.herokuapp.com/api/auth/login",
            "POST",
            email, password,
            "{\"email\":\"$email\", \"password\":\"$password\"}",
            false
        ).execute().get()

        this.jwtToken = if (response != null) response.getString("access_token") else ""

        Log.i(TAG, jwtToken)
    }

    /**
     * Adds json data to url
     */
    private fun prepareGetUrl() {
        val json = JSONObject(this.jsonString)
        val keys = json.keys()
        var completeUrl = "${this.url}?"
        var first = true
        while (keys.hasNext()) {
            val key = keys.next()
            val connector = if (first) "" else "&"
            completeUrl = "$completeUrl$connector$key=${json[key]}"
            first = false
        }
        this.url = completeUrl
    }

    /**
     * Adds token to json string if there is one
     */
    private fun addToken() {
        if(this.jwtToken.isNotEmpty()) {
            this.jsonString = this.jsonString.substring(0..this.jsonString.length-2) + ", \"token\":\"${this.jwtToken}\"}"
        }
    }

    /**
     * Background task
     */
    override fun doInBackground(vararg p0: Void?): JSONObject? {
        return getData()
    }


    /**
     * @return Response data as json object
     */
    private fun getData(): JSONObject? {
        var jsonData: JSONObject? = null

        var out: OutputStream
        var reader: BufferedReader? = null

        try {
            this.connection.requestMethod = method
            this.connection.doInput = true
            if (method=="POST") {
                this.connection.doOutput = true
            }
            this.connection.readTimeout = 10000
            this.connection.connectTimeout = 15000

            Log.i(TAG, this.connection.requestMethod)

            this.connection.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8"
            )

            Log.i(TAG, jsonString)

            if (method=="POST") {
                val bytes: ByteArray = jsonString.toByteArray(charset("UTF-8"))

                out = BufferedOutputStream(this.connection.outputStream)
                out.write(bytes)
                out.flush()
                out.close()
            }

                val data = StringBuffer(2048)

                Log.i(TAG, this.connection.url.toString())

                if (this.connection.responseCode == 200) {
                    reader = BufferedReader(InputStreamReader(this.connection.inputStream))

                    var line = reader.readLine()

                    while (line != null) {
                        data.appendln(line)
                        line = reader.readLine()
                    }

                    jsonData = JSONObject(data.toString())

                } else {
                    Log.d(TAG, "Error in API Call: " + this.connection.responseCode + " " + this.connection.responseMessage)
                }


        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        } finally {
            reader?.close()
            this.connection.disconnect()
        }

        return jsonData
    }

    companion object {
        private const val TAG = "API Call"
    }
}