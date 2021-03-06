package com.orange.pokemon

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orange.pokemon.adapter.PokeAdapter
import com.orange.pokemon.data.PokeDao
import com.orange.pokemon.data.PokeDatabase
import com.orange.pokemon.data.PokeEntity
import com.orange.pokemon.databinding.ActivityMainBinding
import com.orange.pokemon.model.Pokemon
import com.orange.pokemon.networking.ApiService
import com.orange.pokemon.networking.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainActivity"
private lateinit var pokAdapter: PokeAdapter
private lateinit var database: PokeDatabase
private lateinit var dao: PokeDao
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokAdapter = PokeAdapter()
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = pokAdapter
        }

        val service = NetworkClient().getRetrofit().create(ApiService::class.java)
        service.getAllPokemons().enqueue(object : Callback<List<Pokemon>> {
            override fun onResponse(call: Call<List<Pokemon>>, response: Response<List<Pokemon>>) {
                if (response.isSuccessful) {
                    val listPok: List<Pokemon>? = response.body()

                    val database = PokeDatabase.getInstance(this@MainActivity)
                    val dao = database.getPokemonDao()

                    GlobalScope.launch(Dispatchers.Main) {
                        dao.insertAll(listPok!!.map {
                            PokeEntity(
                                name = it.name,
                                attack = it.attack,
                                image = it.imageurl
                            )
                        })

                        var list = dao.getAll().map {
                            Pokemon(
                                name = it.name,
                                attack = it.attack,
                                imageurl = it.image
                            )
                        }

                        pokAdapter.submitList(list)
                    }


                }
            }

            override fun onFailure(call: Call<List<Pokemon>>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
            }

        })


        isOnline(this@MainActivity)

    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                Toast.makeText(this@MainActivity, "connection etabliee", Toast.LENGTH_LONG).show()


            } else {
                Toast.makeText(this@MainActivity, "no connection", Toast.LENGTH_LONG).show()

                val database = PokeDatabase.getInstance(this@MainActivity)
                val dao = database.getPokemonDao()
                GlobalScope.launch(Dispatchers.Main) {
                    var list = dao.getAll().map {
                        Pokemon(
                            name = it.name,
                            attack = it.attack,
                            imageurl = it.image
                        )
                    }
                    pokAdapter.submitList(list)
                }

            }
        }
        return false
    }
}