package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReservationRepositoryTests {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void clearDatabase() {
        reservationRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadReservation() {
        Customer customer = new Customer("bill@gmail.com", "Bill", null);
        customer = customerRepository.save(customer);

        Reservation reservation = new Reservation(2, LocalDate.of(2023,10,23), LocalDate.of(2023,11,01), 1400, false, CheckInStatus.BeforeCheckIn, customer);
        reservation = reservationRepository.save(reservation);

        // retrieves reservation from reservation ID
        Reservation reservationRep = reservationRepository.findReservationByReservationID(reservation.getReservationID());

        // asserts retrieved reservation and verifies properties
        assertNotNull(reservationRep);
        assertEquals(reservation.getCheckIn(), reservationRep.getCheckIn());
        assertEquals(reservation.getCheckOut(), reservationRep.getCheckOut());
        assertEquals(2, reservationRep.getNumPeople());
        assertFalse(reservationRep.isPaid());
    }

    @Test
    public void testFindByCheckInDate(){
        Customer customer = new Customer("bill@gmail.com", "Bill", null);
        customer = customerRepository.save(customer);


        LocalDate checkInDate = LocalDate.of(2023,10,23);
        LocalDate checkOutDate = LocalDate.of(2023,11,1);
        Reservation reservation = new Reservation(2, checkInDate, checkOutDate, 1400, false, CheckInStatus.BeforeCheckIn, customer);
        reservation = reservationRepository.save(reservation);


        checkOutDate = LocalDate.of(2023,10,5);
        Reservation reservation2 = new Reservation(2, checkInDate, checkOutDate, 1700, false, CheckInStatus.BeforeCheckIn, customer);

        reservation2 = reservationRepository.save(reservation2);

        // retrieves properties by check ind date
        List<Reservation> reservations = reservationRepository.findReservationsByCheckin(checkInDate);

        // asserts reservations' repository, as well as properties of the reservations
        assertEquals(2, reservations.size());
        assertEquals(LocalDate.of(2023,11,1), reservations.get(0).getCheckOut());
        assertEquals(LocalDate.of(2023,10,5), reservations.get(1).getCheckOut());
    }


    @Test
    public void testFindReservationsByCustomerEmail(){
        Customer customer = new Customer("bill@gmail.com", "Bill", null);
        customer = customerRepository.save(customer);

        LocalDate checkInDate = LocalDate.of(2023,10,23);
        LocalDate checkOutDate = LocalDate.of(2023,11,1);
        Reservation reservation = new Reservation(2, checkInDate, checkOutDate, 1400, false, CheckInStatus.BeforeCheckIn, customer);
        reservation = reservationRepository.save(reservation);

        checkOutDate = LocalDate.of(2023,10,5);
        Reservation reservation2 = new Reservation(2, checkInDate, checkOutDate, 1700, false, CheckInStatus.BeforeCheckIn, customer);
        reservation2 = reservationRepository.save(reservation2);

        // retrieves the reservation by customer email
        List<Reservation> reservations = reservationRepository.findReservationsByCustomerEmail("bill@gmail.com");

        // asserts reservations' repository, as well as properties of the reservations
        assertEquals(2, reservations.size());
        assertEquals(LocalDate.of(2023,11,1), reservations.get(0).getCheckOut());
        assertEquals(LocalDate.of(2023,10,5), reservations.get(1).getCheckOut());
    }

    @Test
    @Transactional
    public void testDeleteById(){
        Customer customer = new Customer("bill@gmail.com", "Bill", null);
        customer = customerRepository.save(customer);

        LocalDate checkInDate = LocalDate.of(2023,10,23);
        LocalDate checkOutDate = LocalDate.of(2023,11,1);
        Reservation reservation = new Reservation(2, checkInDate, checkOutDate, 1400, false, CheckInStatus.BeforeCheckIn, customer);
        reservation = reservationRepository.save(reservation);
        int reservationID = reservation.getReservationID();

        Reservation reservationRep = reservationRepository.findReservationByReservationID(reservationID);

        assertNotNull(reservationRep);

        // deletes the reservation from the reservation
        reservationRepository.deleteReservationByReservationID(reservationID);

        reservationRep = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        assertNull(reservationRep); // makes sure the reservation is deleted
    }
}