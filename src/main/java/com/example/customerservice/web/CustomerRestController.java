package com.example.customerservice.web;

import com.example.customerservice.dto.CustomerRequestDTO;
import com.example.customerservice.dto.CustomerResponseDTO;
import com.example.customerservice.exceptions.CustomerNotFoundException;
import com.example.customerservice.exceptions.EmailAlreadyUsedException;
import com.example.customerservice.services.CustomerService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class CustomerRestController {

    private CustomerService customerService;


    @GetMapping("/customers")
    public List<CustomerResponseDTO> searchCustomers(){
        return customerService.listCustomers();
    }


    @GetMapping("/customers/search")
    public List<CustomerResponseDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return customerService.findCustomers("%"+keyword+"%");
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id){
        try {
            CustomerResponseDTO customerById = customerService.getCustomerById(id);
            return ResponseEntity.ok(customerById);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage(e.getMessage()));
        }
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/customers")
    public ResponseEntity<?> saveNewCustomer(@RequestBody CustomerRequestDTO request){
        try {
            CustomerResponseDTO customerResponseDTO = customerService.save(request);
            return ResponseEntity.ok(customerResponseDTO);
        } catch (EmailAlreadyUsedException e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage(e.getMessage())); }
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerRequestDTO request, @PathVariable Long id){
        try {
            CustomerResponseDTO customerResponseDTO = customerService.update(request);
            return ResponseEntity.ok(customerResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage(e.getMessage()));
        }
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage(e.getMessage()));
        }
    }
}
