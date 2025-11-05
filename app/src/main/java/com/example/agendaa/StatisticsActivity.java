package com.example.agendaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class StatisticsActivity extends AppCompatActivity {

    // UI Elements
    private ImageButton btnBack, btnExport, btnSettings;;
    private TextView btnAllTime, btnThisMonth, btnToday;
    private TextView tvTotalTime, tvTimeComparison;
    private TextView tvWorkHours, tvPersonalHours, tvFamilyHours, tvOtherHours;
    private LinearLayout topCategoriesList;

    // Data variables
    private int selectedPeriod = 1; // 0: All time, 1: This month, 2: Today
    private StatisticsData currentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initViews();
        setupEventHandlers();
        loadStatisticsData();
        updateUI();
    }

    private void initViews() {
        // App bar
        btnBack = findViewById(R.id.btnBack);
        btnExport = findViewById(R.id.btnExport);
        btnSettings = findViewById(R.id.btnSettings);

        // Time period selectors
        btnAllTime = findViewById(R.id.btnAllTime);
        btnThisMonth = findViewById(R.id.btnThisMonth);
        btnToday = findViewById(R.id.btnToday);

        // Statistics displays
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvTimeComparison = findViewById(R.id.tvTimeComparison);

        // Category hours
        tvWorkHours = findViewById(R.id.tvWorkHours);
        tvPersonalHours = findViewById(R.id.tvPersonalHours);
        tvFamilyHours = findViewById(R.id.tvFamilyHours);
        tvOtherHours = findViewById(R.id.tvOtherHours);

        // Top categories list
        topCategoriesList = findViewById(R.id.topCategoriesList);
    }

    private void setupEventHandlers() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Xuất báo cáo
        btnExport.setOnClickListener(v -> exportReport());

