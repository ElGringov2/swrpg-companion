package com.dragonrider.swrpgcompanion.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by mge637 on 05/08/2015. Editeur d'objet générique
 */
public class GenericEditor {



    public interface IOnPopupClosed {
        void OnClosed();
    }

    public static void Show(final Context context, final Object pObject, final IOnPopupClosed onPopupClosed) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);



        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        int value = (int)(64 * App.getContext().getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, value / 2);


        LinearLayout.LayoutParams editViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, value);



        Class myclass = pObject.getClass();


        Field[] fields = myclass.getDeclaredFields();

        for (final Field field : fields) {

            if (field.getType() != int.class) continue;

            TextView textView = new TextView(context);
            textView.setLayoutParams(textViewParams);
            textView.setText(field.getName());

            EditText editText = new EditText(context);
            editText.setLayoutParams(editViewParams);
            field.setAccessible(true);
            try {
                editText.setText(String.valueOf( (int)field.get(pObject)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        field.set(pObject, Integer.valueOf(s.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mainLayout.addView(textView);
            mainLayout.addView(editText);


        }

        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(mainLayout);


        builder.setView(scrollView);


        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onPopupClosed.OnClosed();
            }
        });

        builder.show();

    }


}
