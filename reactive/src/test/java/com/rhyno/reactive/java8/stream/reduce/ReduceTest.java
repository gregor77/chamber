package com.rhyno.reactive.java8.stream.reduce;

import com.rhyno.reactive.java8.stream.DeviceSummary;
import com.rhyno.reactive.java8.stream.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReduceTest {
    private static Status panelOn;
    private static Status panelOffOne;
    private static Status powerOff;
    private static Status panelOffTwo;

    @Before
    public void setUp() throws Exception {
        panelOn = Status.builder().panelOn(true).build();
        panelOffOne = Status.builder().panelOff(true).build();
        powerOff = Status.builder().powerOff(true).build();
        panelOffTwo = Status.builder().panelOff(true).build();
    }

    @Test
    public void returnMonitorCount() throws Exception {
        List<Status> statuses = Arrays.asList(panelOn, panelOffOne, powerOff, panelOffTwo);
        DeviceSummary deviceSummary = statuses.stream()
                .reduce(DeviceSummary.builder().build()
                        , (summary, status) -> {
                            System.out.println("## accumulate");
                            if (status.isPowerOff()) {
                                summary.increasePowerOffCount();
                            } else if (status.isPanelOff()) {
                                summary.increasePanelOffCount();
                            } else {
                                summary.increasePanelOnCount();
                            }
                            return summary;
                        }
                        , (s1, s2) -> {
                            System.out.println("## combiner");
                            return DeviceSummary.builder()
                                    .powerOffCount(s1.getPowerOffCount() + s2.getPowerOffCount())
                                    .panelOffCount(s1.getPanelOffCount() + s2.getPanelOffCount())
                                    .panelOnCount(s1.getPanelOnCount() + s2.getPanelOnCount())
                                    .build();

//                            s1.setPowerOffCount(s1.getPowerOffCount() + s2.getPowerOffCount());
//                            s1.setPanelOffCount(s1.getPanelOffCount() + s2.getPanelOffCount());
//                            s1.setPanelOnCount(s1.getPanelOnCount() + s2.getPanelOnCount());
//                            return s1;
                        });

        assertThat(deviceSummary.getPowerOffCount()).isEqualTo(1);
        assertThat(deviceSummary.getPanelOffCount()).isEqualTo(2);
        assertThat(deviceSummary.getPanelOnCount()).isEqualTo(1);
    }
}
