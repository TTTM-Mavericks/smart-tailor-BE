package com.smart.tailor.service;

import com.smart.tailor.entities.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Employee getByID(UUID empID);

    List<Employee> getAll();
}
