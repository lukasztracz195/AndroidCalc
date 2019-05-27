package com.example.androidcalc;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class SimpleCalculator extends Activity {

    private TextView textViewOnEquation;
    private TextView textViewOnResult;
    private Configuration config;

    private List<Integer> numericButtonsIds = Arrays.asList(R.id.b0, R.id.b1, R.id.b2, R.id.b3,
            R.id.b4, R.id.b5, R.id.b6, R.id.b7,
            R.id.b8, R.id.b9, R.id.b_);
    private List<Integer> functionalButtonsIds = Arrays.asList(R.id.b_add, R.id.b_substract,
            R.id.b_multiply, R.id.b_diff);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMode.setFullscreen(this);
        config = getResources().getConfiguration();
        onConfigurationChanged(getResources().getConfiguration());
        textViewOnEquation = findViewById(R.id.equation);
        textViewOnResult = findViewById(R.id.result);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            textViewOnEquation.setText(savedInstanceState.getCharSequence("equation"));
            textViewOnResult.setText(savedInstanceState.getCharSequence("result"));
        }
        setBackAction();
        CalculatorONP calc = new CalculatorONP(this, numericButtonsIds, functionalButtonsIds,config);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DisplayMode.setViewByOrintation(this, newConfig, R.layout.simple_portraint, R.layout.simple_landscape);
        setChangeCalculatorOnAdvancedAction();
    }



    private void setBackAction() {
        View buttonBack = findViewById(R.id.back);
        buttonBack.setOnClickListener((View v)-> {
                Intent intent = new Intent(SimpleCalculator.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        });
    }

    private void setChangeCalculatorOnAdvancedAction() {
        View buttonBack = findViewById(R.id.advanced);
        buttonBack.setOnClickListener((View v) -> {
            Intent intent = new Intent(SimpleCalculator.this, AdvancedCalculator.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence("equation", textViewOnEquation.getText());
        outState.putCharSequence("result", textViewOnResult.getText());
        super.onSaveInstanceState(outState);
    }




}


