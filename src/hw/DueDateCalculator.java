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
        final int hourOfDay = startingDateAndTime.get(Calendar.HOUR_OF_DAY);
        final int dayOfWeek = startingDateAndTime.get(Calendar.DAY_OF_WEEK);
        int dayOffset = 0;


        if(hourOfDay < START_OF_DAY || END_OF_DAY < hourOfDay || !DAYS_OF_WEEK.contains(dayOfWeek))
            throw new IllegalArgumentException("A due date is only to be issued from 9 o'clock to 17 o'clock on workdays.");


        Calendar result = Calendar.getInstance();
        if(turnaroundTime + hourOfDay > END_OF_DAY) {
            int calculatedTime = turnaroundTime;
            calculatedTime = calculatedTime - (END_OF_DAY - hourOfDay);

            if(dayOfWeek + dayOffset == Calendar.FRIDAY)
                dayOffset = dayOffset + 4;
            else
                dayOffset++;


            while(calculatedTime + START_OF_DAY > END_OF_DAY) {

                calculatedTime = calculatedTime - (END_OF_DAY - START_OF_DAY);
                if((dayOfWeek + dayOffset % 8) == Calendar.FRIDAY)
                    dayOffset = dayOffset + 4;
                else
                    dayOffset++;
            }

            result.set(Calendar.DAY_OF_WEEK, (dayOfWeek + dayOffset)%8);
            result.set(Calendar.HOUR_OF_DAY, START_OF_DAY + calculatedTime);
        }
        else {

            result.set(Calendar.DAY_OF_WEEK, dayOfWeek + dayOffset);
            result.set(Calendar.HOUR_OF_DAY, hourOfDay + turnaroundTime);
        }

        return result;
    }
}
