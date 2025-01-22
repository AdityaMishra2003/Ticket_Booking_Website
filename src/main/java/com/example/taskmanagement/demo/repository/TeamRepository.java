package com.example.taskmanagement.demo.repository;

import com.example.taskmanagement.demo.entity.Team;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT t FROM Team t JOIN t.memberDetails md WHERE md LIKE %:empId%")
    List<Team> findTeamsByUserId(@Param("empId") String empId);
    


}
