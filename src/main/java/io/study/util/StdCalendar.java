package io.study.util;

import io.entropy.exceptions.EntropyException;
import io.entropy.lang.Variant;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 为避免计算误差，endOfDay为下一天的开始，而不是今天的最后一秒钟。
 */
public class StdCalendar extends GregorianCalendar {

    private static final long serialVersionUID = 5619589316594788112L;

    public static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");


    public static final long SECOND_MILLIS = 1000L;
    public static final long MINUTE_MILLIS = SECOND_MILLIS * 60;
    public static final long HOUR_MILLIS = MINUTE_MILLIS * 60;
    public static final long DAY_MILLIS = HOUR_MILLIS * 24;
    public static final long WEEK_MILLIS = DAY_MILLIS * 7;

    public StdCalendar(long timeInMillis) {
        this.setTimeInMillis(timeInMillis);
    }

    public StdCalendar() {
    }

    public StdCalendar cloneInstance() {
        return new StdCalendar(this.getTimeInMillis());
    }

    static void pad2(StringBuilder sb, int num) {
        if (num < 10)
            sb.append('0');
        sb.append(num);
    }
    public static StdCalendar fromTimestamp(Timestamp timestamp){
        return new StdCalendar(timestamp.getTime());
    }
    public static StdCalendar fromString(String str) {
        Timestamp stamp = Variant.parseTimestamp(str);
        if (stamp == null) {
            if (!StringUtils.isEmpty(str))
                throw new EntropyException("util.err_invalid_timestamp").param("ts", str);
            return null;
        }
        return new StdCalendar(stamp.getTime());
    }

    public String toString() {
        int month = getMonthOfYear();
        int day = getDayOfMonth();
        int hour = getHourOfDay();
        int minute = getMinuteOfHour();
        int second = getSecondOfMinute();
        int millis = getMillisOfSecond();

        StringBuilder sb = new StringBuilder();
        sb.append(getYear());
        sb.append('-');
        pad2(sb, month);

        sb.append('-');
        pad2(sb, day);

        if (hour != 0 || minute != 0 || second != 0 || millis != 0) {
            sb.append(' ');
            pad2(sb, hour);
            sb.append(':');
            pad2(sb, minute);
            sb.append(':');
            pad2(sb, second);

            if (millis != 0) {
                sb.append('.');
                sb.append(millis);
            }
        }

        return sb.toString();
    }

    public StdCalendar toGMTZone() {
        this.setTimeZone(GMT_TIME_ZONE);
        return this;
    }

    public Timestamp toTimestamp() {
        return new Timestamp(this.getTimeInMillis());
    }

    public String toString(String pattern) {
        if (pattern == null)
            return this.toString();
        String ret = StringUtils.formatDate(this.getTime(), pattern);// FastDateFormat.getInstance(pattern).format(this.getTime());
        return ret;
    }

    public int getYear() {
        return get(YEAR);
    }

    /**
     * 1-12表示1月到12月
     *
     * @return
     */
    public int getMonthOfYear() {
        return get(MONTH) + 1;
    }

    /**
     * 从1到366
     *
     * @return
     */
    public int getDayOfYear() {
        return get(DAY_OF_YEAR);
    }

    public int getQuarterOfYear() {
        return (getMonthOfYear() - 1) / 3 + 1;
    }

    /**
     * 与joda time保持一致，从1-31表示一个月第1天到第31天
     *
     * @return
     */
    public int getDayOfMonth() {
        return get(DAY_OF_MONTH);
    }

    /**
     * 与joda time保持一致，1-7表示从周一到周日
     *
     * @return
     */
    public int getDayOfWeek() {
        return get(DAY_OF_WEEK) - 1;
    }

    public int getHourOfDay() {
        return get(HOUR_OF_DAY);
    }

    public int getMinuteOfHour() {
        return get(MINUTE);
    }

    public int getSecondOfMinute() {
        return get(SECOND);
    }

    public int getMillisOfSecond() {
        return get(MILLISECOND);
    }

    public StdCalendar toYear(int year) {
        set(YEAR, year);
        complete();

        int setYear = this.get(YEAR);
        if (year != setYear) {
            this.set(YEAR, year);
            set(DAY_OF_YEAR, 1);
            add(YEAR, 1);
            add(DAY_OF_YEAR, -1);
            this.complete();
        }
        return this;
    }

