package ru.fgv.test8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author prusakovan
 */
public class LambdaMain {
    static final Logger LOG = LogManager.getLogger(LambdaMain.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String test = "hello, world";
        LOG.info("Тест {}", test);

        // Тест обхода коллекций
        Stream<Integer> st = Stream.iterate(1, i -> ++i);
        List<Integer> l = st.limit(300000).collect(toList());

        DateTime start = DateTime.now();
        Integer sum1 = 0;
        for (Integer i : l) {
            sum1 += i;
        }
        DateTime end = DateTime.now();
        Duration dur1 = new Duration(start, end);

        start = DateTime.now();
        Optional<Integer> summ = l.stream().reduce((i, j) -> i + j);
        Integer sum2 = summ.orElse(0);
        end = DateTime.now();
        Duration dur2 = new Duration(start, end);

        start = DateTime.now();
        Optional<Integer> summ2 = l.parallelStream().reduce(Integer::sum);
        Integer sum3 = summ2.orElse(0);
        end = DateTime.now();
        Duration dur3 = new Duration(start, end);

        LOG.info("Результат работы через цикл:     время - {}, сумма = {}]", dur1.getMillis(), sum1);
        LOG.info("Результат работы через Stream:   время - {}, сумма = {}]", dur2.getMillis(), sum2);
        LOG.info("Результат работы через Parallel: время - {}, сумма = {}]", dur3.getMillis(), sum3);

        // тест поиска среднего значения случайных чисел
        Stream<Long> avg = Stream.generate(LambdaMain::intRandom);
        List<Long> g = avg.limit(1000000).collect(toList());
        LongStream avgLong = LongStream.generate(LambdaMain::intRandom);

        start = DateTime.now();
        long m1 = 0;
        for (Long i : g) {
            m1 += i;
        }
        double max1 = (double) m1 / g.size();
        Duration durAvg1 = new Duration(start, DateTime.now());

        start = DateTime.now();
        OptionalDouble a = g.stream().mapToLong(Long::longValue).average();
        Duration durAvg2 = new Duration(start, DateTime.now());

        start = DateTime.now();
        OptionalDouble a2 = g.parallelStream().mapToLong(Long::longValue).average();
        Duration durAvg3 = new Duration(start, DateTime.now());

        start = DateTime.now();
        OptionalDouble an = avgLong.limit(1000000).average();
        Duration durAvgN2 = new Duration(start, DateTime.now());

        LOG.info("Поиск среднего арифмитического: ");
        LOG.info("Результат работы через цикл:     время - {}, среднее = {}]", durAvg1.getMillis(), max1);
        LOG.info("Результат работы через Stream:   время - {}, среднее = {}]", durAvg2.getMillis(), a.orElse(0));
        LOG.info("Результат работы через Parallel: время - {}, среднее = {}]", durAvg3.getMillis(), a2.orElse(0));
        LOG.info("Результат работы через Stream:   время - {}, среднее = {}]", durAvgN2.getMillis(), an.orElse(0));
        //LOG.info("Результат работы через Parallel: время - {}, среднее = {}]", durAvgN3.getMillis(), an2.orElse(0));

        // считываем файл и определяем число слов и пр.
        start = DateTime.now();
        try (InputStream resource = LambdaMain.class.getClassLoader().getResourceAsStream("Losev.txt");
                InputStreamReader isr = new InputStreamReader(resource, "UTF-8");
                BufferedReader br = new BufferedReader(isr);) {

            String find = "античность";
            Stream<String> splitAsStream = br.lines();
            Map<Integer, Map<String, Long>> collect
                    = splitAsStream
                    .flatMap(s -> Stream.of(s.split("[\\P{L}]+")))
                    .map(s -> s.toLowerCase())
                    .filter(s -> s.length() > 2)
                    .collect(
                            groupingBy(s -> s.length(),
                                    groupingBy(s -> s, counting())));

            TreeMap<Integer, Map<String, Long>> sorted = new TreeMap<>(collect);
            LOG.info(collect.toString());

            Map<String, Long> findRaw = sorted.get(find.length());
            LOG.info("Слово {} встречается {} раз. В нем {} букв", find, findRaw.getOrDefault(find, 0L), find.length());

            Map<String, Long> value = sorted.lastEntry().getValue();
            Optional<String> findFirst = value.keySet().stream().findFirst();
            LOG.info("Cамое длинное встречающее слово {} встречается в тексте {} раз", findFirst.orElse("-"), value.get(findFirst.get()));

            Map<Integer, Integer> countingInText = sorted.entrySet().stream()
                    //.map(entr -> entr.getValue().size())
                    .collect(toMap(entr -> entr.getKey(), entr -> entr.getValue().size()));
            LOG.info("Количество вхождений слов определенной длины:");
            countingInText.forEach(
                    (key, v) -> LOG.info("Слова длиной {} символов встречаются в тексте {}", key, v)
            );

        } catch (IOException ex) {
            LOG.warn("Ошибка {}", ex.toString());
        }
        Duration durText = new Duration(start, DateTime.now());

        LOG.info("Вся работа с текстом, включая закрытие потоков заняла: {} мс.", durText.getMillis());
    }

    private static Long intRandom() {
        return (long) (10000000 * Math.abs(Math.random()));
    }

}
