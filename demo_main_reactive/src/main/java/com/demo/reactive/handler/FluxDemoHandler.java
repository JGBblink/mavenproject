package com.demo.reactive.handler;

import ch.qos.logback.core.util.TimeUtil;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.tools.jconsole.Plotter;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;


/**
 * (【数据源】):(【doOnXXX、filter、map、flatMap、zip、delay、线程调度】):(【complete、error】)
 * <p>
 * 用于编程方式自定义生成数据流的create和generate等及其变体方法；
 * 用于“无副作用的peek”场景的doOnNext、doOnError、doOncomplete、doOnSubscribe、doOnCancel等及其变体方法；
 * 用于数据流转换的when、and/or、merge、concat、collect、count、repeat等及其变体方法；
 * 用于过滤/拣选的take、first、last、sample、skip、limitRequest等及其变体方法；
 * 用于错误处理的timeout、onErrorReturn、onErrorResume、doFinally、retryWhen等及其变体方法；
 * 用于分批的window、buffer、group等及其变体方法；
 * 用于线程调度的publishOn和subscribeOn方法。
 * <p>
 * doOnXXXX:表示对元素一个一个进行操作
 */
@Component
public class FluxDemoHandler {


    public static void main(String[] args) throws InterruptedException {

//        Flux.range(0, 10)
//                .doOnNext(e -> System.out.println("base=" + e))
//                .filter(e -> e % 3 == 0)
//                .doOnNext(e -> System.out.println("filter: " + e))
//                .map(e -> "map: " + e)
//                .doOnNext(e -> System.out.println(e))
//                .subscribe(e -> System.out.println("final: " + e + "\r\n"),
//                        throwable -> throwable.printStackTrace(),
//                        () -> System.out.println("complete"),
//                        e -> {
//                            System.out.println("subscriber");
//                            e.request(3);
//                        });

//        Flux.generate(() -> 0, (state, sink) -> {
//            System.out.println("current: " + state);
//            sink.next(state * 3);
//            if (state >= 10) {
//                sink.complete();
//            }
//            return state + 1;
//        }).subscribe(e -> System.out.println(e));


        Flux.just(1, 2, 3, 4, "ss", 3, "a", 7)
                .handle((e, sink) -> {
                    if (e instanceof Integer) {
                        sink.next(e);
                    }
                }).subscribe(e -> System.out.println(e));

    }

    private static void methodSinkCreate() throws InterruptedException {
        Flux.create(sink -> {
            register(new FluxListener() {
                @Override
                public void event(List<String> messages) {
                    messages.forEach(message -> {
                        sink.next(message);
                    });
                }

                @Override
                public void complete() {
                    System.out.println("" +
                            "complete");
                    sink.complete();
                }
            });
        }).subscribe(e -> System.out.println(e));

        // start
        for (int i = 0; i < 5; i++) {
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(new Random().nextInt(3000));
                    start(Thread.currentThread().getName() + " : test");
                    Thread.sleep(new Random().nextInt(1000));
                    end();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        Thread.sleep(5000);
    }

    private static List<FluxListener> listeners = new ArrayList<>();

    private static void register(FluxListener listener) {
        listeners.add(listener);
    }

    private static void start(String message) {
        System.out.println("------");
        listeners.forEach(listener -> listener.event(Arrays.asList(message, "data:" + message)));
    }

    private static void end() {
        listeners.forEach(listener -> listener.complete());
    }

    private static void methodContext() {
        Flux.range(1, 6)
                .flatMap(e -> Mono.subscriberContext().map(ctx -> {
                    Object name = ctx.get("name");
                    return name + " " + e;
                }))
                .doOnNext(System.out::println)
                .subscriberContext(ctx -> ctx.put("name", "JGB"))
                .subscribe();
    }

    private static void methodError() throws InterruptedException {
        Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(e -> {
                    if (e % 2 == 0) {
                        throw new RuntimeException("test error");
                    }
                })
                .doOnError(e -> {
                    System.out.println(e.getMessage());
                })
                .onErrorResume((t) -> Flux.just(1, 2, 3, 4))
                .doOnNext(e -> System.out.printf(":" + e))
                .subscribe(System.out::println, System.out::println, () -> System.out.printf("comple"), e -> {
                    System.out.println("xxxxx");
                    e.request(10);
                });

        Thread.sleep(10000);
    }

    private static void method1() {
        Flux.just(1, 2, 3, 4, "a", "X", null)
                .doOnError(e -> System.out.println("error"))
                .doOnComplete(() -> System.out.println("complete"))
                .doOnNext(e -> {
                    if (e instanceof String) {
                        System.out.println(((String) e).toUpperCase());
                    } else {
                        System.out.println(e);
                    }
                })
                .doOnNext(e -> {
                    System.out.println("===>" + (Integer) e / 2);
                })
                .subscribe();
    }

    private static void method2() {
        Flux.fromIterable(Arrays.asList(1, 3, 4, "a", "c"))
                .filter(e -> e instanceof Integer)
                .flatMap(e -> Flux.just((Integer) e))
                .doOnNext(e -> System.out.println(e))
                .doOnComplete(() -> System.out.printf("comple"))
                .subscribe();
    }

    private static void method3() {
        Flux<Integer> flux_int = Flux.just(1, 2, 3, 4);
        Flux<String> flux_str = Flux.just("a", "b", "c", "d");
        Flux.zip(flux_int, flux_str).doOnNext(e -> {
            System.out.printf(e.getT1() + " : " + e.getT2());
        }).subscribe();
    }
}
