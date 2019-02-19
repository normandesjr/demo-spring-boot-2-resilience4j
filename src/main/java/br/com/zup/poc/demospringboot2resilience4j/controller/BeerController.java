package br.com.zup.poc.demospringboot2resilience4j.controller;

import br.com.zup.poc.demospringboot2resilience4j.helper.BeerType;
import br.com.zup.poc.demospringboot2resilience4j.model.Beer;
import br.com.zup.poc.demospringboot2resilience4j.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/beers")
public class BeerController {

    @Autowired
    private BeerService beerService;

    @GetMapping
    public List<Beer> listByType(@RequestParam("type") BeerType beerType) {
        return beerService.findBeers(beerType);
    }

}
