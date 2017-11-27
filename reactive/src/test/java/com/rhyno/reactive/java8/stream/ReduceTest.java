package com.rhyno.reactive.java8.stream.reduce;

import com.rhyno.reactive.java8.stream.DeviceSummary;
import com.rhyno.reactive.java8.stream.Status;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.assertThat;

public class ReduceTest {
    private static Status panelOn;
    private static Status panelOffOne;
    private static Status powerOff;
    private static Status panelOffTwo;

    private List<Status> statuses;

    @Before
    public void setUp() throws Exception {
        panelOn = Status.builder().panelOn(true).build();
        panelOffOne = Status.builder().panelOff(true).build();
        powerOff = Status.builder().powerOff(true).build();
        panelOffTwo = Status.builder().panelOff(true).build();

        statuses = Arrays.asList(panelOn, panelOffOne, powerOff, panelOffTwo);
    }

    @Test
    public void reduce_parallelStream_workIncorrectly() throws Exception {
        //reduce에 연산 중인 object의 상태를 바꾸는 경우, 제대로 수행되지 않는다
        DeviceSummary deviceSummary = statuses.parallelStream()
                .reduce(DeviceSummary.builder().build()
                        , (summary, status) -> {
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
                            s1.combine(s2);
                            return s1;
                        });

        assertThat(deviceSummary.getPowerOffCount()).isNotEqualTo(1);
        assertThat(deviceSummary.getPanelOffCount()).isNotEqualTo(2);
        assertThat(deviceSummary.getPanelOnCount()).isNotEqualTo(1);
    }

    @Test
    public void collect_parallelStream_workCorrectly() throws Exception {
        DeviceSummary summary = statuses.parallelStream()
                .collect(DeviceSummary::new
                        , DeviceSummary::accumulate
                        , DeviceSummary::combine);

        assertThat(summary.getPowerOffCount()).isEqualTo(1);
        assertThat(summary.getPanelOffCount()).isEqualTo(2);
        assertThat(summary.getPanelOnCount()).isEqualTo(1);
    }

    @Test
    public void parallelStreamIsFasterThanStream() throws Exception {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println("CommonPool : " + commonPool.getParallelism());    // 3

        //given
        List<Status> statues = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            if (i % 3 == 0) {
                statues.add(Status.builder()
                        .id(String.valueOf(i))
                        .powerOff(true)
                        .build());
            } else if (i % 5 == 0) {
                statues.add(Status.builder()
                        .id(String.valueOf(i))
                        .panelOff(true)
                        .build());
            } else {
                statues.add(Status.builder()
                        .id(String.valueOf(i))
                        .panelOn(true)
                        .build());
            }
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("stream");
        statuses.stream()
                .collect(DeviceSummary::new
                        , DeviceSummary::accumulate
                        , DeviceSummary::combine);
        stopWatch.stop();

        stopWatch.start("parallelStream");
        statuses.parallelStream()
                .collect(DeviceSummary::new
                        , DeviceSummary::accumulate
                        , DeviceSummary::combine);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
