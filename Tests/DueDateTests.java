import hw.DueDateCalculator;
import hw.Main;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import javax.security.auth.login.AccountLockedException;
import java.util.*;
import java.util.stream.Stream;

public class DueDateTests {

    private static DueDateCalculator dueDateCalculator;
    private static Calendar calendar;

    private static Stream<Arguments> provideIntForValidSubmissions() {
        return Stream.of(
                Arguments.of(9, 5),
                Arguments.of(10, 2),
                Arguments.of(11, 5),
                Arguments.of(12, 3),
                Arguments.of(13, 2),
                Arguments.of(14, 3),
                Arguments.of(15, 2),
                Arguments.of(16, 1)
        );
    }

    private static Stream<Arguments> provideIntForValidSubmissionsOverEightHoursTurnaround() {
        return Stream.of(
                Arguments.of(9, 8, Calendar.getInstance().get(Calendar.DAY_OF_WEEK), 17),
                Arguments.of(10, 8, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+1, 10),
                Arguments.of(11, 9, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+1, 12),
                Arguments.of(12, 10, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+1, 14),
                Arguments.of(13, 11, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+1, 16),
                Arguments.of(14, 12, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+2, 10),
                Arguments.of(15, 13, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+2, 12),
                Arguments.of(16, 16, Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+2, 16)
        );
    }

    private static Stream<Arguments> provideArgumentsForNextWeekTests() {
        return Stream.of(
                Arguments.of(Calendar.FRIDAY, 10, 16, Calendar.TUESDAY, 10),
                Arguments.of(Calendar.THURSDAY, 10, 24, Calendar.TUESDAY, 10),
                Arguments.of(Calendar.WEDNESDAY, 10, 32, Calendar.TUESDAY, 10),
                Arguments.of(Calendar.TUESDAY, 10, 40, Calendar.TUESDAY, 10),
                Arguments.of(Calendar.MONDAY, 10, 48, Calendar.TUESDAY, 10)
        );
    }

    private static Stream<Arguments> provideArgumentsForMultipleWeekTests() {
        return Stream.of(
                Arguments.of(Calendar.FRIDAY, 10, 56, Calendar.TUESDAY, 10),
                Arguments.of(Calendar.FRIDAY, 10, 112, Calendar.THURSDAY, 10),
                Arguments.of(Calendar.FRIDAY, 10, 168, Calendar.MONDAY, 10)
        );
    }

    @BeforeAll
    public static void SetUp(){
        dueDateCalculator = new DueDateCalculator();
        calendar = Calendar.getInstance();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24})
    public void CalculateDueDate_SubmitDateOutsideOfWorkHours_ThrowsIllegalArgumentException(int hour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class ,() -> dueDateCalculator.CalculateDueDate(calendar, 8));
        Assertions.assertEquals("A due date is only to be issued from 9 o'clock to 17 o'clock on workdays.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {Calendar.SATURDAY, Calendar.SUNDAY})
    public void CalculateDueDate_SubmitDateIsWeekend_ThrowsIllegalArgumentException(int day) {
        calendar.set(Calendar.DAY_OF_WEEK, day);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class ,() -> dueDateCalculator.CalculateDueDate(calendar, 8));
        Assertions.assertEquals("A due date is only to be issued from 9 o'clock to 17 o'clock on workdays.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 10, 11, 12, 13, 14, 15, 16})
    public void CalculateDueDate_SubmitDatesHaveOneHourTurnAround_ReturnsDueDate(int hour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        Assertions.assertEquals(calendar.get(Calendar.HOUR_OF_DAY)+1, dueDateCalculator.CalculateDueDate(calendar, 1).get(Calendar.HOUR_OF_DAY));
    }

    @ParameterizedTest
    @MethodSource("provideIntForValidSubmissions")
    public void CalculateDueDate_SubmitHoursHaveMultipleValuesAsTurnAround_ReturnsDueDate(int hour, int turnAround) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        Assertions.assertEquals(calendar.get(Calendar.HOUR_OF_DAY)+turnAround, dueDateCalculator.CalculateDueDate(calendar, turnAround).get(Calendar.HOUR_OF_DAY));
    }

    @ParameterizedTest
    @MethodSource("provideIntForValidSubmissionsOverEightHoursTurnaround")
    public void CalculateDueDate_SubmitHoursHaveMoreThanEightHoursOfTurnaround_ReturnsDueDate(int hour, int turnAround, int expectedDay, int expectedHour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        Calendar result = dueDateCalculator.CalculateDueDate(calendar, turnAround);

        Assertions.assertEquals(result.get(Calendar.DAY_OF_WEEK), expectedDay);
        Assertions.assertEquals(result.get(Calendar.HOUR_OF_DAY), expectedHour);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForNextWeekTests")
    public void CalculateDueDate_SubmitDueDateWouldEndOnWeekend_ReturnsDueDateOnNextWeek(int day, int hour, int turnaround, int expectedDay, int expectedHour) {
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        Calendar result = dueDateCalculator.CalculateDueDate(calendar, turnaround);

        Assertions.assertEquals(expectedDay, result.get(Calendar.DAY_OF_WEEK));
        Assertions.assertEquals(expectedHour, result.get(Calendar.HOUR_OF_DAY));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForMultipleWeekTests")
    public void CalculateDueDate_SubmitDueDateIsMultipleWeeksAway_ReturnsDueDateMultipleWeeksLater(int day, int hour, int turnaround, int expectedDay, int expectedHour) {
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        Calendar result = dueDateCalculator.CalculateDueDate(calendar, turnaround);

        Assertions.assertEquals(expectedDay, result.get(Calendar.DAY_OF_WEEK));
        Assertions.assertEquals(expectedHour, result.get(Calendar.HOUR_OF_DAY));
    }
}
