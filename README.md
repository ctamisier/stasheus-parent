# Stasheus

Scrapes a Prometheus Endpoint and logs the content to Logstash format

# How

    new Stasheus.Builder()
        .hostname("localhost")
        .port(8080)
        .endpoint("/actuator/prometheus")
        .rate(10)
        .build();
                
# Example

GET http://localhost:8080/actuator/prometheus giving...

    # HELP process_uptime_seconds The uptime of the Java virtual machine
    # TYPE process_uptime_seconds gauge
    process_uptime_seconds 14.395

...will be logged as

    {
      "@timestamp": "2018-07-22T08:53:40.412+02:00",
      "@version": "1",
      "message": "Prometheus scrape (every 5 seconds)",
      "logger_name": "io.stasheus.Stasheus",
      "thread_name": "pool-1-thread-1",
      "level": "INFO",
      "level_value": 20000,
      "process_uptime_seconds": "14.395"
    }

# Show me

- Run DemoApplication from stasheus-demo project
- Have a look at the generated 'file.log' (under stasheus-parent directory)