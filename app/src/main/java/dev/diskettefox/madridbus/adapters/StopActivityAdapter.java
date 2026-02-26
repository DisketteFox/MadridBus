package dev.diskettefox.madridbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.diskettefox.madridbus.R;
import dev.diskettefox.madridbus.models.StopModel;

public class StopActivityAdapter extends RecyclerView.Adapter<StopActivityAdapter.ViewHolder> {

    private final Context context;
    private final List<StopModel.Dataline> linesList;

    public StopActivityAdapter(Context context, List<StopModel.Dataline> linesList) {
        this.context = context;
        this.linesList = linesList;
    }

    @NonNull
    @Override
    public StopActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_bus_time, parent, false);
        return new StopActivityAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StopActivityAdapter.ViewHolder holder, int position) {
        StopModel.Dataline line = linesList.get(position);

        if (line != null) {
            holder.busNumber.setText(line.getLabel());
            if (line.getDirection().equals("A")) {
                holder.busDirection.setText(line.getHeaderA());
            } else {
                holder.busDirection.setText(line.getHeaderB());
            }

            if (line.getTimeArriving() != null) {
                holder.timeArriving.setText(line.getTimeArriving());
            } else {
                holder.timeArriving.setText("---");
            }

            if (line.getTimeNext() != null) {
                holder.timeNext.setText(line.getTimeNext());
            } else {
                holder.timeNext.setText("---");
            }

            try {
                int lineIdInt = Integer.parseInt(line.getLineId());
                if (lineIdInt > 500 && lineIdInt < 600) {
                    holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                    holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.gold));
                    holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.black));
                } else if (lineIdInt == 203) {
                    holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
                    holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.black));
                    holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.gold));
                } else if (lineIdInt > 360 && lineIdInt < 362) {
                    holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.turquoise));
                    holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.white));
                    holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.turquoise));
                } else if (lineIdInt > 89 && lineIdInt < 100) {
                    holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
                    holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.white));
                    holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.lightblue));
                } else if (lineIdInt > 451 && lineIdInt < 458) {
                    holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.brown));
                    holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.white));
                    holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.brown));
                } else if (lineIdInt > 600 && lineIdInt < 604) {
                    holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
                    holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.black));
                    holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                }
            } catch (NumberFormatException e) {
                // Ignore or set default colors
            }
        }
    }

    @Override
    public int getItemCount() {
        return linesList != null ? linesList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView busNumber, busDirection, timeArriving, timeNext;
        View cardColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busNumber = itemView.findViewById(R.id.bus_number);
            busDirection = itemView.findViewById(R.id.bus_direction);
            cardColor = itemView.findViewById(R.id.card_color);
            timeArriving = itemView.findViewById(R.id.time_arriving);
            timeNext = itemView.findViewById(R.id.time_next);
        }
    }
}