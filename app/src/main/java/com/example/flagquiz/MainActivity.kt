package com.example.flagquiz

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.Normalizer

class MainActivity : AppCompatActivity() {

    private data class Question(val drawableId: Int, val name: String)

    private val allQuestions = listOf(
        Question(R.drawable.flag_ar, "Argentina"),
        Question(R.drawable.flag_br, "Brasil"),
        Question(R.drawable.flag_ca, "Canadá"),
        Question(R.drawable.flag_cl, "Chile"),
        Question(R.drawable.flag_cn, "China"),
        Question(R.drawable.flag_de, "Alemanha"),
        Question(R.drawable.flag_es, "Espanha"),
        Question(R.drawable.flag_fr, "França"),
        Question(R.drawable.flag_gb, "Reino Unido"),
        Question(R.drawable.flag_it, "Itália"),
        Question(R.drawable.flag_jp, "Japão"),
        Question(R.drawable.flag_mx, "México"),
        Question(R.drawable.flag_pt, "Portugal"),
        Question(R.drawable.flag_us, "Estados Unidos"),
        Question(R.drawable.flag_uy, "Uruguai")
    )

    private var selectedQuestions = listOf<Question>()
    private var currentIndex = 0
    private var score = 0
    private var playerName = ""

    private lateinit var layoutStart: LinearLayout
    private lateinit var layoutQuiz: LinearLayout
    private lateinit var layoutScore: LinearLayout

    private lateinit var etPlayerName: EditText
    private lateinit var btnStart: Button

    private lateinit var tvCounter: TextView
    private lateinit var imgFlag: ImageView
    private lateinit var etAnswer: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvFeedback: TextView
    private lateinit var btnNext: Button

    private lateinit var tvScoreName: TextView
    private lateinit var tvScorePoints: TextView
    private lateinit var btnRestart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        layoutStart = findViewById(R.id.layoutStart)
        layoutQuiz = findViewById(R.id.layoutQuiz)
        layoutScore = findViewById(R.id.layoutScore)

        etPlayerName = findViewById(R.id.etPlayerName)
        btnStart = findViewById(R.id.btnStart)

        tvCounter = findViewById(R.id.tvCounter)
        imgFlag = findViewById(R.id.imgFlag)
        etAnswer = findViewById(R.id.etAnswer)
        btnSubmit = findViewById(R.id.btnSubmit)
        tvFeedback = findViewById(R.id.tvFeedback)
        btnNext = findViewById(R.id.btnNext)

        tvScoreName = findViewById(R.id.tvScoreName)
        tvScorePoints = findViewById(R.id.tvScorePoints)
        btnRestart = findViewById(R.id.btnRestart)

        btnStart.setOnClickListener {
            val inputName = etPlayerName.text.toString().trim()
            if (inputName.isEmpty()) {
                Toast.makeText(this, "Por favor, digite seu nome", Toast.LENGTH_SHORT).show()
            } else {
                playerName = inputName
                startQuiz()
            }
        }

        btnSubmit.setOnClickListener {
            checkAnswer()
        }

        btnNext.setOnClickListener {
            currentIndex++
            if (currentIndex < selectedQuestions.size) {
                displayQuestion()
            } else {
                showScore()
            }
        }

        btnRestart.setOnClickListener {
            layoutScore.visibility = View.GONE
            layoutStart.visibility = View.VISIBLE
            etPlayerName.text.clear()
        }
    }

    private fun startQuiz() {
        selectedQuestions = allQuestions.shuffled().take(5)
        currentIndex = 0
        score = 0
        layoutStart.visibility = View.GONE
        layoutScore.visibility = View.GONE
        layoutQuiz.visibility = View.VISIBLE
        displayQuestion()
    }

    private fun displayQuestion() {
        val q = selectedQuestions[currentIndex]
        tvCounter.text = "${currentIndex + 1} de ${selectedQuestions.size}"
        imgFlag.setImageResource(q.drawableId)
        etAnswer.text.clear()
        etAnswer.isEnabled = true
        btnSubmit.visibility = View.VISIBLE
        tvFeedback.visibility = View.GONE
        btnNext.visibility = View.GONE
    }

    private fun checkAnswer() {
        val userAnswer = etAnswer.text.toString().trim()
        if (userAnswer.isEmpty()) {
            Toast.makeText(this, "Digite sua resposta", Toast.LENGTH_SHORT).show()
            return
        }

        val q = selectedQuestions[currentIndex]
        val isCorrect = normalizeString(userAnswer) == normalizeString(q.name)

        etAnswer.isEnabled = false
        btnSubmit.visibility = View.GONE

        if (isCorrect) {
            score += 20
            tvFeedback.text = "Correto!"
            tvFeedback.setTextColor(Color.GREEN)
        } else {
            tvFeedback.text = "Incorreto! A resposta certa é: ${q.name}"
            tvFeedback.setTextColor(Color.RED)
        }

        tvFeedback.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
    }

    private fun showScore() {
        layoutQuiz.visibility = View.GONE
        layoutScore.visibility = View.VISIBLE
        tvScoreName.text = "Jogador: $playerName"
        tvScorePoints.text = "Pontuação final: $score de 100"
    }

    private fun normalizeString(str: String): String {
        val unaccented = Normalizer.normalize(str, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        return unaccented.lowercase()
    }
}