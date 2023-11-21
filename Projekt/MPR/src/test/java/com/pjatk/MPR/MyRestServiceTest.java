package com.pjatk.MPR;

import com.pjatk.MPR.Exceptions.DogAlreadyExistsException;
import com.pjatk.MPR.Exceptions.DogNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyRestServiceTest {
    @Mock
    private DogRepository repository;
    private AutoCloseable openMocks;
    private MyRestService myRestService;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        myRestService = new MyRestService(repository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void findDogTest() {
        String name = "Reksio";
        Dog dog = new Dog(name, "Buldog", 7);
        when(repository.findByName(name)).thenReturn(dog);
        Dog result = myRestService.findDogByName(name);
        assertEquals(dog, result);
    }
    @Test
    public void filterDogsTest(){
        Dog dog = new Dog("Reksio", "Buldog", 7);
        Dog dog2 = new Dog("Reksioooobait", "Buldog", 1);
        List<Dog> allDogs  =new ArrayList<>();
        allDogs.add(dog);
        allDogs.add(dog2);
        List<Dog> filteredDogs = new ArrayList<>();
        filteredDogs.add(dog);
        when(repository.findAll()).thenReturn(allDogs);
        assertEquals(myRestService.filterDogsByName("Reksio"), filteredDogs);
    }

    @Test
    public void saveDogTest() {
        String name = "Reksio";
        String breed = "Buldog";
        int age = 7;
        Dog dog = new Dog(name, breed, age);
        ArgumentCaptor<Dog> captor = ArgumentCaptor.forClass(Dog.class);
        when(repository.save(captor.capture())).thenReturn(dog);
        myRestService.addDog(dog);
        verify(repository, Mockito.times(1)).save(Mockito.any());
        Dog dogFromSaveCall = captor.getValue();
        assertEquals(dog, dogFromSaveCall);
    }

    @Test
    public void addDogIfDoesntExistTest() {
        Dog dog = new Dog("Reksio", "Buldog", 4);
        dog.setId(2L);
        when(repository.findById(2L)).thenReturn(Optional.empty());
        myRestService.addDog(dog);
        verify(repository).save(dog);
    }

    @Test
    public void deleteDogTestIfPresent() {
        Dog dog = new Dog("Reksio", "Buldog", 4);
        dog.setId(2L);
        when(repository.findById(2L)).thenReturn(Optional.of(dog));
        myRestService.deleteDog(2L);
        verify(repository).deleteById(2L);
    }

    @Test
    public void deleteDogIfNotPresentTest() {
        Dog dog = new Dog("Reksio", "Buldog", 4);
        dog.setId(2L);
        when(repository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(DogNotFoundException.class, () -> {
            myRestService.deleteDog(2L);
        });
    }
}
