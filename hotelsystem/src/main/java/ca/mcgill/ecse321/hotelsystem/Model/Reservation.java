package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reservationID;
    private int numPeople;
    private Date checkin;
    private Date checkOut;
    private int totalPrice;
    private boolean paid;
    private CheckInStatus checkedIn;

    @ManyToOne
    private Customer customer;


    public Reservation( int numPeople, Date checkin, Date checkOut, int totalPrice, boolean paid, CheckInStatus checkedIn, Customer customer) {
        this.numPeople = numPeople;
        this.checkin = checkin;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.checkedIn = checkedIn;
        this.customer = customer;
    }

    public Reservation() {
    }

    public int getReservationID() {
        return reservationID;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public Date getCheckIn() {
        return checkin;
    }

    public void setCheckIn(Date checkIn) {
        this.checkin = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public CheckInStatus getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(CheckInStatus checkedIn) {
        this.checkedIn = checkedIn;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}