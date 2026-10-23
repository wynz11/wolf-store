package devpro.example.CommerceAdmin.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_user")
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "username", nullable = false)
	private String username;
	
	@Column(name = "email", nullable = true)
	private String email;
	
	@Column(name = "create_date", nullable = true)
	private LocalDate creatDate;
	
	@Column(name = "status", nullable = true)
	private Boolean status = Boolean.TRUE;
	
	@Column(name = "password")
	private String password;
	@Column(name = "mobile")
	private String mobile;
	@Column(name = "description")
	private String description;
	@Column(name = "address")
	private String address;
	@Column(name = "firstname")
	private String firstName;
	@Column(name = "lastname")
	private String lastName;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "tbl_user_role", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	// Helper methods
	public void addRelationalUserRole(Role role) {
		roles.add(role);
		role.getUsers().add(this);
	}

	public void removeRelationalUserRole(Role role) {
		roles.remove(role);
		role.getUsers().remove(this);
	}
	
	public User() {
		super();
	}

	public User(Integer id, String username, String email, LocalDate creatDate, Boolean status, String password,
			String mobile, String description, String address, String firstName, String lastName,
			Set<devpro.example.CommerceAdmin.model.Role> roles) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.creatDate = creatDate;
		this.status = status;
		this.password = password;
		this.mobile = mobile;
		this.description = description;
		this.address = address;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(LocalDate creatDate) {
		this.creatDate = creatDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.roles;
	}
	
	
}
