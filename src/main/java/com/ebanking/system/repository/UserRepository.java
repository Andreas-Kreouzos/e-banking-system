package com.ebanking.system.repository;

import com.ebanking.system.entity.User;
import com.ebanking.system.exception.UserNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserRepository implements IUserRepository {

    private final JdbcTemplate template;

    public UserRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Integer insert(User user) {
        String sql = "INSERT INTO E_BANKING_SYSTEM.USERS (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL) VALUES (?, ?, ?, ?, ?)";
        return template.update(sql, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    @Override
    public User select(Long id) {
        String sql = "SELECT * FROM E_BANKING_SYSTEM.USERS WHERE ID = ?";
        try {
            return template.queryForObject(sql, userRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }

    @Override
    public Integer update(Long id, String firstName, String lastName) {
        String sql = "UPDATE E_BANKING_SYSTEM.USERS SET FIRSTNAME = ?, LASTNAME = ? WHERE ID = ?";
        return template.update(sql, firstName, lastName, id);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getLong("ID"),
                rs.getString("USERNAME"),
                rs.getString("PASSWORD"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL")
        );
    }
}
