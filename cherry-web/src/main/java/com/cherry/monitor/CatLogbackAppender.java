package com.cherry.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ch.qos.logback.classic.spi.ThrowableProxy;
import com.dianping.cat.log4j.CatAppender;
import org.apache.log4j.spi.ThrowableInformation;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Trace;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.status.ErrorStatus;

public class CatLogbackAppender<E> extends UnsynchronizedAppenderBase<E> {


	@Override
	protected void append(E eventObject) {
		boolean isTraceMode = Cat.getManager().isTraceMode();
		if(eventObject instanceof LoggingEvent){
			LoggingEvent event  = (LoggingEvent)eventObject;
			if(event.getLevel().isGreaterOrEqual(Level.ERROR)){
				logError(event);
			}else if (isTraceMode) {
				logTrace(event);
			}
		}


		

	}

	private String buildExceptionStack(Throwable exception) {
		if (exception != null) {
			StringWriter writer = new StringWriter(2048);
			exception.printStackTrace(new PrintWriter(writer));
			return writer.toString();
		} else {
			return "";
		}
	}


	private void logError(LoggingEvent event) {
		IThrowableProxy info = event.getThrowableProxy();

		if (info != null&&info instanceof ThrowableProxy) {
			Throwable exception = ((ThrowableProxy)info).getThrowable();
			Object message = event.getMessage();
			if (message != null) {
				Cat.logError(String.valueOf(message), exception);
			} else {
				Cat.logError(exception);
			}
		}
	}

	private void logTrace(LoggingEvent event) {
		String type = "Log4j";
		String name = event.getLevel().toString();
		Object message = event.getMessage();
		String data;

		if (message instanceof Throwable) {
			data = buildExceptionStack((Throwable) message);
		} else {
			data = event.getMessage().toString();
		}

		Cat.logTrace(type, name, Trace.SUCCESS, data);
	}

}
