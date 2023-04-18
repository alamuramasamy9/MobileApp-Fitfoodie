package edu.northeastern.fitfoodie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<WorkoutTracker> workoutList;

    public WorkoutAdapter(List<WorkoutTracker> workoutList) {
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutTracker workout = workoutList.get(position);
        holder.textViewType.setText(workout.getType());
        holder.textViewStartTime.setText("Start Time: " + workout.getStartTime());
        holder.textViewEndTime.setText("End Time: " + workout.getEndTime());
        holder.textViewTimeSpent.setText("Time Spent: " + workout.getWorkoutDuration() + " seconds.");
        holder.textViewDistance.setText("Distance: " + String.format("%.2f", workout.getDistance()/1609.344) + " mi.");
        holder.textViewAvgSpeed.setText("Average Speed: " + String.format("%.2f", workout.getAverageSpeed()) + " m/h.");
        holder.textViewCaloriesBurnt.setText("Calories Burnt: " + String.format("%.2f", workout.getCaloriesBurnt()));
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewType;
        public TextView textViewStartTime;
        public TextView textViewEndTime;
        public TextView textViewDistance;
        public TextView textViewAvgSpeed;

        public TextView textViewTimeSpent;

        public TextView textViewCaloriesBurnt;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.text_view_type);
            textViewStartTime = itemView.findViewById(R.id.text_view_start_time);
            textViewEndTime = itemView.findViewById(R.id.text_view_end_time);
            textViewDistance = itemView.findViewById(R.id.text_view_distance);
            textViewAvgSpeed = itemView.findViewById(R.id.text_view_avg_speed);
            textViewTimeSpent = itemView.findViewById(R.id.text_view_time_spent);
            textViewCaloriesBurnt = itemView.findViewById(R.id.text_view_calories_burnt);
        }
    }
}

