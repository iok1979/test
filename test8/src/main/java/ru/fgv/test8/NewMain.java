package ru.insros.testjava8;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author prusakovan
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        List<Integer> l = new ArrayList();
//
//        Integer[] randomArray = new Integer[1024];
//        Arrays.setAll(randomArray, 
//                (int value) -> (int) (Math.random() * value)
//        );
//
//        l = Arrays.asList(randomArray);
//
//        long count = l.stream().count();
//        System.out.println(count);
//        System.out.println(l.toString());
//
//        List<Integer> collect = l.stream().filter(i -> Integer.compare((int) i, 100) > 0 && Integer.compare(i, 256) < 0)
//                .sorted()
//                .collect(Collectors.toList());
//        
//        System.out.println(collect.toString());

//        Stream<Double> sd;
//        sd = Stream.generate(Math::random).peek(System.out::println).limit(15);
//        List<Double> collect1 = sd.distinct().map(d -> d * 1000).collect(Collectors.toList());
//        
//        System.out.println(collect1.toString());
        Logger.getGlobal().info("Глобальный логер");

        Stream<BigDecimal> fib = Stream.iterate(BigDecimal.ONE, (BigDecimal n) -> {
            return BigDecimal.ONE.divide(n.add(BigDecimal.ONE), 18, RoundingMode.HALF_UP);
        }).skip(10).limit(100);
        List<BigDecimal> collect = fib.collect(Collectors.toList());

        Logger.getGlobal().info(collect.toString());

        LocalDate ld = LocalDate.of(1979, 11, 1);
        Logger.getGlobal().info(ld.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        Logger.getGlobal().info(ld.plusYears(4).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Logger.getGlobal().info(ld.plusDays(29).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Logger.getGlobal().info(ld.plusMonths(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Logger.getGlobal().info(ld.plusDays(29).plusMonths(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        Logger.getGlobal().info(ld.plusDays(4 * 365).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Logger.getGlobal().info(ld.format(DateTimeFormatter.ofPattern("dd MMMM yyyy EEEE", new Locale("ru"))));
        Logger.getGlobal().info(ld.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru")));
    }

}
