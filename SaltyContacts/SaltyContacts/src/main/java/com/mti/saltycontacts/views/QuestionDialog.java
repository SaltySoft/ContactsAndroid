package com.mti.saltycontacts.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Antoine on 11/24/13.
 */
public class QuestionDialog extends DialogFragment {

    QuestionHandler _handler;
    String _positive;
    String _negative;
    String _message;

    public QuestionDialog(String message, String positive, String negative, QuestionHandler handler) {
        _message = message;
        _positive = positive;
        _negative = negative;
        _handler = handler;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(_message)
                .setPositiveButton(_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        _handler.positiveAction();
                    }
                })
                .setNegativeButton(_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        _handler.negativeAction();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface QuestionHandler {
        public void positiveAction();
        public void negativeAction();
    }
}