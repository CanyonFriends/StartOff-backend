package kr.startoff.backend.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

}
