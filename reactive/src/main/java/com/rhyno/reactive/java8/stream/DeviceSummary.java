package com.rhyno.reactive.java8.stream;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DeviceSummary {
    @Builder.Default
    private Integer totalCount = 0;

    @Builder.Default
    private Integer panelOnCount = 0;

    @Builder.Default
    private Integer panelOffCount = 0;

    @Builder.Default
    private Integer powerOffCount = 0;

    public void increasePanelOnCount() {
        this.panelOnCount++;
    }

    public void increasePanelOffCount() {
        this.panelOffCount++;
    }

    public void increasePowerOffCount() {
        this.powerOffCount++;
    }
}
