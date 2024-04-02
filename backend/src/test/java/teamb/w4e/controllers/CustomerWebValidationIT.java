package teamb.w4e.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import teamb.w4e.dto.CustomerDTO;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.repositories.customers.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Full stack (No class-wide @Transactional here as the controller layer should not be transactional)
class CustomerWebValidationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    @Transactional
    void setUpContext() {
        customerRepository.deleteAll();
    }

    @Test
    void validCustomerTest() throws Exception {
        CustomerDTO validCustomer = new CustomerDTO(null, "john", "1234567890", null);
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCustomer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void missingNameTest() throws Exception {
        CustomerDTO customer = new CustomerDTO(null, null, "1234567890", null);
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void invalidNameTest() throws Exception {
        CustomerDTO customer = new CustomerDTO(null, "", "1234567890", null);
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void invalidCreditCardTest() throws Exception {
        CustomerDTO customer = new CustomerDTO(null, "john", "invalid_credit_card", null);
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void retrieveAllCustomersTest() throws Exception {
        Customer john = new Customer("john", "1234567890");
        customerRepository.save(john);
        mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void retrieveSpecificCustomerTest() throws Exception {
        Customer john = new Customer("john", "1234567890");
        customerRepository.save(john);
        mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.BASE_URI + "/" + john.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void nonExistingCustomerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.BASE_URI + "/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
