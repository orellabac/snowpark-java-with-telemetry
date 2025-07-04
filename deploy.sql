
CREATE STAGE IF NOT EXISTS drools_tests;

PUT file://target/snowpark-java-drools-sample-0.0.1-FAT.jar @drools_tests AUTO_COMPRESS=FALSE OVERWRITE=TRUE;


----- VERSION 0 -----

CREATE OR REPLACE FUNCTION DROOLS_CLASIFY_V0(DATA STRING)
  RETURNS TABLE(RESULT String)
  LANGUAGE JAVA
  RUNTIME_VERSION = '11'
  IMPORTS = ('@drools_tests/snowpark-java-drools-sample-0.0.1-FAT.jar')
  HANDLER = 'org.example.udft.CustomerCategorizeUDTFHandlerV0'
  PACKAGES = ('com.snowflake:snowpark:latest','com.snowflake:telemetry:0.0.1');

-- select count(*) from snowflake_sample_data.tpch_sf10.customer; -- 150000
--select avg(c) from 
--(select count(*) c from snowflake_sample_data.tpch_sf1.customer
--     group by C_NATIONKEY);


with input_data as (
    select C_NATIONKEY,C_CUSTKEY, object_construct(*) data
    from snowflake_sample_data.tpch_sf1.customer
),
results as (
    select r.result from input_data, 
    table(
      DROOLS_CLASIFY_V0(input_data.data::STRING) 
          over (partition by C_NATIONKEY order by C_CUSTKEY)) r
)
select
  f.value:"name" NAME,
  f.value:"key" KEY, 
  f.value:"classification" CLASSIFICATION 
from results, table(flatten(input => parse_json(results.result))) as f;
-- 26s


with input_data as (
    select C_NATIONKEY,C_CUSTKEY, object_construct(*) data
    from snowflake_sample_data.tpch_sf10.customer
),
results as (
    select r.result from input_data, 
    table(
      DROOLS_CLASIFY_V0(input_data.data::STRING) 
          over (partition by C_NATIONKEY order by C_CUSTKEY)) r
)
select
  f.value:"name" NAME,
  f.value:"key" KEY, 
  f.value:"classification" CLASSIFICATION 
from results, table(flatten(input => parse_json(results.result))) as f;

-- 10m 55s

----- VERSION 1 -----

CREATE OR REPLACE FUNCTION DROOLS_CLASIFY_V1(DATA STRING)
  RETURNS TABLE(RESULT String)
  LANGUAGE JAVA
  RUNTIME_VERSION = '11'
  IMPORTS = ('@drools_tests/snowpark-java-drools-sample-0.0.1-FAT.jar')
  HANDLER = 'org.example.udft.CustomerCategorizeUDTFHandlerV1'
  PACKAGES = ('com.snowflake:snowpark:latest','com.snowflake:telemetry:0.0.1');


with input_data as (
select ROW_NUMBER()  OVER (ORDER BY seq8()) % 128  AS part, C_CUSTKEY, object_construct(*) data
    from snowflake_sample_data.tpch_sf1.customer  
),
results as (
    select r.result from input_data, 
    table(
      DROOLS_CLASIFY_V1(input_data.data::STRING) 
          over (partition by part order by C_CUSTKEY)) r
)
select
  f.value:"name" NAME,
  f.value:"key" KEY, 
  f.value:"classification" CLASSIFICATION 
from results, table(flatten(input => parse_json(results.result))) as f;

-- 22s 


with input_data as (
select ROW_NUMBER()  OVER (ORDER BY seq8()) % 512  AS part, C_CUSTKEY, object_construct(*) data
    from snowflake_sample_data.tpch_sf10.customer  
),
results as (
    select r.result from input_data, 
    table(
      DROOLS_CLASIFY_V1(input_data.data::STRING) 
          over (partition by part order by C_CUSTKEY)) r
)
select
  f.value:"name" NAME,
  f.value:"key" KEY, 
  f.value:"classification" CLASSIFICATION 
from results, table(flatten(input => parse_json(results.result))) as f;

-- 3m 37s 

use warehouse xsmall_wh;

create warehouse if not exists drools_wh
  with warehouse_size = 'SMALL'
  auto_suspend = 5
  auto_resume = true;



  CREATE OR REPLACE FUNCTION customSpansJavaExample(data string) RETURNS STRING
  LANGUAGE JAVA
  PACKAGES = ('com.snowflake:telemetry:latest')
  HANDLER = 'MyJavaClass.run'
  as
  $$
  import com.snowflake.telemetry.Telemetry;
  import io.opentelemetry.api.common.AttributeKey;
  import io.opentelemetry.api.common.Attributes;
  import io.opentelemetry.api.GlobalOpenTelemetry;
  import io.opentelemetry.api.trace.Tracer;
  import io.opentelemetry.api.trace.Span;
  import io.opentelemetry.context.Scope;

  import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

  class MyJavaClass {
    public static String run(String data) {

   Logger logger = LoggerFactory.getLogger(MyJavaClass.class);
        return ("SLF4J logger implementation: " + logger.getClass().getName());

    }
  }
  $$;

  select customSpansJavaExample(null);