# 정신과 시간의 방(Hyperbolic Time Chamber) 정리

아래 양식 처럼 배운 것을 정리하면 됩니다. Readme.md 파일은 자유 형식입니다. 
Dictionary.md 파일은 알게 된 것과 모르는 것을 정리해서 정리하시면 됩니다.

이 프로젝트 오른쪽에 fork 버튼을 눌러 포크를 딴 후에 전체 내용은 지우시고 자신만의 컨텐츠를 채워가면 됩니다. [마크 다운 문법](https://gist.github.com/ihoneymon/652be052a0727ad59601)  

fork를 해 가시면 제가 주기적으로 확인해서 SDSACT 내의 Hyperbolic Time Chamber 프로젝트에 링크를 걸도록 하겠습니다.

SDS ACT 정신과 시간의 방 : https://github.com/SDSACT/hyperbolic_time_chamber 

## Reactor for building non-blocking application
스프링5 릴리즈되면서 [webflux](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html) 모듈이 추가되었고 
reactive 웹어플리케이션을 구현할 수 있게 되었다. 현재 프로젝트에서 개발중인 서버는 최대 10만대 디바이스가 접속할 수 있고 최대의 처리량을 낼 수 있는 고성능 서버를 만드는 것을 목표로 한다.
현재 java8에 CompletableFuture 기반의 비동기 처리를 적용 중이다.
#### Reactive Mongo DB 적용
DB connection의 blocking call 제거를 통한 비동기 기반의 API 구현
- [ReactiveMongoTemplate](https://github.com/spring-projects/spring-data-mongodb/blob/master/src/main/asciidoc/reference/reactive-mongodb.adoc)
- [ReactiveMongoRepository](https://github.com/spring-projects/spring-data-mongodb/blob/master/src/main/asciidoc/reference/reactive-mongo-repositories.adoc)

#### CompletableFuture 를 Flux 대체
CompletableFuture 와 Flux 를 비교하여 장점 확인 후 대체
- Mono 0..1
- Flux 0..N

## 커리큘럼
| Seq |    날짜    |             내용             |  비고  |
|:---:|:----------:|:----------------------------:|:------:|
|  1  | 2017/11/21 | Java8 Functional Programming | Stream |
|  2  | 2017/11/22 | Java8 CompletableFuture      |        |
|  3  | 2017/11/23 | Java8 vs RxJava vs Reactore  |        |
  
## 참고 자료
* [Comparing java8 vs RxJava vs Reactor](http://alexsderkach.io/comparing-java-8-rxjava-reactor/)
* [Project reactor document](https://projectreactor.io/docs/core/release/reference/)
* [Why Reactor vs RxJava2](https://www.infoq.com/articles/reactor-by-example)
* [Reactor Bismuth is out](https://spring.io/blog/2017/09/28/reactor-bismuth-is-out)