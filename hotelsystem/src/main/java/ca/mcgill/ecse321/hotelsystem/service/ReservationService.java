package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.RequestRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservedRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservedRoomRepository reservedRoomRepository;

    @Autowired
    RequestRepository requestRepository;

    /**
     * GetAllReservations: service method to fetch all existing reservations in the database
     * @return List of reservations
     * @throws HRSException if no reservations exist in the system
     */
    @Transactional
    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    /**
     * getReservation: get reservation with id
     * @param id unique id
     * @return reservation
     */
    @Transactional
    public Reservation getReservation(int id) {
        Reservation res = reservationRepository.findReservationByReservationID(id);
        if(res == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation not in the system.");
        }
        return res;
    }

    /**
     * createReservation: service method to create a reservation and assign it to a customer
     * @param reservation reservation to be created
     * @return a created reservation
     */
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        isValid(reservation);
        //LOOK CONTROLLER FOR RESERVATION CREATERESERVATION
//        if(reservationRepository.findReservationByReservationID(reservation.getReservationID()) != null) {
//            throw new HRSException(HttpStatus.CONFLICT, "reservation with id already exists");
//        }
        //assume customer is valid
        //reservation.setCustomer(customer);
        return reservationRepository.save(reservation);
    }

    private void isValid(Reservation reservation) {
        if(reservation.getCheckIn().isAfter(reservation.getCheckOut())){
            throw new HRSException(HttpStatus.BAD_REQUEST, "invalid checkIn/checkOut dates");
        }
        if(reservation.getNumPeople() <= 0 || reservation.getTotalPrice() < 0) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "invalid integer");
        }
    }

    /**
     * deleteReservation: deletes a reservation
     * @param reservation reservation to be deleted
     */
    @Transactional
    public void deleteReservation(Reservation reservation) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
        }
        for(ReservedRoom room: reservedRoomRepository.findReservedRoomsByReservation_ReservationID(reservation.getReservationID())) {
            reservedRoomRepository.deleteByReservedID(room.getReservedID());
        }

        for(Request request: requestRepository.findRequestsByReservation_ReservationID(reservation.getReservationID())) {
            requestRepository.deleteRequestByRequestId(request.getRequestId());
        }
        reservationRepository.delete(reservation);
    }

    /**
     * getReservationsByCustomer: service method to get reservations of a customer
     * @param customer customer to select reservations
     * @return list of reservations
     */
    @Transactional
    public List<Reservation> getReservationsByCustomer(Customer customer) {
        //no need to if check customer is valid
        //already checked when we use customerService.getCustomerByEmail(customerEmail) in the controller
        return reservationRepository.getReservationByCustomerEmail(customer.getEmail());
    }
    /**
     * payReservation: method to pay a reservation and returns change
     * @param reservation reservation to be paid
     * @param money amount of money provided
     * @return change
     */
    @Transactional
    public Reservation payReservation(Reservation reservation, int money) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation.isPaid()) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "already paid");
        }

        int remaining = reservation.getTotalPrice() - money;

//        if(change < 0) {
//            throw new HRSException(HttpStatus.BAD_REQUEST, "money not sufficient");
//        }

        //allow for incremental payment
        if(remaining <= 0) {
            reservation.setPaid(true);
            reservation.setTotalPrice(remaining);
        } else {
            reservation.setTotalPrice(remaining);
        }

        return reservationRepository.save(reservation);
    }

    /**
     * check in for a reservation
     * @param reservation reservation
     * @return updated reservation
     */
    @Transactional
    public Reservation checkIn(Reservation reservation) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation.getCheckedIn() == CheckInStatus.CheckedIn) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "already checked in");
        }
        reservation.setCheckedIn(CheckInStatus.CheckedIn);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation checkOut(Reservation reservation) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation.getCheckedIn() == CheckInStatus.CheckedOut) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "already checked out");
        }
        if(reservation.getCheckedIn() == CheckInStatus.BeforeCheckIn) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "pending reservation, not checked in");
        }
        reservation.setCheckedIn(CheckInStatus.CheckedOut);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation noShow(Reservation reservation) {
        if(reservation.getCheckedIn() != CheckInStatus.BeforeCheckIn) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "customer is checkedIn or already checkedOut");
        }
        reservation.setCheckedIn(CheckInStatus.NoShow);
        return reservationRepository.save(reservation);
    }

    /**
     * get non paid reservations
     * @return list of reservations
     */
    @Transactional
    public List<Reservation> getReservationsNotPaid() {
        return reservationRepository.getReservationByPaidIs(false);
    }

    /**
     * cancel a reservation
     * @param reservation
     */
    @Transactional
    public void cancelReservation(Reservation reservation) {
        LocalDate present = LocalDate.now();
        if (ChronoUnit.DAYS.between(reservation.getCheckIn(), present) < 3) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "cannot cancel less than 72 hours before checkIn date");
        }

        //delete it
        this.deleteReservation(reservation);
    }

    //TODO if other methods are needed, add
}
