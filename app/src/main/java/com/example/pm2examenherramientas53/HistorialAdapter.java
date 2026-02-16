package com.example.pm2examenherramientas53;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private final List<Map<String, String>> listaHistorial;

    public HistorialAdapter(List<Map<String, String>> lista) {
        this.listaHistorial = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> item = listaHistorial.get(position);
        holder.herramienta.setText("Herramienta: " + item.get("herramienta"));
        holder.tecnico.setText("Técnico: " + item.get("tecnico"));
        holder.inicio.setText("Inicio: " + item.get("inicio"));
        holder.fin.setText("Fin: " + item.get("fin"));
        
        String devolucion = item.get("devolucion");
        if (devolucion == null || devolucion.isEmpty()) {
            holder.devolucion.setText("Devolución: PENDIENTE");
            holder.devolucion.setTextColor(0xFFD32F2F); // Rojo
        } else {
            holder.devolucion.setText("Devuelto el: " + devolucion);
            holder.devolucion.setTextColor(0xFF388E3C); // Verde
        }
    }

    @Override
    public int getItemCount() {
        return listaHistorial.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView herramienta, tecnico, inicio, fin, devolucion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            herramienta = itemView.findViewById(R.id.hist_herramienta);
            tecnico = itemView.findViewById(R.id.hist_tecnico);
            inicio = itemView.findViewById(R.id.hist_fecha_inicio);
            fin = itemView.findViewById(R.id.hist_fecha_fin);
            devolucion = itemView.findViewById(R.id.hist_fecha_devolucion);
        }
    }
}
