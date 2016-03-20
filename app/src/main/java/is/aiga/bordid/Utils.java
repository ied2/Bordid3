package is.aiga.bordid;

import java.util.ArrayList;
import java.util.Calendar;

public class Utils {

    public static String[] monthsArray = {"January", "February", "March", "April"
            , "May", "June", "July", "August", "September"
            , "October", "November", "December"};

    // DatePicker
    // Gets the suffix for the day
    public static String getSuffix(int dayOfMonth) {

        // The suffix arrays and array lists
        ArrayList<Integer> arrayList_st = new ArrayList<>();
        int[] array_st = {1, 21, 31};
        ArrayList<Integer> arrayList_nd = new ArrayList<>();
        int[] array_nd = {2, 22};
        ArrayList<Integer> arrayList_rd = new ArrayList<>();
        int[] array_rd = {3, 23};

        // Create suffix array lists
        for (int i = 0; i < array_st.length; i++) {
            arrayList_st.add(array_st[i]);
        }

        for (int i = 0; i < array_nd.length; i++) {
            arrayList_nd.add(array_nd[i]);
        }

        for (int i = 0; i < array_rd.length; i++) {
            arrayList_rd.add(array_rd[i]);
        }

        String suffix;
        if (arrayList_st.contains(dayOfMonth)) {
            suffix = "st";
        } else if (arrayList_nd.contains(dayOfMonth)) {
            suffix = "nd";
        } else if (arrayList_rd.contains(dayOfMonth)) {
            suffix = "rd";
        } else {
            suffix = "th";
        }
        return suffix;
    }

    // Gets the current date, "15 March 2016"
    public static String dateToday() {
        // Get the calendar day, month, year
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        String suffix = Utils.getSuffix(day);
        String date =
                String.valueOf(day) + suffix + " "
                        + Utils.monthsArray[month] + " "
                        + String.valueOf(year);
        return date;
    }
}