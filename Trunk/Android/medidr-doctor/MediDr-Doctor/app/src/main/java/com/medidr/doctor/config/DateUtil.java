package com.medidr.doctor.config;

import android.util.Log;

import com.medidr.doctor.model.DocScheduleDtls;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DateUtil {

    private static final String MYSQL_DATE_FORMAT = "yyyy-MM-dd";
    private static final String MYSQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String MYSQL_TIME_24_HR_FORMAT = "HH:mm";

    private static final String DD_MM_YYYY_FORMAT = "dd/MM/yyyy";

    private static final String HH_MM_AM_PM_FORMAT = "hh:mma";
    private static final String HH_MM_SPACE_AM_PM_FORMAT = "hh:mm a";

    private static final String HH_MM_SS = "HH:mm:ss";

    public static String displayDate(DateTime today) {
        today = new DateTime();
        return today.toString("dd-MMM-yyyy");
    }

    public static String getDateForMySQL(String inputString) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(DD_MM_YYYY_FORMAT);
        DateTime inputDate = fmt.parseDateTime(inputString);
        return inputDate.toString(MYSQL_DATE_FORMAT);
    }

    public static String getDateForPresentationLayer(String inputString) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(MYSQL_DATE_FORMAT);
        DateTime inputDate = fmt.parseDateTime(inputString);
        return inputDate.toString(DD_MM_YYYY_FORMAT);
    }

    public static String getDateTimeForPresentationLayer(String inputString) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(MYSQL_DATE_TIME_FORMAT);
        DateTime inputDate = fmt.parseDateTime(inputString);
        return inputDate.toString(DD_MM_YYYY_FORMAT);
    }

    public static String getCurrentTimeForMySQL() {
        DateTime today = new DateTime();
        return today.toString(MYSQL_DATE_TIME_FORMAT);
    }

    public static String getTimeinHHMMFormat() {
        DateTime today = new DateTime();
        return today.toString(MYSQL_TIME_24_HR_FORMAT);
    }


    public static String getCurrentDayOfWeekAsText() {
        DateTime today = new DateTime();
        return today.dayOfWeek().getAsText();
    }

    public static String getFutureTimeForMySQL(Long delay) {
        DateTime now = new DateTime();
        DateTime future = now.plus(delay);
        return future.toString(MYSQL_DATE_TIME_FORMAT);
    }

    public static boolean isDatesInRange(List<String> inputString, String fromString, String toString) {
        boolean flag = false;

        DateTimeFormatter formatter = DateTimeFormat.forPattern(MYSQL_DATE_FORMAT);

        DateTime fromDateTime = formatter.parseDateTime(fromString);
        DateTime toDateTime = formatter.parseDateTime(toString);

        Interval interval = new Interval(fromDateTime, toDateTime);

        for (String inputDt : inputString) {
            DateTime inputDateTime = formatter.parseDateTime(inputDt);
            if (interval.contains(inputDateTime)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public static String getTimeIn24HrsFmt(String inputTime) {
        String inputDateTimeStr = inputTime;
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(HH_MM_SPACE_AM_PM_FORMAT);
            DateTimeFormatter processedFormatter = DateTimeFormat.forPattern(HH_MM_SS);
            DateTime inputDateTime = formatter.parseDateTime(inputDateTimeStr);
            inputDateTimeStr = processedFormatter.print(inputDateTime);
        } catch (Exception exception) {
            Log.d("TAG", "Exception in processing datetime");
        }
        return inputDateTimeStr;
    }

    public static String getTimeIn12HrsFmt(String inputTime) {
        String inputDateTimeStr = inputTime;
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(HH_MM_SS);
            DateTimeFormatter processedFormatter = DateTimeFormat.forPattern(HH_MM_SPACE_AM_PM_FORMAT);
            DateTime inputDateTime = formatter.parseDateTime(inputDateTimeStr);
            inputDateTimeStr = processedFormatter.print(inputDateTime);
        } catch (Exception exception) {
            Log.d("TAG", "Exception in processing datetime");
        }
        return inputDateTimeStr;
    }

    public static boolean isValidTime(String fromString, String toString, boolean is12hr) {
        boolean flag = false;
        DateTimeFormatter formatter = null;
        if (is12hr == true) {
            formatter = DateTimeFormat.forPattern(HH_MM_SPACE_AM_PM_FORMAT);
        } else {
            formatter = DateTimeFormat.forPattern(HH_MM_SS);
        }

        if (fromString != "" && toString != "") {
            DateTime fromDateTime = formatter.parseDateTime(fromString);
            fromDateTime = fromDateTime.plusSeconds(1);
            DateTime toDateTime = formatter.parseDateTime(toString);
            toDateTime = toDateTime.minusSeconds(1);
            if (toDateTime.isBefore(fromDateTime)) {
                flag = true;
            } else if (toDateTime.isEqual(fromDateTime)) {
                flag = true;
            } else if (toDateTime.isAfter(fromDateTime)) {
                flag = false;
            }
        }
        return flag;
    }

    public static boolean isScheduleEndTimeBeforeStartTime(String startTime, String toTime) {
        boolean flag = true;
        DateTimeFormatter formatter = DateTimeFormat.forPattern(HH_MM_SS);

        DateTime fromDateTime = formatter.parseDateTime(startTime);
        DateTime toDateTime = formatter.parseDateTime(toTime);

        if (toDateTime.isBefore(fromDateTime) || toDateTime.isEqual(fromDateTime)) {
            flag = false;
        }
        return flag;
    }


    public static boolean isDatesInRange(Map<String, String> inputTimings, String fromString, String toString) {
        boolean flag = false;

        DateTimeFormatter formatter = DateTimeFormat.forPattern(HH_MM_SS);

        DateTime fromDateTime = formatter.parseDateTime(fromString);
        fromDateTime = fromDateTime.plusSeconds(1);

        DateTime toDateTime = formatter.parseDateTime(toString);
        toDateTime = toDateTime.minusSeconds(1);

        Interval interval = new Interval(fromDateTime, toDateTime);

        Iterator<String> fromTimeStrsIter = inputTimings.keySet().iterator();
        while (fromTimeStrsIter.hasNext()) {
            String fromTime = fromTimeStrsIter.next();
            DateTime inputFromTime = formatter.parseDateTime(fromTime);
            if (interval.contains(inputFromTime)) {
                flag = true;
                break;
            }

            String toTime = inputTimings.get(fromTime);
            DateTime inputToTime = formatter.parseDateTime(toTime);
            if (interval.contains(inputToTime)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static boolean isDatesInRange(List<DocScheduleDtls> existingSchedules, DocScheduleDtls updateSchedule) {
        boolean flag = false;

        boolean isvalidFromTime = false;
        boolean isvalidToTime = false;
        DateTimeFormatter formatter = DateTimeFormat.forPattern(HH_MM_SS);

        int dayId = Integer.parseInt(updateSchedule.getDayId());

        String fromTime = updateSchedule.getTimeFrom();
        DateTime inputFromTime = formatter.parseDateTime(fromTime);

        String toTime = updateSchedule.getTimeTo();
        DateTime inputToTime = formatter.parseDateTime(toTime);

        for (DocScheduleDtls currentSchedule : existingSchedules) {
            //TODO check currentSchedule.getTimeFrom , currentSchedule.getTimeTo format if its 12 hour change it to 24 hour
            String currentFromTime = currentSchedule.getTimeFrom();
            String currentToTime = currentSchedule.getTimeTo();


            isvalidFromTime = checkExistingFormatOfDate(currentFromTime);
            if(!isvalidFromTime){
                //TODO convert it into 24 hour foemt
                currentFromTime = getTimeIn24HrsFmt(currentFromTime);
            }

            isvalidToTime = checkExistingFormatOfDate(currentToTime);
            if(!isvalidToTime)
            {
                currentToTime = getTimeIn24HrsFmt(currentToTime);
            }
            DateTime fromDateTime = formatter.parseDateTime(currentFromTime);

            DateTime toDateTime = formatter.parseDateTime(currentToTime);

            Interval interval = new Interval(fromDateTime, toDateTime);

            if (currentSchedule.getDocScheId() != updateSchedule.getDocScheId() && !flag) {

                if (Integer.parseInt(currentSchedule.getDayId()) == dayId) {

                    if (interval.contains(inputFromTime)) {
                        //check for dayid
                        flag = true;
                        break;
                    }

                    if (interval.contains(inputToTime)) {
                        flag = true;
                        break;
                    }

                    if ( (inputFromTime.isBefore(fromDateTime) || inputFromTime.isEqual(fromDateTime)) && (inputToTime.isAfter(toDateTime) || inputToTime.isEqual(toDateTime))) {
                        flag = true;
                        break;
                    }
                }
            }

        }
        return flag;
    }


    public static boolean isDatesInRangeAdd(List<DocScheduleDtls> existingSchedules, DocScheduleDtls updateSchedule, int index) {
        boolean flag = false;


        DateTimeFormatter formatter = DateTimeFormat.forPattern(HH_MM_SPACE_AM_PM_FORMAT);

        if (updateSchedule.getTimeFrom() != "" && updateSchedule.getTimeTo() != "") {
            int i = 0;
            for (DocScheduleDtls currentSchedule : existingSchedules) {

                if (i != index) {
                    DateTime fromDateTime = formatter.parseDateTime(currentSchedule.getTimeFrom());

                    DateTime toDateTime = formatter.parseDateTime(currentSchedule.getTimeTo());

                    int dayId = Integer.parseInt(updateSchedule.getDayId());

                    Interval interval = new Interval(fromDateTime, toDateTime);

                    String fromTime = updateSchedule.getTimeFrom();
                    DateTime inputFromTime = formatter.parseDateTime(fromTime);
                    if (Integer.parseInt(currentSchedule.getDayId()) == dayId) {
                        if (interval.contains(inputFromTime)) {
                            //check for dayid

                            flag = true;
                            break;
                        }


                        String toTime = updateSchedule.getTimeTo();
                        DateTime inputToTime = formatter.parseDateTime(toTime);
                        if (interval.contains(inputToTime)) {
                            flag = true;
                            break;

                        }

                        if (inputFromTime.isBefore(fromDateTime) && inputToTime.isAfter(toDateTime)) {
                            flag = true;
                            break;
                        }
                    }

                }
                i++;
                return flag;
            }
        }
        return flag;
    }

public static  boolean checkExistingFormatOfDate(String inputDate)
{
    boolean isValidDate = true;
    Date date = null;
    try {

        SimpleDateFormat sdf = new SimpleDateFormat(HH_MM_SS);
        date = sdf.parse(inputDate);
        if (!inputDate.equals(sdf.format(date))) {
            date = null;
        }
    } catch (ParseException ex) {
        ex.printStackTrace();
    }
    if (date == null) {
        // Invalid date format
        isValidDate = false;
    } else {
        // Valid date format
        isValidDate = true;
    }
    return isValidDate;
}
}