    public StdCalendar toMonthOfYear(int month) {
        if (month < 1 || month > 12)
            throw new EntropyException("util.err_invalid_monthOfYear").param("month", month);

        month = month - 1;
        set(MONTH, month);
        complete();

        // 从10月31日到9月31日会导致日期月份错乱
        int setMonth = this.get(MONTH);
        if (month != setMonth) {
            this.set(MONTH, month);
            set(DAY_OF_MONTH, 1);
            add(MONTH, 1);
            add(DAY_OF_MONTH, -1);
            this.complete();
        }
        return this;
    }

    public StdCalendar toDayOfYear(int dayOfYear) {
        set(DAY_OF_YEAR, dayOfYear);
        complete();
        return this;
    }

    public StdCalendar toDayOfMonth(int dayOfMonth) {
        set(DAY_OF_MONTH, dayOfMonth);
        complete();
        return this;
    }

    public StdCalendar toDayOfWeek(int dayOfWeek) {
        if (dayOfWeek < 0 || dayOfWeek > 7)
            throw new EntropyException("util.err_invalid_dayOfWeek").param("dayOfWeek", dayOfWeek);

        if (dayOfWeek == 7) {
            moveWeek(1).toDayOfWeek(0);
            return this;
        }
        set(DAY_OF_WEEK, dayOfWeek + 1);
        complete();
        return this;
    }

    public StdCalendar toHourOfDay(int hour) {
        set(HOUR_OF_DAY, hour);
        complete();
        return this;
    }

    public StdCalendar toMinuteOfHour(int minute) {
        set(MINUTE, minute);
        complete();
        return this;
    }

    public StdCalendar toSecondOfMinute(int second) {
        set(SECOND, second);
        complete();
        return this;
    }

    public StdCalendar toMillisOfSecond(int millis) {
        set(MILLISECOND, millis);
        complete();
        return this;
    }

    public StdCalendar toMillisOfDay(int millis) {
        return this.toStartOfDay().moveMillis(millis);
    }

    public StdCalendar moveYear(int years) {
        if (years == 0)
            return this;

        int year = getYear();
        year += years;
        return toYear(year);
    }

    public StdCalendar moveMonth(int months) {
        if (months == 0) {
            return this;
        } else {
            long curMonths = (long)this.getYear() * 12L + (long)(this.getMonthOfYear() - 1);
            long movedMonths = curMonths + months;
            int movedYear = Variant.valueOf(Math.floorDiv(movedMonths, 12L)).toInt();
            int movedMonth = (int)Math.floorMod(movedMonths, 12L) + 1;
            return resolvePreviousValid(movedYear, movedMonth, this.getDayOfMonth());
        }
    }

    private StdCalendar resolvePreviousValid(int year, int month, int day) {
        switch(month) {
            case 2:
                day = Math.min(day, isLeapYear(year) ? 29 : 28);
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            default:
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day = Math.min(day, 30);
        }
        this.toYear(year);
        this.toMonthOfYear(month);
        this.toDayOfMonth(day);
        return this;
    }


    public StdCalendar moveWeek(int weeks) {
        if (weeks == 0)
            return this;

        long time = this.getTimeInMillis();
        time += weeks * WEEK_MILLIS;
        this.setTimeInMillis(time);
        return this;
    }

    public StdCalendar moveDay(int days) {
        if (days == 0)
            return this;
        long time = this.getTimeInMillis();
        time += days * DAY_MILLIS;
        this.setTimeInMillis(time);
        return this;
    }

    public StdCalendar moveHour(int hours) {
        if (hours == 0)
            return this;
        long time = this.getTimeInMillis();
        time += hours * HOUR_MILLIS;
        this.setTimeInMillis(time);
        return this;
    }

    public StdCalendar moveMinute(int minutes) {
        if (minutes == 0)
            return this;
        long time = this.getTimeInMillis();
        time += minutes * MINUTE_MILLIS;
        this.setTimeInMillis(time);
        return this;
    }

    public StdCalendar moveSecond(int seconds) {
        if (seconds == 0)
            return this;
        long time = this.getTimeInMillis();
        time += seconds * SECOND_MILLIS;
        this.setTimeInMillis(time);
        return this;
    }

    public StdCalendar moveMillis(int millis) {
        if (millis == 0)
            return this;
        long time = this.getTimeInMillis();
        time += millis;
        this.setTimeInMillis(time);
        return this;
    }

    public StdCalendar moveWorkDay(int days) {
        int i = 0;
        int step = days < 0 ? -1 : 1;
        days = Math.abs(days);
        int weeks = days / 5;
        moveWeek(weeks);
        days = days % 5;
        while (i < days) {
            this.moveDay(step);
            int dayOfWeek = this.getDayOfWeek();
            if (dayOfWeek >= 1 && dayOfWeek <= 5) {
                i++;
            }
        }
        return this;
    }

