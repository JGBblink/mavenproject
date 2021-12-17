package com.demo.reactive.router;

import com.demo.reactive.handler.DemoHandler;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.logging.SimpleFormatter;

@Configuration
public class DemoRouter {

    @Bean
    public RouterFunction<ServerResponse> route(DemoHandler demoHandler) {

        HandlerFunction handlerFunction = request -> {


            return Mono.just(ServerResponse.ok());
        };


        return RouterFunctions
                .route(RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), demoHandler::hello)
                .andRoute(RequestPredicates.GET("/mono/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), demoHandler::monoHello)
                .andRoute(RequestPredicates.GET("/mono/hello2").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), this::test)
                .andRoute(RequestPredicates.GET("/times").and(RequestPredicates.accept(MediaType.APPLICATION_STREAM_JSON)), req -> {
                    Mono<ServerResponse> body = ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_STREAM_JSON)
                            .body(Flux.interval(Duration.ofSeconds(1)).map(index -> new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);

                    return body;
                });
    }

    public Mono<ServerResponse> test(ServerRequest request) {

        return ServerResponse.ok().body(Flux.just("a", "b", "c", "d").map(e -> e.toUpperCase()), String.class);
    }

    public static void main(String[] args) {
        String campsGuardToken = "camps-guard-token-2162505";
//        String token = campsGuardToken.substring(12,16);
//
//        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//        int total =  utc.get(Calendar.MONTH) + utc.get(Calendar.DAY_OF_MONTH) + utc.get(Calendar.YEAR) + utc.get(Calendar.MINUTE) + 99;
//        System.out.println(total);
//        System.out.println(token);
        boolean test = checkGuardToken(campsGuardToken);
        System.out.println(test);

    }

    private static boolean checkGuardToken(String campsGuardToken) {
        try {
            int token = Integer.parseInt(campsGuardToken.substring(18,22));
            System.out.println(token);
            Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            int now =  utc.get(Calendar.MONTH) + utc.get(Calendar.DAY_OF_MONTH) + utc.get(Calendar.YEAR) + utc.get(Calendar.MINUTE) + 99;
            return (token <= now) && (token == now || (token + 5) > now);
        }catch (NumberFormatException e) {
            return false;
        }
    }
}
