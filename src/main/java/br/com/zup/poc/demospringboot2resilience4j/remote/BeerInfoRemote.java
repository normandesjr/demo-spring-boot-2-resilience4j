package br.com.zup.poc.demospringboot2resilience4j.remote;

import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BeerInfoRemote {

    @Value("${beerInfo.url}")
    private String beerInfoUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<Beer> findBeerByType(final String beerPath) {
        final String requestUrl = beerInfoUrl + "/" + beerPath;
        return restTemplate.getForObject(requestUrl, List.class);
    }

}
