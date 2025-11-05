package com.example.agendaa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    // UI Elements
    private ImageButton btnBack;
    private EditText edtTitle, edtDescription;
    private TextView btnStartDate, btnStartTime, btnEndDate, btnEndTime, btnRepeat;
    private TextView selectedColorText;
    private View selectedColorIndicator;
    private LinearLayout colorList, colorDropdown;
    private CheckBox cbHigh, cbMedium, cbLow;
    private TextView tagUrgent, tagWork, tagOther, tagPersonal, tagFamily;
    private TextView btnCancel, btnSave;

    private ImageButton btnSmartReminder, btnUseTemplate;

    // Data variables
    private Calendar startCalendar, endCalendar;
    private SimpleDateFormat dateFormat, timeFormat;
    private String selectedColor = "#007BFF";
    private final String[] repeatOptions = {"Không lặp lại", "Hàng ngày", "Hàng tuần", "Hàng tháng", "Hàng năm"};
    private int selectedRepeatIndex = 1; // Default: Hàng ngày
    private boolean isColorListVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initViews();
        setupDateTimeFormats();
        setupEventHandlers();
        setDefaultValues();
    }

    private void initViews() {
        // App bar
        btnBack = findViewById(R.id.btnBack);
        btnSmartReminder = findViewById(R.id.btnSmartReminder);
        btnUseTemplate = findViewById(R.id.btnUseTemplate);

        // Form fields
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);

        // Date and time buttons
        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartTime = findViewById(R.id.btnStartTime);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndTime = findViewById(R.id.btnEndTime);
        btnRepeat = findViewById(R.id.btnRepeat);

        // Priority checkboxes
        cbHigh = findViewById(R.id.cbHigh);
        cbMedium = findViewById(R.id.cbMedium);
        cbLow = findViewById(R.id.cbLow);

        // Tags
        tagUrgent = findViewById(R.id.tagUrgent);
        tagWork = findViewById(R.id.tagWork);
        tagOther = findViewById(R.id.tagOther);
        tagPersonal = findViewById(R.id.tagPersonal);
        tagFamily = findViewById(R.id.tagFamily);

        // Color selection
        selectedColorText = findViewById(R.id.selectedColorText);
        selectedColorIndicator = findViewById(R.id.selectedColorIndicator);
        colorList = findViewById(R.id.colorList);

        // Find the color dropdown container (the LinearLayout that contains selectedColorIndicator)
        colorDropdown = (LinearLayout) selectedColorIndicator.getParent();

        // Action buttons
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupDateTimeFormats() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy (EEE)", new Locale("vi", "VN"));
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.HOUR_OF_DAY, 1); // End time 1 hour after start
    }

    private void setupEventHandlers() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Date and time pickers
        btnStartDate.setOnClickListener(v -> showDatePicker(startCalendar, true));
        btnStartTime.setOnClickListener(v -> showTimePicker(startCalendar, true));
        btnEndDate.setOnClickListener(v -> showDatePicker(endCalendar, false));
        btnEndTime.setOnClickListener(v -> showTimePicker(endCalendar, false));

        // Repeat option
        btnRepeat.setOnClickListener(v -> showRepeatDialog());

        // Priority checkboxes (only one can be selected)
        cbHigh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbMedium.setChecked(false);
                cbLow.setChecked(false);
            }
        });

        cbMedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbHigh.setChecked(false);
                cbLow.setChecked(false);
            }
        });

        cbLow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbHigh.setChecked(false);
                cbMedium.setChecked(false);
            }
        });

        // Tag selection (toggle selection)
        setupTagClickListener(tagUrgent);
        setupTagClickListener(tagWork);
        setupTagClickListener(tagOther);
        setupTagClickListener(tagPersonal);
        setupTagClickListener(tagFamily);

        // Color selection dropdown - Fixed the issue here
        colorDropdown.setOnClickListener(v -> toggleColorList());

        // Setup color list items
        setupColorListItems();

        // Nhắc nhở thông minh
        btnSmartReminder.setOnClickListener(v -> {
            showSmartReminderSuggestion();
        });

