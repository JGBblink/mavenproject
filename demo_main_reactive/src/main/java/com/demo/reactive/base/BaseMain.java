package com.demo.reactive.base;

import java.time.Duration;
import java.util.ArrayList;
import java.util.stream.Stream;
import reactor.core.publisher.Flux;

public class BaseMain {

    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4, 5).subscribe();

        //Flux.just(1, 2, 3, 4, 5).subscribe(System.out::println);

        Flux<String> flux1 = Flux.just("hello", "world")
                .flatMap(e -> Flux.fromArray(e.split("\\s*")));
        Flux<String> flux2 = Flux.just("123", "456")
                .flatMap(e -> Flux.fromArray(e.split("\\s*")));

        Flux.zip(flux1,flux2).subscribe(tuple->{
            System.out.println("T1:" + tuple.getT1() + " T2:" + tuple.getT2());
        });

    }

    /**
     * 创建flux的方式(常用)
     */
    private static void construct() {
        // 创建flux的方式(常用)
        Flux<Integer> just = Flux.just(1, 2, 3, 4, 5);
        Flux.fromArray(new Integer[2]);
        Flux.fromIterable(new ArrayList<>());
        Flux.fromStream(Stream.of(1,2,3,4));
    }
}
