package com.example.guiex1.repository.dbrepo;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.validators.Validator;
import com.example.guiex1.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class PrietenieDbRepository implements Repository<Long, Prietenie> {
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Prietenie findOne(Long id) {
        Iterable<Prietenie> allPrietenii = this.findAll();
        List<Prietenie> prieteniiList = StreamSupport.stream(allPrietenii.spliterator(), false)
                .collect(Collectors.toList());
        for(Prietenie p: prieteniiList)
        {
            if(Objects.equals(p.getId(), id)) return p;
        }
        return null;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> prietenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idPrieten1 = resultSet.getLong("id_prieten_1");
                Long idPrieten2 = resultSet.getLong("id_prieten_2");
                LocalDateTime dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();
                String status = resultSet.getString("status");

                Prietenie prietenie = new Prietenie(idPrieten1, idPrieten2);
                prietenie.setId(id);
                prietenie.setDate(dateTime);
                prietenie.setStatus(status);
                prietenii.add(prietenie);
            }
            return prietenii;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        String sql = "insert into friendships (id_prieten_1, id_prieten_2, date_time, status) values (?, ?, ?, ?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getIdPrieten1());
            ps.setLong(2, entity.getIdPrieten2());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
            ps.setString(4, entity.getStatus());

            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return Optional.ofNullable(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Prietenie> delete(Long id) {
        String sql = "delete from friendships where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            //return Optional.ofNullable(entity);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }
}
