package com.demo.reactive.handler;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Component
public class DemoHandler {

    /**
     * 使用Mono返回0-1条数据
     *
     * @param request
     * @return
     */
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromObject("hello spring"));
    }

    public Mono<ServerResponse> monoHello(ServerRequest request) {
        Mono<ServerResponse> hello_spring = ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromObject("hello spring"));
        return Mono.create(monoSink -> monoSink.success(ServerResponse
                .ok()
                .body(BodyInserters.fromObject("hello spring2"))
                .block())
        );
    }

    public Flux<ServerResponse> monoHello2(ServerRequest request) {


        return Flux.create(fluxSink -> fluxSink.next(Objects.requireNonNull(ServerResponse
                .ok()
                .body(BodyInserters.fromObject("hello spring2"))
                .block()))
        );
    }


    static List list = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws UnsupportedEncodingException {

        String str = "https%3A%2F%2Fcampsui-vip.int.aw.dev.activenetwork.com%2FJCTestQA2whscheck\"whscheck=\"whscheck\"\"\"";
        String decodeUrl = URLDecoder.decode(str, "UTF-8").replaceAll("\"", "'");


        System.out.println(URLEncoder.encode(decodeUrl, "UTF-8"));

    }

    private static void extracted1() {
        Random random = new Random();
        Flux.generate(() -> 1, (count, sink) -> {
            sink.next(random.nextInt(10));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count >= 5) {
                sink.complete();
            }
            return ++count;
        }).subscribe(System.out::println);
    }

    private static void extracted() {
        AtomicInteger count = new AtomicInteger();
        Random random = new Random();
        Flux.generate(sink -> {
            sink.next(random.nextInt(10));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count.getAndIncrement() >= 5) {
                sink.complete();
            }
        }).subscribe(System.out::println);
    }
}
