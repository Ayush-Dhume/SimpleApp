package com.bluest.secureestate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.bluest.secureestate.databinding.ActivityEachBinding

class EachActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEachBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEachBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        val img_second = findViewById<LinearLayout>(R.id.sndpost)
        val heading_second = findViewById<TextView>(R.id.sndheading)

        val intent = intent
        val image = intent?.getIntExtra("resId",0)
        val heading = intent?.getStringExtra("rsId")

        if (image != null) {
            img_second.setHorizontalGravity(image)
        }
        heading_second.text = heading
    }
}