package dev.diskettefox.madridbus.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.diskettefox.madridbus.LineActivity;
import dev.diskettefox.madridbus.R;
import dev.diskettefox.madridbus.models.LineModel;

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

        holder.card.setOnClickListener(v -> {
            Intent intent = new Intent(context, LineActivity.class);
            intent.putExtra("lineId", line.getLineId());
            intent.putExtra("lineLabel", line.getLabel());
            context.startActivity(intent);
        });

        Drawable fondo=holder.tvLineLabel.getBackground();

        if (line.getLineId() > 500 & line.getLineId() < 600) {
            GradientDrawable fondoForma= (GradientDrawable) fondo;
            fondoForma.setColor(ContextCompat.getColor(context, R.color.black));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.gold));
        } else if (line.getLineId() == 203) {
            GradientDrawable fondoForma= (GradientDrawable) fondo;
            fondoForma.setColor(ContextCompat.getColor(context, R.color.gold));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else if (line.getLineId() > 360 & line.getLineId() < 362) {
            GradientDrawable fondoForma= (GradientDrawable) fondo;
            fondoForma.setColor(ContextCompat.getColor(context, R.color.turquoise));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (line.getLineId() > 89 & line.getLineId() < 100) {
            GradientDrawable fondoForma= (GradientDrawable) fondo;
            fondoForma.setColor(ContextCompat.getColor(context, R.color.lightblue));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (line.getLineId() > 451 & line.getLineId() < 458) {
            GradientDrawable fondoForma= (GradientDrawable) fondo;
            fondoForma.setColor(ContextCompat.getColor(context, R.color.brown));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (line.getLineId() > 600 & line.getLineId() < 604) {
            GradientDrawable fondoForma= (GradientDrawable) fondo;
            fondoForma.setColor(ContextCompat.getColor(context, R.color.yellow));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            GradientDrawable fondoForma= (GradientDrawable) fondo;
            fondoForma.setColor(ContextCompat.getColor(context, R.color.blue));
            holder.tvLineLabel.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLineLabel, tvLineNameA, tvLineNameB;
        CardView card;
        LinearLayout linesContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLineLabel = itemView.findViewById(R.id.line_label);
            tvLineNameA = itemView.findViewById(R.id.line_name_a);
            tvLineNameB = itemView.findViewById(R.id.line_name_b);
            card = itemView.findViewById(R.id.busCard_Lines);
            linesContainer = itemView.findViewById(R.id.lines_container);
        }
    }
}
