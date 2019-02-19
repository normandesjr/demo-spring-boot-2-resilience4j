package br.com.zup.poc.demospringboot2resilience4j.helper;

import br.com.zup.poc.demospringboot2resilience4j.beer.BeerRemote;
import org.springframework.beans.factory.BeanFactory;

public enum BeerType {

    IPA("ipa"),
    LAGER("lager"),
    PILSEN("pilsen"),
    STOUT("stout");

    private String beanName;

    BeerType(final String beanName) {
        this.beanName = beanName;
    }

    private BeerRemote beerRemote;

    public BeerRemote getRemote(final BeanFactory beanFactory) {
        return beanFactory.getBean(beanName, BeerRemote.class);
    }

}
