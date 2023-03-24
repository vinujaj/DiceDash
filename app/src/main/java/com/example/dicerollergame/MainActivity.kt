package com.example.dicerollergame
//Link to Demo (Google Drive) - https://drive.google.com/drive/folders/15ih8yM7pYXfrHJNRarrSpp3ZKwbIarlu?usp=sharing
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var aboutButton: Button
    private lateinit var newGame: Button
    private lateinit var homedice : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aboutButton = findViewById(R.id.about_button)
        newGame = findViewById(R.id.new_game_button)
        homedice = findViewById(R.id.home_dice)

        newGame.setOnClickListener { view ->
            view.context.startActivity(Intent(view.context, TargetScoreActivity::class.java))
        }

        aboutButton.setOnClickListener {
            showAboutPopup()
        }
        startAnimation()
    }

    /*The code for this animation is based on the example given in the Android
    Animation API Guide: https://developer.android.com/guide/topics/graphics/prop-animation.html*/
    private fun startAnimation() {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotateAnimation.duration = 2000
        rotateAnimation.repeatCount = Animation.INFINITE

        homedice.startAnimation(rotateAnimation)
    }

    /*AlertDialog is referenced by the Android Developers documentation:
    https://developer.android.com/reference/android/app/AlertDialog*/
    private fun showAboutPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("About")
        builder.setMessage("Author: Vinuja \nStudent Id: w1866977\n\nI confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.  ")

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }


}