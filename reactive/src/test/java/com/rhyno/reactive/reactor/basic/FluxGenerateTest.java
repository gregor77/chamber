package com.rhyno.reactive.reactor.basic;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FluxGenerateTest {
    @Test
    public void createFluxWithList() throws Exception {
        Flux<String> numbers = Flux.fromIterable(Arrays.asList("one", "two", "three"));
        numbers.subscribe(n -> System.out.println(n));
    }

    @Test
    public void createFluxWithRange() throws Exception {
        Calculator result = new Calculator();
        Flux<Integer> range1 = Flux.fromIterable(IntStream.range(0, 5).boxed().collect(Collectors.toList()));
        range1.subscribe(result::sum);

        assertThat(result.getSum()).isEqualTo(10);

        Flux<Integer> range2 = Flux.range(0, 5);
        range2.subscribe(i -> System.out.println(i));
    }

    @Test
    public void createFluxWithStateBasedGenerator() throws Exception {
        Flux<String> tableOf3 = Flux.generate(
                () -> 1,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });

        List<String> result = getTableOf3WithFlux(tableOf3);
        assertMultiplicationTableOfThird(result);
    }

    @Test
    public void createFluxWithMutableStateVariant() throws Exception {
        Flux<String> tableOf3 = Flux.generate(
                () -> new AtomicLong(1),
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                });

        List<String> result = getTableOf3WithFlux(tableOf3);
        assertMultiplicationTableOfThird(result);
    }

    @Test
    public void createFluxWithMutableStateVariantAndConsumer() throws Exception {
        Flux<String> tableOf3 = Flux.generate(
                () -> new AtomicLong(1),
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                }, (state) -> System.out.println("state: " + state));   //마지막 state 확인 가능

        List<String> result = getTableOf3WithFlux(tableOf3);
        assertMultiplicationTableOfThird(result);
    }

    private void assertMultiplicationTableOfThird(List<String> result) {
        assertThat(result.size()).isEqualTo(10);
        assertThat(result).contains("3 x 1 = 3", "3 x 2 = 6", "3 x 3 = 9",
                "3 x 4 = 12", "3 x 5 = 15", "3 x 6 = 18",
                "3 x 7 = 21", "3 x 8 = 24", "3 x 9 = 27",
                "3 x 10 = 30");
    }

    private List<String> getTableOf3WithFlux(Flux<String> tableOf3) {
        List<String> result = new ArrayList<>(10);
        tableOf3.subscribe(s -> result.add(s));
        return result;
    }

    class Calculator {
        private int sum;

        public void sum(int n) {
            this.sum += n;
        }

        public int getSum() {
            return sum;
        }
    }
}
