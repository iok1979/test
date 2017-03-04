/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.fgv.threadstestapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author prusakovan
 */
public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class.getName());

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        final BlockingQueue<Integer> qs = new ArrayBlockingQueue<>(124,true);

        Thread in  = new Thread(new IncomeRun(qs, true), "Входной поток четных");
        Thread in2 = new Thread(new IncomeRun(qs, false), "Входной поток нечетных");

        Thread out;
        out = new Thread(new Runnable() {
            
            
            @Override
            public void run() {
                List<Integer> sum = new ArrayList<>();
                Integer poll;
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        poll = qs.take();
                        sum.add(poll);
                        LOG.info(String.format("Распаковано: %s", poll));
                    }
                } catch (InterruptedException entr) {
                    LOG.info("Поток считывания из очереди прерван главной программой");
                    Integer r = 0;
                    for (Integer i : sum) {
                        r += i;
                    }
                    LOG.info(String.format("сумма полученных элементов = %d", r));
                    LOG.info(String.format("ошибка равна: = %d", 4501500 - r));

                }
            }
        }, "Обрабатывающий поток");

        in2.start();
        in.start();              
        
        Thread.sleep(500);
        out.start();

        while (in.isAlive() || in2.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                LOG.log(Level.ERROR, "Главный поток не смог уснуть", ex);
            }
        }

        out.interrupt();
    }

    static class IncomeRun implements Runnable {

        boolean odd = false;
        BlockingQueue<Integer> q;
        List<Integer> ls = new ArrayList<>();

        public IncomeRun(BlockingQueue q, boolean odd) {
            this.q = q;
            this.odd = odd;
        }

        @Override
        public void run() {
            boolean b;
            for (int i = 1; i <= 3000; i++) {
                b = odd ? i % 2 == 0 : i % 2 != 0;
                if (b) {
                    try {
                        q.put(i);
                    } catch (InterruptedException ex) {
                        LOG.log(Level.ERROR, "Заполнение очереди грубо прервали", ex);
                    }
                    ls.add(i);
                } 
            }
            LOG.info(ls.toString());
        }
    }

}
