package com.pjatk.MPR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyRestController {
    private final MyRestService myRestService;

    @Autowired
    public MyRestController(MyRestService myRestService) {
        this.myRestService = myRestService;
    }

    @GetMapping("dogs/byName/{name}")
    public Dog getDogByName(@PathVariable("name") String name) {
        return this.myRestService.findDogByName(name);
    }

    @GetMapping("dogs/byId/{id}")
    public Dog getDogById(@PathVariable("id") Long id) {
        return this.myRestService.findDogById(id);
    }

    @PostMapping("dogs/add")
    public void addDog(@RequestBody Dog dog) {
        this.myRestService.addDog(dog);
    }

    @DeleteMapping("dogs/delete/{id}")
    public void deleteDog(@PathVariable("id") Long id) {
        this.myRestService.deleteDog(id);
    }

    @PutMapping("dogs/update")
    public void udpdateDog(@RequestBody Dog dog) {
        this.myRestService.updateDog(dog);
    }

    @GetMapping("dogs/filterByName/{name}")
    public List<Dog> filterDogsByName(@PathVariable("name") String name){return this.myRestService.filterDogsByName(name);}
}
