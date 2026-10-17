package pe.edu.unmsm.gestal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.unmsm.gestal.dto.CategoriaResponse;
import pe.edu.unmsm.gestal.entity.Categoria;
import pe.edu.unmsm.gestal.exception.ResourceNotFoundException;
import pe.edu.unmsm.gestal.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categorias;

    public CategoriaService(CategoriaRepository categorias) {
        this.categorias = categorias;
    }

    public List<CategoriaResponse> listar() {
        return categorias.findAll().stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre(), c.getAreaResponsable(), c.getPrioridadInicial()))
                .toList();
    }

    public Categoria porId(Long id) {
        return categorias.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + id));
    }
}
