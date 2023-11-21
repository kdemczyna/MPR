package com.pjatk.MPR;

import com.pjatk.MPR.Exceptions.DogAlreadyExistsException;
import com.pjatk.MPR.Exceptions.DogExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MyRestControllerTests {
    private MockMvc mockMvc;
    @Mock
    private MyRestService service;
    @InjectMocks
    private MyRestController controller;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new DogExceptionHandler(), controller).build();
    }

    @Test
    public void getByIdReturns200WhenDogIsPresent() throws Exception{
        Dog dog = new Dog("Reksio", "Buldog", 3);
        when(service.findDogById(2L)).thenReturn(dog);
        mockMvc.perform(MockMvcRequestBuilders.get("/dogs/byId/2"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.breed").value("Buldog"))
                .andExpect(jsonPath("$.name").value("Reksio"));
    }
    @Test
    public void check400IsReturnedWhenDogIsAlreadyThere() throws Exception {
        Dog dog = new Dog("Reksio", "Buldog", 3);
        doThrow(new DogAlreadyExistsException()).when(service).addDog(any());
        mockMvc.perform(post("/dogs/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Reksio\", \"breed\": \"Buldog\", \"age\": 3 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}
