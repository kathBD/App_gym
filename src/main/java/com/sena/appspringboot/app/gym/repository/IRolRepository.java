package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRolRepository extends JpaRepository<Rol, Long> {
}