// Sử dụng mẫu có sẵn
        btnUseTemplate.setOnClickListener(v -> {
            Intent intent = new Intent(AddEventActivity.this, EventTemplateDetailActivity.class);
            intent.putExtra("mode", "select_template");
            startActivityForResult(intent, 100);
        });

        // Action buttons
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void setDefaultValues() {
        // Set default date and time
        updateDateTimeDisplays();

        // Set default priority
        cbHigh.setChecked(true);

        // Hide color list initially
        colorList.setVisibility(View.GONE);
    }

    private void showSmartReminderSuggestion() {
        String suggestion = "Gợi ý thông minh:\n\n" +
                "Dựa trên thói quen của bạn, nên đặt nhắc nhở:\n" +
                "• 1 ngày trước (09:00)\n" +
                "• 1 giờ trước sự kiện\n" +
                "• 15 phút trước sự kiện";

        SmartReminderDialog dialog = new SmartReminderDialog(this, suggestion, () -> {
            Toast.makeText(this, "Đã áp dụng nhắc nhở thông minh", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    private void showDatePicker(Calendar calendar, boolean isStartDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTimeDisplays();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker(Calendar calendar, boolean isStartTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    // If changing start time, auto-adjust end time
                    if (isStartTime) {
                        endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
                        endCalendar.add(Calendar.HOUR_OF_DAY, 1);
                    }

                    updateDateTimeDisplays();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void showRepeatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn tần suất lặp lại");
        builder.setSingleChoiceItems(repeatOptions, selectedRepeatIndex, (dialog, which) -> {
            selectedRepeatIndex = which;
            btnRepeat.setText(repeatOptions[which]);
            dialog.dismiss();
        });
        builder.show();
    }

    private void updateDateTimeDisplays() {
        btnStartDate.setText(dateFormat.format(startCalendar.getTime()));
        btnStartTime.setText(timeFormat.format(startCalendar.getTime()));
        btnEndDate.setText(dateFormat.format(endCalendar.getTime()));
        btnEndTime.setText(timeFormat.format(endCalendar.getTime()));
    }

    private void setupTagClickListener(TextView tag) {
        tag.setOnClickListener(v -> {
            // Toggle tag selection by changing alpha
            if (tag.getAlpha() == 1.0f) {
                tag.setAlpha(0.5f); // Deselected
            } else {
                tag.setAlpha(1.0f); // Selected
            }
        });
    }

    private void toggleColorList() {
        if (isColorListVisible) {
            colorList.setVisibility(View.GONE);
            isColorListVisible = false;
        } else {
            colorList.setVisibility(View.VISIBLE);
            isColorListVisible = true;
        }
    }

    private void setupColorListItems() {
        // Color options with their values and names
        String[][] colors = {
                {"#007BFF", "Màu mặc định"},
                {"#06D6A0", "Màu húng quế"},
                {"#9B5DE5", "Màu nho"},
                {"#118AB2", "Màu xanh lam"},
                {"#E63946", "Màu đỏ cà chua"},
                {"#FFA500", "Màu cam"},
                {"#FFD166", "Màu chuối"},
                {"#B0BEC5", "Màu khói"}
        };

        // Find all color item layouts and set click listeners
        for (int i = 0; i < colorList.getChildCount(); i++) {
            View colorItem = colorList.getChildAt(i);
            final int index = i;

            if (index < colors.length) {
                colorItem.setOnClickListener(v -> {
                    selectedColor = colors[index][0];
                    selectedColorText.setText(colors[index][1]);
                    selectedColorIndicator.setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(selectedColor))
                    );
                    toggleColorList();
                });
            }
        }
    }

    private void saveEvent() {
        // Validate required fields
        String title = edtTitle.getText().toString().trim();
        if (title.isEmpty()) {
            edtTitle.setError("Vui lòng nhập tiêu đề");
            return;
        }

        // Get form data
        String description = edtDescription.getText().toString().trim();

        // Get priority
        String priority = "";
        if (cbHigh.isChecked()) priority = "Cao";
        else if (cbMedium.isChecked()) priority = "Trung bình";
        else if (cbLow.isChecked()) priority = "Thấp";

        // Get selected tags
        StringBuilder tags = new StringBuilder();
        if (tagUrgent.getAlpha() == 1.0f) tags.append("Khẩn cấp,");
        if (tagWork.getAlpha() == 1.0f) tags.append("Công việc,");
        if (tagOther.getAlpha() == 1.0f) tags.append("Khác,");
        if (tagPersonal.getAlpha() == 1.0f) tags.append("Cá nhân,");
        if (tagFamily.getAlpha() == 1.0f) tags.append("Gia đình,");

        // Remove trailing comma
        String selectedTags = tags.length() > 0 ? tags.substring(0, tags.length() - 1) : "";

        // Create event object or save to database
        Event event = new Event(
                title,
                description,
                startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(),
                repeatOptions[selectedRepeatIndex],
                priority,
                selectedTags,
                selectedColor
        );

        // TODO: Save event to database or pass back to calling activity

        // Show success message
        Toast.makeText(this, "Sự kiện đã được lưu!", Toast.LENGTH_SHORT).show();

        // Return to previous screen
        finish();
    }

    // Event model class
    public static class Event {
        public String title;
        public String description;
        public long startTime;
        public long endTime;
        public String repeatOption;
        public String priority;
        public String tags;
        public String color;

        public Event(String title, String description, long startTime, long endTime,
                     String repeatOption, String priority, String tags, String color) {
            this.title = title;
            this.description = description;
            this.startTime = startTime;
            this.endTime = endTime;
            this.repeatOption = repeatOption;
            this.priority = priority;
            this.tags = tags;
            this.color = color;
        }
    }

    public static class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        private List<AgendaActivity.EventItem> eventList;
        private OnEventClickListener listener;

        public interface OnEventClickListener {
            void onEventClick(AgendaActivity.EventItem event);
        }

        public EventAdapter(List<AgendaActivity.EventItem> eventList, OnEventClickListener listener) {
            this.eventList = eventList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            AgendaActivity.EventItem event = eventList.get(position);
            holder.bind(event, listener);
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        static class EventViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvTime, tvDate;
            View colorIndicator;

            EventViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvEventTitle);
                tvTime = itemView.findViewById(R.id.tvEventTime);
                tvDate = itemView.findViewById(R.id.tvEventDate);
                colorIndicator = itemView.findViewById(R.id.colorIndicator);
            }

            void bind(AgendaActivity.EventItem event, OnEventClickListener listener) {
                tvTitle.setText(event.title);
                tvTime.setText(event.time);
                tvDate.setText(event.date);
                colorIndicator.setBackgroundColor(Color.parseColor(event.color));

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onEventClick(event);
                    }
                });
            }
        }
    }
}