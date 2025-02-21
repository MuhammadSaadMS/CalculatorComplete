package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private String currentInput = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        display = findViewById(R.id.display);
        setNumberButtonListeners();
        setOperatorButtonListeners();
    }

    private void setNumberButtonListeners() {
        int[] numberButtons = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_dot
        };

        View.OnClickListener listener = v -> {
            Button clickedButton = (Button) v;
            if (isNewInput) {
                currentInput = "";
                isNewInput = false;
            }

            if (clickedButton.getId() == R.id.btn_dot) {
                if (!currentInput.contains(".")) {
                    currentInput += ".";
                }
            } else {
                currentInput += clickedButton.getText().toString();
            }

            display.setText(currentInput);
        };

        for (int id : numberButtons) {
            Button button = findViewById(id);
            button.setOnClickListener(listener);
            button.setBackgroundResource(R.drawable.rect_button2);
        }
    }

    private void setOperatorButtonListeners() {
        int[] operatorButtons = {
                R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide,
                R.id.btn_percent, R.id.btn_equal, R.id.btn_CE, R.id.btn_C,
                R.id.btn_backspace, R.id.btn_square, R.id.btn_sqrt, R.id.btn_inverse
        };

        View.OnClickListener listener = v -> {
            Button clickedButton = (Button) v;
            String buttonText = clickedButton.getText().toString();

            switch (buttonText) {
                case "+":
                case "-":
                case "*":
                case "/":
                    performOperation(buttonText);
                    break;
                case "=":
                    calculateResult();
                    break;
                case "C":
                    clearAll();
                    break;
                case "CE":
                    clearEntry();
                    break;
                case "⌫":
                    backspace();
                    break;
                case "%":
                    percentage();
                    break;
                case "x²":
                    square();
                    break;
                case "√x":
                    squareRoot();
                    break;
                case "1/x":
                    reciprocal();
                    break;
            }
        };

        for (int id : operatorButtons) {
            Button button = findViewById(id);
            button.setOnClickListener(listener);
            button.setBackgroundResource(R.drawable.rect_button); // Set pressed state background
        }
    }

    private void performOperation(String selectedOperator) {
        if (!currentInput.isEmpty()) {
            firstNumber = Double.parseDouble(currentInput);
        }
        operator = selectedOperator;
        isNewInput = true;
    }

    private void calculateResult() {
        if (operator.isEmpty() || currentInput.isEmpty()) return;

        double secondNumber = Double.parseDouble(currentInput);
        double result = 0;

        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    display.setText("Error");
                    return;
                }
                break;
        }

        display.setText(formatResult(result));
        currentInput = String.valueOf(result);
        operator = "";
        isNewInput = true;
    }

    private void clearAll() {
        currentInput = "";
        firstNumber = 0;
        operator = "";
        display.setText("0");
        isNewInput = true;
    }

    private void clearEntry() {
        currentInput = "";
        display.setText("0");
        isNewInput = true;
    }

    private void backspace() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            display.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }

    private void percentage() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput) / 100;
            display.setText(formatResult(value));
            currentInput = String.valueOf(value);
            isNewInput = true;
        }
    }

    private void square() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            double result = value * value;
            display.setText(formatResult(result));
            currentInput = String.valueOf(result);
            isNewInput = true;
        }
    }

    private void squareRoot() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            if (value >= 0) {
                double result = Math.sqrt(value);
                display.setText(formatResult(result));
                currentInput = String.valueOf(result);
                isNewInput = true;
            } else {
                display.setText("Error");
            }
        }
    }

    private void reciprocal() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            if (value != 0) {
                double result = 1 / value;
                display.setText(formatResult(result));
                currentInput = String.valueOf(result);
                isNewInput = true;
            } else {
                display.setText("Error");
            }
        }
    }

    private String formatResult(double value) {
        DecimalFormat formatter = new DecimalFormat("#.########");
        return formatter.format(value);
    }
}
