package todoapp.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import todoapp.app.beans.TodoBean;

public interface TodoDao extends JpaRepository<TodoBean, String>{

}
