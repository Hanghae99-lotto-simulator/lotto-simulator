package com.lotto.lotto_simulator.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class Configuration {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}



/*
 @SpringBootTest를 하면 모든 빈이 주입되기 때문에 상관이 없지만, DataJpaTest와 같은 슬라이싱 테스트를 하고싶을 때 문제가 발생한다.
 JpaQueryFactory가 persistenceLayer가 아니어서 빈등록이 되지않아 발생하는 문제인데,
이때 테스트 시 특정부분의 빈만 등록

테스트에서만 사용할 용도의 @TestConfiguration을 이용해 JPAQueryFactory 만 Bean으로 생성해준다.
이후 @Import 어노테이션을 사용해 해당 테스트용 빈을 주입해주면, JpaQueryFactory에 대한 빈도 생성되므로, Querydsl의 슬라이싱 테스트가 가능해진다 :)

 */

