package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.sql.Date;
import java.sql.Time;


@Service
public class ShiftService {

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * GetAllShifts: service method to fetch all existing shifts in the database
     * @return List of shifts
     * @throws HRSException if no shifts exist in the system
     */
    @Transactional
    public List<Shift> getAllShifts(){
        List<Shift> shifts = shiftRepository.findAll();
        if (shifts.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no shifts in the system.");
        }
        return shifts;
    }


    /**
     * getShiftByShiftID: service ID to get the shift corresponding to the shift ID
     * @param shiftID: shift ID of the employee
     * @return shift
     * @throws HRSException if the shift doesn't exist
     */
    @Transactional
    public Shift getShiftByShiftID(int shiftID) {
        if (shiftID < 0) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid shift ID.");
        }
        Shift shift = shiftRepository.findShiftByShiftId(shiftID);
        if (shift == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift not found.");
        }
        return shift;
    }
    /**
     * getShiftsByEmployeeEmail: service email to fetch an existing shift list corresponding to an employee's email
     * @param email: email of the employee
     * @return list of shifts
     * @throws HRSException if the list doesn't exist
     */
    @Transactional
    public List<Shift> getShiftsByEmployeeEmail(String email) {
        List<Shift> shiftList = shiftRepository.findShiftsByEmployeeEmail(email);
        if (shiftList.size() == 0) {
            throw new HRSException(HttpStatus.NOT_FOUND, "No shifts found for this email.");
        }
        return shiftList;
    }
    /**
     * getShiftsByEmployeeEmail: service email to fetch an existing shift list corresponding to an certain date
     * @param date: date of the shifts
     * @return list of shifts
     * @throws HRSException if the list doesn't exist
     */
    @Transactional
    public List<Shift> getShiftsByDate(LocalDate date) {
        List<Shift> sdList = shiftRepository.findShiftsByDate(date);
        if (sdList.size() == 0) {
            throw new HRSException(HttpStatus.NOT_FOUND, "No shifts found for this date.");
        }
        return sdList;
    }

    /**
     * getShiftsByDateAndStartTime: service method to fetch an existing shift list corresponding to a certain date and start time
     * @param date: date of the shifts
     * @param startTime: start time of the shifts
     * @return list of shifts
     * @throws HRSException if list doesn't exist
     */
    @Transactional
    public List<Shift> getShiftsByDateAndStartTime(LocalDate date, Time startTime) {
        List<Shift> stList = shiftRepository.findShiftsByDateAndStartTime(date, startTime);
        if (stList.size() == 0) {
            throw new HRSException(HttpStatus.NOT_FOUND, "No shifts found for this date and start time.");
        }
        return stList;
    }

    /**
     * createShift: creates a shift
     * @param shift: shift to create
     * checks for validity of shift
     * @return shift is created
     */
    @Transactional
    public Shift createShift(Shift shift) {
        isValidShift(shift);
        return shiftRepository.save(shift);
    }

    /**
     * deleteShift: deletes a shift
     * @param shiftID: shiftID of shift to be deleted
     * @throws HRSException if shiftID is invalid or no shift exists with that shift ID
     */
    @Transactional
    public void deleteShift(int shiftID) {
        if (shiftID < 0) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Invalid shift ID.");
        }
        if (!shiftRepository.existsById(shiftID)) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Shift does not exist.");
        }
        Shift shift = getShiftByShiftID(shiftID);
        shiftRepository.delete(shift);
    }

    /**
     * updateShift: updates a shift
     * @param shift : shift to update to
     * @param shiftID : old shift that needs updating
     * @throws HRSException if either shift is null, fields are null, or new shift's timing is invalid
     * @return updated shift
     */
    @Transactional
    public Shift updateShift(Shift shift, int shiftID) {
        if (shift == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift not found.");
        }
        if (shift.getStartTime() == null || shift.getEndTime() == null || shift.getDate() == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Empty fields are present.");
        }
        if (shift.getStartTime().after(shift.getEndTime())) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid start/end times.");
        }
        Shift previousShift = getShiftByShiftID(shiftID);
        if (previousShift == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift not found.");
        }


        previousShift.setDate(shift.getDate());
        previousShift.setStartTime(shift.getStartTime());
        previousShift.setEndTime(shift.getEndTime());
        previousShift.setEmployee(shift.getEmployee());

        return shiftRepository.save(previousShift);
    }

    /**
     * isValidShift: helper method used in service methods to check if a shift is valid
     * @param shift : shift that needs checking
     */
    private void isValidShift(Shift shift) {
        if (shift == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift not found.");
        }
        if (shift.getStartTime() == null || shift.getEndTime() == null || shift.getDate() == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Empty fields are present.");
        }
        if (shift.getStartTime().after(shift.getEndTime())) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid start/end times.");
        }
        // checks if an employee already has a shift at the same date and time
        List<Shift> shiftsWithSameDateAndTime = shiftRepository.findShiftsByDateAndStartTime(shift.getDate(), shift.getStartTime());

        if (shiftsWithSameDateAndTime.stream().anyMatch(s -> s.getEmployee().getEmail().equals(shift.getEmployee().getEmail()))) {
            throw new HRSException(HttpStatus.CONFLICT, "A shift with this start date, start time, and employee already exists.");
        }

        // write to determine no person overlaps shift
        // same employee, same date, no overlap can occur
        List<Shift> shiftsOnSameDate = shiftRepository.findShiftsByDate(shift.getDate());

        if (shiftsOnSameDate.stream().anyMatch(existingShift ->
                existingShift.getEmployee().getEmail().equals(shift.getEmployee().getEmail()) &&
                        (shift.getStartTime().before(existingShift.getEndTime()) &&
                                shift.getEndTime().after(existingShift.getStartTime())))) {

            throw new HRSException(HttpStatus.CONFLICT, "The employee has an overlapping shift on this date.");
        }

        Employee employee = shift.getEmployee();

        if (shift.getEmployee() != null && employeeRepository.findEmployeeByEmail(employee.getEmail()) == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Employee does not exist.");
        }
    }
}
