# Java 8 Functional Programming
## Stream
Stream은 Monads 다. Java에 functional Programming 개념이 들어온 큰 부분이다
```
Functional Programmin에서 Monad는 step의 sequence로 정의된 연산을 나타내는 구조체다.
모나드 구조를 갖는 타입은 operation의 chain화와 그 타입의 함수를 함께 중첩시키는 것을 정의한다
```
### Reduce
Reduce는 스트림의 모든 요소들의 각각의 결과를 combine하는 연산이다. java 8에 reduce는 세가지 함수 형식을 지원한다.
1) accumulate 2) init , accumulate 3) init, accumulate, combine
세번째 combine과 함께하는 경우, parallelStream을 사용하는 경우 combine이 동작한다

#### reduce vs collect
parallelStream 사용시 **reduce는 accumulate 중의 객체의 상태가 변하면 안된다.** 연산이 어떤 순서로 싫행되도 결과가
바뀌면 안된다.

reduce의 경우 리턴 뿐만 아니라 accumulate 내부에서도 새로운 객체를 생성한다. 따라서 메모리 할당을 많이 하기 때문에 object를 
사용하는 경우 성능 저하의 원인이 된다. 이렇게 원시값보다 복잡한 object를 사용하는 경우 collect를 사용한다.
* [oracle java8 reduce vs collect](https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html)

#### stream vs parallelStream 성능
과연 parallelStream을 사용한다고 해서 stream보다 성능이 좋을 것인가?
* 속도 : parallelStream을 사용할 때, context switch 비용으로 인해 속도가 더 늦을 수 있다.
* 처리량 : jMeter로 request를 lamp-up을 요청시 처리량 증가하는지 확인


#### 참고
* [Stream 예제](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)


## Optional

## CompletableFuture