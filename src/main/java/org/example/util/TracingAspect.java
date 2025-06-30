package org.example.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Scope;

@Aspect
public final class TracingAspect {

    

    @Around("@annotation(SFTrack)")
    public final Object trace(ProceedingJoinPoint pjp) throws Throwable {
        var threadName = Thread.currentThread().getName();
        var shortName = pjp.getSignature().toShortString();
        var tracer = GlobalOpenTelemetry.getTracerProvider().get(shortName);
        String spanName = pjp.getSignature().getName();
    
        Span span = tracer.spanBuilder(spanName).startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("thread.name", threadName);
            var result = pjp.proceed();
            span.setStatus(StatusCode.OK);
            return result; 
        } catch (Throwable t) {
            span.recordException(t);
            span.setStatus(StatusCode.ERROR);
            throw t;
        } finally {
            span.end();
        }
    }
}
