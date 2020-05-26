package com.example.naidich.tom.tomsprojectapplication.ui.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class TouchableButton extends AppCompatButton {
    public TouchableButton(Context context) {
        super(context);
    }

    @Override
    public boolean performClick() {
        // do what you want
        return true;
    }
}
