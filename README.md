## Resilience4j demo with Spring Boot



### Module simple-http-server-python

This is just a http server written in Python to be able return some 
results like 200 OK, 500 INTERNAL SERVER ERROR, long running to cause 
timeout and so on.

### BeerLagerRemote

The remote service for this class always return an error, so its 
demonstrated how to recover from it.

### BeerStoutRemote

The remote service for this class return or 200 OK or an 500 INTERNAL 
SERVER ERROR randomically, so it demonstrate the Circuit Breaker with 
Retry.

And to create something more interesting, what if we add TimeLimiter too?


#### Prometheus

```
docker run --rm -v ~/dev/poc/resilience4j/demo-spring-boot-2-resilience4j/prometheus.yml:/etc/prometheus/prometheus.yml -p 9090:9090 --name prometheus  prom/prometheus
```
