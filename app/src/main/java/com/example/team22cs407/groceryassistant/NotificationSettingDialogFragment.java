package com.example.team22cs407.groceryassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by yuanyuanji on 3/21/18.
 */

public class NotificationSettingDialogFragment extends DialogFragment implements TimePickerFragment.TimePickerFragmentListener {
    private int closeDays = 2;
    private int hourOfDay = 16;
    private int minute = 0;
    private NotificationSettingDialogListener notificationSettingDialogListener;

    // this listener only for positive click
    public interface NotificationSettingDialogListener{
         void cancelCurrentTimerTask();

         // create a new timer task with arguments and replace current one.
         void updateCurrentTimerTask(int hourOfDay, int minute);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_notification_setting, null);

        onAttachToParentFragment(getTargetFragment());

        final CheckBox checkBox = view.findViewById(R.id.notification_setting_checkbox);

        final NumberPicker closeInDays = view.findViewById(R.id.notification_setting_closeInDays);
        closeInDays.setMinValue(0);
        closeInDays.setMaxValue(12);
        closeInDays.setValue(closeDays);
        closeInDays.setWrapSelectorWheel(true);
        closeInDays.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                closeDays = newVal;
            }
        });

        Button notificationSettingTime = view.findViewById(R.id.notification_setting_time);

        notificationSettingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.setTargetFragment(NotificationSettingDialogFragment.this, 0);
                newFragment.show(getFragmentManager(),"TimePickerFragment");
            }
        });


        builder.setView(view)
                .setTitle("Notification Setting")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // how to get closeInDays and time to run a new TimerTask
                        System.out.println("checkbox value: " + checkBox.isChecked());
                        System.out.println("selected close in days:" + closeDays);
                        System.out.println("I am in Notification setting fragment, save button: hour: " + hourOfDay);
                        System.out.println("I am in Notification setting fragment: save button: " + minute);
                        // sending data back to myGrocery
                        ((MainActivity) getActivity()).cancelTimerTask();
                        if (!checkBox.isChecked()) { // we assume user give different time

                            updateCurrentTimerTask(hourOfDay, minute);
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("I am in Negative: " + i);
                    }
                });
        return builder.create();
    }


    @Override
    public void onTimeSetDone(int hourOfDay, int minute) {

        this.hourOfDay = hourOfDay;
        this.minute = minute;

    }

    public void onAttachToParentFragment(Fragment parentFragment) {
        if (parentFragment != null) {
            try {
                notificationSettingDialogListener = (NotificationSettingDialogListener) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString() + " must implement NotificationSettingDialogListener interface.");
            }
        }
    }

    public void updateCurrentTimerTask(int hourOfDay, int minute) {
        CheckCloseExpiredTimerTask newTimerTask = new CheckCloseExpiredTimerTask(getContext(), closeDays, hourOfDay, minute);
        System.out.println("I am here");
        //MainActivity.scheduleTask(newTimerTask);
        ((MainActivity) getActivity()).scheduleTask(newTimerTask);
    }
}
