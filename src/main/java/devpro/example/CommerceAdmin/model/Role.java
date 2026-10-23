package devpro.example.CommerceAdmin.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_role")
public class Role implements GrantedAuthority{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "create_date", nullable = true)
	private LocalDate createDate;
	
	@Column(name = "description", nullable = true)
	private String description;
	
	@Column(name = "status", nullable = true)
	private Boolean status = Boolean.TRUE;
	
	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<User>();

	public Role() {
		super();
	}

	public Role(Integer id, String name, LocalDate createDate, String description, Boolean status, Set<User> users) {
		super();
		this.id = id;
		this.name = name;
		this.createDate = createDate;
		this.description = description;
		this.status = status;
		this.users = users;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
}
