package com.example.androidcalc;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalculatorONP implements Serializable {
    private Activity contex;
    private Configuration config;

    private TextView textViewOnEquation;
    private TextView textViewOnResult;
    private List<Integer> numericTextViewsIDs;
    private List<Integer> functionalTextViewsIDs;
    private List<Integer> specialTextViewsIDs;
    private List<Integer> constTextViewsIDs;
    private List<String> listOfOperationsString;
    private TextView TextViewEqual;
    private TextView TextViewCE;
    private TextView TextViewC;
    private TextView TextViewDot;
    private String selectedFunction = "";
    private boolean clickedFunctionTextView = false;
    private boolean clickedEquelTextView = false;
    private boolean error = false;

    public CalculatorONP(Activity contex, List<Integer> numericTextViewsIDs, List<Integer> functionalTextViewsIDs, Configuration config) {
        this.contex = contex;
        this.config = config;
        this.numericTextViewsIDs = numericTextViewsIDs;
        this.functionalTextViewsIDs = functionalTextViewsIDs;
        setTextViews();
        setActionTextViews();
    }

    public void setActionTextViews() {
        setActionOnNumericTextViews();
        setActionOnFunctionalTextViews();
        createListOfOperations();
        setActionOnCTextView();
        setActionOnCETextView();
        setActionOnTextViewEqual();
        setActionPlusMinus();
        setActionDot();
    }

    public void setTextViews() {
        textViewOnEquation = contex.findViewById(R.id.equation);
        textViewOnResult = contex.findViewById(R.id.result);
        TextViewC = contex.findViewById(R.id.bC);
        TextViewCE = contex.findViewById(R.id.bCE);
        TextViewEqual = contex.findViewById(R.id.b_equals);
        TextViewDot = contex.findViewById(R.id.b_);
    }

    private void setActionOnConstTextViews() {
        for (Integer id : constTextViewsIDs) {
            TextView TextView = contex.findViewById(id);
            TextView.setOnClickListener((View v) -> {
                TextView tmp = (TextView) v;
                if (containActionFunctionalSignInTextViewOnEquation() || isEmptyTextViewOnEquation()) {
                    double constValue = 0.0;
                    switch (tmp.getText().toString()) {
                        case "pi": {
                            constValue = Math.PI;
                            break;
                        }
                        case "e": {
                            constValue = Math.E;
                            break;
                        }
                    }
                    textViewOnResult.setText(String.valueOf(constValue));
                } else {
                    Toast.makeText(contex.getApplicationContext(), "Error: You must select Action", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setSpecialTextViewsIDs(List<Integer> specialTextViewsIDs) {
        this.specialTextViewsIDs = specialTextViewsIDs;
        setActionOnSpecialTextViews();
    }

    public void setConstTextViewsIDs(List<Integer> constTextViewsIDs) {
        this.constTextViewsIDs = constTextViewsIDs;
        setActionOnConstTextViews();
    }

    private void setActionDot() {
        TextViewDot.setOnClickListener((View v) -> {
            TextView TextView = (TextView) v;
            int size = getHowManyDigitsIsPossibleDisplayOnTextViewOnResult();
            System.out.println("setActionDot");
            if (!String.valueOf(textViewOnResult.getText()).contains(".") && textViewOnResult.getText().length() > 0) {
                setTextOnTextViewOnResultFromTextView(TextView);
                System.out.println("yes");
            } else {
                System.out.println("no");
                if (textViewOnResult.getText().length() >= size) {
                    System.out.println("yes");
                }
            }
        });
    }

    private void setActionOnTextViewEqual() {
        TextViewEqual.setOnClickListener((View v) -> {
            TextView TextView = (TextView) v;
            clickedEquelTextView = true;
            System.out.println("setActionOnTextViewEqual");
            double value = 0.0;
            if (isEmptyTextViewOnResult()) {
                if (isEmptyTextViewOnEquation()) {
		/*
			TextViewOnEquation = ""
			TextViewOnResult = ""
		*/
                    System.out.println("isEmptyTextViewOnResult isEmptyTextViewOnEquation");
                    textViewOnEquation.setText("0");
                    if (error) {
                        TextViewCE.callOnClick();
                        error = false;
                    }
		/*
			TextViewOnEquation = "0"
			TextViewOnResult = ""
		*/
                } else {
                    if (containOperationInTextViewOnEquation()) {
			/*
				TextViewOnEquation = "5.0/"
				TextViewOnResult = ""
			*/
                        System.out.println("isEmptyTextViewOnResult !isEmptyTextViewOnEquation containOperationInTextViewOnEquation");
                        textViewOnResult.setText("0");
                        textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, textViewOnResult));
                        value = calculate();
                        verificationError(value);
                        if (error) {
                            TextViewCE.callOnClick();
                            error = false;
                        }
                    } else {
			/*
				TextViewOnEquation = "5.0"
				TextViewOnResult = ""
			*/
                        System.out.println("isEmptyTextViewOnResult !isEmptyTextViewOnEquation !containOperationInTextViewOnEquation");
                        //textViewOnResult.setText(textViewOnEquation.getText());
                        if (error) {
                            TextViewCE.callOnClick();
                            error = false;
                        }
			/*
				TextViewOnEquation = "5.0"
				TextViewOnResult = ""
			*/
                    }
                }
            } else {
                if (isEmptyTextViewOnEquation()) {
                    int last = textViewOnResult.length();
                    if (lastCharInTextViewOnResultIsDot()) {
                        /*
				            TextViewOnEquation = ""
				            TextViewOnResult = "2."
			            */
                        System.out.println("!isEmptyTextViewOnResult isEmptyTextViewOnEquation lastCharInTextViewOnResultIsDot");
                        textViewOnEquation.setText(connectTextWithTextViews(textViewOnResult.getText().toString(), "0"));
                        clearTextViewOnResult();
                        if (error) {
                            TextViewCE.callOnClick();
                            error = false;
                        }
                        /*
				            TextViewOnEquation = "2.0"
				            TextViewOnResult = ""
			            */
                    } else {
			            /*
				            TextViewOnEquation = ""
				            TextViewOnResult = "2.0"
			            */
                        System.out.println("!isEmptyTextViewOnResult isEmptyTextViewOnEquation !lastCharInTextViewOnResultIsDot");
                        textViewOnEquation.setText(textViewOnResult.getText());
                        clearTextViewOnResult();
                        if (error) {
                            TextViewCE.callOnClick();
                            error = false;
                        }
			/*
				TextViewOnEquation = "2.0"
				TextViewOnResult = ""
			*/
                    }
                } else {
                    if (containCharOperationInTextViewOnEquation()) {
			/*
				TextViewOnEquation = "5.0/"
				TextViewOnResult = "2.0"
			*/
                        System.out.println("!isEmptyTextViewOnResult !isEmptyTextViewOnEquation containCharOperationInTextViewOnEquation");
                        textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, textViewOnResult));
                        value = calculate();
                        verificationError(value);
                        textViewOnEquation.setText(String.valueOf(value));
                        textViewOnResult.setText("");
                        if (error) {
                            TextViewCE.callOnClick();
                            error = false;
                        }
			/*
				TextViewOnEquation = "5.0/2.0"
				TextViewOnResult = "2.5"
			*/
                    } else {
                        /*
                        TextViewOnEquation = "5.0"
                        TextViewOnResult = "2.0"
                        */
                        System.out.println("!isEmptyTextViewOnResult !isEmptyTextViewOnEquation !containCharOperationInTextViewOnEquation");
                        textViewOnEquation.setText(textViewOnResult.getText());
                        textViewOnResult.setText("");
                        if (error) {
                            TextViewCE.callOnClick();
                            error = false;
                        }
                        /*
                        TextViewOnEquation = "2.0"
                        TextViewOnResult = ""
                        */
                    }
                }
            }
        });
    }

    private void setActionOnNumericTextViews() {
        for (Integer id : numericTextViewsIDs) {
            TextView tmp = contex.findViewById(id);
            tmp.setOnClickListener((View v) -> {
                TextView tmpTextView = (TextView) v;
                System.out.println("setActionOnNumericTextViews: " + tmpTextView.getText());
                int size = getHowManyDigitsIsPossibleDisplayOnTextViewOnResult();
                if (clickedFunctionTextView) {
                    clearTextViewOnResult();
                    clickedFunctionTextView = false;
                }
                if (textViewOnResult.getText().length() < size) {
                    if ("0".equals(tmpTextView.getText().toString())) {
                        if (textViewOnResult.getText().length() >= 1 && textViewOnResult.getText().toString().charAt(0) != '0' || isEmptyTextViewOnResult() || textViewOnResult.getText().toString().contains("0.")) {
                            setTextOnTextViewOnResultFromTextView(tmpTextView);
                        }
                    } else {
                        if (textViewOnResult.getText().toString().equals("0")) {
                            clearTextViewOnResult();
                        }
                        setTextOnTextViewOnResultFromTextView(tmpTextView);
                    }

                }
            });
        }
    }

    private void setActionOnFunctionalTextViews() {
        for (Integer id : functionalTextViewsIDs) {
            TextView tmp = contex.findViewById(id);
            tmp.setOnClickListener((View v) -> {
                TextView tmpTextView = (TextView) v;
                double value = 0.0;
                System.out.println("setActionOnFunctionalTextViews");
                if (isEmptyTextViewOnEquation()) {
                    if (isEmptyTextViewOnResult()) {
                    /*
                        TextViewOnEquation = ""
                        TextViewOnResult = ""
                        znakFunkcyjny = "*"
                    */
                        System.out.println("isEmptyTextViewOnEquation isEmptyTextViewOnResult");
                        textViewOnEquation.setText(connectTextWithTextViews("0", tmpTextView));
                        if (error) {
                            TextViewCE.callOnClick();
                            error = false;
                        }
                        /*
                        textViewOnResult.setText("0");
                        textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, textViewOnResult));
                        value = calculate();
                        verificationError(value);
                        */
                    } else {
                        if (lastCharInTextViewOnResultIsDot()) {
                            /*
                                TextViewOnEquation = ""
                                TextViewOnResult = "2."
                                znakFunkcyjny = "*"
                            */
                            System.out.println("isEmptyTextViewOnEquation !isEmptyTextViewOnResult lastCharInTextViewOnResultIsDot ");
                            textViewOnEquation.setText(connectTextWithTextViews(textViewOnResult, "0"));
                            textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, tmpTextView));
                            textViewOnResult.setText("");
                            if (error) {
                                TextViewCE.callOnClick();
                                error = false;
                            }
                                /*
                                TextViewOnEquation = "2.0*"
                                TextViewOnResult = ""
                                znakFunkcyjny = "*"
                                */
                        } else {
                            /*
                                TextViewOnEquation = ""
                                TextViewOnResult = "2.0"
                                znakFunkcyjny = "*"
                            */
                            System.out.println("isEmptyTextViewOnEquation !isEmptyTextViewOnResult !lastCharInTextViewOnResultIsDot ");
                            textViewOnEquation.setText(connectTextWithTextViews(textViewOnResult, tmpTextView));
                            textViewOnResult.setText("");
                            System.out.println(textViewOnEquation.getText().toString());
                            if (error) {
                                TextViewCE.callOnClick();
                                error = false;
                            }
                            /*
                                TextViewOnEquation = "2.0*"
                                TextViewOnResult = ""
                            */
                        }
                    }
                } else {
                    if (containCharOperationInTextViewOnEquation()) {
                        if (isEmptyTextViewOnResult()) {
                        /*
                            TextViewOnEquation = "32.0/"
                            TextViewOnResult = ""
                            znakFunkcyjny = "*"
                        */
                            System.out.println("!isEmptyTextViewOnEquation containOperationInTextViewOnEquation isEmptyTextViewOnResult");
                            textViewOnEquation.setText(textViewOnEquation.getText().subSequence(0, textViewOnEquation.length() - 1));
                            textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, tmpTextView));
                            if (error) {
                                TextViewCE.callOnClick();
                                error = false;
                            }
                            /*
                            TextViewOnEquation = "32.0*"
                            TextViewOnResult = "0.0"
                        */
                        } else {
                        /*
                            TextViewOnEquation = "32.0/"
                            TextViewOnResult = "3.0"
                        */
                            if (clickedEquelTextView) {
                                System.out.println("!isEmptyTextViewOnEquation containOperationInTextViewOnEquation !isEmptyTextViewOnResult clickedEquelTextView");
                                textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, textViewOnResult));
                                value = calculate();
                                verificationError(value);
                                textViewOnEquation.setText(connectTextWithTextViews(String.valueOf(value), tmpTextView));
                                textViewOnResult.setText("");
                                clickedEquelTextView = false;
                                if (error) {
                                    TextViewCE.callOnClick();
                                    error = false;
                                }
                            } else {
                                System.out.println("!isEmptyTextViewOnEquation containOperationInTextViewOnEquation !isEmptyTextViewOnResult !clickedEquelTextView");
                                textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, textViewOnResult));
                                value = calculate();
                                verificationError(value);
                                textViewOnEquation.setText(connectTextWithTextViews(String.valueOf(value), tmpTextView));
                                textViewOnResult.setText("");
                                if (error) {
                                    TextViewCE.callOnClick();
                                    error = false;
                                }

                            }
                        }
                    } else {
                        if (isEmptyTextViewOnResult()) {
                            if (lastCharInTextViewOnResultIsDot()) {
                            /*
                                TextViewOnEquation = "32."
                                TextViewOnResult = ""
                                znakFunkcyjny = "*"
                            */
                                System.out.println("!isEmptyTextViewOnEquation !containOperationInTextViewOnEquation isEmptyTextViewOnResult lastCharInTextViewOnResultIsDot");
                                textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, "0"));
                                textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, tmpTextView));
                                textViewOnResult.setText("");
                                if (error) {
                                    TextViewCE.callOnClick();
                                    error = false;
                                }
                                /*
                                TextViewOnEquation = "32.0*"
                                TextViewOnResult = ""
                                znakFunkcyjny = "*"
                                */
                            } else {
                            /*
                                TextViewOnEquation = "32.0"
                                TextViewOnResult = ""
                                znakFunkcyjny = "*"
                            */
                                System.out.println("!isEmptyTextViewOnEquation !containOperationInTextViewOnEquation isEmptyTextViewOnResult");
                                textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, tmpTextView));
                                textViewOnResult.setText("");
                                if (error) {
                                    TextViewCE.callOnClick();
                                    error = false;
                                }
			/*
				TextViewOnEquation = "32.0*"
				TextViewOnResult = ""
			*/
                            }
                        } else {
			/*
				TextViewOnEquation = "32.0"
				TextViewOnResult = "5.0"
				znakFunkcyjny = "*"
			*/
                            System.out.println("!isEmptyTextViewOnEquation !containOperationInTextViewOnEquation !isEmptyTextViewOnResult");
                            textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, tmpTextView));
                            System.out.println(textViewOnEquation.getText().toString());
                            textViewOnEquation.setText(connectTextWithTextViews(textViewOnEquation, textViewOnResult));
                            System.out.println(textViewOnEquation.getText().toString());
                            value = calculate();
                            System.out.println(textViewOnEquation.getText().toString());
                            verificationError(value);
                            if (error) {
                                TextViewCE.callOnClick();
                                error = false;
                            }
			/*
				TextViewOnEquation = "32.0*5.0"
				TextViewOnResult = "160.0"
				znakFunkcyjny = "*"
			*/
                        }
                    }
                }
                selectedFunction = tmpTextView.getText().toString();
            });
        }

    }

    private int getHowManyDigitsIsPossibleDisplayOnTextViewOnResult() {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 17;
        } else {
            return 33;
        }
    }

    private void setTextOnTextViewOnResultFromTextView(TextView TextView) {
        System.out.println("setTextOnTextViewOnResultFromTextView(" + TextView.getText() + ")");
        String sb = String.valueOf(textViewOnResult.getText()) +
                TextView.getText();
        textViewOnResult.setText(sb);
    }

    private void setActionOnCETextView() {
        TextViewCE.setOnClickListener((View v) -> {
            System.out.println("setActionOnCETextView");
            clearTextViewOnResult();
            clearTextViewOnEquation();

        });
    }

    private void setActionOnCTextView() {
        TextViewC.setOnClickListener((View v) -> {
            System.out.println("setActionOnCTextView");
            StringBuilder sb = new StringBuilder();
            if (isEmptyTextViewOnResult() && containActionFunctionalSignInTextViewOnEquation()) {
                sb.append(textViewOnEquation.getText().subSequence(0, textViewOnEquation.getText().length() - 1));
                textViewOnEquation.setText(sb.toString());
            } else {
                if (!isEmptyTextViewOnResult()) {
                    sb.append(textViewOnResult.getText().subSequence(0, textViewOnResult.getText().length() - 1));
                    textViewOnResult.setText(sb.toString());
                } else {

                    clearTextViewOnEquation();
                    clickedEquelTextView = false;
                }
            }
            if (textViewOnResult.getText().length() < getHowManyDigitsIsPossibleDisplayOnTextViewOnResult()) {
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

    private String fitNumberToNumbersOfPlaceOnTextViewOnResult(int AllDigitsOnScreen,
                                                               double number) {
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
        if (!isEmptyTextViewOnResult()) {
            String numberFromScreen = textViewOnResult.getText().toString();
            if (numberFromScreen.charAt(numberFromScreen.length() - 1) == '.') {
                int lengthNumberOnScreen = numberFromScreen.length();
                char lastChar = numberFromScreen.charAt(lengthNumberOnScreen - 1);
                if (lastChar == '.') {
                    String sb = String.valueOf(textViewOnResult.getText()) +
                            0;
                    textViewOnResult.setText(sb);
                }
            }
        }
    }

    private String replaceComasOnDotsInText(String text) {
        String resultText = text;
        if (text.contains(",")) {
            resultText = text.replaceAll(",", "");
        }
        return resultText;
    }

    private String convertStringOfNumberWithENotationToNormal(String number) {
        System.out.println("convertStringOfNumberWithENotationToNormal(" + number + ")");
        Double d = null;
        String toReturn = replaceComasOnDotsInText(number);
        try {
            d = Double.parseDouble(toReturn);
        } catch (NumberFormatException e) {
            return number;
        }
        System.out.println("convertStringOfNumberWithENotationToNormal( return" + d.toString() + ")");
        if (d > 99999999999999.0) {
            return parseToCientificNotation(d);
        }

        return d.toString();
    }

    private static String parseToCientificNotation(double value) {
        int cont = 0;
        java.text.DecimalFormat DECIMAL_FORMATER = new java.text.DecimalFormat("0.##");
        while (((int) value) != 0) {
            value /= 10;
            cont++;
        }
        return DECIMAL_FORMATER.format(value).replace(",", ".") + "E" + cont;
    }

    private void addTextFromTextViewToTextViewOnEquation(TextView TextView) {
        System.out.println("addTextFromTextViewToTextViewOnEquation( " + TextView.getText() + " )");
        String sb = String.valueOf(textViewOnEquation.getText()) +
                TextView.getText();
        textViewOnEquation.setText(sb);

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
        for (Integer item : functionalTextViewsIDs) {
            TextView TextView = contex.findViewById(item);
            if (!equation.isEmpty()) {
                if (equation.charAt(equation.length() - 1) == TextView.getText().toString().charAt(0)) {
                    return true;
                }
            }
        }
        return equation.contains(("="));
    }

    private boolean containNegativeNumbersInTextViewOnEquation() {
        System.out.println("containNegativeNumbersInTextViewOnEquation");
        String equation = textViewOnEquation.getText().toString();

        if (!equation.isEmpty()) {
            return equation.contains("-");
        }

        return false;
    }

    private boolean containNegativeNumbersInTextViewOnResult() {
        System.out.println("containNegativeNumbersInTextViewOnResult");
        String equation = textViewOnResult.getText().toString();
        if (!equation.isEmpty()) {
            return equation.contains("-");
        }
        return false;
    }

    private void setActionOnSpecialTextViews() {
        for (Integer item : specialTextViewsIDs) {
            TextView TextView = contex.findViewById(item);
            TextView.setOnClickListener((View v) -> {
                if (!isEmptyTextViewOnResult() || !isEmptyTextViewOnEquation()) {
                    TextView b = (TextView) v;
                    System.out.println("setActionOnSpecialTextViews( " + b.getText() + " )");
                    String action = b.getText().toString();
                    String number = "";
                    double numberD = 0.0;
                    if (!isEmptyTextViewOnResult()) {
                        number = textViewOnResult.getText().toString();
                        number = convertStringOfNumberWithENotationToNormal(number);
                        numberD = Double.parseDouble(number);
                    } else {
                        if (containCharOperationInTextViewOnEquation()) {
                            TextViewC.callOnClick();
                        }
                        number = textViewOnEquation.getText().toString();
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
                            if (numberD == 0.0) {
                                Toast.makeText(contex.getApplicationContext(), "Error: Negative Infinite", Toast.LENGTH_SHORT).show();
                                error = true;
                            } else {
                                if (numberD > 0.0) {
                                    numberD = Math.log(numberD);
                                } else {
                                    Toast.makeText(contex.getApplicationContext(), "Error: Natural logar not exist for negative Number", Toast.LENGTH_SHORT).show();
                                    error = true;
                                }
                            }
                            break;
                        }
                        case "sqrt": {
                            if (numberD >= 0.0) {
                                numberD = Math.sqrt(numberD);
                            } else {
                                Toast.makeText(contex.getApplicationContext(), "Error: Square root from negative number", Toast.LENGTH_SHORT).show();
                                error = true;
                            }
                            break;
                        }
                    }
                    textViewOnResult.setText(convertStringOfNumberWithENotationToNormal(fitNumberToNumbersOfPlaceOnTextViewOnResult(getHowManyDigitsIsPossibleDisplayOnTextViewOnResult(), numberD)));
                } else {
                    Toast.makeText(contex.getApplicationContext(), "Error: You must select Action", Toast.LENGTH_SHORT).show();
                    error = true;
                }
            });
        }
    }

    private boolean textViewOnResultContainDot() {
        return textViewOnResult.getText().toString().contains(".");
    }


    private void setActionPlusMinus() {
        TextView TextView = contex.findViewById(R.id.b_plus_minus);
        TextView.setOnClickListener((View v) -> {
            TextView tmp = (TextView) v;
            if (!isEmptyTextViewOnResult()) {
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

    private double negativeNumberCalculator() throws Exception {
        System.out.println("negativeNumberCalculator");
        addZeroAfterDotInTextViewOnResult();
        String diff = selectedFunction;
        String[] tab;
        String equation = textViewOnEquation.getText().toString();
        String result;
        if (equation.contains("^")) {
            diff = "^";
        }
        System.out.println("Diff= " + diff);
        if (countCharInString(equation, '-') == 3) {
            int[] tabMinus = getPositionSignInString(equation, '-');
            result = equation.substring(tabMinus[1] + 1);
            equation = equation.substring(0, tabMinus[1]);
        } else if ((countCharInString(equation, '-') == 2)) {
            equation = equation.replace("--", "+");
            diff = "+";
            System.out.println(equation);
            String[] array = manualSplit(equation, diff.charAt(0));
            equation = array[0];
            result = array[1];

        } else {
            System.out.println(equation);
            String[] array = manualSplit(equation, diff.charAt(0));
            equation = array[0];
            result = array[1];
        }
        System.out.println(equation);
        System.out.println(result);
        Double a = Double.parseDouble(equation);
        Double b = Double.parseDouble(result);

        System.out.println("Equation : " + a + " " + diff + " " + b);

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

    private String[] manualSplit(String source, char diff) {
        StringBuilder sb = new StringBuilder();
        String[] tab = new String[2];
        int start = 0;
        for (int i = 0; i < source.length(); i++) {
            start = i;
            if (source.charAt((i)) == diff) {
                break;
            }
            sb.append(source.charAt(i));
        }
        tab[0] = sb.toString();
        sb = new StringBuilder();
        for (int i = start + 1; i < source.length(); i++) {
            if (source.charAt((i)) == diff) {
                break;
            }
            sb.append(source.charAt(i));
        }
        tab[1] = sb.toString();
        return tab;
    }

    private int countCharInString(String text, char sign) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == sign) {
                count++;
            }
        }
        return count;
    }

    private int[] getPositionSignInString(String text, char sign) {
        int[] tab = {0, 0, 0};
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == sign) {
                tab[index] = i;
                index++;
            }
        }
        return tab;
    }

    public void setContex(Activity contex) {
        this.contex = contex;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setNumericTextViewsIDs(List<Integer> numericTextViewsIDs) {
        this.numericTextViewsIDs = numericTextViewsIDs;
    }

    public void setFunctionalTextViewsIDs(List<Integer> functionalTextViewsIDs) {
        this.functionalTextViewsIDs = functionalTextViewsIDs;
    }

    private void verificationError(Double number) {
        if (Double.isNaN(number)) {
            Toast.makeText(contex.getApplicationContext(), "Error: Nan Value!", Toast.LENGTH_SHORT).show();
            error = true;
        } else if (Double.isInfinite(number)) {
            if (number > 0) {
                Toast.makeText(contex.getApplicationContext(), "Error: Positive Infinite!", Toast.LENGTH_SHORT).show();
                error = true;
            } else {
                Toast.makeText(contex.getApplicationContext(), "Error: Negative Infinite!", Toast.LENGTH_SHORT).show();
                error = true;
            }
        } else {
            System.out.println(number);
            String resultS = fitNumberToNumbersOfPlaceOnTextViewOnResult(getHowManyDigitsIsPossibleDisplayOnTextViewOnResult(), number);
            textViewOnResult.setText(convertStringOfNumberWithENotationToNormal(resultS));
        }
    }

    private void createListOfOperations() {
        listOfOperationsString = new ArrayList<>();
        for (Integer item : functionalTextViewsIDs) {
            TextView TextView = contex.findViewById(item);
            listOfOperationsString.add(TextView.getText().toString());
        }
        listOfOperationsString.add("^");
    }

    private boolean containOperationInTextViewOnEquation() {
        int last = textViewOnEquation.length();
        String probablyOperation;
        if (last >= 2) {
            probablyOperation = textViewOnEquation.getText().subSequence(last - 2, last - 1).toString();
            return listOfOperationsString.contains(probablyOperation);
        } else {
            return false;
        }

    }

    private boolean containCharOperationInTextViewOnEquation() {
        for (int i = 1; i < textViewOnEquation.length(); i++) {
            String tmp = String.valueOf(textViewOnEquation.getText().charAt(i));
            if (listOfOperationsString.contains(tmp)) {
                return true;
            }
        }
        return false;
    }

    private double calculate() {
        double result = 0.0;
        if (!isEmptyTextViewOnEquation() && containNegativeNumbersInTextViewOnEquation() || containNegativeNumbersInTextViewOnResult()) {
            try {
                result = negativeNumberCalculator();
            } catch (Exception e) {
                Toast.makeText(contex.getApplicationContext(), "Error: Syntax Error!", Toast.LENGTH_SHORT).show();
                TextViewCE.callOnClick();
                result = 0.0;
            }
        } else {
            try {
                ONP onp = new ONP(String.valueOf(textViewOnEquation.getText()));
                result = onp.oblicz();
            } catch (Exception e) {
                Toast.makeText(contex.getApplicationContext(), "Error: Syntax Error!", Toast.LENGTH_SHORT).show();
                TextViewCE.callOnClick();
                result = 0.0;
            }
        }
        return result;
    }

    private String connectTextWithTextViews(TextView one, TextView two) {
        return String.valueOf(one.getText()) +
                two.getText();
    }

    private String connectTextWithTextViews(String one, TextView two) {
        return String.valueOf(one) +
                two.getText();
    }

    private String connectTextWithTextViews(TextView one, String two) {
        return one.getText().toString() +
                two;
    }

    private String connectTextWithTextViews(String one, String two) {
        return String.valueOf(one) +
                two;
    }

    private boolean canAddZeroToTextViewOnResult() {
        if (isEmptyTextViewOnResult()) return true;
        else {
            String textFromResult = textViewOnResult.getText().toString();
            return textFromResult.charAt(0) != '0';
        }
    }

    private boolean lastCharInTextViewOnResultIsDot() {
        int last = textViewOnResult.length();
        return textViewOnResult.getText().toString().charAt(last - 1) == '.';
    }

}

