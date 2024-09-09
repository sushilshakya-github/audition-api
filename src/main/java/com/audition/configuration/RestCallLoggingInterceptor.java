package com.audition.configuration;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.util.IOUtils;
import io.prometheus.client.exemplars.tracer.common.SpanContextSupplier;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestCallLoggingInterceptor implements ClientHttpRequestInterceptor {

	private final SpanContextSupplier spanContextSupplier;
	
	public RestCallLoggingInterceptor(SpanContextSupplier spanContextSupplier) {
        this.spanContextSupplier = spanContextSupplier;
    }
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		logRequest(request, body);
		
		ClientHttpResponse response = execution.execute(request, body);
		
		response.getHeaders().add("X-B3-TraceId:-", spanContextSupplier.getTraceId());
		response.getHeaders().add("X-B3-SpanId:-", spanContextSupplier.getSpanId());
		logResponse(response);

		return response;
	}

	private void logRequest(HttpRequest request, byte[] body) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("************** Request Log start *********************");
			log.debug("URI: {}", request.getURI());
			log.debug("Method: {}", request.getMethod());
			log.debug("Headers: {}", request.getHeaders());
			log.debug("Request body: {}", new String(body, "UTF-8"));
			log.debug("************** Request Log end *********************");
		}
	}

	private void logResponse(ClientHttpResponse response) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("************** Response Log start *********************");
			log.debug("Status code: {}", response.getStatusCode());
			log.debug("Status text: {}", response.getStatusText());
			log.debug("Headers: {}", response.getHeaders());
			log.debug("Response body: {}", IOUtils.toString(response.getBody()));
			log.debug("************** Response Log end *********************");
		}
	}
}