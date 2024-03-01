package commons;

import java.util.Objects;

/**
 * Date class if any operations on dates were necessary
 */
public class Date implements Comparable<Date> {
    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(String date) {
        String[] split = date.split("[-./]");
        this.day = Integer.parseInt(split[0]);
        this.month = Integer.parseInt(split[1]);
        this.year = Integer.parseInt(split[2]);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int compareTo(Date o) {
        if (this.year != o.year) return this.year - o.year;
        if (this.month != o.month) return this.month - o.month;
        if (this.day != o.day) return this.day - o.day;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return day == date.day && month == date.month && year == date.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }

    @Override
    public String toString() {
        return day + "-" +((month < 10) ? "0" : "")+ month + "-" + year;
    }
}
