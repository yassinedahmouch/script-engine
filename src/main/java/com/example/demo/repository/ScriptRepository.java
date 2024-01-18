package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Script;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Integer> {

}
