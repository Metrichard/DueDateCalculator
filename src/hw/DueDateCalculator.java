package hw;

import java.util.*;

public class DueDateCalculator {
    private static final int START_OF_DAY = 9;
    private static final int END_OF_DAY = 17;
    private static final List<Integer> DAYS_OF_WEEK = new ArrayList<>() {{
        add(Calendar.MONDAY);
        add(Calendar.TUESDAY);
        add(Calendar.WEDNESDAY);
        add(Calendar.THURSDAY);
        add(Calendar.FRIDAY);
    }};

    public Calendar CalculateDueDate(Calendar startingDateAndTime, int turnaroundTime) {
        final int startingHour = startingDateAndTime.get(Calendar.HOUR_OF_DAY);
        final int startingDay = startingDateAndTime.get(Calendar.DAY_OF_WEEK);
        int dayOffset = 0;

        if(startingHour < START_OF_DAY || END_OF_DAY < startingHour || !DAYS_OF_WEEK.contains(startingDay))
            throw new IllegalArgumentException("A due date is only to be issued from 9 o'clock to 17 o'clock on workdays.");
        
        if(turnaroundTime + startingHour > END_OF_DAY) {
            int timeOffSet = turnaroundTime;
            timeOffSet = timeOffSet - (END_OF_DAY - startingHour);

            dayOffset = startingDay + dayOffset == Calendar.FRIDAY ? dayOffset + 4 : dayOffset + 1;

            while(timeOffSet + START_OF_DAY > END_OF_DAY) {
                timeOffSet = timeOffSet - (END_OF_DAY - START_OF_DAY);
                dayOffset = (startingDay + dayOffset % 8) == Calendar.FRIDAY ? dayOffset + 4 : dayOffset + 1;
            }
            return CreateResultCalendar((startingDay + dayOffset)%8, START_OF_DAY + timeOffSet);
        }
        return CreateResultCalendar(startingDay + dayOffset, startingHour + turnaroundTime);
    }

    private Calendar CreateResultCalendar(int day, int hour) {
        Calendar result = Calendar.getInstance();
        result.set(Calendar.DAY_OF_WEEK, day);
        result.set(Calendar.HOUR_OF_DAY, hour);
        return result;
    }
}
