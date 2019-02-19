package br.com.zup.poc.demospringboot2resilience4j.beer;

import br.com.zup.poc.demospringboot2resilience4j.model.Beer;

import java.util.List;

public interface BeerRemote {

    public List<Beer> findBeers();
}
