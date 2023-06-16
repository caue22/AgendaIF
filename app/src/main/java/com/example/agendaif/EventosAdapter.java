package com.example.agendaif;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolderClass> {
    //ViewHolder é responsável por manter as referências aos elementos do layout

    // Fonte de dados para o adptador
    List<Evento> eventosList;

    public EventosAdapter(List<Evento> eventosList) {
        this.eventosList = eventosList;
    }
    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item_evento.xml para criar a visualização do item do evento
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
        // Retornar uma instância de ViewHolderClass contendo a visualização do item do evento
        return new ViewHolderClass(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        // Obter o evento na posição específica da lista
        Evento evento = eventosList.get(position);

        // Definir os valores dos TextViews do item do evento com base nos dados do evento
        holder.textNomeEvento.setText(evento.getNomeEvento());
        holder.textDataEvento.setText(evento.getDataEvento());
        holder.textHorarioInicio.setText(evento.getHorarioInicio());
        holder.textHorarioTermino.setText(evento.getHorarioTermino());
        holder.textLocalEvento.setText(evento.getLocalEvento());
    }
    @Override
    public int getItemCount() {
        // Retornar o número total de eventos na lista
        return eventosList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView textNomeEvento;
        TextView textDataEvento;
        TextView textHorarioInicio;
        TextView textHorarioTermino;
        TextView textLocalEvento;

        public ViewHolderClass(View itemView) {
            super(itemView);
            // Obter as referências dos TextViews no layout do item_evento.xml
            textNomeEvento = itemView.findViewById(R.id.eventoNome);
            textDataEvento = itemView.findViewById(R.id.viewData);
            textHorarioInicio = itemView.findViewById(R.id.viewInicio);
            textHorarioTermino = itemView.findViewById(R.id.viewFinal);
            textLocalEvento = itemView.findViewById(R.id.viewLocal);
        }
    }
}
