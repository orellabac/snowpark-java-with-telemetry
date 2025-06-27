
CREATE STAGE IF NOT EXISTS drools_tests;

PUT file://target/snowpark-java-drools-sample-0.0.1-FAT.jar @drools_tests AUTO_COMPRESS=FALSE OVERWRITE=TRUE;

CREATE OR REPLACE FUNCTION DROOLS_CLASIFY(DATA STRING)
  RETURNS TABLE(RESULT String)
  LANGUAGE JAVA
  RUNTIME_VERSION = '11'
  IMPORTS = ('@drools_tests/snowpark-java-drools-sample-0.0.1-FAT.jar')
  HANDLER = 'org.example.udft.CustomerCategorizeUDTFHandler'
  PACKAGES = ('com.snowflake:snowpark:latest','com.snowflake:telemetry:latest');

-- testing the functionality

with input_data as (
    select C_NATIONKEY,C_CUSTKEY, object_construct(*) data
    from snowflake_sample_data.tpch_sf1.customer
)
select r.result from input_data, 
table(
  drools_clasify(input_data.data::STRING) 
      over (partition by C_NATIONKEY order by C_CUSTKEY)) r;
 