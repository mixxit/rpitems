package net.gamerservices.rpitems;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;

@Entity
@Table(name="fantasyraces_item")
public class sqlItem {
	
	@Id
	private int id;
	 
	@Length(max=16)
	@NotEmpty
	private String name;
	
	@Length(max=64)
	private String usemessage;
	
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

	public String getUsemessage() {
		// TODO Auto-generated method stub
		return usemessage;
	}
	
	public void setUsemessage(String message)
	{
		this.usemessage = message;
	}
	 
}
