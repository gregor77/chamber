#  학습계획
* Java 8 Feature
  - [Stream](https://github.com/gregor77/chamber/blob/master/reactive/src/test/java/com/rhyno/reactive/java8/stream/java8-stream.md)
    * reduce
  - Optional
  - CompletableFuture
* CompletableFuture vs RxJava vs Reactor 
* Reactor

# Reactor
Reactor는 효율적인 요구 관리("backpressure"형태로)와 함께 JVM을 위한 완벽한 non-blocking 리액티브 프로그래밍 기반이다.
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

# 3. Reactive Programming 소개
Reactor는 리액티브 프로그래밍 패러다임 구현체이다.
```
리액티브 프로그래밍은 데이터 스트림 및 변경 전파와 관련된 비동기 프로그래밍 패러다임이다.
이는 채용된 프로그래밍 언어를 통해 정적(예: array) 또는 정적(예:event emitters) 데이터 스트림을
쉽게 표현할 수 있음을 의미한다.
    - https://en.wikipedia.org/wiki/Reactive_programming-
```
리액티브 프로그래밍 패러다임은 종종 옵저버 디자인 패턴의 확장으로서 객체지향 언어로 제공된다. 이러한 모든 라이브러리의 
Iterable-Iterator 쌍에 대한 이중성이 있으므로 익숙한 반복자 디자인 패턴과 기본 반응 스트림을 패턴을
비교할 수도 있습니다. 한 가지 큰 차이점은 Iterator는 pull기반이지만 Reactive stream은 push 기반이라는 점입니다

## 3.1 Blocking은 낭비일 수 있다
모던 어플리케이션은 많은 수의 동시 사용자에게 다가 갈 수 있으며 현재 하드웨어의 기능이 계속 향상 되더라도
현대 소프트웨어의 성능은 여전히 중요한 관심사이다.

프로그램의 성능을 향상할 수 있는 두가지 방법에는 크게 두 가지가 있다.
1. **parallelize** : 더 많은 쓰레드와 많은 하드웨어 자원의 사용
2. **현재 자원이 어떻게 사용되는지에 대해 더 많은 효율성을 추가하라**

일반적으로 자바 개발자은 blocking code를 사용하여 프로그램을 작성한다 성능의 병목이 발생하기 전까지는 이 방법은 문제가 없으며
비슷한 시점에 비슷한 쓰레드 코드를 실행하여 쓰레드를 추가로 도입해야 한다. 그러나 자원 활용도의 이러한 확장으로 인해 contention(경합) 
및 동시성 문제가 신속하게 발생할 수 있다.

더욱 나쁜 것은 블록킹 자원을 차단하는 것이다. 자세히 보면 프로그램의 대기시간 (특히 DB요청, 네트워크 I/O)이 포함되는 즉시 쓰레드
(또는 많은 쓰레드)가 유휴 상태에 있고, 데이터를 기다리기 때문에 리소스는 낭비된다.

parallelization의 접근은 만능 해결책이 아니다 하드웨어의 전체 성능에 엑세스 하는데 필요하지만 
리소스 낭비를 추론하고 복잡하게 만드는 이유다.

## 3.2 Asynchronicity(비동기성)는 구조할 수 있나?
효율성을 추구하는 두 번째 방법은 자원 낭비 문제에 대한 해결책이 될 수 있다. 비동기 비 블록킹 코드를 작성함으로써 동일한 기본 자원을
사용하는 다른 활성 태스크로 실행을 전환하고 나중에 비동기 처리가 완료되면 현재 프로세스가 되돌아 간다.
 
어떻께 JVM위에서 비동기 코드를 작성할 수 있나? 자바는 비동기 프로그래밍을 위한 두 가지 모델을 제공한다.
* **Callbacks**
  - 비동기 메소드는 리턴 값이 없지만 결과를 사용할 수 있을때 호출되는 추가 callback 파라미터를 사용한다.
  - 잘 알려진 예제는 Swing의 EventListener 계층 구조다
* **Futures**
  - 비동기 메소드는 Future<T>를 즉시 리턴한다 비동기 프로세스는 T 값을 계산하지 Future 객체는 계산값에 접근할 수 있게 wrapping 한다
  - 값을 즉시 사용할 수 없으며 값을 사용할 때까지 객체를 폴링 할 수 있다.
  - 예를 들어 Callable <T> 작업을 실행하는 ExecutorService는 Future 객체를 사용한다

이러한 방법으로 충분한가? 모든 유즈 케이스에 해당하는 것은 아니며 두 가지 접근 방법 모두에 한계가 있다.
Callback은 함께 작성하기 어렵고, 읽기 및 유지가 어려운 코드로 빠르게 이어진다.(소위 "콜백 지옥"이라고 한다)

Callback과 Future를 사용한 코드는 Reacotr에 제공하는 api를 통해서 더 쉽게 작성할 수 있다.

## 3.3 명령형 프로그래밍에서 반응형 프로그래밍으로
Reactor 같은 Reactive 라이브러리는 JVM에서 "고전적인" 비동기 방식의 이러한 단점을 해결하는 동시에
몇 가지 추가 측면에 초점을 맞추고 있다.
* Composability(합성성) and readablity(가독성)
* 풍부한 연산자의 풍부한 어휘로 조작된 데이터
* subscribe 할때까지 아무 일도 일어나지 않는다
* backpressure 또는 consumer가 배출량이 너무 높음을 producer에게 알릴 수 있는 능력
* 동시성에 의존하지 않는 높은 수준이지만 높은 가치의 추상화

### 3.3.6 Hot vs Cold
반응성 라이브러리의 Rx 계열에서 반응성 시퀀스의 두 가지 범주인 Hot, Cold것을 구별할 수 있다 이러한 구분은
반응성 스트림이 subscribe와 어떻게 반응하지와 관련 있다

* 데이터 소스를 포함하여 각 subscriber에 대해 Cold 시퀀스가 새로 시작된다. 소스가 HTTP 호출을 랩하면 각
subscription에 대해 새 HTTP 요청이 작성된다

* Hot sequence는 각 subscriber에 대해 처음부터 시작되지 않는다. 오히려 늦은 구독자는 가입한 후 방출되는
신호를 받는다. 그러나 일부 고 반응성 스트림은 전체 또는 부분적으로 배출 기록을 캐쉬하거나 재생할 수 있다
일반적인 관점에서 볼 때 구독자가 없는 경우 핫 시퀀스를 내보낼 수도 있다 ("구독하기 전에는 아무 일도 일어나지 않는다" 규칙) 


# 4. Reactor core
## Reactor의 Type
* Flux, Asynchronous Sequences of 0-N Items
* Mono, Asynchronous 0-1 Result

## Subscribe Item
작업중

## Programmatically creating a sequence
### Generate
generate() 메소드를 사용하여 Flux를 생성하는 방법을 테스트 케이스를 통해서 알아보자. generate 메소드를 사용하면 state를 기반으로
Flux를 생성할 수 있다. 또한 AtomicLong 객체를 사용하여 mutuable한 state를 생성할 수 있다. 또한 Consumer 함수를 사용하는 경우
가장 마지막의 state를 알 수 있다. **마지막 state를 알 수 있는 경우, 프로세스가 끝나는 시점에 db connection을 종료하거나 프로세스가 
끝나는 시점의 프로세스를 수행할 수 있는 장점이 있다.**

* 예제 : [FluxGenerateTest.java]()
* state based generate
* mutable state variant

## 참고 자료
* [Reactor by example](https://www.infoq.com/articles/reactor-by-example)
* [Reactor document](https://projectreactor.io/docs/core/release/reference/#getting-started)