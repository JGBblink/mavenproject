package com.demo.reactive.handler;

import java.util.List;

public interface FluxListener {

    void event(List<String> message);

    void complete();
}
