package com.il.tikkun.utils;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.NumberPicker;


import java.lang.reflect.Field;
import java.util.Calendar;

import com.il.tikkun.R;

/**
 * Created by David vardi on 7/12/2016.
 */
public class MyDatePicker extends DialogFragment {

    NumberPicker month;

    NumberPicker year;

    MyDatePickerListener mListener;

    private static MyDatePickerListener listener;

    public static MyDatePicker newInstance(MyDatePickerListener listener) {

        MyDatePicker myDatePicker = new MyDatePicker();

        myDatePicker.setListener(listener);

        return myDatePicker;

    }

    public void setListener(MyDatePickerListener listener) {

        mListener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_date, null);



        initView(view);

        return view;
    }

    private void initView(View view) {

        month = (NumberPicker) view.findViewById(R.id.DD_month);

        year = (NumberPicker) view.findViewById(R.id.DD_year);

        setNumberPickerTextColor(month, Color.parseColor("#FF000000"));

        setNumberPickerTextColor(year, Color.parseColor("#FF000000"));

        month.setMinValue(1);

        month.setMaxValue(12);

        month.setDisplayedValues(getMonths());

        int y = Calendar.getInstance().get(Calendar.YEAR);

        year.setMinValue(y - 2000);

        year.setMaxValue(y + 20 - 2000);

        year.setValue(y - 2000);

        month.setWrapSelectorWheel(false);

        year.setWrapSelectorWheel(false);

        view.findViewById(R.id.DD_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (month.getValue() < 10) {

                    mListener.onSetDate("0" + month.getValue() + "/" + (year.getValue()));

                } else {

                    mListener.onSetDate(month.getValue() + "/" + (year.getValue()));

                }


                dismiss();
            }
        });

    }

    public interface MyDatePickerListener {

        void onSetDate(String date);

        void onDismiss();
    }

    private String[] getMonths() {

        return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();

        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                } catch (IllegalAccessException e) {
                } catch (IllegalArgumentException e) {
                }
            }
        }
        return false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        mListener.onDismiss();
    }
}