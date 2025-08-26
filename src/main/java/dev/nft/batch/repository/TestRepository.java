package dev.nft.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.nft.batch.entity.TestEntity;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
