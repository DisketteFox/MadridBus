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

public class BusTimeAdapter extends RecyclerView.Adapter<BusTimeAdapter.ViewHolder> {

    private final Context context;
    private final List<StopModel.Dataline> linesList;

    public BusTimeAdapter(Context context, List<StopModel.Dataline> linesList) {
        this.context = context;
        this.linesList = linesList;
    }

    @NonNull
    @Override
    public BusTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_bus_time, parent, false);
        return new BusTimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusTimeAdapter.ViewHolder holder, int position) {
        StopModel.Dataline line = linesList.get(position);

        if (line != null) {
            holder.busNumber.setText(line.getLabel());
            if (line.getDirection().equals("A")) {
                holder.busDirection.setText(line.getHeaderA());
            } else {
                holder.busDirection.setText(line.getHeaderB());
            }

            if (Integer.parseInt(line.getLineId()) > 500 & Integer.parseInt(line.getLineId()) < 600) {
                holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.gold));
                holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.black));
            } else if (Integer.parseInt(line.getLineId()) == 203) {
                holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
                holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.gold));
            } else if (Integer.parseInt(line.getLineId()) > 360 & Integer.parseInt(line.getLineId()) < 362) {
                holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.turquoise));
                holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.turquoise));
            } else if (Integer.parseInt(line.getLineId()) > 89 & Integer.parseInt(line.getLineId()) < 100) {
                holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
                holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.lightblue));
            } else if (Integer.parseInt(line.getLineId()) > 451 & Integer.parseInt(line.getLineId()) < 458) {
                holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.brown));
                holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.brown));
            } else if (Integer.parseInt(line.getLineId()) > 600 & Integer.parseInt(line.getLineId()) < 604) {
                holder.cardColor.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
                holder.busDirection.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.busNumber.setTextColor(ContextCompat.getColor(context, R.color.yellow));
            }
            
            // MORE TO IMPLEMENT
        }
    }

    @Override
    public int getItemCount() {
        return linesList != null ? linesList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView busNumber, busDirection;
        View cardColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busNumber = itemView.findViewById(R.id.bus_number);
            busDirection = itemView.findViewById(R.id.bus_direction);
            cardColor= itemView.findViewById(R.id.card_color);
        }
    }
}
