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
import com.snowflake.snowpark_java.*;


class OutputRow {
    public String result;
    public OutputRow(String result) {
        this.result = result;
    }
}
/**
 * Customer Categorization UDTF (see https://docs.snowflake.com/en/developer-guide/udf/java/udf-java-tabular-functions)
 * This is the actual categorization UDTF,
 * it will leverage the Drools Rule
 * @author mauricio.rojas
 */
public class CustomerCategorizeUDTFHandler {

    public static void main(String[] args)
    {
        Session session = Session.builder().configFile("connection.properties").create();
        Row[] data = session.sql("select object_construct(*) from SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.CUSTOMER limit 10").collect();
        ObjectMapper mapper = new ObjectMapper();
        CustomerCategorizeUDTFHandler handler = new CustomerCategorizeUDTFHandler();
        for (Row row : data) 
        {
            String json = row.getString(0);
            handler.process(json).count();
        }
        handler.endPartition().forEach(output -> {
            System.out.println("Result: " + output.result);
        });
    }

    private transient DroolsCategorizeService service=null;

    List<String> inputData = new ArrayList<String>();
    int receivedChars = 0;

    public CustomerCategorizeUDTFHandler() {

    }


    @SFTrack
    private void setupService() {  
        var config = new DroolsConfig();
        var container = config.kieContainer();
        this.service = new DroolsCategorizeService(container);
    }

    public static Class<?> getOutputClass() {
      return OutputRow.class;
    }

    public Stream<OutputRow> process(String data) {
        if (this.service==null) setupService();
        receivedChars += data.length();
        inputData.add(data);
        return Stream.empty();
    }

   @SFTrack
    public List<Customer> deSerializeData() {
        ObjectMapper mapper = new ObjectMapper();
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
    public Stream<OutputRow> endPartition() {
        var customers = deSerializeData();
        var result_str = this.service.apply(customers);
        return Stream.of(new OutputRow(result_str));
    }

}
