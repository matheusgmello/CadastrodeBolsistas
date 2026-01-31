package dev.matheus.CadastroDeBolsistas.Bolsistas.Repositories;

import dev.matheus.CadastroDeBolsistas.Bolsistas.Models.BolsistaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BolsistaRepository extends JpaRepository<BolsistaModel, Long> {
}
