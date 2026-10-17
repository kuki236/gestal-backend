package pe.edu.unmsm.gestal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(name = "area_responsable", nullable = false)
    private String areaResponsable;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad_inicial", nullable = false)
    private Prioridad prioridadInicial;

    public Categoria() {
    }

    public Categoria(String nombre, String areaResponsable, Prioridad prioridadInicial) {
        this.nombre = nombre;
        this.areaResponsable = areaResponsable;
        this.prioridadInicial = prioridadInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAreaResponsable() {
        return areaResponsable;
    }

    public void setAreaResponsable(String areaResponsable) {
        this.areaResponsable = areaResponsable;
    }

    public Prioridad getPrioridadInicial() {
        return prioridadInicial;
    }

    public void setPrioridadInicial(Prioridad prioridadInicial) {
        this.prioridadInicial = prioridadInicial;
    }
}
