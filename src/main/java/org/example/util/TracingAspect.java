package org.example.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Scope;

@Aspect
public class TracingAspect {

    private final Tracer tracer = GlobalOpenTelemetry.getTracer("my-app");

    @Around("@annotation(SFTrack)")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable {
        String spanName = pjp.getSignature().toShortString();
        Span span = tracer.spanBuilder(spanName).startSpan();
        try (Scope scope = span.makeCurrent()) {
            return pjp.proceed();
        } catch (Throwable t) {
            span.recordException(t);
            span.setStatus(StatusCode.ERROR);
            throw t;
        } finally {
            span.end();
        }
    }
}
