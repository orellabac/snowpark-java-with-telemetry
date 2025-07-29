# Snowpark Java with OpenTelemetry - Customer Categorization UDTFs

This project demonstrates how to leverage **OpenTelemetry** in Snowpark Java UDTFs to gain valuable insights for performance optimization. It showcases the evolution from an initial implementation (V0) to an optimized version (V1) guided by telemetry data, resulting in significant performance improvements.

## 🚀 Key Achievements

- **Performance Improvement**: Achieved **2.2x faster execution** on SF1 dataset (26s → 12s) and **41x improvement** on SF10 dataset (10m 55s → 16s)
- **Telemetry-Driven Optimization**: Used OpenTelemetry traces and spans to identify bottlenecks and guide optimizations
- **Production-Ready Observability**: Minimal runtime impact telemetry that can be enabled/disabled in production workloads

## 📊 Project Overview

This project implements customer categorization using a **Drools rules engine** within Snowpark Java UDTFs. It processes TPC-H customer data and applies business rules to classify customers into different categories such as:

- High Balance customers (>$10,000)
- Automobile Segment customers
- VIP customers from specific nations
- Customers with suspicious comment patterns
- Inactive customers

## 🏗️ Project Structure

```
snowpark-java-with-telemetry/
├── src/main/java/org/example/
│   ├── configuration/
│   │   └── DroolsConfig.java              # Drools KIE container configuration
│   ├── model/
│   │   ├── Customer.java                  # TPC-H customer data model
│   │   ├── Classification.java            # Output classification model
│   │   ├── CustomerCategory.java          # Customer category enum
│   │   ├── CustomerType.java              # Customer type enum
│   │   └── LineItem.java                  # Line item model
│   ├── service/
│   │   └── DroolsCategorizeService.java   # Core business logic using Drools
│   ├── udft/
│   │   ├── CustomerCategorizeUDTFHandlerV0.java  # Initial UDTF implementation
│   │   └── CustomerCategorizeUDTFHandlerV1.java  # Optimized UDTF implementation
│   ├── util/
│   │   ├── SFTrack.java                   # Custom telemetry annotation
│   │   └── TracingAspect.java             # AOP aspect for automatic telemetry
│   └── main/
│       └── LocalSession.java              # Local development entry point
├── src/main/resources/
│   └── rules/
│       └── customer-category.drl          # Drools business rules
├── deploy.sql                             # Snowflake deployment and test queries
└── pom.xml                               # Maven build configuration
```

## 🔧 Build Instructions

### Prerequisites

- **Java 11** or higher
- **Maven 3.6+**
- **Snowflake account** with appropriate permissions
- **SnowSQL CLI** (optional, for deployment) or just use **VSCode with Snowflake Extension**

### Building the Project

1. **Clone the repository**:

   ```bash
   git clone <repository-url>
   cd snowpark-java-with-telemetry
   ```
2. **Build the project**:

   ```bash
   mvn clean package
   ```

   This creates a fat JAR at `target/snowpark-java-drools-sample-0.0.1-FAT.jar` with all dependencies included.
3. **Deploy to Snowflake**:

   ```sql
   -- Run the commands in deploy.sql
   -- This will upload the JAR and create the UDTF functions
   ```

### Key Dependencies

- **Snowpark Java**: Core Snowflake processing framework
- **Snowflake Telemetry**: OpenTelemetry integration for Snowflake
- **Drools**: Business rules engine (v9.44.0.Final)
- **AspectJ**: Aspect-oriented programming for telemetry weaving
- **Jackson**: JSON processing
- **OpenTelemetry API**: Observability and tracing

## 📈 UDTF Implementations Comparison

### CustomerCategorizeUDTFHandlerV0 (Baseline)

**Architecture**:

- Lazy initialization of DroolsCategorizeService in `endPartition()`
- Instance-level service management
- Basic telemetry coverage

**Performance Characteristics**:

- SF1 dataset: **26 seconds**
- SF10 dataset: **10 minutes 55 seconds**
- Partitioned by `C_NATIONKEY` (natural data distribution)

**Key Issues Identified via Telemetry**:

- Service initialization overhead per partition
- Suboptimal partitioning strategy
- JIT compilation inefficiencies

### CustomerCategorizeUDTFHandlerV1 (Optimized)

**Architecture**:

- **Static initialization** of DroolsCategorizeService (shared across instances)
- **Final modifiers** for better JIT optimization
- **Enhanced telemetry** with detailed span events

**Performance Improvements**:

- SF1 dataset: **12 seconds** (2.2x improvement)
- SF10 dataset: **16 seconds** (41x improvement!)
- **Smart partitioning** using `ROW_NUMBER() % N` for better load distribution

**Key Optimizations**:

```java
// V1: Static initialization (done once)
static {
    var config = new DroolsConfig();
    var container = config.kieContainer();
    service = new DroolsCategorizeService(container);
}

// V0: Instance initialization (done per partition)
private void setupService() {
    var config = new DroolsConfig();
    var container = config.kieContainer();
    this.service = new DroolsCategorizeService(container);
}
```

