package fullstack.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @Column(name = "rut", unique = true, nullable = false, length = 8)
    private Integer rut;

    @Column(name = "dv", nullable = false)
    private char dv;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "correo", unique = true, nullable = false)
    private String email;

    @Column(name = "hash_contrasena", nullable = false)
    private String hashContrasena;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "rol")
    private String rol = "USER";

    @Column(name = "activo")
    private Boolean activo = true;
}