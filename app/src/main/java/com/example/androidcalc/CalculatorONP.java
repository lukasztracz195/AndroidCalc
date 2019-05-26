package com.example.androidcalc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

public class CalculatorONP {
    private Activity contex;
    private Configuration config;
    private StringBuilder sb;
    private TextView textViewOnResult;
    private TextView textViewOnEquation;
    private List<Integer> numericButtons;
    private List<Integer> functionalButtons;
    private List<Integer> specialButtons;
    private Button buttonEqual;
    private Button buttonCE;
    private Button buttonC;
    private boolean clickedFunctionButton = false;
    private boolean clickedEquelsButton = false;
    private boolean clickedSpecialButton = false;
    private boolean clickedCE = false;
    private boolean bracketIsOpen = false;
    private boolean piButton;
    private boolean eButton;


    public CalculatorONP(Activity contex, List<Integer> numericButtons, List<Integer> functionalButtons, Configuration config) {
        this.contex = contex;
        this.numericButtons = numericButtons;
        this.functionalButtons = functionalButtons;
        this.config = config;
        textViewOnResult = contex.findViewById(R.id.result);
        buttonC = contex.findViewById(R.id.bC);
        buttonCE = contex.findViewById(R.id.bCE);
        buttonEqual = contex.findViewById(R.id.b_equals);
        textViewOnEquation = contex.findViewById(R.id.equation);
        setActionButtons();
    }

    private void setActionButtons() {
        setActionOnNumericButtons();
        setActionOnFunctionalButtons();
        setActionOnCButton();
        setActionOnCEButton();
        setActionOnButtonEqual();
        setActionPlusMinus();
    }

    public CalculatorONP(Activity contex, List<Integer> numericButtons, List<Integer> functionalButtons, List<Integer> specialButtons, Configuration config) {
        this.contex = contex;
        this.numericButtons = numericButtons;
        this.functionalButtons = functionalButtons;
        this.specialButtons = specialButtons;
        this.config = config;
        textViewOnResult = contex.findViewById(R.id.result);
        buttonC = contex.findViewById(R.id.bC);
        buttonCE = contex.findViewById(R.id.bCE);
        buttonEqual = contex.findViewById(R.id.b_equals);
        textViewOnEquation = contex.findViewById(R.id.equation);
        setActionButtons();
        setActionOnSpecialButtons();
    }


    private void updateContentInTextViewOnEquation() {
        System.out.println("updateContentInTextViewOnEquation()");
        textViewOnEquation.setText(convertStringOfNumberWithENotationToNormal(textViewOnResult.getText().toString()));

    }

    private void updateContentInTextViewOnEquation(Button button) {
        System.out.println("updateContentInTextViewOnEquation(Button button) ");
        StringBuilder sb = new StringBuilder();
        if (isEmptyTextViewOnEquation()) {
            sb.append(textViewOnEquation.getText());
            sb.append(convertStringOfNumberWithENotationToNormal(textViewOnResult.getText().toString()));
            sb.append(button.getText());
        } else {
            sb.append(textViewOnEquation.getText());
            sb.append(button.getText());
            // sb.append(convertStringOfNumberWithENotationToNormal(textViewOnResult.getText().toString()));
        }
        clearTextViewOnResult();
        textViewOnEquation.setText(sb.toString());

    }

