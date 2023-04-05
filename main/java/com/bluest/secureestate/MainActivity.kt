package com.bluest.secureestate


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bluest.secureestate.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import nl.joery.animatedbottombar.AnimatedBottomBar


class MainActivity : AppCompatActivity() {
    private lateinit var bottomBar: AnimatedBottomBar
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()

        bottomBar = findViewById(R.id.bottom_bar)

        replaceFragment(HomeFragment())
        bottomBar.setOnTabSelectListener(object: AnimatedBottomBar.OnTabSelectListener{
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {

                when(newIndex){
                    0->replaceFragment(HomeFragment())
                    1->replaceFragment(ShortlistedFragment())
                    2->replaceFragment(NotificationsFragment())
                    3->replaceFragment(ProfileFragment())

                    else->{

                    }
                }
            }
        })




    }
    fun replaceFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction().replace(R.id.fragments,fragment).commit()

    }


}




