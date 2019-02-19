package br.com.zup.poc.demospringboot2resilience4j.beer;

import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import br.com.zup.poc.demospringboot2resilience4j.remote.BeerInfoRemote;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;

@Component("lager")
public class BeerLagerRemote implements BeerRemote {

    @Autowired
    private BeerInfoRemote beerInfoRemote;

    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public BeerLagerRemote(CircuitBreakerRegistry circuitBreakerRegistry) {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("drinkApp");

        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(4)
                .waitDuration(Duration.ofMillis(2000))
                .build();
        retry = Retry.of("retryLager", retryConfig);
    }

    @Override
    public List<Beer> findBeers() {
        Supplier<List<Beer>> retrySupplier = Retry.decorateSupplier(retry, () -> beerInfoRemote.findBeerByType("lagers"));
        Supplier<List<Beer>> chainedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, retrySupplier);

        // If you don't want/need retry, just remove it and use like bellow:
        //        Supplier<List<Beer>> remoteFunction
        //                = CircuitBreaker.decorateSupplier(circuitBreaker, () -> beerInfoRemote.findBeerByType("lagers"));
        // Try.ofSupplier(remoteFunction)
        return Try.ofSupplier(chainedSupplier)
                .recover((exception) -> emptyList()).get();
    }
}
