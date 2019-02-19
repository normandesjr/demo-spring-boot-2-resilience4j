package br.com.zup.poc.demospringboot2resilience4j.beer;

import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import br.com.zup.poc.demospringboot2resilience4j.remote.BeerInfoRemote;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;

@Component("ipa")
public class BeerIpaRemote implements BeerRemote {

    @Autowired
    private BeerInfoRemote beerInfoRemote;

    private final CircuitBreaker circuitBreaker;

    public BeerIpaRemote(CircuitBreakerRegistry circuitBreakerRegistry) {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("drinkApp");
    }

    @Override
    public List<Beer> findBeers() {
        Supplier<List<Beer>> chainedSupplier
                = CircuitBreaker.decorateSupplier(circuitBreaker, () -> beerInfoRemote.findBeerByType("ipas"));
        return Try.ofSupplier(chainedSupplier)
                .recover(e -> emptyList()).get();
    }

}
