package com.gestionproyectos.demo.controller;

import com.gestionproyectos.demo.model.EstadoProyecto;
import com.gestionproyectos.demo.model.Proyecto;
import com.gestionproyectos.demo.service.ProyectoService;
import com.gestionproyectos.demo.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public String listarProyectos(Model model) {
        model.addAttribute("proyectos", proyectoService.listarProyectos());
        return "proyectos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("estados", EstadoProyecto.values());
        return "proyectos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoService.guardarProyecto(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/editar/{id}")
    public String editarProyecto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Proyecto> proyectoOptional = proyectoService.obtenerProyectoPorId(id);
        if (proyectoOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El proyecto no existe.");
            return "redirect:/proyectos";
        }

        Proyecto proyecto = proyectoOptional.get();
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("estados", EstadoProyecto.values());
        model.addAttribute("tareas", tareaService.listarTareasPorProyecto(id));

        return "proyectos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable Long id) {
        proyectoService.eliminarProyecto(id);
        return "redirect:/proyectos";
    }

    @GetMapping("/{id}/tareas")
    public String verTareas(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Proyecto> proyectoOptional = proyectoService.obtenerProyectoPorId(id);
        if (proyectoOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El proyecto no existe.");
            return "redirect:/proyectos";
        }

        Proyecto proyecto = proyectoOptional.get();
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("tareas", tareaService.listarTareasPorProyecto(id));

        return "proyectos/tareas"; // Verifica que proyectos/tareas.html exista
    }
}