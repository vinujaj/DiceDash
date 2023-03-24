package com.example.dicerollergame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class TargetScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_score)

        val targetScoreEditText = findViewById<EditText>(R.id.targetScoreEditText)
        val startGameButton = findViewById<Button>(R.id.startGameButton)

        val userWin = intent.getIntExtra("userWin", 0)
        val comWin = intent.getIntExtra("computerWin", 0)
        // Set the default target score
        val defaultTargetScore = 101

        startGameButton.setOnClickListener {
            // Retrieve the user's chosen target score from the EditText field
            val customTargetScore = targetScoreEditText.text.toString().toIntOrNull() ?: defaultTargetScore

            // Pass the custom target score to NewGameActivity using an intent
            val intent = Intent(this, NewGame::class.java).apply {
                putExtra("TARGET_SCORE", customTargetScore)
                putExtra("USER_WIN", userWin)
                putExtra("COM_WIN", comWin)
            }
            startActivity(intent)
        }
    }
}
