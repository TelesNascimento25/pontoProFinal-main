package br.com.pontoPro.model;

import java.time.LocalTime;

public class IntervaloDeTempo {
    private LocalTime inicio;
    private LocalTime fim;

    public IntervaloDeTempo(LocalTime inicio, LocalTime fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalTime inicio) {
        this.inicio = inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public void setFim(LocalTime fim) {
        this.fim = fim;
    }
}
