package com.example.myapplicationfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    TextView calculation, answer;
    String sCalculation = "", sAnswer = "", number_one = "", number_two = "", current_oprator = "", prev_ans = "";
    String  function;
    Double Result = 0.0, numberOne = 0.0, numberTwo = 0.0, temp = 0.0;
    Boolean dot_present = false, number_allow = true;
    Boolean constant_present = false, function_present = false, invert_allow = true, value_inverted = false;
    NumberFormat format, longformate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculation = findViewById(R.id.calculation);
        answer = findViewById(R.id.answer);

        //we set the answer up to four decimal
        format = new DecimalFormat("#.####");
        //we need to reformat answer if it's long
        longformate = new DecimalFormat("0.#E0");

    }

    public void onClickNumber(View v) {
        //we need to find which button is pressed
        if (number_allow) {
            Button bn = (Button) v;
            sCalculation += bn.getText();
            number_one += bn.getText();
            numberOne = Double.parseDouble(number_one);

            if (function_present) {
                calculateFunction(function);
                return;
            }
            switch (current_oprator) {

                case ""://if current oprator is null
                        temp = Result + numberOne;
                    break;

                case "+":
                        temp = Result + numberOne;
                    break;

                case "-":
                        temp = Result - numberOne;
                    break;

                case "x":

                        temp = Result * numberOne;
                    break;

                case "/":
                    try {
                        // divided by 0 cause execption
                            temp = Result / numberOne;
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    break;

            }
            sAnswer = format.format(temp).toString();
            updateCalculation();
        }
    }

    public void onClickOprator(View v) {
        Button ob = (Button) v;
        //if sAnswer is null means no calculation needed
        if (sAnswer != "") {
            //we check last char is operator or not
            if (current_oprator != "") {
                char c = getcharfromLast(sCalculation, 2);// 2 is the char from last because our las char is " "
                if (c == '+' || c == '-' || c == 'x' || c == '/') {
                    sCalculation = sCalculation.substring(0, sCalculation.length() - 3);
                }
            }
            sCalculation = sCalculation + "\n" + ob.getText() + " ";
            number_one = "";
            Result = temp;
            current_oprator = ob.getText().toString();
            updateCalculation();
            //when operator click dot is not present in number_one
            number_two = "";
            numberTwo = 0.0;
            dot_present = false;
            number_allow = true;
            constant_present = false;
            function_present = false;
            value_inverted = false;
        }

    }

    private char getcharfromLast(String s, int i) {
        char c = s.charAt(s.length() - i);
        return c;
    }

    public void onClickClear(View v) {
        cleardata();
    }

    public void cleardata() {
        sCalculation = "";
        sAnswer = "";
        current_oprator = "";
        number_one = "";
        number_two = "";
        prev_ans = "";
        Result = 0.0;
        numberOne = 0.0;
        numberTwo = 0.0;
        temp = 0.0;
        updateCalculation();
        dot_present = false;
        number_allow = true;
        function_present = false;
        constant_present = false;
        value_inverted = false;
    }

    public void updateCalculation() {
        calculation.setText(sCalculation);
        answer.setText(sAnswer);
    }

    public void onDotClick(View view) {
        //create boolean dot_present check if dot is present or not.
        if (!dot_present) {
            //check length of numberone
            if (number_one.length() == 0) {
                number_one = "0.";
                sCalculation += "0.";
                sAnswer = "0.";
                dot_present = true;
                updateCalculation();
            } else {
                number_one += ".";
                sCalculation += ".";
                sAnswer += ".";
                dot_present = true;
                updateCalculation();
            }
        }

    }

    public void onClickEqual(View view) {
        showresult();
    }

    public void showresult() {
        if (sAnswer != "" && sAnswer != prev_ans) {
            sCalculation += "\n= " + sAnswer + "\n----------\n" + sAnswer + " ";
            number_one = "";
            number_two = "";
            numberTwo = 0.0;
            numberOne = 0.0;
            Result = temp;
            prev_ans = sAnswer;
            updateCalculation();
            //we  don't allow to edit our ans so
            dot_present = true;
            number_allow = false;
            constant_present = false;
            function_present = false;
            value_inverted = false;
        }
    }

    public void onModuloClick(View view) {
        if (sAnswer != "" && getcharfromLast(sCalculation, 1) != ' ') {
            sCalculation += "% ";
            //value of temp will change according to the operator
            switch (current_oprator) {
                case "":
                    temp = temp / 100;
                    break;
                case "+":
                    temp = Result + ((Result * numberOne) / 100);
                    break;
                case "-":
                    temp = Result - ((Result * numberOne) / 100);
                    break;
                case "x":
                    temp = Result * (numberOne / 100);
                    break;
                case "/":
                    try {
                        temp = Result / (numberOne / 100);
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    break;
            }
            sAnswer = format.format(temp).toString();
            if (sAnswer.length() > 9) {
                sAnswer = longformate.format(temp).toString();
            }
            Result = temp;
            //now we show the result
            showresult();

        }
    }

    public void onPorMClick(View view) {
        if (invert_allow) {
            if (sAnswer != "" && getcharfromLast(sCalculation, 1) != ' ') {
                numberOne = numberOne * (-1);
                number_one = format.format(numberOne).toString();
                switch (current_oprator) {
                    case "":
                        temp = numberOne;
                        sCalculation = number_one;
                        break;
                    case "+":
                        temp = Result + numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += number_one;
                        break;
                    case "-":
                        temp = Result - numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += number_one;
                        break;
                    case "*":
                        temp = Result * numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += number_one;
                        break;
                    case "/":
                        try {
                            temp = Result / numberOne;
                            //we need to add - sign in the starting of the string
                            removeuntilchar(sCalculation, ' ');
                            sCalculation += number_one;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }
                        break;
                }
                sAnswer = format.format(temp).toString();
                value_inverted = value_inverted ? false : true;
                updateCalculation();
            }
        }
    }


    public void removeuntilchar(String str, char chr) {
        char c = getcharfromLast(str, 1);
        if (c != chr) {
            //remove last char
            str = removechar(str, 1);
            sCalculation = str;
            updateCalculation();
            removeuntilchar(str, chr);
        }
    }

    public String removechar(String str, int i) {
        char c = str.charAt(str.length() - i);
        //we need to check if dot is removed or not
        if (c == '.' && !dot_present) {
            dot_present = false;
        }
        if (c == ' ') {
            return str.substring(0, str.length() - (i - 1));
        }
        return str.substring(0, str.length() - i);
    }


    public void calculateFunction(String function) {
        function_present = true;
        if (current_oprator != "" && getcharfromLast(sCalculation, 1) == ' ') {
            updateCalculation();
        } else {
            switch (current_oprator) {
                case "":
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "+":
                    removeuntilchar(sCalculation, ' ');
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "-":
                    removeuntilchar(sCalculation, ' ');
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "x":
                    removeuntilchar(sCalculation, ' ');
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "/":
                    removeuntilchar(sCalculation, ' ');
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;
            }
        }
    }

    public void onClickDelete(View view) {
        if (function_present) {
            DeleteFunction();
            return;
        }
        if (sAnswer != "") {
            if (getcharfromLast(sCalculation, 1) != ' ') {
                if (number_one.length() < 2 && current_oprator != "") {
                    number_one = "";
                    temp = Result;
                    sAnswer = format.format(Result).toString();
                    sCalculation = removechar(sCalculation, 1);
                    updateCalculation();
                } else {
                    switch (current_oprator) {
                        case "":
                            if (value_inverted) {
                                sAnswer = sAnswer.substring(1, sAnswer.length());
                                sCalculation = sCalculation.substring(1, sAnswer.length());
                                updateCalculation();
                            }
                            if (sCalculation.length() < 2) {
                                cleardata();
                            } else {
                                if (getcharfromLast(sCalculation, 1) == '.') {
                                    dot_present = false;
                                }
                                number_one = removechar(number_one, 1);
                                numberOne = Double.parseDouble(number_one);
                                temp = numberOne;
                                sCalculation = number_one;
                                sAnswer = number_one;
                                updateCalculation();
                            }
                            break;

                        case "+":
                            if (value_inverted) {
                                numberOne = numberOne * (-1);
                                number_one = format.format(numberOne).toString();
                                temp = Result + numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculation, ' ');
                                sCalculation += number_one;
                                updateCalculation();
                                value_inverted = value_inverted ? false : true;
                            }
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result + numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "-":
                            if (value_inverted) {
                                numberOne = numberOne * (-1);
                                number_one = format.format(numberOne).toString();
                                temp = Result - numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculation, ' ');
                                sCalculation += number_one;
                                updateCalculation();
                                value_inverted = value_inverted ? false : true;
                            }
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result - numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "x":
                            if (value_inverted) {
                                numberOne = numberOne * (-1);
                                number_one = format.format(numberOne).toString();
                                temp = Result * numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculation, ' ');
                                sCalculation += number_one;
                                updateCalculation();
                                value_inverted = value_inverted ? false : true;
                            }
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result * numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "/":
                            try {
                                if (value_inverted) {
                                    numberOne = numberOne * (-1);
                                    number_one = format.format(numberOne).toString();
                                    temp = Result / numberOne;
                                    sAnswer = format.format(temp).toString();
                                    removeuntilchar(sCalculation, ' ');
                                    sCalculation += number_one;
                                    updateCalculation();
                                    value_inverted = value_inverted ? false : true;
                                }
                                if (getcharfromLast(sCalculation, 1) == '.') {
                                    dot_present = false;
                                }
                                number_one = removechar(number_one, 1);
                                if (number_one.length() == 1 && number_one == ".") {
                                    numberOne = Double.parseDouble(number_one);
                                }
                                numberOne = Double.parseDouble(number_one);
                                temp = Result / numberOne;
                                sAnswer = format.format(temp).toString();
                                sCalculation = removechar(sCalculation, 1);
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            updateCalculation();
                            break;
                    }
                }
            }
        }
    }


    public void DeleteFunction() {
        if (current_oprator == "") {
            if (getcharfromLast(sCalculation, 1) == ' ') {
                cleardata();
            } else if (getcharfromLast(sCalculation, 2) == ' ') {
                cleardata();
            } else {
                sCalculation = removechar(sCalculation, 1);
                number_one = removechar(number_one, 1);
                numberOne = Double.parseDouble(number_one);
                calculateFunction(function);
            }
            updateCalculation();
        } else {
            if (getcharfromLast(sCalculation, 1) == '(') {
                removeuntilchar(sCalculation, ' ');
                function_present = false;
            } else if (getcharfromLast(sCalculation, 2) == '(') {
                sCalculation = removechar(sCalculation, 1);
                number_one = "";
                temp = Result;
                sAnswer = format.format(Result).toString();
            } else {
                sCalculation = removechar(sCalculation, 1);
                number_one = removechar(number_one, 1);
                numberOne = Double.parseDouble(number_one);
                calculateFunction(function);
            }
            updateCalculation();
        }
    }


}
