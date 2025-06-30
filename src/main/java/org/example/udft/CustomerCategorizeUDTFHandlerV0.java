package org.example.udft;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.configuration.DroolsConfig;
import org.example.model.Customer;
import org.example.service.DroolsCategorizeService;
import org.example.util.SFTrack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;

class OutputRow_V0 {
    public String result;

    public OutputRow_V0(String result) {
        this.result = result;
    }
}

/**
 * Customer Categorization UDTF
 * 
 * @author mauricio.rojas
 */
public class CustomerCategorizeUDTFHandlerV0 {
    private transient DroolsCategorizeService service = null;
    List<String> inputData = new ArrayList<String>();
    int receivedChars = 0;
    private static final ObjectMapper mapper = new ObjectMapper();
    public CustomerCategorizeUDTFHandlerV0() {}

    @SFTrack
    private void setupService() {
        var config = new DroolsConfig();
        var container = config.kieContainer();
        this.service = new DroolsCategorizeService(container);
    }

    public static Class<?> getOutputClass() { return OutputRow_V0.class;}

    public Stream<OutputRow_V0> process(String data) {
        receivedChars += data.length();
        inputData.add(data);
        return Stream.empty();
    }

    @SFTrack
    public List<Customer> deSerializeData() {
        Span.current().addEvent("Deserializing objects", Attributes.of(
                AttributeKey.longKey("object.bytes"), Long.valueOf(receivedChars),
                AttributeKey.longKey("object.count"), Long.valueOf(inputData.size())));
        List<Customer> customers = inputData.stream()
                .map(json -> {
                    try {
                        return mapper.readValue(json, Customer.class);
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                        return null;
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(customer -> customer != null)
                .collect(Collectors.toList());
        return customers;
    }

    @SFTrack
    public Stream<OutputRow_V0> endPartition() {
        var customers = deSerializeData();
        if (this.service == null)
            setupService();
        var result_str = this.service.apply(customers);
        return Stream.of(new OutputRow_V0(result_str));
    }

}
