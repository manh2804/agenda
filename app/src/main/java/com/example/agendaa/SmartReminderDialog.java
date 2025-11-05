package com.example.agendaa;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;

public class SmartReminderDialog extends Dialog {

    private TextView tvReminderText;
    private Button btnUnderstood;
    private String reminderMessage;
    private ReminderDialogListener listener;

    public interface ReminderDialogListener {
        void onUnderstood();
    }

    public SmartReminderDialog(@NonNull Context context, String reminderMessage, ReminderDialogListener listener) {
        super(context);
        this.reminderMessage = reminderMessage;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_smart_reminder);

        // Làm background mờ
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        tvReminderText = findViewById(R.id.tvReminderText);
        btnUnderstood = findViewById(R.id.btnUnderstood);

        // Set reminder message
        if (reminderMessage != null && !reminderMessage.isEmpty()) {
            tvReminderText.setText(reminderMessage);
        }
    }

    private void setupListeners() {
        btnUnderstood.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUnderstood();
            }
            dismiss();
        });
    }
}