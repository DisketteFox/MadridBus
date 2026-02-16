package dev.diskettefox.madridbus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.diskettefox.madridbus.Api_requests.ModeloStop;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private Context contexto;
    private ArrayList<ModeloStop.Stop> listaDParadas; // Changed to Stop

    public BusAdapter(Context context, ArrayList<ModeloStop.Stop> listaDParadas) { // Changed to Stop
        this.contexto = context;
        this.listaDParadas = listaDParadas;
    }

    @NonNull
    @Override
    public BusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View view = inflater.inflate(R.layout.card_stop, parent, false);
        return new BusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusAdapter.ViewHolder holder, int position) {
        ModeloStop.Stop stop = listaDParadas.get(position);

        // Set stop text
        if (stop != null) {
            holder.tvStopId.setText(stop.getStopId());
            holder.tvStopName.setText(stop.getName());
        }
    }

    @Override
    public int getItemCount() {
        return listaDParadas != null ? listaDParadas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStopId, tvStopName;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStopId = itemView.findViewById(R.id.stop_id_Stops);
            tvStopName = itemView.findViewById(R.id.stop_name_Stops);
            card = itemView.findViewById(R.id.busCard_Stop);
        }
    }
}
