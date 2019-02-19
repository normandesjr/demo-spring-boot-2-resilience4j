package br.com.zup.poc.demospringboot2resilience4j.beer;

import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import br.com.zup.poc.demospringboot2resilience4j.remote.BeerInfoRemote;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Component("stout")
public class BeerStoutRemote implements BeerRemote {

    private final CircuitBreaker circuitBreaker;
    private final TimeLimiter timeLimiter;
    private final Retry retry;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private BeerInfoRemote beerInfoRemote;

    public BeerStoutRemote(CircuitBreakerRegistry circuitBreakerRegistry) {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("drinkApp");

        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(3))
                .cancelRunningFuture(true)
                .build();

        timeLimiter = TimeLimiter.of(config);

        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(200))
                .build();
        retry = Retry.of("retryStout", retryConfig);

        retry.getEventPublisher()
                .onSuccess(event -> System.out.println("Retry sucesso!!!"))
                .onError(event -> System.out.println("Retry erro!!!"));
    }

    @Override
    public List<Beer> findBeers() {
        Callable<List<Beer>> retryCallable = Retry.decorateCallable(retry, () -> beerInfoRemote.findBeerByType("stouts"));
        Callable<List<Beer>> chainedCallable = CircuitBreaker.decorateCallable(circuitBreaker, retryCallable);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Supplier<Future<List<Beer>>> futureSupplier = () -> executorService.submit(chainedCallable);

        Callable<List<Beer>> timeLimitterCallable = TimeLimiter
                .decorateFutureSupplier(timeLimiter, futureSupplier);

        return Try.ofCallable(timeLimitterCallable)
                .recover((exception) -> Collections.emptyList()).get();
    }

}
