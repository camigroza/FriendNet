package com.example.guiex1.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<Long> {
    private LocalDateTime date;
    private Long idPrieten1;
    private Long idPrieten2;
    private String status;

    public Prietenie(Long idPrieten1, Long idPrieten2) {
        this.idPrieten1 = idPrieten1;
        this.idPrieten2 = idPrieten2;
        this.date = LocalDateTime.now();
        this.status = "pending";
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getIdPrieten1() {
        return idPrieten1;
    }

    public void setIdPrieten1(Long idPrieten1) {
        this.idPrieten1 = idPrieten1;
    }

    public Long getIdPrieten2() {
        return idPrieten2;
    }

    public void setIdPrieten2(Long idPrieten2) {
        this.idPrieten2 = idPrieten2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.date = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prietenie prietenie = (Prietenie) o;
        return Objects.equals(date, prietenie.date) && Objects.equals(idPrieten1, prietenie.idPrieten1) && Objects.equals(idPrieten2, prietenie.idPrieten2) && Objects.equals(status, prietenie.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, idPrieten1, idPrieten2, status);
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "date=" + date +
                ", idPrieten1=" + idPrieten1 +
                ", idPrieten2=" + idPrieten2 +
                ", status='" + status + '\'' +
                '}';
    }
}
