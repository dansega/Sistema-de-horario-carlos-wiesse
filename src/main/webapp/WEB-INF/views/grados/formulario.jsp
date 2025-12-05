<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty grado ? 'Nuevo' : 'Editar'} Grado - Sistema Horarios</title>
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
                            <i class="bi bi-diagram-3"></i>
                            ${empty grado ? 'Nuevo Grado/Sección' : 'Editar Grado/Sección'}
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
                        
                        <form action="${pageContext.request.contextPath}/grados" method="post" id="gradoForm">
                            <input type="hidden" name="action" value="${empty grado ? 'crear' : 'actualizar'}">
                            <c:if test="${not empty grado}">
                                <input type="hidden" name="id" value="${grado.id}">
                            </c:if>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nivel" class="form-label">
                                        Nivel Educativo <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" 
                                            id="nivel" 
                                            name="nivel" 
                                            required>
                                        <option value="">Seleccione un nivel...</option>
                                        <c:forEach items="${niveles}" var="niv">
                                            <option value="${niv}" 
                                                    ${grado.nivel == niv ? 'selected' : ''}>
                                                ${niv.nombre}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <small class="text-muted">
                                        <i class="bi bi-info-circle"></i>
                                        Primaria o Secundaria
                                    </small>
                                </div>
                                
                                <div class="col-md-3 mb-3">
                                    <label for="numero" class="form-label">
                                        Número de Grado <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" 
                                            id="numero" 
                                            name="numero" 
                                            required>
                                        <option value="">Seleccione...</option>
                                        <option value="1" ${grado.numero == 1 ? 'selected' : ''}>1°</option>
                                        <option value="2" ${grado.numero == 2 ? 'selected' : ''}>2°</option>
                                        <option value="3" ${grado.numero == 3 ? 'selected' : ''}>3°</option>
                                        <option value="4" ${grado.numero == 4 ? 'selected' : ''}>4°</option>
                                        <option value="5" ${grado.numero == 5 ? 'selected' : ''}>5°</option>
                                        <option value="6" ${grado.numero == 6 ? 'selected' : ''}>6°</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-3 mb-3">
                                    <label for="seccion" class="form-label">
                                        Sección <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" 
                                           class="form-control text-uppercase" 
                                           id="seccion" 
                                           name="seccion" 
                                           value="${grado.seccion}"
                                           placeholder="Ej: A, B, C"
                                           maxlength="10"
                                           required>
                                    <small class="text-muted">Máx. 10 caracteres</small>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="aulaId" class="form-label">
                                    Aula Asignada (Opcional)
                                </label>
                                <select class="form-select" 
                                        id="aulaId" 
                                        name="aulaId">
                                    <option value="">Sin aula asignada</option>
                                    <c:forEach items="${aulas}" var="aula">
                                        <option value="${aula.id}" 
                                                ${grado.aulaId == aula.id ? 'selected' : ''}>
                                            ${aula.codigo} - ${aula.nombre} (Cap: ${aula.capacidad})
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="text-muted">
                                    <i class="bi bi-info-circle"></i>
                                    Puedes asignar el aula después
                                </small>
                            </div>
                            
                            <div class="alert alert-info">
                                <i class="bi bi-lightbulb"></i>
                                <strong>Nota:</strong> La combinación de Nivel + Número + Sección debe ser única. 
                                Por ejemplo, no puede haber dos "1° A - Primaria".
                            </div>
                            
                            <hr>
                            
                            <div class="d-flex justify-content-between mt-4">
                                <a href="${pageContext.request.contextPath}/grados?action=listar" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save"></i> 
                                    ${empty grado ? 'Crear Grado' : 'Guardar Cambios'}
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
        // Convertir sección a mayúsculas automáticamente
        document.getElementById('seccion').addEventListener('input', function(e) {
            this.value = this.value.toUpperCase();
        });
        
        // Validación adicional del formulario
        document.getElementById('gradoForm').addEventListener('submit', function(e) {
            const nivel = document.getElementById('nivel').value;
            const numero = document.getElementById('numero').value;
            const seccion = document.getElementById('seccion').value.trim();
            
            if (!nivel || !numero || !seccion) {
                e.preventDefault();
                alert('Todos los campos marcados con * son obligatorios');
                return false;
            }
            
            if (seccion.length > 10) {
                e.preventDefault();
                alert('La sección no puede tener más de 10 caracteres');
                return false;
            }
        });
        
        // Auto-focus en el primer campo
        document.getElementById('nivel').focus();
    </script>
</body>
</html>