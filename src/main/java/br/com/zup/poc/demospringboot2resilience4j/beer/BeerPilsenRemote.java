package br.com.zup.poc.demospringboot2resilience4j.beer;

import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import br.com.zup.poc.demospringboot2resilience4j.remote.BeerInfoRemote;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;

@Component("pilsen")
public class BeerPilsenRemote implements BeerRemote {

    private final CircuitBreaker circuitBreaker;
    private final TimeLimiter timeLimiter;

    @Autowired
    private BeerInfoRemote beerInfoRemote;

    public BeerPilsenRemote(CircuitBreakerRegistry circuitBreakerRegistry, CircuitBreakerProperties circuitBreakerProperties) {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("drinkApp");

        // TODO: Qual melhor forma de configurar esse cara? Perceba que o timeout é específico por serviço, né?
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(3))
                .cancelRunningFuture(true)
                .build();

        timeLimiter = TimeLimiter.of(config);
    }

    @Override
    public List<Beer> findBeers() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Wrap your call to BackendService.doSomething() in a future provided by your executor
        Supplier<Future<List<Beer>>> futureSupplier = () -> executorService.submit(() -> beerInfoRemote.findBeerByType("pilsens"));

        // Decorate your supplier so that the future can be retrieved and executed upon
        Callable<List<Beer>> restrictedCall = TimeLimiter
                .decorateFutureSupplier(timeLimiter, futureSupplier);

        // Decorate the restricted callable with a CircuitBreaker
        Callable<List<Beer>> chainedCallable = CircuitBreaker.decorateCallable(circuitBreaker, restrictedCall);

        return Try.of(chainedCallable::call)
                .recover((exception) -> Collections.emptyList()).get();
    }
}
