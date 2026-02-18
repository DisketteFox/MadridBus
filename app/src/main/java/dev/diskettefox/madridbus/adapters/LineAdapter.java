package dev.diskettefox.madridbus.adapters;

import android.content.Context;
import android.content.Intent;
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
import dev.diskettefox.madridbus.api.LineModel;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<LineModel.Data> data;

    public LineAdapter(Context context, ArrayList<LineModel.Data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public LineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_line, parent, false);
        return new LineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineAdapter.ViewHolder holder, int position) {
        LineModel.Data line = data.get(position);
        holder.tvLineLabel.setText(line.getLabel());
        holder.tvLineNameA.setText(line.getNameA());
        holder.tvLineNameB.setText(line.getNameB());

        if (line.getLineId() > 500 & line.getLineId() < 600) {
            holder.tvLineLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.gold));
        } else if (line.getLineId() == 203) {
            holder.tvLineLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else if (line.getLineId() > 360 & line.getLineId() < 362) {
            holder.tvLineLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.turquoise));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (line.getLineId() > 89 & line.getLineId() < 100) {
            holder.tvLineLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (line.getLineId() > 451 & line.getLineId() < 458) {
            holder.tvLineLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.brown));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (line.getLineId() > 600 & line.getLineId() < 604) {
            holder.tvLineLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.tvLineLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLineLabel, tvLineNameA, tvLineNameB;
        LinearLayout linesContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLineLabel = itemView.findViewById(R.id.line_label);
            tvLineNameA = itemView.findViewById(R.id.line_name_a);
            tvLineNameB = itemView.findViewById(R.id.line_name_b);
            linesContainer = itemView.findViewById(R.id.lines_container);
        }
    }
}
