package com.shaluambasta.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {

    private lateinit var etResult: EditText
    private lateinit var etNewNumber: EditText
    private val textOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.text_operation) }

    // variables to hold operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etResult = findViewById(R.id.et_result)
        etNewNumber = findViewById(R.id.et_new_number)

        // Data input buttons
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.button_dot)


        //Operations buttons
        val buttonEquals = findViewById<Button>(R.id.button_equals)
        val buttonAdd = findViewById<Button>(R.id.button_add)
        val buttonSub = findViewById<Button>(R.id.button_sub)
        val buttonMultiply = findViewById<Button>(R.id.button_multiply)
        val buttonDivide = findViewById<Button>(R.id.button_divide)
        val buttonNeg = findViewById<Button>(R.id.button_neg)

        val listener = View.OnClickListener { view ->
            val button = view as Button
            etNewNumber.append(button.text)
        }

        buttonNeg.setOnClickListener {
            val value = etNewNumber.text.toString()
            if (value.isEmpty()) {
                etNewNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    etNewNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    etNewNumber.setText("")
                }
            }

        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { view ->

            val operation = (view as Button).text.toString()
            try {
                val value = etNewNumber.text.toString().toDouble()
                performOperation(value, operation)
            } catch (e: NumberFormatException) {
                etNewNumber.setText("")
            }
            pendingOperation = operation
            textOperation.text = pendingOperation
        }


        buttonEquals.setOnClickListener(opListener)
        buttonAdd.setOnClickListener(opListener)
        buttonSub.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)


    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {

                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }

        }
        etResult.setText(operand1.toString())
        etNewNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        textOperation.text = pendingOperation

    }

}