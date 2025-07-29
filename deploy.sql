
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

-- 12s 



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

-- 16s 

