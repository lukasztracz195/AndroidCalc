package com.example.androidcalc;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

public class AdvancedCalculator extends Activity {

    private TextView textViewOnEquation;
    private TextView textViewOnResult;
    private List<Integer> numericButtonsIds = Arrays.asList(R.id.b0, R.id.b1, R.id.b2, R.id.b3,
            R.id.b4, R.id.b5, R.id.b6, R.id.b7,
            R.id.b8, R.id.b9);
    private List<Integer> functionalButtonsIds = Arrays.asList(R.id.b_add, R.id.b_substract,
            R.id.b_multiply, R.id.b_diff,R.id.pow);
    private List<Integer> specialButtonsIds = Arrays.asList(R.id.sin,
            R.id.cos, R.id.tan, R.id.sqrt, R.id.ln);

    private List<Integer> constButtonsIds = Arrays.asList(R.id.pi, R.id.e);
    private CalculatorONP calc;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMode.setFullscreen(this);
        Configuration config = getResources().getConfiguration();
        onConfigurationChanged(getResources().getConfiguration());
        textViewOnEquation = findViewById(R.id.equation);
        textViewOnResult = findViewById(R.id.result);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            textViewOnEquation.setText(savedInstanceState.getCharSequence("equation"));
            textViewOnResult.setText(savedInstanceState.getCharSequence("result"));
            if(savedInstanceState.containsKey("onpCalcAdw")) {
                calc = (CalculatorONP) savedInstanceState.getSerializable("onpCalcAdw");
                calc.setContex(this);
                calc.setConfig(config);
                calc.setNumericTextViewsIDs(numericButtonsIds);
                calc.setFunctionalTextViewsIDs(functionalButtonsIds);
                calc.setSpecialTextViewsIDs(specialButtonsIds);
                calc.setConstTextViewsIDs(constButtonsIds);
                calc.setTextViews();
                calc.setActionTextViews();
            }
        }else{
            calc = new CalculatorONP(this, numericButtonsIds, functionalButtonsIds,config);
            calc.setContex(this);
            calc.setConfig(config);
            calc.setNumericTextViewsIDs(numericButtonsIds);
            calc.setFunctionalTextViewsIDs(functionalButtonsIds);
            calc.setSpecialTextViewsIDs(specialButtonsIds);
            calc.setConstTextViewsIDs(constButtonsIds);
            calc.setTextViews();
            calc.setActionTextViews();
        }
        setBackAction();
        setChangeCalculatorOnSimpleAction();

    }




    public void onConfigurationChanged(Configuration newConfig) {
        DisplayMode.setViewByOrintation(this, newConfig, R.layout.advanced_portraint, R.layout.advanced_landscape);
    }


    private void setBackAction() {
        View buttonBack = findViewById(R.id.back);
        String value = buttonBack.getTransitionName();
        System.out.println("buttonBack: " + value);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvancedCalculator.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setChangeCalculatorOnSimpleAction() {
        View buttonSimple = findViewById(R.id.simple);
        buttonSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvancedCalculator.this, SimpleCalculator.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("onpCalcAdw",calc);
        outState.putCharSequence("equation", textViewOnEquation.getText());
        outState.putCharSequence("result", textViewOnResult.getText());
        super.onSaveInstanceState(outState);
    }
}

