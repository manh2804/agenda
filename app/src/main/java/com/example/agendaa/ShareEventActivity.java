package com.example.agendaa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ShareEventActivity extends AppCompatActivity {

    // UI Elements
    private ImageButton btnBack, btnMenu;
    private TextView tvMonth, tvEventDate, tvEventTitle, tvEventSubtitle;
    private TextView tvEventFullDate, tvEventTime, tvEventLocation;
    private Button btnShareEvent;

    // Event data
    private EventData currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);

        initViews();
        loadEventData();
        setupEventHandlers();
        updateUI();
    }

    private void initViews() {
        // App bar
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);

        // Month
        tvMonth = findViewById(R.id.tvMonth);

        // Event details
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventTitle = findViewById(R.id.tvEventTitle);
        tvEventSubtitle = findViewById(R.id.tvEventSubtitle);
        tvEventFullDate = findViewById(R.id.tvEventFullDate);
        tvEventTime = findViewById(R.id.tvEventTime);
        tvEventLocation = findViewById(R.id.tvEventLocation);

        // Share button
        btnShareEvent = findViewById(R.id.btnShareEvent);
    }

    private void loadEventData() {
        // Get event data from intent or database
        Intent intent = getIntent();

        if (intent.hasExtra("event_data")) {
            // Load from intent
            currentEvent = (EventData) intent.getSerializableExtra("event_data");
        } else {
            // Load sample data
            currentEvent = createSampleEvent();
        }
    }

    private EventData createSampleEvent() {
        EventData event = new EventData();
        event.title = "Họp nhóm Marketing";
        event.description = "Thảo luận về kế hoạch quý 4";
        event.location = "Phòng họp 112 nhà C";

        // Set date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.OCTOBER, 26, 15, 0); // Oct 26, 2025, 3:00 PM
        event.startTime = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 16); // 4:00 PM
        event.endTime = calendar.getTimeInMillis();

        event.category = "Công việc";
        event.priority = "Cao";

        return event;
    }

    private void setupEventHandlers() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Menu button
        btnMenu.setOnClickListener(v -> showMenuOptions());

        // Share button
        btnShareEvent.setOnClickListener(v -> showShareOptions());
    }

    private void updateUI() {
        if (currentEvent == null) return;

        // Update month
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentEvent.startTime);
        SimpleDateFormat monthFormat = new SimpleDateFormat("'Tháng' MM", new Locale("vi", "VN"));
        tvMonth.setText(monthFormat.format(calendar.getTime()));

        // Update event date
        SimpleDateFormat dateFormat = new SimpleDateFormat("'Ngày' dd/MM/yyyy", new Locale("vi", "VN"));
        tvEventDate.setText(dateFormat.format(calendar.getTime()));

        // Update event details
        tvEventTitle.setText(currentEvent.title);
        tvEventSubtitle.setText(currentEvent.description);

        // Update full date
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEEE, 'ngày' dd 'tháng' MM, yyyy", new Locale("vi", "VN"));
        tvEventFullDate.setText(fullDateFormat.format(calendar.getTime()));

        // Update time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(currentEvent.endTime);
        String timeText = timeFormat.format(calendar.getTime()) + " - " + timeFormat.format(endCalendar.getTime());
        tvEventTime.setText(timeText);

        // Update location
        tvEventLocation.setText(currentEvent.location);
    }

    private void showMenuOptions() {
        String[] options = {"Chỉnh sửa sự kiện", "Xóa sự kiện", "Sao chép sự kiện"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tùy chọn")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            editEvent();
                            break;
                        case 1:
                            deleteEvent();
                            break;
                        case 2:
                            copyEvent();
                            break;
                    }
                });
        builder.show();
    }

    private void showShareOptions() {
        String[] shareOptions = {
                "Chia sẻ qua Email",
                "Chia sẻ qua Tin nhắn",
                "Chia sẻ qua WhatsApp",
                "Chia sẻ qua Telegram",
                "Sao chép liên kết",
                "Xuất file ICS"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn cách chia sẻ")
                .setItems(shareOptions, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            shareViaEmail();
                            break;
                        case 1:
                            shareViaSMS();
                            break;
                        case 2:
                            shareViaWhatsApp();
                            break;
                        case 3:
                            shareViaTelegram();
                            break;
                        case 4:
                            copyEventLink();
                            break;
                        case 5:
                            exportToICS();
                            break;
                    }
                });
        builder.show();
    }

    private void shareViaEmail() {
        String subject = "Lời mời tham gia sự kiện: " + currentEvent.title;
        String body = createShareText();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Gửi email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Không có ứng dụng email nào được cài đặt", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaSMS() {
        String message = createShareText();

        Intent smsIntent = new Intent(Intent.ACTION_SEND);
        smsIntent.setType("text/plain");
        smsIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(smsIntent, "Gửi tin nhắn"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Không có ứng dụng tin nhắn nào được cài đặt", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaWhatsApp() {
        String message = createShareText();

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "WhatsApp chưa được cài đặt", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaTelegram() {
        String message = createShareText();

        Intent telegramIntent = new Intent(Intent.ACTION_SEND);
        telegramIntent.setType("text/plain");
        telegramIntent.setPackage("org.telegram.messenger");
        telegramIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(telegramIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Telegram chưa được cài đặt", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyEventLink() {
        String eventLink = "https://agenda.app/event/" + currentEvent.id;

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Event Link", eventLink);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Đã sao chép liên kết sự kiện", Toast.LENGTH_SHORT).show();
    }

    private void exportToICS() {
        // Create ICS file content
        String icsContent = createICSContent();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/calendar");
        shareIntent.putExtra(Intent.EXTRA_TEXT, icsContent);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentEvent.title + ".ics");

        try {
            startActivity(Intent.createChooser(shareIntent, "Xuất file lịch"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Không thể xuất file lịch", Toast.LENGTH_SHORT).show();
        }
    }

    private String createShareText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(currentEvent.startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(currentEvent.endTime);

        StringBuilder shareText = new StringBuilder();
        shareText.append("Lời mời tham gia sự kiện\n\n");
        shareText.append("Tiêu đề: ").append(currentEvent.title).append("\n");
        shareText.append("Mô tả: ").append(currentEvent.description).append("\n");
        shareText.append("Ngày: ").append(dateFormat.format(startCalendar.getTime())).append("\n");
        shareText.append("Thời gian: ")
                .append(timeFormat.format(startCalendar.getTime()))
                .append(" - ")
                .append(timeFormat.format(endCalendar.getTime())).append("\n");
        shareText.append("Địa điểm: ").append(currentEvent.location).append("\n");
        shareText.append("Danh mục: ").append(currentEvent.category).append("\n");
        shareText.append("Ưu tiên: ").append(currentEvent.priority).append("\n\n");

        return shareText.toString();
    }

    private String createICSContent() {
        SimpleDateFormat icsDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.getDefault());

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(currentEvent.startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(currentEvent.endTime);

        StringBuilder ics = new StringBuilder();
        ics.append("BEGIN:VCALENDAR\n");
        ics.append("VERSION:2.0\n");
        ics.append("PRODID:-//Agenda App//Agenda App//EN\n");
        ics.append("BEGIN:VEVENT\n");
        ics.append("UID:").append(currentEvent.id).append("@agenda.app\n");
        ics.append("DTSTART:").append(icsDateFormat.format(startCalendar.getTime())).append("\n");
        ics.append("DTEND:").append(icsDateFormat.format(endCalendar.getTime())).append("\n");
        ics.append("SUMMARY:").append(currentEvent.title).append("\n");
        ics.append("DESCRIPTION:").append(currentEvent.description).append("\n");
        ics.append("LOCATION:").append(currentEvent.location).append("\n");
        ics.append("END:VEVENT\n");
        ics.append("END:VCALENDAR\n");

        return ics.toString();
    }

    private void editEvent() {
        // Chuyển sang AddEventActivity ở chế độ edit
        Intent editIntent = new Intent(this, AddEventActivity.class);
        editIntent.putExtra("event_data", currentEvent);
        editIntent.putExtra("mode", "edit");
        startActivity(editIntent);
        finish(); // Đóng màn hình share sau khi chuyển sang edit
    }

    private void deleteEvent() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sự kiện này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(this, "Đã xóa sự kiện", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void copyEvent() {
        // TODO: Navigate to add event with copied data
        Intent copyIntent = new Intent(this, AddEventActivity.class);
        copyIntent.putExtra("event_data", currentEvent);
        copyIntent.putExtra("mode", "copy");
        startActivity(copyIntent);
    }

    // Event data class
    public static class EventData implements java.io.Serializable {
        public String id = java.util.UUID.randomUUID().toString();
        public String title;
        public String description;
        public String location;
        public long startTime;
        public long endTime;
        public String category;
        public String priority;
        public String color = "#007BFF";
    }
}