package io.stasheus;

import net.logstash.logback.marker.LogstashMarker;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Stasheus {

    private Logger logger = LoggerFactory.getLogger(Stasheus.class);
    private URL url;

    private Stasheus(String hostname, int port, String endpoint, int rate) {
        try {
            url = new URL("http://" + hostname + ":" + port + endpoint);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        schedule(rate);
    }

    private void schedule(int rate) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                Metrics metrics = scrape();
                LogstashMarker logstashMarker = Markers.appendEntries(metrics.asMap());
                logger.info(logstashMarker, "Prometheus scrape (every " + rate + " seconds)");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }, rate, rate, TimeUnit.SECONDS);
    }

    private Metrics scrape() throws IOException {
        Metrics metrics = new Metrics();

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (InputStreamReader isr = new InputStreamReader(con.getInputStream());
             BufferedReader in = new BufferedReader(isr)) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (isMetric(inputLine)) {
                    Metric metric = Metric.fromLine(inputLine);
                    metrics.addMetric(metric);
                }
            }
            con.disconnect();
            return metrics;
        }
    }

    private boolean isMetric(String inputLine) {
        return !inputLine.trim().startsWith("#");
    }

    public static class Builder {
        private String hostname;
        private int port;
        private String endpoint;
        private int rate = 15;

        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder rate(int rate) {
            this.rate = rate;
            return this;
        }

        public Stasheus build() {
            return new Stasheus(hostname, port, endpoint, rate);
        }
    }
}
