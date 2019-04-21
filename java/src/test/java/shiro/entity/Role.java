package shiro.entity;

import org.apache.ibatis.mapping.BoundSql;

import java.io.Serializable;

public class Role implements Serializable {
    private Long id;
    private String role;
    private String description;
    private Boolean avaiable = false;

    public Role() {
    }

    public Role(String role, String description, Boolean avaiable) {
        this.role = role;
        this.description = description;
        this.avaiable = avaiable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvaiable() {
        return avaiable;
    }

    public void setAvaiable(Boolean avaiable) {
        this.avaiable = avaiable;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        if (id != null ? !id.equals(role.id) : role.id != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", avaiable=" + avaiable +
                '}';
    }
}
