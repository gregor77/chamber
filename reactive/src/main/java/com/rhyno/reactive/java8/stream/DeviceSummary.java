package com.rhyno.reactive.java8.stream;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class DeviceSummary {
    public DeviceSummary() {
        this.totalCount = 0;
        this.powerOffCount = 0;
        this.panelOffCount = 0;
        this.panelOnCount = 0;
    }

    @Builder.Default
    private Integer totalCount = 0;

    @Builder.Default
    private Integer panelOnCount = 0;

    @Builder.Default
    private Integer panelOffCount = 0;

    @Builder.Default
    private Integer powerOffCount = 0;

    public void accumulate(Status status) {
        if (status.isPowerOff()) {
            this.powerOffCount++;
        } else if (status.isPanelOff()) {
            this.panelOffCount++;
        } else if (status.isPanelOn()) {
            this.panelOnCount++;
        }
    }

    public void combine(DeviceSummary other) {
        this.powerOffCount += other.powerOffCount;
        this.panelOffCount += other.panelOffCount;
        this.panelOnCount += other.panelOnCount;
    }

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
