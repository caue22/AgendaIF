package com.example.agendaif;

public class Evento {
    private String nomeEvento;
    private String dataEvento;
    private String horarioInicio;
    private String horarioTermino;
    private String localEvento;

    private String userId;
    private String userName;
    private String userProfilePic;


    public Evento() {
        // Construtor vazio necessário para o Firebase Realtime Database
    }

    public Evento(String nomeEvento, String dataEvento, String horarioInicio, String horarioTermino, String localEvento) {
        this.nomeEvento = nomeEvento;
        this.dataEvento = dataEvento;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.localEvento = localEvento;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(String horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public String getLocalEvento() {
        return localEvento;
    }

    public void setLocalEvento(String localEvento) {
        this.localEvento = localEvento;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }
}


