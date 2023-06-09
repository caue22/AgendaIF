package com.example.agendaif;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventosAdapter extends RecyclerView.Adapter {

    List<Evento> eventosList;

    public EventosAdapter(List<Evento> eventosList) {

        this.eventosList = eventosList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
        ViewHolderClass vhClass = new ViewHolderClass(view);
        return vhClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass vhClass = (ViewHolderClass) holder;

        Evento evento = eventosList.get(position);


        ((ViewHolderClass) holder).textNomeEvento.setText(evento.getNomeEvento());
        ((ViewHolderClass) holder).textDataEvento.setText(evento.getDataEvento());
        ((ViewHolderClass) holder).textHorarioInicio.setText(evento.getHorarioInicio());
        ((ViewHolderClass) holder).textHorarioTermino.setText(evento.getHorarioTermino());
        ((ViewHolderClass) holder).textLocalEvento.setText(evento.getLocalEvento());
    }

    @Override
    public int getItemCount() {
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
            textNomeEvento = itemView.findViewById(R.id.eventoNome);
            textDataEvento = itemView.findViewById(R.id.viewData);
            textHorarioInicio = itemView.findViewById(R.id.viewInicio);
            textHorarioTermino = itemView.findViewById(R.id.viewFinal);
            textLocalEvento = itemView.findViewById(R.id.viewLocal);
        }


    }
}
