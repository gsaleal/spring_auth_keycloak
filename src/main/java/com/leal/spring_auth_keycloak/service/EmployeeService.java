package com.leal.spring_auth_keycloak.service;

import com.leal.spring_auth_keycloak.model.dto.EmployeesDTO;
import com.leal.spring_auth_keycloak.model.entity.Employee;
import com.leal.spring_auth_keycloak.model.mapper.EmployeeMapper;
import com.leal.spring_auth_keycloak.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Transactional(readOnly = true)
    public List<EmployeesDTO> getAll() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeesDTO getById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    @Transactional
    public EmployeesDTO create(EmployeesDTO employeesDTO) {
        if (employeeRepository.existsByEmail(employeesDTO.getEmail())) {
           throw new IllegalArgumentException("Email in use");
        }
        Employee employee = employeeMapper.toEntity(employeesDTO);
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    @Transactional
    public EmployeesDTO update(EmployeesDTO employeesDTO, Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setName(employeesDTO.getName());
        employee.setEmail(employeesDTO.getEmail());

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
