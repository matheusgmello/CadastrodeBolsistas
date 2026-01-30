CREATE TABLE tb_laboratorios (
 id BIGSERIAL PRIMARY KEY,
 nome VARCHAR(100) NOT NULL,
 area_atuacao VARCHAR(100)
);

CREATE TABLE tb_bolsistas (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  idade INT,
  email VARCHAR(100) UNIQUE NOT NULL,
  nivel_bolsa VARCHAR(50),
  laboratorio_id BIGINT,
  CONSTRAINT fk_laboratorio
      FOREIGN KEY (laboratorio_id)
          REFERENCES tb_laboratorios(id)
          ON DELETE SET NULL
);