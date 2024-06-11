package com.example.gourmetguide

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class Choice(val foodType: String, val liked: Boolean, val checkBoxId: Int)

class MainActivity : AppCompatActivity() {

    private val choices = mutableListOf<Choice>()
    private val history = mutableListOf<Choice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        val resetButton = findViewById<Button>(R.id.btnReset)
        val previousButton = findViewById<Button>(R.id.btnPrevious)
        val coinFlipButton = findViewById<Button>(R.id.btnCoinFlip)

        val checkboxes = listOf(
            Pair(findViewById<CheckBox>(R.id.ckbxPizzaYes), findViewById<CheckBox>(R.id.ckbxPizzaNo)),
            Pair(findViewById<CheckBox>(R.id.ckbxBurgerYes), findViewById<CheckBox>(R.id.ckbxBurgerNo)),
            Pair(findViewById<CheckBox>(R.id.ckbxItalianYes), findViewById<CheckBox>(R.id.ckbxItalianNo)),
            Pair(findViewById<CheckBox>(R.id.ckbxMexicanYes), findViewById<CheckBox>(R.id.ckbxMexicanNo)),
            Pair(findViewById<CheckBox>(R.id.ckbxAmericanYes), findViewById<CheckBox>(R.id.ckbxAmericanNo)),
            Pair(findViewById<CheckBox>(R.id.ckbxVeganYes), findViewById<CheckBox>(R.id.ckbxVeganNo))
        )

        resetButton.setOnClickListener {
            resetChoices(checkboxes)
        }

        previousButton.setOnClickListener {
            undoLastChoice()
        }

        coinFlipButton.setOnClickListener {
            performCoinFlip()
        }

        checkboxes.forEach { (yesCheckBox, noCheckBox) ->
            yesCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noCheckBox.isChecked = false
                    addChoice(yesCheckBox.text.toString(), true, yesCheckBox.id)
                }
            }

            noCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    yesCheckBox.isChecked = false
                    addChoice(noCheckBox.text.toString(), false, noCheckBox.id)
                }
            }
        }
    }

    private fun addChoice(foodType: String, liked: Boolean, checkBoxId: Int) {
        val choice = Choice(foodType, liked, checkBoxId)
        choices.add(choice)
        history.add(choice)
    }

    private fun resetChoices(checkboxes: List<Pair<CheckBox, CheckBox>>) {
        choices.clear()
        history.clear()

        checkboxes.forEach { (yesCheckBox, noCheckBox) ->
            yesCheckBox.isChecked = false
            noCheckBox.isChecked = false
        }
        Toast.makeText(this, "Choices have been reset.", Toast.LENGTH_SHORT).show()
    }

    private fun undoLastChoice() {
        if (history.isNotEmpty()) {
            val lastChoice = history.removeAt(history.size - 1)
            choices.remove(lastChoice)
            val checkBox = findViewById<CheckBox>(lastChoice.checkBoxId)
            checkBox.isChecked = false
            Toast.makeText(this, "Last choice undone.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No actions to undo.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performCoinFlip() {
        val result = if (Math.random() < 0.5) "Heads" else "Tails"
        findViewById<TextView>(R.id.txtCoinFlip).text = "Result: $result"
    }
}
