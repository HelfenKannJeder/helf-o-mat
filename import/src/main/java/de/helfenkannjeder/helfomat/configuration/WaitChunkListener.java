package de.helfenkannjeder.helfomat.configuration;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class WaitChunkListener implements ChunkListener {

	private final int waitTime;
	private static final Logger LOGGER = Logger.getLogger(WaitChunkListener.class);

	public WaitChunkListener(int waitTime) {
		this.waitTime = waitTime;
	}

	@Override
	public void beforeChunk(ChunkContext chunkContext) {

	}

	@Override
	public void afterChunk(ChunkContext chunkContext) {
		try {
			LOGGER.debug("wating 40s");
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterChunkError(ChunkContext chunkContext) {

	}
}
