package com.example.classclue.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classclue.R;
import com.example.classclue.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private boolean isAdmin;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public EventAdapter(List<Event> eventList, boolean isAdmin, OnItemClickListener listener) {
        this.eventList = eventList;
        this.isAdmin = isAdmin;
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
        Event event = eventList.get(position);
        holder.tvEventName.setText(event.getName());
        holder.tvEventType.setText(event.getType());
        holder.tvEventDateTime.setText(String.format("%s at %s", event.getDate(), event.getTime()));
        holder.tvEventLocation.setText(event.getLocation());

        if (isAdmin) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(event));
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvEventType, tvEventDateTime, tvEventLocation;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventType = itemView.findViewById(R.id.tvEventType);
            tvEventDateTime = itemView.findViewById(R.id.tvEventDateTime);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
        }
    }
}