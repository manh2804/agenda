package com.example.agendaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AgendaActivity extends AppCompatActivity {

    private ImageButton btnBack, btnSearch, btnFilter;
    private FloatingActionButton fabAddEvent;
    private RecyclerView recyclerViewEvents;
    private TextView tvNoEvents, tvCurrentMonth;
    private List<EventItem> eventList;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        initViews();
        setupEventHandlers();
        loadEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        btnFilter = findViewById(R.id.btnFilter);
        fabAddEvent = findViewById(R.id.fabAddEvent);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        tvNoEvents = findViewById(R.id.tvNoEvents);
        tvCurrentMonth = findViewById(R.id.tvCurrentMonth);

        // Set current month
        SimpleDateFormat monthFormat = new SimpleDateFormat("'Tháng' MM", new Locale("vi", "VN"));
        tvCurrentMonth.setText(monthFormat.format(Calendar.getInstance().getTime()));
    }

    private void loadEvents() {
        // TODO: Load from database
        eventList = new ArrayList<>();

        // Mock data - Thay bằng database thực tế
        Calendar cal = Calendar.getInstance();

        eventList.add(new EventItem(1, "Họp team Marketing", "08:00 - 09:30", "22/10/2025", "#007BFF", "Cao"));
        eventList.add(new EventItem(2, "Gặp khách hàng", "14:00 - 15:30", "22/10/2025", "#06D6A0", "Trung bình"));
        eventList.add(new EventItem(3, "Tập gym", "18:00 - 19:00", "22/10/2025", "#FFA500", "Thấp"));
        eventList.add(new EventItem(4, "Họp dự án", "10:00 - 11:30", "23/10/2025", "#E63946", "Cao"));
        eventList.add(new EventItem(5, "Training Intern", "15:00 - 17:00", "23/10/2025", "#9B5DE5", "Trung bình"));

        if (eventList.isEmpty()) {
            tvNoEvents.setVisibility(View.VISIBLE);
            recyclerViewEvents.setVisibility(View.GONE);
        } else {
            tvNoEvents.setVisibility(View.GONE);
            recyclerViewEvents.setVisibility(View.VISIBLE);
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        eventAdapter = new EventAdapter(eventList, this::onEventClick);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(eventAdapter);
    }

    private void setupEventHandlers() {
        // Quay lại MainActivity
        btnBack.setOnClickListener(v -> finish());

        // Thêm sự kiện mới
        fabAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(AgendaActivity.this, AddEventActivity.class);
            startActivity(intent);
        });

        // Tìm kiếm
        btnSearch.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng tìm kiếm đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        // Lọc
        btnFilter.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng lọc đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void onEventClick(EventItem event) {
        // Khi click vào event -> Mở ShareEventActivity để xem chi tiết
        Intent intent = new Intent(AgendaActivity.this, ShareEventActivity.class);
        intent.putExtra("event_id", event.id);
        intent.putExtra("event_title", event.title);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents(); // Refresh khi quay lại
    }

    // Event Item class
    public static class EventItem {
        public long id;
        public String title;
        public String time;
        public String date;
        public String color;
        public String priority;

        public EventItem(long id, String title, String time, String date, String color, String priority) {
            this.id = id;
            this.title = title;
            this.time = time;
            this.date = date;
            this.color = color;
            this.priority = priority;
        }
    }
}