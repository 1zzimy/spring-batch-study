package dev.nft.batch.config;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import dev.nft.batch.entity.AfterEntity;
import dev.nft.batch.entity.BeforeEntity;
import dev.nft.batch.repository.AfterRepository;
import dev.nft.batch.repository.BeforeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FirstBatch {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	private final BeforeRepository beforeRepository;
	private final AfterRepository afterRepository;

	@Bean
	public Job firstJob() { // Table to Table
		return new JobBuilder("firstJob", jobRepository)
			.start(firstStep())
			.build();
	}

	@Bean
	public Step firstStep() {
		log.info("first step");

		return new StepBuilder("firstStep", jobRepository)
			// BeforeEntity 데이터를 읽고, AfterEntity 에 쓸거임
			.<BeforeEntity, AfterEntity>chunk(10, platformTransactionManager) // 대량의 데이터를 끊어 읽는 최소 단위 -> 효율적 처리 가능
			.reader(beforeReader()) // 읽기
			.processor(middleProcessor()) // 처리하기
			.writer(afterWriter()) // 쓰기
			.build();
	}

	@Bean
	public RepositoryItemReader<BeforeEntity> beforeReader() {
		return new RepositoryItemReaderBuilder<BeforeEntity>()
			.name("beforeReader")
			.pageSize(10)
			.methodName("findAll")
			.repository(beforeRepository)
			.sorts(Map.of("id", Sort.Direction.ASC))
			.build();
	}

	@Bean
	public ItemProcessor<BeforeEntity, AfterEntity> middleProcessor() {
		return new ItemProcessor<BeforeEntity, AfterEntity>() {
			@Override
			public AfterEntity process(BeforeEntity beforeEntity) throws Exception {
				AfterEntity afterEntity = new AfterEntity();
				afterEntity.setUsername(beforeEntity.getUsername());
				return afterEntity;
			}
		};
	}

	@Bean
	public RepositoryItemWriter<AfterEntity> afterWriter() {
		return new RepositoryItemWriterBuilder<AfterEntity>()
			.repository(afterRepository)
			.methodName("save")
			.build();
	}
}
