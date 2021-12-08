package hw;

import java.util.*;

public class DueDateCalculator {
    private static final int START_OF_DAY = 9;
    private static final int END_OF_DAY = 17;
    private static final List<Integer> DAYS_OF_WEEK = new ArrayList<>(5) {{
        add(Calendar.MONDAY);
        add(Calendar.TUESDAY);
        add(Calendar.WEDNESDAY);
        add(Calendar.THURSDAY);
        add(Calendar.FRIDAY);
    }};

    public Calendar CalculateDueDate(Calendar startingDateAndTime, int turnaroundTime)
    {
        int hourOfDay = startingDateAndTime.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = startingDateAndTime.get(Calendar.DAY_OF_WEEK);
        int dayOffset = 0;


        if(hourOfDay < START_OF_DAY || END_OF_DAY < hourOfDay || !DAYS_OF_WEEK.contains(dayOfWeek))
            throw new IllegalArgumentException("A due date is only to be issued from 9 o'clock to 17 o'clock on workdays.");


        Calendar result = Calendar.getInstance();
        if(turnaroundTime + hourOfDay > END_OF_DAY) {
            int calculatedTime = turnaroundTime;
            dayOffset++;
            calculatedTime = calculatedTime - (END_OF_DAY - hourOfDay);

            while(calculatedTime + START_OF_DAY > END_OF_DAY) {
                dayOffset++;
                calculatedTime = calculatedTime - (END_OF_DAY - START_OF_DAY);
            }



            int weekendOffset = (startingDateAndTime.get(Calendar.DAY_OF_WEEK) - startingDateAndTime.get(Calendar.DAY_OF_WEEK)+dayOffset) * -1;
            if(weekendOffset > 0) dayOffset += weekendOffset;

            result.set(Calendar.DAY_OF_MONTH, startingDateAndTime.get(Calendar.DAY_OF_MONTH) + dayOffset);
            result.set(Calendar.HOUR_OF_DAY, START_OF_DAY + calculatedTime);
        }
        else {

            result.set(Calendar.DAY_OF_WEEK, startingDateAndTime.get(Calendar.DAY_OF_WEEK) + dayOffset);
            result.set(Calendar.HOUR_OF_DAY, startingDateAndTime.get(Calendar.HOUR_OF_DAY) + turnaroundTime);
        }

        return result;
    }
}