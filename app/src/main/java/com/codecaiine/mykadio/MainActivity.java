package com.codecaiine.mykadio;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String plus1;

    TextView resultat;
    Button btn_effacerMemoire, btn_ajouterMemoire, btn_soustractionMemoire, btn_rappelMemoire;
    Button btn_effacer, btn_toggleSign;

    DecimalFormat decimalFormat = new DecimalFormat("#.##########");

    private static String operand1, operand2;
    private static Operation operationType;
    private static boolean newOperand;

    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, NONE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultat = (TextView) findViewById(R.id.resultat);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        resultat.setTypeface(font);

        // Default operation
        operationType = Operation.NONE;

        // Operand Click
        findViewById(R.id.btn_un).setOnClickListener(this);
        findViewById(R.id.btn_deux).setOnClickListener(this);
        findViewById(R.id.btn_trois).setOnClickListener(this);
        findViewById(R.id.btn_quatre).setOnClickListener(this);
        findViewById(R.id.btn_cinq).setOnClickListener(this);
        findViewById(R.id.btn_six).setOnClickListener(this);
        findViewById(R.id.btn_sept).setOnClickListener(this);
        findViewById(R.id.btn_huit).setOnClickListener(this);
        findViewById(R.id.btn_neuf).setOnClickListener(this);
        findViewById(R.id.btn_zero).setOnClickListener(this);
        findViewById(R.id.btn_point).setOnClickListener(this);

        // Clear Text
        findViewById(R.id.btn_effacer).setOnClickListener(this);

        // Operation
        findViewById(R.id.btn_moins).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_multiplication).setOnClickListener(this);
        findViewById(R.id.btn_division).setOnClickListener(this);
        findViewById(R.id.btn_egale).setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {
        String buttonText = ((Button) view).getText().toString();
        String textDisplay = resultat.getText().toString();
        switch (view.getId()){
            case R.id.btn_un:
            case R.id.btn_deux:
            case R.id.btn_trois:
            case R.id.btn_quatre:
            case R.id.btn_cinq:
            case R.id.btn_six:
            case R.id.btn_sept:
            case R.id.btn_huit:
            case R.id.btn_neuf:
            case R.id.btn_zero:
                if (!newOperand) {

                    // if text already contains 0
                    // than remove preceding 0 and display numbers accordingly
                    if (textDisplay.length() == 1 && textDisplay.equalsIgnoreCase(getString(R.string.zero)))
                        resultat.setText("");

                    resultat.append(buttonText);
                } else {
                    // Set new operand text
                    resultat.setText(buttonText);
                    newOperand = false;
                }
                break;

            case R.id.btn_point:
                if (!newOperand) {
                    // Check if text already contains decimal
                    if (!textDisplay.contains(getString(R.string.point))) {
                        if (textDisplay.length() == 0)
                            resultat.append("0" + buttonText);
                        else
                            resultat.append(buttonText);
                    }

                    // else do not add more decimal dots
                } else {
                    resultat.setText("0" + buttonText);
                    newOperand = false;
                }
                break;

            case R.id.btn_effacer:
                resultat.setText("0");

                // Reset all operations and operands

                operand1 = operand2 = null;
                operationType = Operation.NONE;
                newOperand = false;
                break;

            case R.id.btn_moins:
            case R.id.btn_plus:
            case R.id.btn_division:
            case R.id.btn_multiplication:
                if (!newOperand) {
                    newOperand = true;

                    performOperation(textDisplay);
                }
                if (operationType == Operation.NONE)
                    operand1 = textDisplay;
                setupOperator(buttonText);
                break;

            case R.id.btn_egale:
                if (operationType != Operation.NONE) {
                    performOperation(textDisplay);
                    operand1 = operand2 = null;
                    operationType = Operation.NONE;
                    newOperand = true;
                }
                break;
        }
    }

    private void setupOperator(String buttonText) {
        switch (buttonText){
            case "+":
                operationType = Operation.ADD;

                resultat.append(" + ");

                break;
            case "-":
                operationType = Operation.SUBTRACT;

                resultat.append(" - ");

                break;
            case "x":
                operationType = Operation.MULTIPLY;

                resultat.append(" x ");

                break;
            case "÷":
                operationType = Operation.DIVIDE;

                resultat.append(" ÷ ");

                break;
            default:
                operationType = Operation.NONE;
                break;
        }
    }

    private void performOperation(String text) {
        if (operationType == Operation.NONE) {

            // Operation Type not specified then do not do anything
            operand1 = text;
        } else {

            // Performing Operation
            double op1 = Double.parseDouble(operand1);
            double op2 = Double.parseDouble(text);

            if (operationType == Operation.ADD) {
                op1 = op1 + op2;
                
            } else if (operationType == Operation.SUBTRACT) {
                op1 = op1 - op2;
            } else if (operationType == Operation.MULTIPLY) {
                op1 = op1 * op2;
            } else if (operationType == Operation.DIVIDE) {

                op1 = op1 / op2;    // Todo: Check if op2 is 0 to prevent DivideByZero Exception
            }

            operand1 = String.valueOf(op1);
            op1 = op2 = Double.NaN; // Resetting for GC
            resultat.setText(" = " + operand1);
        }
    }
}
