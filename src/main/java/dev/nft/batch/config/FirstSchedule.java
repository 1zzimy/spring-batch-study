package dev.nft.batch.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FirstSchedule {
	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;

	@Scheduled(cron = "10 * * * * *", zone = "Asia/Seoul")
	public void runFirstJob() throws Exception {

		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dataFormat.format(new Date());

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("date", date)
			.toJobParameters();

		jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);
	}
}
