package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;

import java.time.LocalDate;

public class ReservationResponseDto {
    private int reservationId;
    private int numPeople;
    private LocalDate checkin;
    private LocalDate checkOut;
    private int totalPrice;
    private Boolean paid;
    private CheckInStatus checkedIn;

    private Customer customer;

    public ReservationResponseDto() {

    }

    public ReservationResponseDto(Reservation reservation){
        this.reservationId = reservation.getReservationID();
        this.numPeople = reservation.getNumPeople();
        this.checkin = reservation.getCheckIn();
        this.checkOut = reservation.getCheckOut();
        this.totalPrice = reservation.getTotalPrice();
        this.paid = reservation.isPaid();
        this.checkedIn = reservation.getCheckedIn();
        this.customer = reservation.getCustomer();
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
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