    private void setActionOnButtonEqual() {
        System.out.println("setActionOnButtonEqual");
        buttonEqual.setOnClickListener((View v) -> {
            Button button = (Button) v;
            clickedEquelsButton = true;
            if (clickedCE && !isEmptyTextViewOnEquation() && !containActionFunctionalSignInTextViewOnEquation()) {
                if (containActionFunctionalSignInTextViewOnEquation()) {
                    textViewOnResult.setText(textViewOnEquation.getText().subSequence(0, textViewOnEquation.getText().length()));
                } else {
                    textViewOnResult.setText(textViewOnEquation.getText());
                }
                clickedCE = false;
            } else {
                if (!isEmptyTextViewOnResult() && !isEmptyTextViewOnEquation()) {
                    addZeroAfterDotInTextViewOnResult();
                    updateTextViewOnEquationAfterUsedButtonEqual();
                    // System.out.println(textViewOnEquation.getText());
                    double result = 0.0;
                    if (!isEmptyTextViewOnEquation() && containNegativeNumbersInTextViewOnEquation()) {
                        result = negativeNumberCalculator();
                    } else {
                        ONP onp = new ONP(String.valueOf(textViewOnEquation.getText()));
                        result = onp.oblicz();
                    }
                    if (Double.isNaN(result)) {
                        Toast.makeText(contex.getApplicationContext(), "Error: NotDevideByZero!", Toast.LENGTH_SHORT).show();
                        clearTextViewOnResult();
                        clearTextViewOnEquation();
                    } else if (Double.isInfinite(result)) {
                        if (result > 0) {
                            Toast.makeText(contex.getApplicationContext(), "Error: Positive Infinite!", Toast.LENGTH_SHORT).show();
                            clearTextViewOnResult();
                            textViewOnEquation.setText("");
                        } else {
                            Toast.makeText(contex.getApplicationContext(), "Error: Negative Infinite!", Toast.LENGTH_SHORT).show();
                            clearTextViewOnResult();
                            textViewOnEquation.setText("");
                        }
                    } else {
                        System.out.println(result);
                        String resultS = fitNumberToNumbersOfPlaceOnTextViewOnResult(getHowManyDigitsIsPossibleDisplayOnTextViewOnResult(), result);
                        textViewOnResult.setText(convertStringOfNumberWithENotationToNormal(resultS));
                        updateContentInTextViewOnEquation();
                        disableNumericButtons();
                    }

                }
                enableFunctionalButtons();
                buttonEqual.setEnabled(false);
            }
        });
    }

    private void updateTextViewOnEquationAfterUsedFunctionalButton(Button button) {
        System.out.println("updateTextViewOnEquationAfterUsedFunctionalButton(Button button)");
        String sb = String.valueOf(textViewOnEquation.getText()) + textViewOnResult.getText() +
                button.getText();
        clearTextViewOnResult();
        textViewOnEquation.setText(convertStringOfNumberWithENotationToNormal(sb));

    }

    private void updateTextViewOnEquationAfterUsedButtonEqual() {
        System.out.println("updateTextViewOnEquationAfterUsedButtonEqual()");
        System.out.println("Equation: " + textViewOnEquation.getText());
        System.out.println("Screen: " + textViewOnResult.getText());
        String sb = (textViewOnEquation.getText()) + convertStringOfNumberWithENotationToNormal(textViewOnResult.getText().toString());
        clearTextViewOnResult();
        textViewOnEquation.setText(sb);

    }

    private void setActionOnNumericButtons() {
        for (Integer id : numericButtons) {
            Button tmp = contex.findViewById(id);
            tmp.setOnClickListener((View v) -> {
                Button tmpButton = (Button) v;
                System.out.println("setActionOnNumericButtons: " + tmpButton.getText());
                int size = getHowManyDigitsIsPossibleDisplayOnTextViewOnResult();
                if (tmp.getText().equals("(")) {
                    bracketIsOpen = true;
                }
                if (tmp.getText().equals(")")) {
                    bracketIsOpen = false;
                }
                if (clickedFunctionButton) {
                    clearTextViewOnResult();
                    clickedFunctionButton = false;
                }
                if (tmpButton.getId() != R.id.b_ && textViewOnResult.getText().length() < size) {
                    setTextOnTextViewOnResultFromButton(tmpButton);
                    enableSpecialButtons();
                } else {
                    if (!String.valueOf(textViewOnResult.getText()).contains(".") && textViewOnResult.getText().length() <= size && textViewOnResult.getText().length() > 0) {
                        setTextOnTextViewOnResultFromButton(tmpButton);
                    } else {
                        if (textViewOnResult.getText().length() >= size) {
                            disableFunctionalButtons();
                            disableNumericButtons();
                        }
                    }
                }
                buttonEqual.setEnabled(true);
                enableFunctionalButtons();
            });
        }
    }

