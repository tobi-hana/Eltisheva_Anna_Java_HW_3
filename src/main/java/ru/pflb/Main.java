package ru.pflb;

//        Необходимо реализовать программу со следующим функционалом:
//        пользователь вводит регистрационный номер автомобиля в любом
//        формате (предусмотрите как можно больше вариантов), например,
//        форматы "А123АА777", "А 123АА77", "A 123 AA RUS 777" и т.д.,
//        должны восприниматься программой. программа должна проверять
//        введенный номер на соответствие формату федеральных номерных
//        знаков. Если введенный номерной знак не может существовать,
//        то ваша программа должна сообщить минимум одну причину. Например,
//        "А123АА333" - такого региона не существует или "Ю123ААRUS30"
//        - недопустимая буква в номере и т.д. также программа должна
//        вывести информацию по введенному номеру. Например, вернуть регион
//        к которому относится данный номер. Можете расширить выводимую
//        информацию по своему усмотрению. программа должна работать пока
//        пользователь не введен команду выхода. Также необходимо внутри
//        классов и методов оставлять комментарии, которые пояснят ваше
//        решение по проектированию данной программы, а также расскажут
//        про возможности вашей программы: все поддерживаемые форматы
//        ввода, все ошибки, которые проверяет программа и т.д.
//
//        P.S.
//        Не обязательно реализовывать все регионы. Важно показать сам принцип.
//        Обязательно надо взять регион у которого несколько разных типов
//        региональных значений, и несколько дополнительных регионов.

import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String carNum = "";
        String region = "";
        System.out.println("To leave enter 'EXIT'");
        Scanner in = new Scanner(System.in);
//        ну нагуглила это выражение, как бы поняла
//        [АВЕКМНОРСТУХABEKMHOPCTYXD] это буквы, которые можно использовать
//        мне для проверки неудобно было переключаться между Рус и Англ раскладкой, поэтому продублировала
//        первой идет кирилица У, потом латиница Y видно по этому символу
//        \\d любая цифра
//        {}количество повторений
//        в конце регион, который 2-3 цифры и не может быть 00 или 000
//        из-за того что регион может быть 2-3 цифры возникают проблемы
//        которые устраняются вводом региона отдельно
//        поэтому запись standard будто дублируется
        String validSymbols = "^([АВЕКМНОРСТУХABEKMHOPCTYXD\\d]+)";
        String gost4 = "^(\\d{3}(?<!000)[АВЕКМНОРСТУХABEKMHOPCTYX])(\\d{2,3})"; //4 прицеп
        String gost5 = "^(([АВЕКМНОРСТУХABEKMHOPCTYX]{2}\\d{3}(?<!000))(\\d{2,3})|" + //5 формат АА 000 000 для прицепов, сюда не дойдет никогда
                "([АВЕКМНОРСТУХABEKMHOPCTYX]\\d{4}(?<!000))(\\d{2,3})|" +
                "(\\d{4}(?<!000)[АВЕКМНОРСТУХABEKMHOPCTYX])(\\d{2,3}))";
        String gost6 = "^(([АВЕКМНОРСТУХABEKMHOPCTYX]\\d{3}(?<!000)[АВЕКМНОРСТУХABEKMHOPCTYX]{2})(\\d{2,3})|" +
            "([АВЕКМНОРСТУХABEKMHOPCTYX]{2}\\d{4}(?<!0000))(\\d{2,3})|" + //АА 000 000 для прицепов сюда попадет, как АА 0000 00
            "(\\d{4}(?<!0000)[АВЕКМНОРСТУХABEKMHOPCTYX]{2})(\\d{2,3})|" +
            "([АВЕКМНОРСТУХABEKMHOPCTYX]{2}\\d{2}(?<!00)[АВЕКМНОРСТУХABEKMHOPCTYX]{2})(\\d{2,3})|" +
            "(\\d{3}(?<!000)(CD)\\d(?<!0))(\\d{2,3})|" + // дипломат
            "([АВЕКМНОРСТУХABEKMHOPCTYX]{2}\\d{3}(?<!000)[АВЕКМНОРСТУХABEKMHOPCTYX])(\\d{2,3})|" +
            "(([ТT]|[КK]|[СC])[АВЕКМНОРСТУХABEKMHOPCTYX]{2}\\d{3}(?<!000))(\\d{2,3}))";

        String gost7 = "^(\\d{3}(?<!000)(D|[TТ])\\d{3}(?<!000))(\\d{2,3})";  //7 дипломат

        Pattern patternValidSymbols = Pattern.compile(validSymbols);
        Pattern pattern4 = Pattern.compile(gost4);
        Pattern pattern5 = Pattern.compile(gost5);
        Pattern pattern6 = Pattern.compile(gost6);
        Pattern pattern7 = Pattern.compile(gost7);

