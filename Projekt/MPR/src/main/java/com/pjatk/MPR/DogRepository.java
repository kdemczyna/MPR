package com.pjatk.MPR;

import org.springframework.data.repository.CrudRepository;

public interface DogRepository extends CrudRepository<Dog, Long> {
public Dog findByName(String name);


}
