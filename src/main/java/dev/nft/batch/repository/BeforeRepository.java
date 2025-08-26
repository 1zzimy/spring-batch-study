package dev.nft.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.nft.batch.entity.BeforeEntity;

public interface BeforeRepository extends JpaRepository<BeforeEntity, Long> {

}