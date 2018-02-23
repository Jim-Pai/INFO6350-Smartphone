package com.example.jim_pai.neufirebase;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jim_Pai on 2018/2/22.
 */

public class GenericTextWatcher implements TextWatcher {

    View v;

    public GenericTextWatcher(View v) {
        this.v = v;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        clearErrorMessage(v);
    }

    public void clearErrorMessage(View view) {
        ((TextView)view).setText("Welcome!");
        ((TextView)view).setTextColor(Color.GRAY);
    }
}
