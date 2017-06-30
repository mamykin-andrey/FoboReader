package ru.mamykin.foreignbooksreader.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class Utils {
    /**
     * Получаем строку с подходящей еденицей измерения
     * @param size размер файла
     * @return строка с размером файла и соотв. еденицей измерения
     */
    public static String getSizeString(long size) {
        final NumberFormat formatter = new DecimalFormat("#0.00");
        if (size <= 1024) {
            return size + " б";
        } else if (size <= 1024d * 1024d) {
            return formatter.format(size / 1024d) + " кб";
        } else {
            return formatter.format(size / (1024d * 1024d)) + " мб";
        }
    }

    /**
     * Получаем строку с датой последнего изменения файла
     * @param modifiedTime дата изменения в формате timestamp
     * @return строка с с датой последнего изменения файла
     */
    public static String getLastModifiedString(long modifiedTime) {
        final long diffInDays = (new Date().getTime() - modifiedTime) / (1000 * 60 * 60 * 24);
        if (diffInDays == 0) {
            return "изменён сегодня";
        } else if (diffInDays < 7) {
            // Прошло меньше одной недели
            return "изменён " + diffInDays + getRightEnding(diffInDays, "дней", "день", "дня") + " назад";
        } else if (diffInDays < 30) {
            return "изменён " + diffInDays / 7
                    + getRightEnding(diffInDays / 7, "недель", "неделю", "недели") + " назад";
        } else if (diffInDays < 365) {
            return "изменён " + diffInDays / 30
                    + getRightEnding(diffInDays / 30, "месяцев", "месяц", "месяца") + " назад";
        } else {
            return "изменён " + diffInDays / 365
                    + getRightEnding(diffInDays / 365, "лет", "год", "года") + " назад";
        }
    }

    /**
     * Выбираем правильное окончание для числа
     * @param number       само число
     * @param firstFormat  первый вариант
     * @param secondFormat второй вариант
     * @param thirdFormat  третий вариант
     * @return правильное окончание для числа
     */
    public static String getRightEnding(long number, String firstFormat, String secondFormat,
                                        String thirdFormat) {
        if (number % 10 == 1) {
            return " " + secondFormat;
        } else if (number % 10 >= 2 && number <= 4) {
            return " " + thirdFormat;
        }
        return " " + firstFormat;
    }

    /**
     * Применяем Schedulers к Observable
     * @param <T> тип Observable
     * @return Observable с ioThread и mainThread
     */
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Получаем дату из строки в нужном формате
     * @param src строка с датой
     * @return дата, полученная из строки
     */
    public static Date getDateFromString(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("ru"));
        try {
            return format.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Получем строку с датой из объекта {@link Date}
     * @param date объект с датой
     * @return строка с форматированной датой
     */
    public static String getFormattedDate(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", new Locale("ru"));
            return format.format(date);
        } catch (Exception e) {
            return null;
        }
    }
}