    private void setActionOnFunctionalButtons() {
        for (Integer id : functionalButtons) {
            Button tmp = contex.findViewById(id);
            tmp.setOnClickListener((View v) -> {
                Button tmpButton = (Button) v;
                System.out.println("setActionOnFunctionalButtons: " + tmp.getText());
                if (clickedSpecialButton) {
                    textViewOnEquation.setText("");
                }
                if (clickedCE && !isEmptyTextViewOnEquation()) {
                    addTextFromButtonToTextViewOnEquation(tmpButton);
                    enableNumericButtons();
                    clickedCE = false;
                }
                if (bracketIsOpen) {
                    addTextFromButtonToTextViewOnResult(tmpButton);
                } else {

                    if (textViewOnResultContainDot()) {
                        addZeroAfterDotInTextViewOnResult();
                    }
                    if (isEmptyTextViewOnEquation()) {
                        disableFunctionalButtons();
                    }
                    if (!containActionFunctionalSignInTextViewOnEquation()) {
                        System.out.println("ActionOnFunctionalnButton: " + tmpButton.getText());
                        enableNumericButtons();
                        clickedFunctionButton = true;
                        int size = getHowManyDigitsIsPossibleDisplayOnTextViewOnResult();
                        //After used Equal Button
                           /* if (textViewOnResult.getText().equals(textViewOnEquation.getText())) {
                                System.out.println("if(textViewOnResult.getText().equals(textViewOnEquation.getText()))");
                                clearTextViewOnResult();
                                addTextFromButtonToTextViewOnEquation(tmpButton);
                            }
                            //After used another functional button
                            else {
                            */
                        // System.out.println("if (textViewOnResult.getText().length() < size)");
                        if (isEmptyTextViewOnEquation() && !containActionFunctionalSignInTextViewOnEquation()) {
                            System.out.println("if (isEmptyTextViewOnEquation()");
                            updateContentInTextViewOnEquation(tmpButton);
                        } else {
                            if (clickedEquelsButton) {
                                System.out.println("if (clickedEquelsButton)");
                                if (!isEmptyTextViewOnResult() && !isEmptyTextViewOnEquation()) {
                                    updateContentInTextViewOnEquation(tmpButton);
                                }
                                clearTextViewOnResult();
                                clickedEquelsButton = false;
                            } else {
                                addTextFromButtonToTextViewOnEquation(tmp);
                                clearTextViewOnResult();
                            }
                        }

                        if (textViewOnResult.getText().length() == size) {
                            disableFunctionalButtons();
                            disableNumericButtons();
                        }
                        if (containActionFunctionalSignInTextViewOnEquation()) {
                            disableFunctionalButtons();
                        } else {
                            enableFunctionalButtons();
                        }
                        // }
                    }
                }
                buttonEqual.setEnabled(true);
                //enableSpecialButtons();
            });
        }

    }

