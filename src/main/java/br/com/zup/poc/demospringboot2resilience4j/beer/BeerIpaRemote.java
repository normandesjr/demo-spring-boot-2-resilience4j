package br.com.zup.poc.demospringboot2resilience4j.beer;

import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import br.com.zup.poc.demospringboot2resilience4j.remote.BeerInfoRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ipa")
public class BeerIpaRemote implements BeerRemote {

    @Autowired
    private BeerInfoRemote beerInfoRemote;

    @Override
    public List<Beer> findBeers() {
        return beerInfoRemote.findBeerByType("ipas");
    }

}
