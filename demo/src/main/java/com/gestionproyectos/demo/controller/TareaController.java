package com.gestionproyectos.demo.controller;

import com.gestionproyectos.demo.model.Proyecto;
import com.gestionproyectos.demo.model.Tarea;
import com.gestionproyectos.demo.repository.ProyectoRepository;
import com.gestionproyectos.demo.repository.TareaRepository;
import com.gestionproyectos.demo.service.TareaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;

    public TareaController(TareaRepository tareaRepository, ProyectoRepository proyectoRepository) {
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @GetMapping("/{proyectoId}")
    public String listarTareas(@PathVariable Long proyectoId, Model model) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(proyectoId);
        if (proyecto.isEmpty()) {
            return "error";
        }
        model.addAttribute("proyecto", proyecto.get());
        model.addAttribute("tareas", tareaRepository.findByProyectoId(proyectoId));
        return "tareas/lista";
    }

    @GetMapping("/nueva/{proyectoId}")
    public String formularioTarea(@PathVariable Long proyectoId, Model model) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(proyectoId);
        if (proyecto.isEmpty()) {
            return "error";
        }
        model.addAttribute("tarea", new Tarea());
        model.addAttribute("proyecto", proyecto.get());
        model.addAttribute("estados", List.of("Pendiente", "En Curso", "Completada"));
        return "tareas/formulario";
    }

    @PostMapping
    public String guardarTarea(@ModelAttribute Tarea tarea, @RequestParam Long proyectoId) {
        Optional<Proyecto> proyecto = proyectoRepository.findById(proyectoId);
        if (proyecto.isPresent()) {
            tarea.setProyecto(proyecto.get());
            tareaRepository.save(tarea);
        }
        return "redirect:/tareas/" + proyectoId;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id) {
        Optional<Tarea> tarea = tareaRepository.findById(id);
        if (tarea.isPresent()) {
            Long proyectoId = tarea.get().getProyecto().getId();
            tareaRepository.deleteById(id);
            return "redirect:/tareas/" + proyectoId;
        }
        return "error";
    }
}