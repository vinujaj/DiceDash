package com.example.dicerollergame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.view.ContextThemeWrapper
import android.text.Html
import androidx.appcompat.app.AlertDialog

class NewGame : AppCompatActivity() {
    private lateinit var throwButton: Button
    private lateinit var scoreButton: Button
    private lateinit var userScore: TextView
    private lateinit var computerScore: TextView
    private lateinit var humanDiceList: MutableList<ImageView>
    private lateinit var computerDiceList: MutableList<ImageView>
    private lateinit var selectedImageList: MutableList<ImageView>
    private var total = mutableListOf<Int>()

    var userWin=0
    var comWin=0
    var localUserScore = 0
    var allUserTotal = 0

    //scores after score btn pressed
    var userScore_value: Int = 0
    var comScore_value: Int = 0
    var Count: Int = 0
    var attempt = 0

    var human_score: Int = 0
    var computer_score: Int = 0
    var optionalRethrow = 0
    private var throwCount = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)

        val UwinCount: TextView = findViewById(R.id.UwinCount)
        userWin = intent.getIntExtra("userWin", 0)
        UwinCount.setText(userWin.toString())
        comWin = intent.getIntExtra("computerWin", 0)
        val CwinCount: TextView = findViewById(R.id.CwinCount)
        CwinCount.setText(comWin.toString())
        println(userWin)
        println(comWin)

        // Get references to UI elements
        throwButton = findViewById(R.id.throw_button)


        total = arrayListOf()

        // Find the ImageView objects in the layout and assign them to variables
        val user1: ImageView = findViewById(R.id.human_dice1)
        val user2: ImageView = findViewById(R.id.human_dice2)
        val user3: ImageView = findViewById(R.id.human_dice3)
        val user4: ImageView = findViewById(R.id.human_dice4)
        val user5: ImageView = findViewById(R.id.human_dice5)
        humanDiceList = mutableListOf()
        humanDiceList.add(user1)
        humanDiceList.add(user2)
        humanDiceList.add(user3)
        humanDiceList.add(user4)
        humanDiceList.add(user5)

        // Find the ImageView objects in the layout and assign them to variables
        val com1: ImageView = findViewById(R.id.computer_dice1)
        var com2: ImageView = findViewById(R.id.computer_dice2)
        var com3: ImageView = findViewById(R.id.computer_dice3)
        var com4: ImageView = findViewById(R.id.computer_dice4)
        var com5: ImageView = findViewById(R.id.computer_dice5)
        computerDiceList = mutableListOf()
        computerDiceList.add(com1)
        computerDiceList.add(com2)
        computerDiceList.add(com3)
        computerDiceList.add(com4)
        computerDiceList.add(com5)

        selectedImageList = mutableListOf()
        // Check if there is a saved instance state and restore the UserScore and comScore if they are present
        if (savedInstanceState != null) {
            var afterUScore = savedInstanceState.getInt("UserScore", 0)
            userScore.setText("" + afterUScore.toString())

            var afterCScore = savedInstanceState.getInt("comScore", 0)

            computerScore.setText("" + afterCScore.toString())

        }

        // Set click listener for the Throw button
        throwButton.setOnClickListener {
            localUserScore = 0
            if (optionalRethrow == 0) {
                Throw()
                diceClicked()
                optionalRethrow++
            } else {
                if (optionalRethrow == 1 || optionalRethrow == 2) {
                    reRoll()
                    selectedImageList.clear()
                    for (q in humanDiceList) {
                        q.clearColorFilter()
                    }
                    optionalRethrow++
                    if (optionalRethrow == 3) {
                        Score()
                        optionalRethrow = 0
                    }
                }
            }
        }

        scoreButton = findViewById(R.id.score_button)
        // Set click listener for the Score button
        scoreButton.setOnClickListener {
            // Calculate and display scores
            Score()
            attempt = 0
        }
        userScore = findViewById(R.id.userScore_value)
        computerScore = findViewById(R.id.comScore_value)
    }

    override fun onBackPressed() {
        // Create a new intent to launch the main activity
        val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
            // Add extra data to the intent that contains the win counts
            putExtra("userWin", userWin)
            putExtra("computerWin", comWin)
        }

        // Print the win counts to the console
        println("User win count: $userWin")
        println("Computer win count: $comWin")

        // Start the main activity with the new intent
        startActivity(mainActivityIntent)
    }
    private fun Score() {
        for ((index, num) in total.withIndex()) {
            if (index < 5) {
                userScore_value += num
            }
        }
        total.clear()
        userScore = findViewById(R.id.userScore_value)
        computerScore = findViewById(R.id.comScore_value)
        userScore.setText("" + userScore_value)
        computerScore.setText("" + comScore_value)
        Count++


        // Get the custom target score from the intent
        val customTargetScore = intent.getIntExtra("TARGET_SCORE", 101)

        // Check if either player has reached the custom target score
        if (comScore_value >= customTargetScore || userScore_value >= customTargetScore) {
            throwButton.isEnabled = false
            scoreButton.isEnabled = false
            if (userScore_value > comScore_value) {
                userWin++
                println(userWin)
                var UwinCount: TextView = findViewById(R.id.UwinCount)
                UwinCount.setText("" + userWin)
                val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.MyAlertDialogTheme_Green))
                builder.setTitle(Html.fromHtml("<font color='#FFFFFF'>YOU WIN !</font>"))
                    .setCancelable(true)
                    .setPositiveButton("Play Again") { dialogInterface, it ->
                        dialogInterface.cancel()
                        val intent = Intent(this, TargetScoreActivity::class.java)
                        startActivity(intent)

                    }.show()
            } else if (comScore_value > userScore_value) {
                comWin++
                var CwinCount: TextView = findViewById(R.id.CwinCount)
                CwinCount.setText("" + comWin)
                val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.MyAlertDialogTheme_Red)) /*AlertDialog is referenced by the Android Developers documentation:https://developer.android.com/reference/android/app/AlertDialog*/
                builder.setTitle(Html.fromHtml("<font color='#FFFFFF'>YOU LOST</font>"))
                    .setCancelable(true)
                    .setPositiveButton("Play Again") { dialogInterface, it ->
                        dialogInterface.cancel()
                        val intent = Intent(this, TargetScoreActivity::class.java)
                        startActivity(intent)
                    }.show()
            }else {
                // show tie-breaker dialog and re-enable buttons
                throwButton.isEnabled = true
                scoreButton.isEnabled = true
                val builder =
                    AlertDialog.Builder(ContextThemeWrapper(this, R.style.MyAlertDialogTheme))
                builder.setTitle(Html.fromHtml("<font color='#000B87'>IT'S A TIE </font>"))
                    .setCancelable(true)
                    .setPositiveButton(Html.fromHtml("<font color='#1C31BA'>Click OK to play another throw</font>")) { dialogInterface, it ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        throwCount = 0
        optionalRethrow = 0
        selectedImageList.clear()
    }

    // This function updates the scores of the human and the computer
    // It calls setImage() to set the image and score of the dice for the human and the computer
    fun Throw() {
        human_score = setImage(humanDiceList)
        computer_score = setImage(computerDiceList)
        comScore_value += computer_score
    }

    // This function sets the image of the dice based on the random number generated for each dice in the humanDiceList
    fun setImage(humanDiceList: MutableList<ImageView>): Int {
        localUserScore = 0
        for (a in humanDiceList) {
            val an = ((1..5).random())
            if (an == 1) {
                a.setImageResource(R.drawable.dice_1)
                localUserScore += an
                total.add(an)
            } else if (an == 2) {
                a.setImageResource(R.drawable.dice_2)
                localUserScore += an
                total.add(an)
            } else if (an == 3) {
                a.setImageResource(R.drawable.dice_3)
                localUserScore += an
                total.add(an)
            } else if (an == 4) {
                a.setImageResource(R.drawable.dice_4)
                localUserScore += an
                total.add(an)
            } else if (an == 5) {
                a.setImageResource(R.drawable.dice_5)
                localUserScore += an
                total.add(an)
            }
        }
        allUserTotal += localUserScore // Add the score of the dice to the total score of all the dice
        return localUserScore // Return the score of the dice

    }
    // This function is called when the user decides to re-roll selected dice
    // It generates random numbers for the non-selected dice and updates the images and scores in a similar way as setImage()
    // The scores of the re-rolled dice are added to localUserScore
    fun reRoll(): Int {
        localUserScore = 0
        for (x in selectedImageList) {
            println(x)
        }
        for ((index, a) in humanDiceList.withIndex()) {
            if (a !in selectedImageList) {
                val an = ((1..5).random())
                if (an == 1) {
                    a.setImageResource(R.drawable.dice_1)
                    localUserScore += an
                    total[index] = an
                } else if (an == 2) {
                    a.setImageResource(R.drawable.dice_2)
                    localUserScore += an
                    total[index] = an
                } else if (an == 3) {
                    a.setImageResource(R.drawable.dice_3)
                    localUserScore += an
                    total[index] = an
                } else if (an == 4) {
                    a.setImageResource(R.drawable.dice_4)
                    localUserScore += an
                    total[index] = an
                } else if (an == 5) {
                    a.setImageResource(R.drawable.dice_5)
                    localUserScore += an
                    total[index] = an
                }
            }
        }
        return localUserScore // Return the score of the dice
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("UserScore", userScore_value)
        outState.putInt("ComScore", comScore_value)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userScore_value = savedInstanceState.getInt("UserScore", 0)
        comScore_value = savedInstanceState.getInt("ComScore", 0)

        userScore = findViewById(R.id.userScore_value)
        computerScore = findViewById(R.id.comScore_value)

        userScore.text = userScore_value.toString()
        computerScore.text = comScore_value.toString()
    }



    fun diceClicked() {
        for (i in humanDiceList) {
            if (i !in selectedImageList) {
                i.setOnClickListener {
                    selectedImageList.add(i)
                    i.setColorFilter(R.color.white)
                }
            }
        }

    }

}

