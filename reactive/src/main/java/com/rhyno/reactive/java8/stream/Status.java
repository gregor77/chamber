package com.rhyno.reactive.java8.stream;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Status {
    private boolean panelOn;
    private boolean panelOff;
    private boolean powerOff;
}
