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

import dev.diskettefox.madridbus.Api_requests.Modelo_parada;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private Context contexto;
    private ArrayList<Modelo_parada.Parada> listaDParadas;
    public BusAdapter(Context context, ArrayList<Modelo_parada.Parada>listaDParadas) {
        this.contexto = context;
        this.listaDParadas=listaDParadas;
    }

    @NonNull
    @Override
    public BusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(contexto);
        View view=inflater.inflate(R.layout.card_stop,parent,false);
        return new BusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusAdapter.ViewHolder holder, int position) {
        holder.tvStopId.setText(listaDParadas.get(position).getIdParada());
        holder.tvStopName.setText(listaDParadas.get(position).getNombreParada());

    }

    @Override
    public int getItemCount() {return listaDParadas.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStopId, tvStopName;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStopId=itemView.findViewById(R.id.stop_id_Stops);
            tvStopName=itemView.findViewById(R.id.stop_name_Stops);

        }
    }
}