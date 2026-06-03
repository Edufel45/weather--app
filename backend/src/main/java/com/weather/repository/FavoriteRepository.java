package com.weather.repository;

import com.weather.model.Favorite;
import com.weather.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndCityName(User user, String cityName);
    void deleteByUserAndCityName(User user, String cityName);
    long countByUser(User user);
}