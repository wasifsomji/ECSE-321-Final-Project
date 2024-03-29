package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.RequestRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservedRoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReservationServiceTests {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservedRoomRepository reservedRoomRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void testGetAllReservations() {
        Reservation res1 = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);
        Reservation res2 = new Reservation(5, LocalDate.of(1990,3,4), LocalDate.of(1990,3,7), 6, false, CheckInStatus.BeforeCheckIn, null);
        reservationRepository.save(res1);
        reservationRepository.save(res2);
        List<Reservation> list = new ArrayList<>();

        list.add(res1);
        list.add(res2);

        when(reservationRepository.findAll()).thenReturn(list);

        List<Reservation> reservations = reservationService.getAllReservations();
        assertEquals(2, reservations.size());
        assertEquals(reservations.get(0), list.get(0));
        assertEquals(reservations.get(1), list.get(1));
    }

    @Test
    public void testGetAllReservationsNotPaid() {
        Reservation res1 = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);
        Reservation res2 = new Reservation(5, LocalDate.of(1990,3,4), LocalDate.of(1990,3,7), 6, false, CheckInStatus.BeforeCheckIn, null);
        reservationRepository.save(res1);
        reservationRepository.save(res2);
        List<Reservation> list = new ArrayList<>();

        list.add(res1);
        list.add(res2);

        when(reservationRepository.getReservationByPaidIs(false)).thenReturn(list);

        List<Reservation> reservations = reservationService.getReservationsNotPaid();
        assertEquals(2, reservations.size());
        assertEquals(reservations.get(0), list.get(0));
        assertEquals(reservations.get(1), list.get(1));
    }

    @Test
    public void testGetReservationById() {
        Reservation res1 = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);
        when(reservationRepository.findReservationByReservationID(res1.getReservationID())).thenReturn(res1);

        Reservation res = reservationService.getReservation(res1.getReservationID());

        assertNotNull(res);
        assertEquals(res1, res);
    }

    @Test
    public void testGetReservationInvalidId(){
        int id = 2;
        when(reservationRepository.findReservationByReservationID(id)).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> reservationService.getReservation(id));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "reservation not in the system.");
    }


    @Test
    public void testCreateReservation() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, customer);

        when(reservationRepository.save(res)).thenReturn(res);

        Reservation out = reservationService.createReservation(res);

        assertNotNull(out);
        assertEquals(res, out);
        verify(reservationRepository, times(1)).save(res);
    }

    @Test
    public void testCreateInvalidReservationDate() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,6), LocalDate.of(1990,3,3), 5, false, CheckInStatus.BeforeCheckIn, customer);

        HRSException e = assertThrows(HRSException.class, () -> reservationService.createReservation(res));
        assertEquals(e.getMessage(), "invalid checkIn/checkOut dates");
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateInvalidReservationNegativeValue() {
        Customer customer = new Customer("email", "random", null);
        Reservation res1 = new Reservation(-4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), -5, false, CheckInStatus.BeforeCheckIn, customer);

        HRSException e = assertThrows(HRSException.class, () -> reservationService.createReservation(res1));
        assertEquals(e.getMessage(), "invalid integer");
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetReservationByCustomer() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, customer);
        List<Reservation> list = new ArrayList<>();
        list.add(res);

        when(reservationRepository.getReservationByCustomerEmail(res.getCustomer().getEmail())).thenReturn(list);

        List<Reservation> out = reservationService.getReservationsByCustomer(customer);

        assertNotEquals(0, list.size());
        assertEquals(list.get(0), out.get(0));
    }

    @Test
    public void testCheckInReservationValid() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, customer);
        Reservation res1 = new Reservation(4,LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.CheckedIn, customer);

        when(reservationRepository.findReservationByReservationID(res.getReservationID())).thenReturn(res);

        when(reservationRepository.save(res)).thenReturn(res);

        Reservation out = reservationService.checkIn(res);

        assertEquals(res1.getCheckedIn(), out.getCheckedIn());
    }

    @Test
    public void testCheckInReservationInValid() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.CheckedIn, customer);

        when(reservationRepository.findReservationByReservationID(res.getReservationID())).thenReturn(res);

        HRSException e = assertThrows(HRSException.class, () -> reservationService.checkIn(res));
        assertEquals(e.getMessage(), "already checked in");
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testPayReservationValid() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, customer);
        Reservation res1 = new Reservation(4,LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, true, CheckInStatus.BeforeCheckIn, customer);

        when(reservationRepository.findReservationByReservationID(res.getReservationID())).thenReturn(res);

        when(reservationRepository.save(res)).thenReturn(res);

        Reservation out = reservationService.payReservation(res, 5);

        assertEquals(res1.isPaid(), out.isPaid());
    }

    @Test
    public void testPayReservationInvalidPaid() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, true, CheckInStatus.BeforeCheckIn, customer);

        when(reservationRepository.findReservationByReservationID(res.getReservationID())).thenReturn(res);

        HRSException e = assertThrows(HRSException.class, () -> reservationService.payReservation(res, 5));
        assertEquals(e.getMessage(), "already paid");
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testPayReservationInvalidMoney() {
        Customer customer = new Customer("email", "random", null);
        Reservation res1 = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, customer);

        when(reservationRepository.findReservationByReservationID(res1.getReservationID())).thenReturn(res1);
        HRSException e = assertThrows(HRSException.class, () -> reservationService.payReservation(res1, -5));
        assertEquals(e.getMessage(), "money not sufficient");
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDeleteReservation() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, customer);

        //these should be deleted too
        ReservedRoom room = new ReservedRoom(res, null);
        Request request = new Request(null, null, res);

        List<ReservedRoom> list = new ArrayList<>();
        list.add(room);

        List<Request> list1 = new ArrayList<>();
        list1.add(request);

        when(reservationRepository.findReservationByReservationID(res.getReservationID())).thenReturn(res);
        when(reservedRoomRepository.findReservedRoomsByReservation_ReservationID(res.getReservationID())).thenReturn(list);
        when(requestRepository.findRequestsByReservation_ReservationID(res.getReservationID())).thenReturn(list1);

        assertDoesNotThrow(() -> reservationService.deleteReservation(res));
        assertDoesNotThrow(() -> requestRepository.deleteRequestByRequestId(request.getRequestId()));
        assertDoesNotThrow(() -> reservedRoomRepository.deleteByReservedID(room.getReservedID()));
    }

    @Test
    public void testDeleteInvalidReservation() {
        Customer customer = new Customer("email", "random", null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, customer);

        when(reservationRepository.findReservationByReservationID(res.getReservationID())).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> reservationService.deleteReservation(res));
        assertEquals(e.getMessage(), "reservation does not exist");
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    }

}