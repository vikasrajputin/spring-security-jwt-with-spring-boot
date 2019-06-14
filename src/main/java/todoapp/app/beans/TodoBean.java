package todoapp.app.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TodoBean {

	@Id
	private String id;
	@Column
	private String title;
	@Column
	private String description;
	@Column
	private String status;
	
	
}
