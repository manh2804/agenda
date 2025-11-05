package com.example.agendaa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class EventTemplateDetailActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription;
    private TextView btnStartDate, btnStartTime, btnEndDate, btnEndTime, btnRepeat, btnColor, tagWork;
    private CheckBox cbHigh, cbMedium, cbLow;
    private Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_template_detail);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartTime = findViewById(R.id.btnStartTime);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndTime = findViewById(R.id.btnEndTime);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnColor = findViewById(R.id.btnColor);
        tagWork = findViewById(R.id.tagWork);
        cbHigh = findViewById(R.id.cbHigh);
        cbMedium = findViewById(R.id.cbMedium);
        cbLow = findViewById(R.id.cbLow);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        // Chọn ngày bắt đầu/kết thúc
        btnStartDate.setOnClickListener(view -> showDatePicker(btnStartDate));
        btnEndDate.setOnClickListener(view -> showDatePicker(btnEndDate));
        // Chọn giờ bắt đầu/kết thúc
        btnStartTime.setOnClickListener(view -> showTimePicker(btnStartTime));
        btnEndTime.setOnClickListener(view -> showTimePicker(btnEndTime));

        // Lặp lại
        btnRepeat.setOnClickListener(view -> showRepeatDialog());

        // Chọn màu sắc
        btnColor.setOnClickListener(view -> showColorDialog());

        // Chọn ưu tiên chỉ được 1 checkbox
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

        // TagWork toggle (ví dụ, sau này có thể nhiều thẻ)
        tagWork.setOnClickListener(view -> {
            if (tagWork.getAlpha() == 1.0f)
                tagWork.setAlpha(0.5f);
            else
                tagWork.setAlpha(1.0f);
        });

        btnCancel.setOnClickListener(v -> finish()); // Quay lại
        btnSave.setOnClickListener(v -> saveTemplate());
    }

    private void showDatePicker(TextView tv) {
        DatePickerDialog dialog = new DatePickerDialog(this,
                (d, y, m, day) -> tv.setText(String.format("%02d/%02d/%04d (-)", day, m + 1, y)),
                2025, 9, 22);
        dialog.show();
    }

    private void showTimePicker(TextView tv) {
        TimePickerDialog dialog = new TimePickerDialog(this,
                (d, h, min) -> tv.setText(String.format("%02d:%02d", h, min)),
                8, 0, true);
        dialog.show();
    }

    private void showRepeatDialog() {
        final String[] items = {"Không lặp lại", "Hàng ngày", "Hàng tuần", "Hàng tháng"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lặp lại")
                .setItems(items, (dialog, which) -> btnRepeat.setText(items[which]));
        builder.show();
    }

    private void showColorDialog() {
        final String[] items = {"Màu mặc định", "Màu đỏ cà chua", "Màu xanh", "Màu vàng"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn màu")
                .setItems(items, (dialog, which) -> {
                    btnColor.setText(items[which]);
                    // TODO: Đổi background màu của btnColor theo lựa chọn
                });
        builder.show();
    }

    private void saveTemplate() {
        String title = edtTitle.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();
        String startDate = btnStartDate.getText().toString();
        String endDate = btnEndDate.getText().toString();
        String startTime = btnStartTime.getText().toString();
        String endTime = btnEndTime.getText().toString();
        String repeat = btnRepeat.getText().toString();
        String color = btnColor.getText().toString();
        String priority = cbHigh.isChecked() ? "Cao" : cbMedium.isChecked() ? "Trung bình" : cbLow.isChecked() ? "Thấp" : "";
        boolean selectedTag = tagWork.getAlpha() == 1.0f;

        Toast.makeText(this, "Đã lưu mẫu sự kiện!", Toast.LENGTH_SHORT).show();
        finish();
    }
}