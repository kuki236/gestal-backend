package pe.edu.unmsm.gestal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.unmsm.gestal.dto.CategoriaResponse;
import pe.edu.unmsm.gestal.entity.Prioridad;
import pe.edu.unmsm.gestal.service.CategoriaService;

@RestController
@RequestMapping("/api")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/categorias")
    public List<CategoriaResponse> categorias() {
        return categoriaService.listar();
    }

    @GetMapping("/prioridades")
    public Prioridad[] prioridades() {
        return Prioridad.values();
    }
}
