package com.cwiesse.horarios.util;

import com.cwiesse.horarios.dao.DocenteDao;
import com.cwiesse.horarios.dao.impl.DocenteDaoImpl;
import com.cwiesse.horarios.model.Docente;
import java.util.List;
import java.util.Optional;

/**
 * Clase para probar el DocenteDao
 */
public class TestDaoDocente {
    
    public static void main(String[] args) {
        System.out.println("=== TEST DE DOCENTE DAO ===\n");
        
        DocenteDao docenteDao = new DocenteDaoImpl();
        
        try {
            // 1. LISTAR TODOS
            System.out.println("1. LISTANDO TODOS LOS DOCENTES:");
            System.out.println("================================");
            List<Docente> docentes = docenteDao.listarTodos();
            
            for (Docente d : docentes) {
                System.out.println("- " + d.getNombreCompleto() + " (DNI: " + d.getDni() + ")");
            }
            System.out.println("Total: " + docentes.size() + " docentes\n");
            
            // 2. BUSCAR POR DNI
            System.out.println("2. BUSCANDO DOCENTE POR DNI (12345678):");
            System.out.println("=========================================");
            Optional<Docente> docenteOpt = docenteDao.buscarPorDni("12345678");
            
            if (docenteOpt.isPresent()) {
                Docente d = docenteOpt.get();
                System.out.println("✅ Encontrado: " + d.getNombreCompleto());
                System.out.println("   Email: " + d.getEmail());
                System.out.println("   Teléfono: " + d.getTelefono());
            } else {
                System.out.println("❌ No encontrado");
            }
            System.out.println();
            
            // 3. INSERTAR NUEVO DOCENTE
            System.out.println("3. INSERTANDO NUEVO DOCENTE:");
            System.out.println("=============================");
            Docente nuevoDocente = new Docente(
                "99999999",
                "Roberto",
                "Gómez",
                "Silva"
            );
            nuevoDocente.setEmail("rgomez@cwiesse.edu.pe");
            nuevoDocente.setTelefono("987654399");
            
            boolean insertado = docenteDao.insertar(nuevoDocente);
            
            if (insertado) {
                System.out.println("✅ Docente insertado con ID: " + nuevoDocente.getId());
            } else {
                System.out.println("❌ Error al insertar");
            }
            System.out.println();
            
            // 4. ACTUALIZAR DOCENTE
            System.out.println("4. ACTUALIZANDO DOCENTE:");
            System.out.println("=========================");
            nuevoDocente.setEmail("roberto.gomez@cwiesse.edu.pe");
            nuevoDocente.setTelefono("999888777");
            
            boolean actualizado = docenteDao.actualizar(nuevoDocente);
            
            if (actualizado) {
                System.out.println("✅ Docente actualizado");
            } else {
                System.out.println("❌ Error al actualizar");
            }
            System.out.println();
            
            // 5. VERIFICAR EXISTENCIA DE DNI
            System.out.println("5. VERIFICANDO SI EXISTE DNI (99999999):");
            System.out.println("=========================================");
            boolean existe = docenteDao.existeDni("99999999");
            System.out.println(existe ? "✅ El DNI existe" : "❌ El DNI no existe");
            System.out.println();
            
            // 6. ELIMINAR DOCENTE
            System.out.println("6. ELIMINANDO DOCENTE DE PRUEBA:");
            System.out.println("=================================");
            boolean eliminado = docenteDao.eliminar(nuevoDocente.getId());
            
            if (eliminado) {
                System.out.println("✅ Docente eliminado correctamente");
            } else {
                System.out.println("❌ Error al eliminar");
            }
            System.out.println();
            
            // 7. LISTAR ACTIVOS
            System.out.println("7. LISTANDO SOLO DOCENTES ACTIVOS:");
            System.out.println("===================================");
            List<Docente> activos = docenteDao.listarActivos();
            System.out.println("Total activos: " + activos.size());
            
            System.out.println("\n✅ ¡TODAS LAS PRUEBAS COMPLETADAS!");
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}