//        можно было б тут do while прописать, но если первым будет введено EXIT неправильно отработает
        carNum = in.nextLine();
        while(!carNum.equals("EXIT")) {
            carNum = carNum.replaceAll(" ", "");
            if (!patternValidSymbols.matcher(carNum).matches()) {
                System.out.println("Ваш номер содержит недопустимые символы");
            } else if (pattern7.matcher(carNum).matches()) {
                System.out.println("Номер принадлежит дипломатическому представительству");
                region = carNum.substring(7);
                checkRegion(region);
            } else if (pattern6.matcher(carNum).matches()) {
                if (carNum.contains("CD")) {
                    System.out.println("Номер принадлежит дипломатическому представительству");
                }
                region = carNum.substring(6);
                checkRegion(region);
            } else if (pattern5.matcher(carNum).matches()) {
                region = carNum.substring(5);
                checkRegion(region);
            }else if (pattern4.matcher(carNum).matches()) {
                System.out.println("Это номер прицепа");
                region = carNum.substring(4);
                checkRegion(region);
            } else {
                System.out.println("Ваш номер не соответсвует стандарту");
            }
            carNum = in.nextLine();
        }
    }
    public static void checkRegion(String region) {
        if(region.equals("00")){
            System.out.println("регион не может быть 00");
        }else if (region.equals("000")) {
            System.out.println("регион не может быть 000");
        } else if(regionInfo.containsKey(region)) {
            System.out.printf("регион - %s\n", regionInfo.get(region));
        }else if (region.length() == 2) {
            System.out.printf("Регион %s правильный, но его нет в этой программе\n", region);
        } else {
            System.out.printf("Возможно и правильный регион %s, но его нет в базе\n", region);
        }
    }
    private static Map<String, String> regionInfo = new HashMap();

    static {
        regionInfo.put("77", "Москва");
        regionInfo.put("99", "Москва");
        regionInfo.put("97", "Москва");
        regionInfo.put("177", "Москва");
        regionInfo.put("199", "Москва");
        regionInfo.put("197", "Москва");
        regionInfo.put("777", "Москва");
        regionInfo.put("799", "Москва");
        regionInfo.put("797", "Москва");
        regionInfo.put("977", "Москва");
        regionInfo.put("50", "Московская область");
        regionInfo.put("90", "Московская область");
        regionInfo.put("150", "Московская область");
        regionInfo.put("190", "Московская область");
        regionInfo.put("750", "Московская область");
        regionInfo.put("790", "Московская область");
        regionInfo.put("78", "Санкт-Петербург");
        regionInfo.put("98", "Санкт-Петербург");
        regionInfo.put("178", "Санкт-Петербург");
        regionInfo.put("198", "Санкт-Петербург");
        regionInfo.put("16", "Республика Татарстан");
        regionInfo.put("116", "Республика Татарстан");
        regionInfo.put("716", "Республика Татарстан");
        regionInfo.put("74", "Челябинская область");
        regionInfo.put("174", "Челябинская область");
        regionInfo.put("774", "Челябинская область");

    }

}