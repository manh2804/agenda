package com.example.agendaa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnStatistics, btnTemplates, btnSettings;
    private FloatingActionButton fabAddEvent;
    private LinearLayout btnAgenda, btnStatisticsCard, btnShare, btnTemplatesCard, btnReminder;

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

        // Main menu cards
        btnAgenda = findViewById(R.id.btnAgenda);
        btnStatisticsCard = findViewById(R.id.btnStatisticsCard);
        btnShare = findViewById(R.id.btnShare);
        btnTemplatesCard = findViewById(R.id.btnTemplatesCard);
        btnReminder = findViewById(R.id.btnReminder);
    }

    private void setupEventHandlers() {
        // 1. Lịch trình (Agenda)
        btnAgenda.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgendaActivity.class);
            startActivity(intent);
        });

        // 2. Thống kê - Card
        btnStatisticsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        // 2b. Thống kê - Top bar
        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        // 3. Chia sẻ
        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShareEventActivity.class);
            startActivity(intent);
        });

        // 4. Mẫu sự kiện - Card
        btnTemplatesCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EventTemplateDetailActivity.class);
            startActivity(intent);
        });

        // 4b. Mẫu sự kiện - Top bar
        btnTemplates.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EventTemplateDetailActivity.class);
            startActivity(intent);
        });

        // 5. Nhắc nhở thông minh
        btnReminder.setOnClickListener(v -> {
            showSmartReminder();
        });

        // 6. FAB - Thêm sự kiện
        fabAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
        });

        // 7. Settings
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Cài đặt đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void showSmartReminder() {
        String reminderMessage = "🔔 Nhắc nhở thông minh\n\n" +
                "Bạn có 3 sự kiện sắp diễn ra trong ngày mai:\n\n" +
                "• 08:00 - Họp team Marketing\n" +
                "• 14:00 - Gặp khách hàng\n" +
                "• 18:00 - Tập thể dục\n\n" +
                "Đừng quên chuẩn bị!";

        SmartReminderDialog dialog = new SmartReminderDialog(
                this,
                reminderMessage,
                () -> {
                    Toast.makeText(MainActivity.this, "Đã hiểu!", Toast.LENGTH_SHORT).show();
                }
        );
        dialog.show();
    }
}