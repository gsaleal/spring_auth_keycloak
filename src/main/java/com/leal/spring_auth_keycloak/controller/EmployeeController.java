package com.leal.spring_auth_keycloak.controller;

import com.leal.spring_auth_keycloak.model.dto.EmployeesDTO;
import com.leal.spring_auth_keycloak.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @Operation(summary = "Get all Employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List employees")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EmployeesDTO>> getAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeesDTO> create(@RequestBody @Valid EmployeesDTO employeesDTO) {
        return new ResponseEntity<>(employeeService.create(employeesDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeesDTO> update(@RequestBody @Valid EmployeesDTO employeesDTO, @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.update(employeesDTO, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }

}