## 🔍 Telemetry Implementation

### Custom Annotation-Based Telemetry

The project uses a custom `@SFTrack` annotation combined with AspectJ for automatic method instrumentation:

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SFTrack {
    String value() default "";
}
```

### Aspect-Oriented Programming (AOP)

The `TracingAspect` automatically wraps annotated methods with OpenTelemetry spans:

```java
@Around("@annotation(SFTrack)")
public final Object trace(ProceedingJoinPoint pjp) throws Throwable {
    String spanName = pjp.getSignature().getName();
    Span span = tracer.spanBuilder(spanName).startSpan();
  
    try (Scope scope = span.makeCurrent()) {
        span.setAttribute("thread.name", Thread.currentThread().getName());
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
```

### Telemetry Data Captured

- **Method execution times** via automatic span creation
- **Object processing metrics**: bytes processed, object counts
- **Drools engine metrics**: rules fired, facts inserted
- **Exception tracking** with full stack traces
- **Thread-level execution context**

Example telemetry event:

```java
Span.current().addEvent("Deserializing objects", Attributes.of(
    AttributeKey.longKey("object.bytes"), Long.valueOf(receivedChars),
    AttributeKey.longKey("object.count"), Long.valueOf(inputData.size())
));
```

## 🎯 Business Rules

The project includes 5 Drools rules for customer categorization:

1. **High Account Balance**: Customers with balance > $10,000
2. **Automobile Segment**: Customers in the AUTOMOBILE market segment
3. **Suspicious Comments**: Customers with complaints or negative feedback
4. **VIP Nation 5**: High-value customers from nation key 5
5. **Inactive Customers**: Customers with zero account balance

## 🚦 Usage Examples

### Running the UDTFs

```sql
-- Test V1 with optimized partitioning
WITH input_data AS (
    SELECT ROW_NUMBER() OVER (ORDER BY seq8()) % 128 AS part, 
           C_CUSTKEY, 
           OBJECT_CONSTRUCT(*) data
    FROM snowflake_sample_data.tpch_sf1.customer  
),
results AS (
    SELECT r.result 
    FROM input_data, 
    TABLE(DROOLS_CLASIFY_V1(input_data.data::STRING) 
          OVER (PARTITION BY part ORDER BY C_CUSTKEY)) r
)
SELECT f.value:"name" NAME,
       f.value:"key" KEY, 
       f.value:"classification" CLASSIFICATION 
FROM results, 
     TABLE(FLATTEN(input => PARSE_JSON(results.result))) AS f;
```

### Local Development

```java
// Run locally for testing
public class LocalSession {
    public static void main(String[] args) {
        // Test UDTF logic locally before deployment
        var handler = new CustomerCategorizeUDTFHandlerV1();
        // ... test implementation
    }
}
```

## 📊 Performance Results

| Dataset | Version | Execution Time | Improvement    |
| ------- | ------- | -------------- | -------------- |
| SF1     | V0      | 26 seconds     | baseline       |
| SF1     | V1      | 12 seconds     | **2.2x** |
| SF10    | V0      | 10m 55s        | baseline       |
| SF10    | V1      | 16 seconds     | **41x**  |

## 🔧 Configuration

### Maven Build Configuration

The project uses several Maven plugins:

- **AspectJ Maven Plugin**: Weaves telemetry aspects at compile time
- **Maven Shade Plugin**: Creates fat JAR with all dependencies
- **Maven Surefire Plugin**: Manages test execution

### Snowflake Configuration

- **Runtime Version**: Java 11
- **Required Packages**: `com.snowflake:snowpark:latest`, `com.snowflake:telemetry:0.0.1`
- **Memory**: Automatically managed by Snowflake

## 🎉 Key Takeaways

1. **Observability Drives Optimization**: Telemetry data revealed specific bottlenecks that guided targeted improvements
2. **AOP for Seamless Telemetry**: Annotation-based telemetry minimizes code intrusion while providing comprehensive coverage
3. **Static vs Instance Initialization**: Moving expensive setup to static blocks dramatically improved performance
4. **Smart Partitioning**: Replacing natural keys with computed partitions improved load distribution
5. **JIT-Friendly Code**: Using `final` modifiers and other JIT optimizations contributed to performance gains

## 🛠️ Development Tips

- **Enable telemetry selectively**: Use feature flags to enable/disable telemetry in production
- **Monitor telemetry overhead**: The current implementation has <1% runtime impact
- **Use local testing**: Test business logic locally before deploying to Snowflake
- **Leverage IDE support**: AspectJ weaving works seamlessly with modern IDEs

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

*This project demonstrates the power of combining observability with performance optimization in Snowpark Java applications. The telemetry-driven approach resulted in significant performance improvements while maintaining code clarity and production safety.*
