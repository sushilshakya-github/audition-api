package com.audition.configuration;

import org.springframework.stereotype.Component;

@Component
public class ResponseHeaderInjector {

    // TODO Inject openTelemetry trace and span Ids in the response headers.

	/** Kindly refer RestCallLoggingInterceptor.java, added below headers
		response.getHeaders().add("X-B3-TraceId:-", spanContextSupplier.getTraceId());
		response.getHeaders().add("X-B3-SpanId:-", spanContextSupplier.getSpanId());
	 * 
	 */
}
