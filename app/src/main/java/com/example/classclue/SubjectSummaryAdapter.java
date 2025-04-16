package com.example.classclue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SubjectSummaryAdapter extends RecyclerView.Adapter<SubjectSummaryAdapter.ViewHolder> {
    private List<SubjectSummary> subjectSummaries;

    public SubjectSummaryAdapter(List<SubjectSummary> subjectSummaries) {
        this.subjectSummaries = subjectSummaries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SubjectSummary summary = subjectSummaries.get(position);
        holder.subjectTextView.setText(summary.subject);
        holder.taskCountTextView.setText(summary.taskCount + " Tasks");
    }

    @Override
    public int getItemCount() {
        return subjectSummaries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView, taskCountTextView;

        ViewHolder(View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            taskCountTextView = itemView.findViewById(R.id.taskCountTextView);
        }
    }
}