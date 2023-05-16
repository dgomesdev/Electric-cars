package com.electricCars.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.electricCars.R
import com.electricCars.data.CarsApi
import com.electricCars.data.local.CarRepository
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_BATTERY
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_NAME
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_POWER
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_PRICE
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_RECHARGE
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_URL_PHOTO
import com.electricCars.data.local.CarsContract.CarEntry.TABLE_NAME
import com.electricCars.data.local.CarsDbHelper
import com.electricCars.domain.Car
import com.electricCars.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONTokener
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {

    private lateinit var btnCalculation: FloatingActionButton
    private lateinit var carsList: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var noInternetImage: ImageView
    private lateinit var noInternetText: TextView
    private lateinit var carsApi: CarsApi

    var carsArray : ArrayList<Car> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupListeners()
        setupRetrofit()
    }

    override fun onResume() {
        super.onResume()
        if (checkInternetConnection(context)) {
            //callService()
            getAllCars()
        }
        else emptyState()
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dgomesdev.github.io/Electric-cars/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = retrofit.create(CarsApi::class.java)
    }

    private fun getAllCars() {
        carsApi.getAllCArs().enqueue(object : Callback<List<Car>> {
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful) {
                    progress.isVisible = false
                    noInternetImage.isVisible = false
                    noInternetText.isVisible = false
                    response.body()?.let { setupList(it) }
                } else {
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun emptyState() {
        progress.isVisible = false
        carsList.isVisible = false
        noInternetImage.isVisible = true
        noInternetText.isVisible = true
    }

    private fun setupViews(view: View) {
        view.apply {
            btnCalculation = findViewById(R.id.fab_calculation)
            carsList = findViewById(R.id.rv_carsList)
            progress = findViewById(R.id.pb_loader)
            noInternetImage = findViewById(R.id.iv_empty_state)
            noInternetText = findViewById(R.id.tv_no_internet)
        }
    }

    private fun setupList(list: List<Car>) {
        val carAdapter = CarAdapter(list)
        carsList.apply {
            isVisible = true
            adapter = carAdapter
        }
        carAdapter.carItemListener = { car ->
            var isSaved = CarRepository(requireContext()).save(car)
        }
    }

    private fun setupListeners() {
        btnCalculation.setOnClickListener {
        startActivity(Intent(context, ActivityCalculation::class.java))
        }
    }

    private fun callService() {
        val urlBase = "https://dgomesdev.github.io/api-simulations/cars.json"
        MyTask().execute(urlBase)
        progress.visibility = View.VISIBLE
    }

    private fun checkInternetConnection(context: Context?) : Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    //Retrofit used as abstraction of the AsyncTask
    inner class MyTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])
                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000
                urlConnection.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)
                }

            } catch (_: Exception) {

            } finally {
                    urlConnection?.disconnect()
            }

            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()){

                    val id = jsonArray.getJSONObject(i).getString("id")
                    val name = jsonArray.getJSONObject(i).getString("name")
                    val price = jsonArray.getJSONObject(i).getString("price")
                    val power = jsonArray.getJSONObject(i).getString("power")
                    val battery = jsonArray.getJSONObject(i).getString("battery")
                    val recharge = jsonArray.getJSONObject(i).getString("recharge")
                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")

                    val model = Car(
                        id = id.toInt(),
                        name = name,
                        price = price,
                        battery = battery,
                        power = power,
                        recharge = recharge,
                        urlPhoto = urlPhoto,
                        isFavorite = false
                    )
                    carsArray.add(model)
                }
                progress.visibility = View.GONE
                noInternetImage.visibility = View.GONE
                noInternetText.visibility = View.GONE
                //setupList()
            } catch (_: Exception) {

            }
        }

    }

}