    public StdCalendar toFirstDayOfYear() {
        set(DAY_OF_YEAR, 1);
        complete();
        return this;
    }

    public StdCalendar toLastDayOfYear() {
        set(DAY_OF_YEAR, 1);
        add(YEAR, 1);
        add(DAY_OF_YEAR, -1);
        complete();
        return this;
    }

    public StdCalendar toFirstDayOfQuarter() {
        int q = this.getQuarterOfYear();
        int m = (q - 1) * 3 + 1;
        return toMonthOfYear(m);
    }

    public StdCalendar toLastDayOfQuarter() {
        return this.toFirstDayOfQuarter().moveMonth(3).moveDay(-1);
    }

    public StdCalendar toFirstDayOfMonth() {
        set(DAY_OF_MONTH, 1);
        complete();
        return this;
    }

    public StdCalendar toLastDayOfMonth() {
        set(DAY_OF_MONTH, 1);
        add(MONTH, 1);
        add(DAY_OF_MONTH, -1);
        complete();
        return this;
    }

    public StdCalendar toFirstDayOfWeek(boolean mondayFirst) {

        if (mondayFirst) {
            int curDayOfWeek = this.getDayOfWeek();
            set(DAY_OF_WEEK, MONDAY);
            if(curDayOfWeek == 0) {
                this.add(WEEK_OF_MONTH, -1);
            }
        } else {
            set(DAY_OF_WEEK, SUNDAY);
        }

        this.complete();
        return this;
    }

    public StdCalendar toLastDayOfWeek(boolean mondayFirst) {
        return toFirstDayOfWeek(mondayFirst).moveDay(6);
    }

    public StdCalendar toMonday() {
        set(DAY_OF_WEEK, MONDAY);
        complete();
        return this;
    }

    public StdCalendar toFriday() {
        set(DAY_OF_WEEK, FRIDAY);
        complete();
        return this;
    }

    public StdCalendar toStartOfDay() {
        set(HOUR_OF_DAY, 0);
        set(MINUTE, 0);
        set(SECOND, 0);
        set(MILLISECOND, 0);
        complete();
        return this;
    }

    public StdCalendar toExclusiveEndOfDay() {
        return this.toStartOfDay().moveDay(1);
    }

    public StdCalendar toInclusiveEndOfDay() {
        return this.toStartOfDay().moveMillis((int) (DAY_MILLIS - 1L));
    }

    public StdCalendar toStartOfYear() {
        return this.toFirstDayOfYear().toStartOfDay();
    }

    public StdCalendar toExclusiveEndOfYear() {
        return this.toFirstDayOfYear().moveYear(1);
    }

    public StdCalendar toInclusiveEndOfYear() {
        return this.toExclusiveEndOfYear().moveMillis(-1);
    }

    public StdCalendar toStartOfQuarter() {
        return toFirstDayOfQuarter().toStartOfDay();
    }

    public StdCalendar toExclusiveEndOfQuarter() {
        return this.toStartOfQuarter().moveMonth(3);
    }

    public StdCalendar toInclusiveEndOfQuarter() {
        return this.toExclusiveEndOfQuarter().moveMillis(-1);
    }

    public StdCalendar toStartOfMonth() {
        return this.toFirstDayOfMonth().toStartOfDay();
    }

    public StdCalendar toExclusiveEndOfMonth() {
        return this.toStartOfMonth().moveMonth(1);
    }

    public StdCalendar toInclusiveEndOfMonth() {
        return this.toExclusiveEndOfMonth().moveMillis(-1);
    }

    public StdCalendar toStartOfWeek(boolean mondayFirst) {
        return this.toFirstDayOfWeek(mondayFirst).toStartOfDay();
    }

    public StdCalendar toExclusiveEndOfWeek(boolean mondayFirst) {
        return this.toStartOfWeek(mondayFirst).moveWeek(1);
    }

    public StdCalendar toInclusiveEndOfWeek(boolean mandayFirst) {
        return this.toExclusiveEndOfWeek(mandayFirst).moveMillis(-1);
    }

    public int getMaxDayOfMonth() {
        return this.getActualMaximum(DAY_OF_MONTH);
    }

    public int getMaxDayOfYear() {
        return this.getActualMaximum(DAY_OF_YEAR);
    }
}