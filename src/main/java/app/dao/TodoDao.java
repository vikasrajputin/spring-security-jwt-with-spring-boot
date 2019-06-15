package app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.Todo;

public interface TodoDao extends JpaRepository<Todo, String>{

}
