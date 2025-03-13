package com.gestionproyectos.demo.controller;

import com.gestionproyectos.demo.model.EstadoTarea;
import com.gestionproyectos.demo.model.Proyecto;
import com.gestionproyectos.demo.model.Tarea;
import com.gestionproyectos.demo.service.ProyectoService;
import com.gestionproyectos.demo.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private ProyectoService proyectoService;

    @GetMapping("/nuevo/{proyectoId}")
    public String nuevaTarea(@PathVariable Long proyectoId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Proyecto> proyectoOptional = proyectoService.obtenerProyectoPorId(proyectoId);
        if (proyectoOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El proyecto no existe.");
            return "redirect:/proyectos";
        }

        Proyecto proyecto = proyectoOptional.get();
        Tarea tarea = new Tarea();
        tarea.setProyecto(proyecto);

        model.addAttribute("tarea", tarea);
        model.addAttribute("estados", EstadoTarea.values());

        return "tareas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarTarea(@ModelAttribute Tarea tarea, RedirectAttributes redirectAttributes) {
        try {
            tareaService.guardarTarea(tarea);
            redirectAttributes.addFlashAttribute("mensaje", "Tarea guardada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo guardar la tarea.");
        }
        return "redirect:/proyectos/" + tarea.getProyecto().getId() + "/tareas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Tarea> tareaOptional = tareaService.obtenerTareaPorId(id);
        if (tareaOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La tarea no existe.");
            return "redirect:/proyectos";
        }

        Tarea tarea = tareaOptional.get();
        Long proyectoId = tarea.getProyecto().getId();

        try {
            tareaService.eliminarTarea(id);
            redirectAttributes.addFlashAttribute("mensaje", "Tarea eliminada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la tarea.");
        }

        return "redirect:/proyectos/" + proyectoId + "/tareas";
    }
}