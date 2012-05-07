package net.gamerservices.rpitems;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;

@Entity
@Table(name="fantasyraces_ability")
public class sqlAbility {
	
	@Id
	private int id;
	 
	@Length(max=16)
	@NotEmpty
	private String name;
	
	public void setId(int id)
	{
		this.id = id;
	}
	 
	public int getId() {
		return this.id;
	}
	 
	public void setName(String name)
	{
		this.name = name;
	}
	 
	public String getName() {
		return this.name;
	}
	 
}
