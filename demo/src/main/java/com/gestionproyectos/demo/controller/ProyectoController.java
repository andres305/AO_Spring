package com.gestionproyectos.demo.controller;

import com.gestionproyectos.demo.model.Proyecto;
import com.gestionproyectos.demo.repository.ProyectoRepository;
import com.gestionproyectos.demo.service.ProyectoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoRepository proyectoRepository;

    public ProyectoController(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @GetMapping
    public String listarProyectos(Model model) {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        model.addAttribute("proyectos", proyectos);
        return "proyectos/lista";
    }

    @GetMapping("/nuevo")
    public String formularioProyecto(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("estados", List.of("Activo", "En Progreso", "Finalizado"));
        return "proyectos/formulario";
    }

    @PostMapping
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoRepository.save(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable Long id) {
        proyectoRepository.deleteById(id);
        return "redirect:/proyectos";
    }
}