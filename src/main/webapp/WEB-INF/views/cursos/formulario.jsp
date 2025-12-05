<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty curso ? 'Nuevo' : 'Editar'} Curso - Sistema Horarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <i class="bi bi-calendar-week"></i> Sistema Horarios
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text text-white me-3">
                    <i class="bi bi-person-circle"></i> ${sessionScope.username}
                </span>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">
                    <i class="bi bi-box-arrow-right"></i> Salir
                </a>
            </div>
        </div>
    </nav>
    
    <div class="container mt-4">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">
                            <i class="bi bi-book"></i>
                            ${empty curso ? 'Nuevo Curso' : 'Editar Curso'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="bi bi-exclamation-circle"></i> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty mensaje}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="bi bi-check-circle"></i> ${mensaje}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/cursos" method="post" id="cursoForm">
                            <input type="hidden" name="action" value="${empty curso ? 'crear' : 'actualizar'}">
                            <c:if test="${not empty curso}">
                                <input type="hidden" name="id" value="${curso.id}">
                            </c:if>
                            
                            <div class="mb-3">
                                <label for="nombre" class="form-label">
                                    Nombre del Curso <span class="text-danger">*</span>
                                </label>
                                <input type="text" 
                                       class="form-control" 
                                       id="nombre" 
                                       name="nombre" 
                                       value="${curso.nombre}"
                                       placeholder="Ej: Matemática, Comunicación, Inglés"
                                       required>
                                <small class="text-muted">
                                    <i class="bi bi-info-circle"></i>
                                    El nombre debe ser único en el sistema
                                </small>
                            </div>
                            
                            <div class="mb-3">
                                <label for="descripcion" class="form-label">
                                    Descripción
                                </label>
                                <textarea class="form-control" 
                                          id="descripcion" 
                                          name="descripcion" 
                                          rows="3"
                                          placeholder="Descripción breve del curso (opcional)">${curso.descripcion}</textarea>
                                <small class="text-muted">
                                    <i class="bi bi-info-circle"></i>
                                    Información adicional sobre el curso
                                </small>
                            </div>
                            
                            <div class="mb-3">
                                <label for="horasSemanales" class="form-label">
                                    Horas Semanales <span class="text-danger">*</span>
                                </label>
                                <input type="number" 
                                       class="form-control" 
                                       id="horasSemanales" 
                                       name="horasSemanales" 
                                       value="${empty curso ? 2 : curso.horasSemanales}"
                                       min="1" 
                                       max="10" 
                                       required>
                                <small class="text-muted">
                                    <i class="bi bi-info-circle"></i>
                                    Cantidad de horas semanales del curso (entre 1 y 10)
                                </small>
                            </div>
                            
                            <div class="alert alert-info">
                                <i class="bi bi-lightbulb"></i>
                                <strong>Nota:</strong> Este curso podrá ser asignado a cualquier grado al crear los horarios.
                            </div>
                            
                            <div class="d-flex justify-content-between mt-4">
                                <a href="${pageContext.request.contextPath}/cursos?action=listar" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save"></i> 
                                    ${empty curso ? 'Crear Curso' : 'Guardar Cambios'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación adicional del formulario
        document.getElementById('cursoForm').addEventListener('submit', function(e) {
            const nombre = document.getElementById('nombre').value.trim();
            const horasSemanales = parseInt(document.getElementById('horasSemanales').value);
            
            if (nombre.length < 3) {
                e.preventDefault();
                alert('El nombre del curso debe tener al menos 3 caracteres');
                return false;
            }
            
            if (horasSemanales < 1 || horasSemanales > 10) {
                e.preventDefault();
                alert('Las horas semanales deben estar entre 1 y 10');
                return false;
            }
        });
        
        // Auto-focus en el primer campo
        document.getElementById('nombre').focus();
    </script>
</body>
</html>