    private int getHowManyDigitsIsPossibleDisplayOnTextViewOnResult() {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 17;
        } else {
            return 35;
        }
    }

    private void disableFunctionalButtons() {
        System.out.println("disableFunctionalButtons");
        for (Integer item : functionalButtons) {
            Button button = contex.findViewById(item);
            button.setEnabled(false);
        }
    }

    private void enableFunctionalButtons() {
        System.out.println("enableFunctionalButtons");
        for (Integer item : functionalButtons) {
            Button button = contex.findViewById(item);
            button.setEnabled(true);
        }
    }

    private void disableNumericButtons() {
        System.out.println("disableNumericButtons");
        for (Integer item : numericButtons) {
            Button button = contex.findViewById(item);
            button.setEnabled(false);
        }
    }

    private void enableNumericButtons() {
        System.out.println("enableNumericButtons");
        for (Integer item : numericButtons) {
            Button button = contex.findViewById(item);
            button.setEnabled(true);
        }
    }

    private void setTextOnTextViewOnResultFromButton(Button button) {
        System.out.println("setTextOnTextViewOnResultFromButton(" + button.getText() + ")");
        String sb = String.valueOf(textViewOnResult.getText()) +
                button.getText();
        textViewOnResult.setText(sb);
    }

    private void setActionOnCEButton() {
        buttonCE.setOnClickListener((View v) -> {
            System.out.println("setActionOnCEButton");
            clickedCE = true;
            clearTextViewOnResult();
            buttonEqual.setEnabled(false);
            if (!isEmptyTextViewOnEquation()) {
                disableNumericButtons();
            }
        });
    }

    private void setActionOnCButton() {
        buttonC.setOnClickListener((View v) -> {
            System.out.println("setActionOnCButton");
            StringBuilder sb = new StringBuilder();
            if (isEmptyTextViewOnResult() && containActionFunctionalSignInTextViewOnEquation()) {
                sb.append(textViewOnEquation.getText().subSequence(0, textViewOnEquation.getText().length() - 1));
                textViewOnEquation.setText(sb.toString());
                enableFunctionalButtons();
            } else {
                if (!isEmptyTextViewOnResult()) {
                    sb.append(textViewOnResult.getText().subSequence(0, textViewOnResult.getText().length() - 1));
                    textViewOnResult.setText(sb.toString());
                } else {

                    clearTextViewOnEquation();
                    clickedEquelsButton = false;
                    enableNumericButtons();
                    disableFunctionalButtons();
                    buttonEqual.setEnabled(false);
                }
            }
            if (textViewOnResult.getText().length() < getHowManyDigitsIsPossibleDisplayOnTextViewOnResult()) {
                enableNumericButtons();
            }

        });
    }

    private void clearTextViewOnResult() {
        textViewOnResult.setText("");
    }

    private void clearTextViewOnEquation() {
        textViewOnEquation.setText("");
    }

    private int getDigitsBeforeDot(double number) {
        int decimal = (int) number;
        String sNumber = String.valueOf(decimal);
        return sNumber.length();
    }

    private String fitNumberToNumbersOfPlaceOnTextViewOnResult(int AllDigitsOnScreen, double number) {
        System.out.println("fitNumberToNumbersOfPlaceOnTextViewOnResult");
        StringBuilder sb = new StringBuilder();
        Locale locale = Locale.ENGLISH;
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        int beforeDotDigits = getDigitsBeforeDot(number);
        int restDigits = 0;
        if (beforeDotDigits > AllDigitsOnScreen) {
            beforeDotDigits = AllDigitsOnScreen;
        } else {
            restDigits = AllDigitsOnScreen - (beforeDotDigits + 1);
        }
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(restDigits);

        return nf.format(number);
    }

    private void addZeroAfterDotInTextViewOnResult() {
        System.out.println("addZeroAfterDotInTextViewOnResult");
        String numberFromScreen = textViewOnResult.getText().toString();
        if (numberFromScreen.contains(".")) {
            int lengthNumberOnScreen = numberFromScreen.length();
            char lastChar = numberFromScreen.charAt(lengthNumberOnScreen - 1);
            if (lastChar == '.') {
                StringBuilder sb = new StringBuilder();
                sb.append(textViewOnResult.getText());
                sb.append(0);
                textViewOnResult.setText(sb.toString());
            }
        }
    }

    private String convertStringOfNumberWithENotationToNormal(String number) {
        System.out.println("convertStringOfNumberWithENotationToNormal(" + number + ")");
        Double d = null;
        String toReturn = number;
        if (number.contains(",")) {
            toReturn = number.replaceAll(",", "");
        }
        try {
            d = Double.parseDouble(toReturn);
        } catch (NumberFormatException e) {
            return number;
        }
        return d.toString();
    }

    private void addTextFromButtonToTextViewOnEquation(Button button) {
        System.out.println("addTextFromButtonToTextViewOnEquation( " + button.getText() + " )");
        StringBuilder sb = new StringBuilder();
        sb.append(textViewOnEquation.getText());
        sb.append(button.getText());
        textViewOnEquation.setText(sb.toString());

    }

    private void addTextFromButtonToTextViewOnResult(Button button) {
        System.out.println("addTextFromButtonToTextViewOnResult( " + button.getText() + " )");
        StringBuilder sb = new StringBuilder();
        sb.append(textViewOnResult.getText());
        sb.append(button.getText());
        textViewOnResult.setText(sb.toString());

    }

    private boolean isEmptyTextViewOnEquation() {
        return textViewOnEquation.getText().toString().isEmpty();
    }

    private boolean isEmptyTextViewOnResult() {
        return textViewOnResult.getText().toString().isEmpty();
    }

    private boolean containActionFunctionalSignInTextViewOnEquation() {
        System.out.println("containActionFunctionalSignInTextViewOnEquation");
        String equation = textViewOnEquation.getText().toString();
        for (Integer item : functionalButtons) {
            Button button = contex.findViewById(item);
            if (!equation.isEmpty()) {
                if (equation.charAt(equation.length() - 1) == button.getText().toString().charAt(0)) {
                    return true;
                }
            }
        }
        return equation.contains(("="));
    }

    private String containActionFunctionalSignInTextViewOnEquationReturnString() {
        System.out.println("containActionFunctionalSignInTextViewOnEquation");
        String equation = textViewOnEquation.getText().toString();
        if (containNegativeNumbersInTextViewOnEquation()) {
            equation = equation.substring(1, equation.length());
        }
        System.out.println("Equation: " + equation);
        for (Integer item : functionalButtons) {
            Button button = contex.findViewById(item);
            if (!equation.isEmpty()) {
                if (equation.contains(button.getText())) {
                    return button.getText().toString();
                }
            }
        }
        return null;
    }

    private boolean containNegativeNumbersInTextViewOnEquation() {
        System.out.println("containNegativeNumbersInTextViewOnEquation");
        String equation = textViewOnEquation.getText().toString();

            if (!equation.isEmpty()) {
                if (equation.contains("-")) {
                    return true;
                }
            }

        return false;
    }

    @SuppressLint("SetTextI18n")
    private void setActionOnSpecialButtons() {
        for (Integer item : specialButtons) {
            Button button = contex.findViewById(item);
            button.setOnClickListener((View v) -> {

                clickedSpecialButton = true;
                Button b = (Button) v;
                System.out.println("setActionOnSpecialButtons( " + b.getText() + " )");
                String action = b.getText().toString();
                String number = "";
                double numberD = 0.0;
                if (!isEmptyTextViewOnResult()) {
                    number = textViewOnResult.getText().toString();
                    number = convertStringOfNumberWithENotationToNormal(number);
                    numberD = Double.parseDouble(number);
                }
                switch (action) {
                    case "sin": {
                        numberD = Math.sin(numberD);
                        break;
                    }
                    case "cos": {
                        numberD = Math.cos(numberD);
                        break;
                    }
                    case "tan": {
                        numberD = Math.tan(numberD);
                        break;
                    }
                    case "ln": {
                        if (numberD >= 0.0) {
                            numberD = Math.log(numberD);
                        } else {
                            Toast.makeText(contex.getApplicationContext(), "Error: This is not positive number!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case "pi": {
                        numberD = Math.PI;
                        break;
                    }
                    case "e": {
                        numberD = Math.E;
                        break;
                    }
                    case "sqrt": {
                        if (numberD >= 0.0) {
                            numberD = Math.sqrt(numberD);
                        } else {
                            Toast.makeText(contex.getApplicationContext(), "Error: This is not positive number!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                textViewOnResult.setText(convertStringOfNumberWithENotationToNormal(String.valueOf(numberD)));
            });
        }
    }

    private boolean textViewOnResultContainDot() {
        return textViewOnResult.getText().toString().contains(".");
    }

    private void enableSpecialButtons() {
        if (specialButtons != null) {
            System.out.println("enableSpecialButtons");
            for (Integer item : specialButtons) {
                Button button = contex.findViewById(item);
                button.setEnabled(true);
            }
        }
    }

    private void disableSpecialButtons() {
        if (specialButtons != null) {
            System.out.println("disableSpecialButtons");
            for (Integer item : specialButtons) {
                Button button = contex.findViewById(item);
                button.setEnabled(false);
            }
        }
    }

    private void setActionPlusMinus() {
        Button button = contex.findViewById(R.id.b_plus_minus);
        button.setOnClickListener((View v) -> {
            Button tmp = (Button) v;
            if(!isEmptyTextViewOnResult()) {
                String screen = textViewOnResult.getText().toString();
                StringBuilder sb = new StringBuilder();
                if (screen.charAt(0) == '-') {
                    sb.append(textViewOnResult.getText().subSequence(1, screen.length()));

                } else {
                    sb.append("-");
                    sb.append(textViewOnResult.getText().toString());
                }
                textViewOnResult.setText(convertStringOfNumberWithENotationToNormal(sb.toString()));
            }
        });
    }

    private double negativeNumberCalculator() {
        System.out.println("negativeNumberCalculator");
        addZeroAfterDotInTextViewOnResult();
        String diff = containActionFunctionalSignInTextViewOnEquationReturnString();
        String[] tab = {"", ""};
        String equation = textViewOnEquation.getText().toString();
        System.out.println("Equation :: "+equation);
        boolean firstNegative = false;
        if(equation.contains("^")){
            diff = "^";
        }
        System.out.println("Diff= " + diff);
        System.out.println(textViewOnEquation.getText());
        if (containNegativeNumbersInTextViewOnEquation() && equation.charAt(0) =='-') {
            System.out.println("firstNegative");
            equation = equation.substring(1, equation.length());
            firstNegative = true;
        }
        if(!equation.contains("^")) {
            System.out.println("EquationBeforeSplit: " + equation);
            try {
                tab = equation.split(diff);
            } catch (PatternSyntaxException e) {
                tab = manualSplit(equation, diff.charAt(0));
            }
            System.out.println("Array: " + tab[0] + "   " + tab[1]);
            if (firstNegative) {
                tab[0] = String.valueOf("-" + tab[0]);
            }
        }else{
            tab = manualSplit(equation,diff.charAt(0));
            if (firstNegative) {
                tab[0] = String.valueOf("-" + tab[0]);
            }
        }
        Double a = Double.parseDouble(tab[0]);
        Double b = Double.parseDouble(tab[1]);
        double w = 0.0;
        switch (diff) {
            case "+": {
                w = a + b;
                break;
            }
            case "-": {
                w = a - b;
                break;
            }
            case "*": {
                w = a * b;
                break;
            }
            case "/": {
                if (b != 0.0) {
                    w = a / b;
                } else {
                    return Double.NaN;
                }
                break;
            }
            case "^": {
                w = Math.pow(a, b);
                break;
            }
        }
        return w;
    }

    private String[] manualSplit(String source, char diff){
        StringBuilder sb = new StringBuilder();
        String[] tab= new String[2];
        int start = 0;
        for(int i=0; i<source.length();i++){
            start = i;
            if(source.charAt((i)) == diff){
                break;
            }
            sb.append(source.charAt(i));
        }
        tab[0] = sb.toString();
         sb = new StringBuilder();
        for(int i=start+1; i<source.length();i++){
            if(source.charAt((i)) == diff){
                break;
            }
            sb.append(source.charAt(i));
        }
        tab[1] = sb.toString();
        return tab;
    }
}

