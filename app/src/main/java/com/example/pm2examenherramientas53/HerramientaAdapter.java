package com.example.pm2examenherramientas53;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm2examenherramientas53.Models.HerramientaVista;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HerramientaAdapter extends RecyclerView.Adapter<HerramientaAdapter.ViewHolder> {

    private final List<HerramientaVista> listaOriginal;
    private List<HerramientaVista> listaFiltrada;
    private final Context context;
    private final OnHerramientaListener listener;

    public interface OnHerramientaListener {
        void onHerramientaLongClick(HerramientaVista h);
    }

    public HerramientaAdapter(List<HerramientaVista> lista, Context context, OnHerramientaListener listener) {
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = lista;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_herramienta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HerramientaVista item = listaFiltrada.get(position);
        holder.nombre.setText(item.getHerramienta().getNombre());
        holder.estado.setText("Estado: " + item.getHerramienta().getEstado());
        holder.tecnico.setText("Técnico: " + (item.getTecnicoAsignado() != null ? item.getTecnicoAsignado() : "--"));
        holder.fechaFin.setText("Entrega: " + (item.getFechaFin() != null ? item.getFechaFin() : "--"));

        if (item.getHerramienta().getFoto_uri() != null && !item.getHerramienta().getFoto_uri().isEmpty()) {
            holder.foto.setImageURI(Uri.parse(item.getHerramienta().getFoto_uri()));
        } else {
            holder.foto.setImageResource(R.drawable.ic_launcher_background);
        }

        // Lógica de colores según el estado
        int color = Color.parseColor("#E0E0E0"); // Gris por defecto (Disponible)

        if (item.getFechaDevolucion() != null && !item.getFechaDevolucion().isEmpty()) {
            color = Color.parseColor("#C8E6C9"); // Verde (Devuelta)
        } else if ("ASIGNADA".equalsIgnoreCase(item.getHerramienta().getEstado()) && item.getFechaFin() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date fechaFin = sdf.parse(item.getFechaFin());
                Date hoy = new Date();

                if (hoy.after(fechaFin)) {
                    color = Color.parseColor("#FFCDD2"); // Rojo (Vencida)
                } else {
                    long diff = fechaFin.getTime() - hoy.getTime();
                    long horas = diff / (1000 * 60 * 60);
                    if (horas <= 48) {
                        color = Color.parseColor("#FFF9C4"); // Ámbar (Próxima <= 48h)
                    }
                }
            } catch (Exception ignored) {}
        }

        holder.card.setCardBackgroundColor(color);
        holder.itemView.setOnLongClickListener(v -> {
            listener.onHerramientaLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void filter(String query) {
        if (query.isEmpty()) {
            listaFiltrada = new ArrayList<>(listaOriginal);
        } else {
            String q = query.toLowerCase().trim();
            List<HerramientaVista> filtered = new ArrayList<>();
            for (HerramientaVista item : listaOriginal) {
                if (item.getHerramienta().getNombre().toLowerCase().contains(q) ||
                    (item.getTecnicoAsignado() != null && item.getTecnicoAsignado().toLowerCase().contains(q)) ||
                    item.getHerramienta().getEspecificaciones().toLowerCase().contains(q)) {
                    filtered.add(item);
                }
            }
            listaFiltrada = filtered;
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, estado, tecnico, fechaFin;
        ImageView foto;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.lbl_nombre_herramienta);
            estado = itemView.findViewById(R.id.lbl_estado_herramienta);
            tecnico = itemView.findViewById(R.id.lbl_tecnico_herramienta);
            fechaFin = itemView.findViewById(R.id.lbl_fecha_fin_herramienta);
            foto = itemView.findViewById(R.id.img_herramienta_item);
            card = itemView.findViewById(R.id.card_herramienta);
        }
    }
}
