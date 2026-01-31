package dev.matheus.CadastroDeBolsistas.Laboratorios.Repositories;

import dev.matheus.CadastroDeBolsistas.Laboratorios.Models.LaboratorioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratorioRepository extends JpaRepository<LaboratorioModel, Long > {
}
