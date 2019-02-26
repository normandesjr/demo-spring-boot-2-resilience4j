package br.com.zup.poc.demospringboot2resilience4j.remote;

import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "beerService", url = "http://localhost:5000")
public interface BeerInfoFeignRemote {

    @GetMapping("/{beer}")
    List<Beer> findBeers(@PathVariable("beer") String beer);

}
