package com.pjatk.MPR;

import com.pjatk.MPR.Exceptions.DogAlreadyExistsException;
import com.pjatk.MPR.Exceptions.DogNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class MyRestService {
    private DogRepository dogRepository;

    public MyRestService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Dog findDogByName(String name) {
        Dog d = this.dogRepository.findByName(name);
        if (d == null) {
            throw new DogNotFoundException();
        } else return d;
    }

    public void addDog(Dog dog) {
                 this.dogRepository.save(dog);
    }

    public void deleteDog(Long id) {
        Optional<Dog> d = this.dogRepository.findById(id);
        if (d.isPresent()) {
            this.dogRepository.deleteById(id);
        }
        else throw new DogNotFoundException();
    }

    public Dog findDogById(long id) {
        Optional<Dog> d = this.dogRepository.findById(id);
        if (!d.isPresent()) {
            throw new DogNotFoundException();
        } else return d.get();
    }

    public void updateDog(Dog dog) {
        Optional<Dog> optionalDog = this.dogRepository.findById(dog.getId());
        if (optionalDog.isPresent()) {
            Dog dogToUpdate = optionalDog.get();
            dogToUpdate.setName(dog.getName());
            dogToUpdate.setBreed(dog.getBreed());
            dogToUpdate.setAge(dog.getAge());
            this.dogRepository.save(dogToUpdate);
        }
    }
    public List<Dog> filterDogsByName(String name){
//        List<Dog> filteredDogs = new ArrayList<>();
//        for (Dog dog : dogRepository.findAll()) {
//            if(dog.getName().equals(name)) {
//            filteredDogs.add(dog);
//            }
//        }
//            return filteredDogs;
        return StreamSupport.stream(dogRepository.findAll().spliterator(), false).filter(dog -> dog.getName().equals(name)).toList();
    }

}