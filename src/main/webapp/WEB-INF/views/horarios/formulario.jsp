<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty horario ? 'Nuevo' : 'Editar'} Horario - Sistema Horarios</title>
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
            <div class="col-md-10 offset-md-1">
                <div class="card">
                    <div class="card-header bg-danger text-white">
                        <h4 class="mb-0">
                            <i class="bi bi-calendar3"></i>
                            ${empty horario ? 'Nuevo Horario' : 'Editar Horario'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="bi bi-exclamation-circle"></i> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/horarios" method="post">
                            <input type="hidden" name="action" value="${empty horario ? 'crear' : 'actualizar'}">
                            <c:if test="${not empty horario}">
                                <input type="hidden" name="id" value="${horario.id}">
                            </c:if>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="docenteId" class="form-label">
                                        <i class="bi bi-person"></i> Docente <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" 
                                            id="docenteId" 
                                            name="docenteId" 
                                            required>
                                        <option value="">Seleccione un docente...</option>
                                        <c:forEach items="${docentes}" var="doc">
                                            <option value="${doc.id}" 
                                                    ${horario.docenteId == doc.id ? 'selected' : ''}>
                                                ${doc.nombreCompleto} (${doc.dni})
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="cursoId" class="form-label">
                                        <i class="bi bi-book"></i> Curso <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" 
                                            id="cursoId" 
                                            name="cursoId" 
                                            required>
                                        <option value="">Seleccione un curso...</option>
                                        <c:forEach items="${cursos}" var="cur">
                                            <option value="${cur.id}" 
                                                    ${horario.cursoId == cur.id ? 'selected' : ''}>
                                                ${cur.nombre} - ${cur.nivel} ${cur.grado}°
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="aulaId" class="form-label">
                                        <i class="bi bi-door-open"></i> Aula <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" 
                                            id="aulaId" 
                                            name="aulaId" 
                                            required>
                                        <option value="">Seleccione un aula...</option>
                                        <c:forEach items="${aulas}" var="au">
                                            <option value="${au.id}" 
                                                    ${horario.aulaId == au.id ? 'selected' : ''}>
                                                ${au.codigo} - ${au.nombre} (Cap: ${au.capacidad})
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="dia" class="form-label">
                                        <i class="bi bi-calendar-day"></i> Día <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" 
                                            id="dia" 
                                            name="dia" 
                                            required>
                                        <option value="">Seleccione un día...</option>
                                        <c:forEach items="${dias}" var="d">
                                            <option value="${d}" 
                                                    ${horario.dia == d ? 'selected' : ''}>
                                                ${d.nombre}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="horaInicio" class="form-label">
                                        <i class="bi bi-clock"></i> Hora Inicio <span class="text-danger">*</span>
                                    </label>
                                    <input type="time" 
                                           class="form-control" 
                                           id="horaInicio" 
                                           name="horaInicio" 
                                           value="${horario.horaInicio}"
                                           required>
                                    <small class="text-muted">Formato 24 horas (Ej: 08:00)</small>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="horaFin" class="form-label">
                                        <i class="bi bi-clock-fill"></i> Hora Fin <span class="text-danger">*</span>
                                    </label>
                                    <input type="time" 
                                           class="form-control" 
                                           id="horaFin" 
                                           name="horaFin" 
                                           value="${horario.horaFin}"
                                           required>
                                    <small class="text-muted">Formato 24 horas (Ej: 09:00)</small>
                                </div>
                            </div>
                            
                            <div class="alert alert-info">
                                <i class="bi bi-info-circle"></i>
                                <strong>Importante:</strong>
                                <ul class="mb-0 mt-2">
                                    <li>El sistema validará automáticamente que no haya choques de horarios</li>
                                    <li>Un docente no puede tener dos clases al mismo tiempo</li>
                                    <li>Un aula no puede estar ocupada por dos clases simultáneamente</li>
                                    <li>La hora de inicio debe ser menor que la hora de fin</li>
                                </ul>
                            </div>
                            
                            <hr>
                            
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/horarios?action=listar" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-danger">
                                    <i class="bi bi-save"></i> Guardar Horario
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
        // Validación adicional en el cliente
        document.querySelector('form').addEventListener('submit', function(e) {
            const horaInicio = document.getElementById('horaInicio').value;
            const horaFin = document.getElementById('horaFin').value;
            
            if (horaInicio && horaFin && horaInicio >= horaFin) {
                e.preventDefault();
                alert('La hora de inicio debe ser menor que la hora de fin');
                return false;
            }
        });
    </script>
</body>
</html>