// Cài đặt thống kê
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Cài đặt thống kê", Toast.LENGTH_SHORT).show();
        });

        // Time period selectors
        btnAllTime.setOnClickListener(v -> {
            selectedPeriod = 0;
            updateTimePeriodButtons();
            loadStatisticsData();
            updateUI();
        });

        btnThisMonth.setOnClickListener(v -> {
            selectedPeriod = 1;
            updateTimePeriodButtons();
            loadStatisticsData();
            updateUI();
        });

        btnToday.setOnClickListener(v -> {
            selectedPeriod = 2;
            updateTimePeriodButtons();
            loadStatisticsData();
            updateUI();
        });
    }

    private void exportReport() {
        String reportText = createReportText();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Báo cáo thống kê lịch làm việc");
        shareIntent.putExtra(Intent.EXTRA_TEXT, reportText);

        try {
            startActivity(Intent.createChooser(shareIntent, "Xuất báo cáo"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Không thể xuất báo cáo", Toast.LENGTH_SHORT).show();
        }
    }

    private String createReportText() {
        return "BÁO CÁO THỐNG KÊ LỊCH LÀM VIỆC\n\n" +
                "Tổng thời gian: " + String.format("%.1f giờ", currentData.totalHours) + "\n" +
                "Công việc: " + String.format("%.0f giờ", currentData.workHours) + "\n" +
                "Cá nhân: " + String.format("%.0f giờ", currentData.personalHours) + "\n" +
                "Gia đình: " + String.format("%.0f giờ", currentData.familyHours) + "\n" +
                "Khác: " + String.format("%.1f giờ", currentData.otherHours) + "\n\n" +
                currentData.comparisonText;
    }

    private void updateTimePeriodButtons() {
        // Reset all buttons
        btnAllTime.setBackgroundResource(R.drawable.bg_time_selector);
        btnThisMonth.setBackgroundResource(R.drawable.bg_time_selector);
        btnToday.setBackgroundResource(R.drawable.bg_time_selector);

        // Highlight selected button
        switch (selectedPeriod) {
            case 0:
                btnAllTime.setBackgroundResource(R.drawable.bg_time_selector_selected);
                break;
            case 1:
                btnThisMonth.setBackgroundResource(R.drawable.bg_time_selector_selected);
                break;
            case 2:
                btnToday.setBackgroundResource(R.drawable.bg_time_selector_selected);
                break;
        }
    }

    private void loadStatisticsData() {
        // TODO: Load real data from database
        // For now, using mock data

        switch (selectedPeriod) {
            case 0: // All time
                currentData = generateMockData("all");
                break;
            case 1: // This month
                currentData = generateMockData("month");
                break;
            case 2: // Today
                currentData = generateMockData("today");
                break;
        }
    }

    private StatisticsData generateMockData(String period) {
        StatisticsData data = new StatisticsData();

        switch (period) {
            case "all":
                data.totalHours = 156.5f;
                data.comparisonText = "18.2 Giờ so với tháng trước";
                data.workHours = 98.0f;
                data.personalHours = 35.5f;
                data.familyHours = 18.0f;
                data.otherHours = 5.0f;
                data.topCategories = Arrays.asList(
                        new CategoryData("#Họp team", 45.5f),
                        new CategoryData("#Dự án", 32.0f),
                        new CategoryData("#Cá nhân", 28.5f),
                        new CategoryData("#Gia đình", 18.0f),
                        new CategoryData("#Thể thao", 12.5f)
                );
                break;

            case "month":
                data.totalHours = 42.5f;
                data.comparisonText = "5.2 Giờ so với tháng trước";
                data.workHours = 30.0f;
                data.personalHours = 10.0f;
                data.familyHours = 10.0f;
                data.otherHours = 2.5f;
                data.topCategories = Arrays.asList(
                        new CategoryData("#Họp team", 12.0f),
                        new CategoryData("#Gặp đối tác", 8.0f),
                        new CategoryData("#Cá nhân", 5.0f),
                        new CategoryData("#Gia đình", 4.0f),
                        new CategoryData("#Khác", 3.0f)
                );
                break;

            case "today":
                data.totalHours = 8.5f;
                data.comparisonText = "2.5 Giờ so với hôm qua";
                data.workHours = 6.0f;
                data.personalHours = 1.5f;
                data.familyHours = 1.0f;
                data.otherHours = 0.0f;
                data.topCategories = Arrays.asList(
                        new CategoryData("#Họp team", 3.5f),
                        new CategoryData("#Email", 2.0f),
                        new CategoryData("#Cá nhân", 1.5f),
                        new CategoryData("#Gia đình", 1.0f),
                        new CategoryData("#Học tập", 0.5f)
                );
                break;
        }

        return data;
    }

    private void updateUI() {
        if (currentData == null) return;

        // Update total time
        tvTotalTime.setText(String.format("%.1f giờ", currentData.totalHours));
        tvTimeComparison.setText(currentData.comparisonText);

        // Update category hours
        tvWorkHours.setText(String.format("%.0f giờ", currentData.workHours));
        tvPersonalHours.setText(String.format("%.0f giờ", currentData.personalHours));
        tvFamilyHours.setText(String.format("%.0f giờ", currentData.familyHours));
        tvOtherHours.setText(String.format("%.1f giờ", currentData.otherHours));

        // Update top categories (already populated in layout)
        // You can dynamically update this if needed
        updateTopCategoriesList();
    }

    private void updateTopCategoriesList() {
        // Clear existing views
        topCategoriesList.removeAllViews();

        // Add top categories dynamically
        for (int i = 0; i < Math.min(5, currentData.topCategories.size()); i++) {
            CategoryData category = currentData.topCategories.get(i);
            View categoryView = createCategoryView(i + 1, category);
            topCategoriesList.addView(categoryView);
        }
    }

    private View createCategoryView(int rank, CategoryData category) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.bottomMargin = (int) (12 * getResources().getDisplayMetrics().density);
        layout.setLayoutParams(layoutParams);

        // Rank number
        TextView rankText = new TextView(this);
        rankText.setText(rank + ".");
        rankText.setTextColor(getResources().getColor(android.R.color.black));
        rankText.setTextSize(14);
        rankText.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rankParams.rightMargin = (int) (8 * getResources().getDisplayMetrics().density);
        rankText.setLayoutParams(rankParams);
        layout.addView(rankText);

        // Category name
        TextView categoryText = new TextView(this);
        categoryText.setText(category.name);
        categoryText.setTextColor(getResources().getColor(R.color.blue_primary, null));
        categoryText.setTextSize(14);
        categoryText.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams categoryParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        categoryParams.rightMargin = (int) (8 * getResources().getDisplayMetrics().density);
        categoryText.setLayoutParams(categoryParams);
        layout.addView(categoryText);

        // Dotted line
        View dotLine = new View(this);
        dotLine.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                0,
                1,
                1.0f
        );
        lineParams.leftMargin = (int) (8 * getResources().getDisplayMetrics().density);
        lineParams.rightMargin = (int) (8 * getResources().getDisplayMetrics().density);
        dotLine.setLayoutParams(lineParams);
        layout.addView(dotLine);

        // Hours
        TextView hoursText = new TextView(this);
        hoursText.setText(String.format("%.0f giờ", category.hours));
        hoursText.setTextColor(getResources().getColor(android.R.color.black));
        hoursText.setTextSize(14);
        hoursText.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams hoursParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        hoursText.setLayoutParams(hoursParams);
        layout.addView(hoursText);

        return layout;
    }

    // Data classes
    public static class StatisticsData {
        public float totalHours;
        public String comparisonText;
        public float workHours;
        public float personalHours;
        public float familyHours;
        public float otherHours;
        public List<CategoryData> topCategories;
    }

    public static class CategoryData {
        public String name;
        public float hours;

        public CategoryData(String name, float hours) {
            this.name = name;
            this.hours = hours;
        }
    }
}