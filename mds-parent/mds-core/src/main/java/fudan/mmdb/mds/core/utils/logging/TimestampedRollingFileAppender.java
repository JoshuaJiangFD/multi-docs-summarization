package fudan.mmdb.mds.core.utils.logging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Layout;
import org.apache.log4j.RollingFileAppender;

public class TimestampedRollingFileAppender extends RollingFileAppender {

	private static final String TARGET = "\\{timestamp\\}";

	protected String timestampPattern = null;

	public TimestampedRollingFileAppender() {
		super();
	}

	public TimestampedRollingFileAppender(Layout layout, String filename,
			boolean append) throws IOException {
		super(layout, filename, append);
	}

	public TimestampedRollingFileAppender(Layout layout, String filename)
			throws IOException {
		super(layout, filename);
	}

	@Override
	public synchronized void setFile(String fileName, boolean append,
			boolean bufferedIO, int bufferSize) throws IOException {
		if (timestampPattern != null) {
			super.setFile(
					fileName.replaceAll(TARGET, new SimpleDateFormat(
							timestampPattern).format(Calendar.getInstance()
							.getTime())), append, bufferedIO, bufferSize);
		} else {
			super.setFile(fileName, append, bufferedIO, bufferSize);
		}
	}

	public String getTimestampPattern() {
		return timestampPattern;
	}

	public void setTimestampPattern(String timestampPattern) {
		this.timestampPattern = timestampPattern;
	}
	
}
