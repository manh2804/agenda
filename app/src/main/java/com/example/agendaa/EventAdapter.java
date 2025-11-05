package com.example.agendaa;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

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
        TextView tvEventTitle, tvEventTime, tvEventDate;
        View colorIndicator;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventTitle = itemView.findViewById(R.id.tvEventTitle);
            tvEventTime = itemView.findViewById(R.id.tvEventTime);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            colorIndicator = itemView.findViewById(R.id.colorIndicator);
        }

        void bind(AgendaActivity.EventItem event, OnEventClickListener listener) {
            tvEventTitle.setText(event.title);
            tvEventTime.setText(event.time);
            tvEventDate.setText(event.date);

            try {
                colorIndicator.setBackgroundColor(Color.parseColor(event.color));
            } catch (Exception e) {
                colorIndicator.setBackgroundColor(Color.parseColor("#007BFF"));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEventClick(event);
                }
            });
        }
    }
}