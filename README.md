## 개요 
- 테스트 기반 간단 게시판 CRUD 구현 프로젝트
- 헥사고날 아키텍처 적용 
- 단위 테스트, 레이어 테스트, 통합 테스트

### 1. 패키지 구조 
```text
article
    ├── domain
    │    ├── Article.java  
    ├── application
    │    ├── service
    │    │   └── ArticleService.java
    │    └── port
    │        ├── in
    │        │   ├── CreateArticleUseCase.java
    │        │   ├── UpdateArticleUseCase.java
    │        │   ├── DeleteArticleUseCase.java
    │        │   └── QueryArticleUseCase.java
    │        └── out
    │            ├── CommandArticlePort.java
    │            ├── QueryArticlePort.java
    └── adaptor/port
             ├── in
             │    └── ArticleController.java
             └── out
                 └── repository
                       ├── ArticlePersistenceAdapter.java
                       ├── ArticleRepository.java
                       ├── entity
                             ├── ArticleEntity.java
                            
```

<br/>

**application/port/in패키지**
- 서비스가 구현해야 하는 interface 
- 인커밍 어답터 (ex. Controller)가 의존하는 인커밍 포트 인터페이스 존재

**applcation/port/out패키지**
- 서비스가 의존하는 interface 
- 아웃고잉 어답터 (ex. Repository)에서 구현해야 하는 interface

**apdaptor/port/in패키지**
- 인커밍 포트를 의존/호출하는 인커밍 어답터 존재 (ex. Controller)
- 외부 API 요청을 받아 처리하는 Controller 위치

**apdaptor/port/out패키지**
- 아웃고잉 포트에 대한 구현 어답터 존재 (ex. Repository)
- JPA 관련 영속성 처리하는 Entity, JpaRepository 위치

<br/>

### 2. 참고 사이트
- Junit5: https://junit.org/junit5/
- Assertj : https://assertj.github.io/doc/
- Mockito : https://github.com/mockito/mockito
- Hamcrest : https://hamcrest.org/
- jsonpath : https://github.com/json-path/JsonPath
- java-test-fixture : https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures
- hexagonal-architecture: https://alistair.cockburn.us/hexagonal-architecture/
