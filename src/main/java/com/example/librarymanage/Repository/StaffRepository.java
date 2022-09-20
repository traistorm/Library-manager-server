package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Staff findByStaffid(String id);
    List<Staff> findAllByStaffidContainingIgnoreCase(String staffID, Pageable pageable);
    List<Staff> findAllByStaffnameContainingIgnoreCase(String staffName, Pageable pageable);
    List<Staff> findAllByStaffidContainingIgnoreCaseAndStaffnameContainingIgnoreCase(String staffID, String StaffName, Pageable pageable);
}
