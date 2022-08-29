package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.Staff;
import lombok.Data;

import java.util.List;

@Data
public class StaffDTO {
    private Integer maxPage;
    private List<Staff> staffList;
}
