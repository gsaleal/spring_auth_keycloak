package com.leal.spring_auth_keycloak.model.mapper;

import com.leal.spring_auth_keycloak.model.dto.EmployeesDTO;
import com.leal.spring_auth_keycloak.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeesDTO toDto(Employee employee);

    Employee toEntity(EmployeesDTO employeesDTO);
}
