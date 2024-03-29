package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int requestId;
    private CompletionStatus status;
    private String description;

    @ManyToOne
    private Reservation reservation;

    public Request(CompletionStatus status, String description, Reservation reservation) {
        this.status = status;
        this.description = description;
        this.reservation = reservation;
    }

    public Request() {
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletionStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setStatus(CompletionStatus status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Repair{" +
                "status=" + status +
                ", description='" + description + '\'' +
                ", reservation=" + reservation +
                '}';
    }
}