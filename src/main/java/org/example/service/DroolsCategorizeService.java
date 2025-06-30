package org.example.service;

import java.util.List;

import org.example.util.SFTrack;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.LoggerFactory;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;


/**
 * Categorization service.
 */
final public class DroolsCategorizeService {

    private final KieContainer kieContainer;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(DroolsCategorizeService.class);

    public DroolsCategorizeService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    @SFTrack
    private final KieSession getKieSession() {
        return kieContainer.newKieSession();
    }

    @SFTrack
    public final String apply(List<?> facts) {
        KieSession kieSession = getKieSession();
        // Populate the kieSession with the facts
        facts.forEach(fact -> kieSession.insert(fact));
        Span.current().addEvent("Inserting facts into KieSession", Attributes.of(
            AttributeKey.stringKey("facts.size"), String.valueOf(facts.size())
        ));
        
        var results = fireRulesAndGetResults(facts, kieSession);
        return factsToString(results);

    }

    @SFTrack
    private final Object[] fireRulesAndGetResults(List<?> facts, KieSession kieSession) {
        int firedRules = kieSession.fireAllRules();
        logger.info("Fired {} rules", firedRules);
        logger.info("Facts in KieSession: {}", kieSession.getFactCount());
        var results = kieSession.getObjects(x -> !facts.contains(x)).toArray(new Object[0]);
        kieSession.dispose();
        Span.current().addEvent("New facts into KieSession", Attributes.of(
            AttributeKey.stringKey("results.length"), String.valueOf(results.length)
        ));
        return results;
    }

    @SFTrack
    private static String factsToString(Object[] facts) {
                        StringBuilder results = new StringBuilder();
                        results.append("[");
                        boolean first = true;
                        for (Object fact : facts) {
                            try {
                                String json = OBJECT_MAPPER.writeValueAsString(fact);
                                if (!first) results.append(", ");
                                results.append(json);
                            } catch (JsonProcessingException e) {
                                logger.error("Error converting fact to JSON", e);
                            }
                            first = false;
                        }
                        results.append("]");
                        return results.toString();
                    }
}
