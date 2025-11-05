package com.example.agendaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnStatistics, btnTemplates, btnSettings;
    private FloatingActionButton fabAddEvent;
    private LinearLayout btnAgenda, btnShare, btnReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupEventHandlers();
    }

    private void initViews() {
        // Top bar buttons
        btnStatistics = findViewById(R.id.btnStatistics);
        btnTemplates = findViewById(R.id.btnTemplates);
        btnSettings = findViewById(R.id.btnSettings);

        // Floating Action Button
        fabAddEvent = findViewById(R.id.fabAddEvent);

        // Main menu items
        btnAgenda = findViewById(R.id.btnAgenda);
        btnShare = findViewById(R.id.btnShare);
        btnReminder = findViewById(R.id.btnReminder);
    }

    private void setupEventHandlers() {
        // 1. Lịch trình (Agenda)
        btnAgenda.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgendaActivity.class);
            startActivity(intent);
        });

        // 2. Thêm sự kiện - FAB
        fabAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
        });

        // 3. Thống kê và Báo cáo
        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        // 4. Chia sẻ lịch/sự kiện
        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShareEventActivity.class);
            startActivity(intent);
        });

        // 5. Tạo mẫu sự kiện (Event Templates)
        btnTemplates.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EventTemplateDetailActivity.class);
            startActivity(intent);
        });

        // 6. Nhắc nhở thông minh
        btnReminder.setOnClickListener(v -> {
            showSmartReminder();
        });
    }

    private void showSmartReminder() {
        String reminderMessage = "Bạn có 3 sự kiện sắp diễn ra trong ngày mai:\n\n" +
                "• 08:00 - Họp team Marketing\n" +
                "• 14:00 - Gặp khách hàng\n" +
                "• 18:00 - Thể dục";

        SmartReminderDialog dialog = new SmartReminderDialog(
                this,
                reminderMessage,
                () -> {
                    // Callback khi người dùng nhấn "Đã hiểu"
                }
        );
        dialog.show();
    }
}