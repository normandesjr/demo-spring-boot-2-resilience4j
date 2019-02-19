package br.com.zup.poc.demospringboot2resilience4j.service;

import br.com.zup.poc.demospringboot2resilience4j.helper.BeerType;
import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerService {

    @Autowired
    private BeanFactory beanFactory;

    public List<Beer> findBeers(final BeerType beerType) {
        return beerType.getRemote(beanFactory).findBeers();
    }

}
