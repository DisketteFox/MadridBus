package dev.diskettefox.madridbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.diskettefox.madridbus.R;
import dev.diskettefox.madridbus.api.StopModel;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<StopModel.Stop> stopsList;

    public BusAdapter(Context context, ArrayList<StopModel.Stop> stopsList) {
        this.context = context;
        this.stopsList = stopsList;
    }

    @NonNull
    @Override
    public BusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_stop, parent, false);
        return new BusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusAdapter.ViewHolder holder, int position) {
        StopModel.Stop stop = stopsList.get(position);

        if (stop != null) {
            holder.tvStopId.setText(stop.getStopId());
            holder.tvStopName.setText(stop.getName());

            // Clear previous views to fix duplication in recycler views
            holder.linesContainer.removeAllViews();

            List<StopModel.Dataline> lines = stop.getDataLine();
            if (lines != null) {
                if (lines.size() < 5) {
                    for (StopModel.Dataline line : lines) {
                        View lineView = LayoutInflater.from(context).inflate(R.layout.button_line, holder.linesContainer, false);
                        Button btnLine = lineView.findViewById(R.id.line_button);
                        if (Integer.parseInt(line.getLineId()) > 500 & Integer.parseInt(line.getLineId()) < 600) {
                            btnLine.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                            btnLine.setTextColor(ContextCompat.getColor(context, R.color.gold));
                        } else if (Integer.parseInt(line.getLineId()) == 203) {
                            btnLine.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
                            btnLine.setTextColor(ContextCompat.getColor(context, R.color.black));
                        } else if (Integer.parseInt(line.getLineId()) > 360 & Integer.parseInt(line.getLineId()) < 362) {
                            btnLine.setBackgroundColor(ContextCompat.getColor(context, R.color.turquoise));
                            btnLine.setTextColor(ContextCompat.getColor(context, R.color.white));
                        } else if (Integer.parseInt(line.getLineId()) > 89 & Integer.parseInt(line.getLineId()) < 100) {
                            btnLine.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
                            btnLine.setTextColor(ContextCompat.getColor(context, R.color.white));
                        } else if (Integer.parseInt(line.getLineId()) > 451 & Integer.parseInt(line.getLineId()) < 458) {
                            btnLine.setBackgroundColor(ContextCompat.getColor(context, R.color.brown));
                            btnLine.setTextColor(ContextCompat.getColor(context, R.color.white));
                        } else if (Integer.parseInt(line.getLineId()) > 600 & Integer.parseInt(line.getLineId()) < 604) {
                            btnLine.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
                            btnLine.setTextColor(ContextCompat.getColor(context, R.color.black));
                        }
                        btnLine.setText(line.getLabel());
                        holder.linesContainer.addView(lineView);
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        View lineView = LayoutInflater.from(context).inflate(R.layout.button_line, holder.linesContainer, false);
                        Button btnLine = lineView.findViewById(R.id.line_button);
                        if (i < 4) {
                            btnLine.setText(lines.get(i).getLabel());
                        } else {
                            btnLine.setText("...");
                        }
                        holder.linesContainer.addView(lineView);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return stopsList != null ? stopsList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStopId, tvStopName;
        CardView card;
        LinearLayout linesContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStopId = itemView.findViewById(R.id.stop_id_Stops);
            tvStopName = itemView.findViewById(R.id.stop_name_Stops);
            card = itemView.findViewById(R.id.busCard_Stops);
            linesContainer = itemView.findViewById(R.id.lines_container);
        }
    }
}
