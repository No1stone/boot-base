package io.origemite.lib.common.util;

import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * 날짜관련 클래스
 */
@Component
public class DateUtils {
    public static final String LAST_DATE_STR = "99991231";
    public static final LocalDate FIRST_DATE = LocalDate.of(1900, 1, 1);
    public static final LocalDate LAST_DATE = LocalDate.of(9999, 12, 31);
    
    /**
     * 날짜포멧
     */
    public static final String PATTERN_YEAR = "yyyy";
    public static final String PATTERN_MONTH = "MM";
    public static final String PATTERN_DATE = "dd";
    public static final String PATTERN_YYYYMM = "yyyyMM";
    public static final String PATTERN_YYYMMDD = "yyyyMMdd";
    public static final String PATTERN_HMS = "HHmmss";
    public static final String PATTERN_DATE_DASH = "yyyy-MM-dd";
    public static final String PATTERN_DATETIME = "yyyyMMddHHmmss";
    public static final String DEFAULT_TIME_ZONE_ID = "Asia/Seoul";


    /**
     * 문자열을 로컬 데이트 타임으로 반환
     */
    public static LocalDateTime of(String dateStr){
        return StrDateUtils.parseDateTime(dateStr);
    }


    /**
     * 문자열을 로컬 데이트 로 반환
     */
    public static LocalDate ofDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_YYYMMDD);
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return date;
    }


    /**
     * 현재 일시에 해당하는 날짜시간을 반환
     * @return
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 타임존에 해당하는 현재 일시에 해당하는 날짜시간을 반환
     * @return
     */
    public static LocalDateTime now(ZoneId zoneId) {
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        return now.toLocalDateTime();
    }

    public static String formatNowAsDate(ZoneId zoneId) {
        LocalDateTime now = now(zoneId);
        return now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    public static String formatNowPlusDayAsDate(long day,ZoneId zoneId) {
        LocalDateTime now = now(zoneId);
        return now.plusDays(day).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String formatNowMinusDayAsDate(long day,ZoneId zoneId) {
        LocalDateTime now = now(zoneId);
        return now.minusDays(day).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 타임오프셋에 해당하는 현재 일시에 해당하는 날짜시간을 반환
     * @return
     */
    public static LocalDateTime now(ZoneOffset zoneOffset) {
        ZonedDateTime now = ZonedDateTime.now(zoneOffset);
        return now.toLocalDateTime();
    }

    /**
     * 현재일자가 1일 인지롤 반환 한다.
     * @return
     */
    public static boolean isFirstDay() {
        return isFirstDay(now());
    }

    /**
     * 현재 일시에 해당하는 날짜시간을 주어진 포맷으로 반환한다.
     *
     * DateUtils.format("yyyy-MM-dd") = 2019-08-07
     * DateUtils.format("yyyyMM") = 201908
     * DateUtils.format("yyyy-MM-dd HH:mm:ss") = 2019-08-07 18:06:13
     *
     * @param dateFormat 날짜 포맷
     * @return
     */
    public static String now(String dateFormat) {
        return format(LocalDateTime.now(), dateFormat);
    }


    public static String format(String dateFormat) {
        return now().format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * 주어진 일시에 해당하는 날짜시간을 주어진 포맷으로 반환한다.
     *
     * DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss") = 20190807180301
     * DateUtils.format(LocalDateTime.now(), "yyyyMM") = 201908
     * DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss") = 2019-08-07 18:06:13
     *
     * @param dateTime 기준일시
     * @return
     */
    public static String format(LocalDateTime dateTime, String dateFormat) {
        return dateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

     /**
     * 기준일자가 1일 인지롤 반환 한다.
     * @param dateTime 기준일자
     * @return
     */
    public static boolean isFirstDay(LocalDateTime dateTime) {
        return dateTime.get(ChronoField.DAY_OF_MONTH) == 1;
    }

     /**
     * 현재일자가 해당월의 마지막 일인지 여부를 반환 한다.
     * @return
     */
    public static boolean isLastDay() {
        return isLastDay(now());
    }

    /**
     * 기준일자가 해당월의 마지막 일인지 여부를 반환 한다.
     * @param dateTime 기준일자
     * @return
     */
    public static boolean isLastDay(LocalDateTime dateTime) {
        return dateTime.get(ChronoField.DAY_OF_MONTH) == dateTime.toLocalDate().lengthOfMonth();
    }

    /**
     * dateTime1이 dateTime2보다 크거나 같은지 여부 반환
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean greaterEquals(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2) || dateTime1.isEqual(dateTime2);
    }


    /**
     * dateTime1이 dateTime2보다 큰지 여부 반환
     * @return
     */
    public static boolean greaterThen(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2);
    }


    /**
     * dateTime이 현재시간보다 큰지 여부 반환
     * @return
     */
    public static boolean greaterThenNow(LocalDateTime dateTime) {
        return dateTime.isAfter(now());
    }


    /**
     * dateTime1이 dateTime2보다 작거나 같은지 여부 반환
     * @return
     */
    public static boolean lessEquals(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return  dateTime1.isBefore(dateTime2) || dateTime1.isEqual(dateTime2);
    }

    /**
     * dateTime1이 dateTime2보다 작은지 여부 반환
     * @return
     */
    public static boolean lessThen(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }

    /**
     * dateTime이 현재시간보다 작은지 여부 반환
     * @return
     */
    public static boolean lessThenNow(LocalDateTime dateTime) {
        return dateTime.isBefore(now());
    }

    /**
     * dateTime1이 datetime2와 datetime3 사이에 있는지 확인 
     * @return
     */
    public static boolean between(LocalDateTime dateTime1, LocalDateTime dateTime2, LocalDateTime dateTime3) {
        return greaterEquals(dateTime1, dateTime2) && lessEquals(dateTime1, dateTime3);
    }

    /**
     * 현재 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * @return
     */
    public static LocalDateTime add(TemporalUnit field, long amountToAdd) {
        return add(now(), field, amountToAdd);
    }

    /**
     * 주어진 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * @return
     */
    public static LocalDateTime add(LocalDateTime dateTime, TemporalUnit field, long amountToAdd) {
        if(amountToAdd == 0) {
            return dateTime;
        }else if(amountToAdd > 0) {
            return dateTime.plus(amountToAdd, field);
        }else{
            return dateTime.minus(Math.abs(amountToAdd), field);
        }
    }

    public static LocalDateTime of(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

}