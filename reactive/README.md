#  학습계획
* Java 8 Feature
  - Stream
    * reduce
  - Optional
  - CompletableFuture
* CompletableFuture vs RxJava vs Reactor 
* Reactor

# Reactor
Reactor는 RxJava2처럼 4세대 reactive library다. Spring Pivotal에서 시작됐고 Reactive Stream 명세, Java8, ReactiveX 용어 기반으로 구현된다.

Observable은 소스를 push하고 Observer는 subscribe를 통해서 소스를 소비하는 간단한 interface다.
Observable과 계약은 Observer에게 onNext 메소드를 통해서 0 또는 그 이상의 데이터 아이템을 전달한다.
선택적으로 onError 또는 onComplete를 통해서 event를 제거한다.

Observable 테스트하기 위해 RxJava는 스트림에서 이벤트를 assert하게 해주는 특별한 Observer인 
TestSubscriber를 제공한다.

## Reactor의 타입
Reactor의 주된 타입은 Flux<T>와 Mono<T>이다. Flux는 RxJava의 Observable과 동일하고, 0 또는 N개의 항목을 방출하거나, 선택적으로 완료 또는 에러 처리를 할 수 있다.

Mono는 최소 한개의 아이템만 방출할 수 있다. RxJava에 Single과 Maybe와 상응한다. 한 개의 처리를 원하는 비동기 태스크의 경우 Mono<Void>를 사용한다.

두가지 유형을 간단히 구별하면 쉽게 이해할 수 있고, reactive API에서 의미있는 semantic을 제공할 수 있다. 단지 리턴되는 reactive type만 보면 메소드가 한 개의 항목을 리턴하는지
또는 stream 같은 여러개의 항목을 처리하는지 알 수 있다.

## Reactive Libraries and Reactive Streams 채택
Reactive Stream은 "non-blocking으로 비동기 스트림 처리를 위한 표준을 제공하는 계획"이다
Java 9에도 포함된 Publisher, Subscriber, Subscription, Processor 인터페이스 스펙의 집합이다

RS의 최초 접근과 Reactor의 key 차이점은 Flux와 Mono는 RS의 Publisher의 구현체고 reactive-pull back-pressure를 따른다
 

## 참고 자료
*[Reactor by example](https://www.infoq.com/articles/reactor-by-example)
