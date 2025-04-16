package com.example.classclue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events != null ? events : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitleTextView.setText(event.getTitle() + "\n" + event.getSubject());
        holder.eventDateTextView.setText(event.getDate());
        if (event.getTitle().contains("Exam")) {
            holder.eventIcon.setImageResource(R.drawable.ic_exam); // Ensure ic_exam exists
        } else if (event.getTitle().contains("Submission")) {
            holder.eventIcon.setImageResource(R.drawable.ic_submission); // Ensure ic_submission exists
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventIcon;
        TextView eventTitleTextView, eventDateTextView;

        ViewHolder(View itemView) {
            super(itemView);
            eventIcon = itemView.findViewById(R.id.eventIcon);
            eventTitleTextView = itemView.findViewById(R.id.eventTitleTextView);
            eventDateTextView = itemView.findViewById(R.id.eventDateTextView);
        }
    }
}