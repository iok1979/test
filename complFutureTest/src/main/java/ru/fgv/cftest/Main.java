/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.fgv.cftest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 *
 * @author prusakovan
 */
public class Main {

    public static void main(String[] args) {
        CompletableFuture<List<Integer>> cf01 = CompletableFuture.supplyAsync(TestMethods::fillNotOddList);
        CompletableFuture<List<Integer>> cf02 = CompletableFuture.supplyAsync(TestMethods::fillOddList);
        LOG.info("Начало --- *** ---");

        /*LOG.info("Четные --- *** ---");
        cf01.thenAccept((List<Integer> lists) -> {
        LOG.info(format("Размер массива: %d", lists.size()));
        lists.stream().forEach((l) -> {
        LOG.info(format("Значение: %d", l));
        });
        });        
        LOG.info("Нечетные --- *** ---");
        cf02.thenAccept((List<Integer> lists) -> {
        LOG.info(format("Размер массива: %d", lists.size()));
        lists.stream().forEach((l) -> {
        LOG.info(format("Значение: %d", l));
        });
        });*/
        cf01.thenCombine(cf02, (List<Integer> list, List<Integer> c) -> {
            list.addAll(c);
            return list.stream()
                    .filter((Integer e) -> {
                        return e % 57 == 0;
                    }).
                    sorted().
                    collect(Collectors.toList());
            //reduce(Integer::sum);
        }).thenAccept((List<Integer> lists) -> {
            LOG.info(format("Сумма массива: %d", lists.size()));
            lists.stream().forEach((l) -> {
                LOG.info(format("Значение: %d", l));
            });
        });
        LOG.info("Конец --- *** ---");
    }
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

}
