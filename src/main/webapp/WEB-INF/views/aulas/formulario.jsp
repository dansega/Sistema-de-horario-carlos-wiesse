<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty aula ? 'Nueva' : 'Editar'} Aula - Sistema Horarios</title>
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
                    <div class="card-header bg-success text-white">
                        <h4 class="mb-0">
                            <i class="bi bi-door-open"></i>
                            ${empty aula ? 'Nueva Aula' : 'Editar Aula'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="bi bi-exclamation-circle"></i> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/aulas" method="post">
                            <input type="hidden" name="action" value="${empty aula ? 'crear' : 'actualizar'}">
                            <c:if test="${not empty aula}">
                                <input type="hidden" name="id" value="${aula.id}">
                            </c:if>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="codigo" class="form-label">
                                        Código <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="codigo" 
                                           name="codigo" 
                                           value="${aula.codigo}"
                                           placeholder="Ej: A-101"
                                           maxlength="20"
                                           required>
                                    <small class="text-muted">Ejemplo: A-101, B-205, LAB-01</small>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="nombre" class="form-label">
                                        Nombre
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="nombre" 
                                           name="nombre" 
                                           value="${aula.nombre}"
                                           placeholder="Nombre descriptivo del aula"
                                           maxlength="100">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="capacidad" class="form-label">
                                        Capacidad <span class="text-danger">*</span>
                                    </label>
                                    <input type="number" 
                                           class="form-control" 
                                           id="capacidad" 
                                           name="capacidad" 
                                           value="${aula.capacidad}"
                                           placeholder="Número de alumnos"
                                           min="1"
                                           max="50"
                                           required>
                                    <small class="text-muted">Entre 1 y 50 alumnos</small>
                                </div>
                                
                                <div class="col-md-4 mb-3">
                                    <label for="piso" class="form-label">
                                        Piso <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" 
                                            id="piso" 
                                            name="piso" 
                                            required>
                                        <option value="">Seleccione...</option>
                                        <option value="1" ${aula.piso == 1 ? 'selected' : ''}>Piso 1</option>
                                        <option value="2" ${aula.piso == 2 ? 'selected' : ''}>Piso 2</option>
                                        <option value="3" ${aula.piso == 3 ? 'selected' : ''}>Piso 3</option>
                                        <option value="4" ${aula.piso == 4 ? 'selected' : ''}>Piso 4</option>
                                        <option value="5" ${aula.piso == 5 ? 'selected' : ''}>Piso 5</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-4 mb-3">
                                    <label for="edificio" class="form-label">
                                        Edificio
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="edificio" 
                                           name="edificio" 
                                           value="${aula.edificio}"
                                           placeholder="Ej: A, B, C"
                                           maxlength="10">
                                    <small class="text-muted">Opcional</small>
                                </div>
                            </div>
                            
                            <div class="alert alert-info">
                                <i class="bi bi-info-circle"></i>
                                <strong>Nota:</strong> Los campos marcados con <span class="text-danger">*</span> son obligatorios.
                            </div>
                            
                            <hr>
                            
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/aulas?action=listar" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-success">
                                    <i class="bi bi-save"></i> Guardar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>