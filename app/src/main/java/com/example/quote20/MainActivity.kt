package com.example.quote20

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quote20.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        getQuote()

        binding.nextBtn.setOnClickListener {
            getQuote()
        }
    }

    private fun getQuote(){
        setInProgress(true)
        GlobalScope.launch {
            try {
                val response= RetrofitInstance.quoteApi.getRandomQuote()
                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let {
                        setUI(it)
                    }
                }
            }
            catch (e: Exception){
                runOnUiThread {
                    setInProgress(false)
                    Toast.makeText(applicationContext,"No Internet Connection",Toast.LENGTH_SHORT).show()

                }

            }

        }

    }

    private fun setInProgress(inProgress: Boolean){
        if(inProgress){
            binding.progressBar.visibility= View.VISIBLE
            binding.nextBtn.visibility= View.GONE
        }
        else{
            binding.progressBar.visibility=View.GONE
            binding.nextBtn.visibility=View.VISIBLE
        }
    }
    private fun setUI(quote: QuoteModel){

        binding.quoteTv.text= quote.q
        binding.authorTv.text=quote.a

    }
}