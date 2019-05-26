package com.example.androidcalc;
/*
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Calculator {
    private Activity contex;
    private TextView equation;
    private TextView result;
    private List<Integer> numericButtons;
    private List<Integer> functionalButtons;
    private Double value;
    private Double factor;
    private StringBuilder sb;
    private Button buttonEql;
    private Button buttonCE;
    private Button buttonC;
    private Button buttonDot;
    private boolean alowOnInputNumericButtons = true;

    public Calculator(Activity contex, TextView equation, TextView result, List<Integer> numericButtons, List<Integer> functionalButtons) {
        this.contex = contex;
        this.equation = equation;
        this.result = result;
        this.numericButtons = numericButtons;
        this.functionalButtons = functionalButtons;
        sb = new StringBuilder();
        equation.setText("");
        result.setText("");
        buttonCE = contex.findViewById(R.id.bCE);
        buttonC = contex.findViewById(R.id.bC);
        buttonEql = contex.findViewById(R.id.b_equals);
        buttonDot = contex.findViewById(R.id.b_);
        setFuctionalAction();
        setNumericActionButtons();
    }

    private void setNumericButtonsActions() {
        for (Integer item : numericButtons) {
            Button button = contex.findViewById(item);
            button.setOnClickListener((View v) -> {
                TextView tmp = (TextView) v;
                switch(tmp.getText()){
                    case "/":{

                        break;
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append(result.getText());

                if (sb.toString().length() < 22) {
                    sb.append(tmp.getText());
                } else {
                    Toast.makeText(contex.getApplicationContext(), "Error: OverideBuffer!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setCEAction() {
        buttonCE.setOnClickListener((View v) -> {
            result.setText("");
            equation.setText("");
        });
    }

    private void setCAction() {
        buttonC.setOnClickListener((View v) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(result.getText().subSequence(0, result.getText().length() - 1));
            result.setText(sb.toString());

        });
    }

    private void disableFunctionButtons() {
        for (Integer item : functionalButtons) {
            Button button = contex.findViewById(item);
            button.setEnabled(false);
        }
    }

    private void enableFunctionButtons() {
        for (Integer item : functionalButtons) {
            Button button = contex.findViewById(item);
            button.setEnabled(true);
        }
    }



    private void setFuctionalAction() {
        for (Integer item : functionalButtons) {
            Button button = contex.findViewById(item);
            button.setOnClickListener((View v) -> {
                value = Double.valueOf(String.valueOf(result.getText()));
                Button b = (Button) v;
                StringBuilder tmp = new StringBuilder();
                tmp.append(equation);
                tmp.append(result);
                tmp.append(b.getText());
                disableFunctionButtons();
            });
        }
    }

    private void setNumericActionButtons() {
        for (Integer item : numericButtons) {
            Button button = contex.findViewById(item);
            button.setOnClickListener((View v) -> {
                Button b = (Button) v;
                StringBuilder tmp = new StringBuilder();
                tmp.append(result.getText());
                tmp.append(b.getText());
                result.setText(tmp.toString());
                sb = tmp;
                enableFunctionButtons();
            });
        }
    }
}
*/