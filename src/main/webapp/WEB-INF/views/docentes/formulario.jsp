<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty docente ? 'Nuevo' : 'Editar'} Docente - Sistema Horarios</title>
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
                            <i class="bi bi-person-badge"></i>
                            ${empty docente ? 'Nuevo Docente' : 'Editar Docente'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="bi bi-exclamation-circle"></i> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/docentes" method="post">
                            <input type="hidden" name="action" value="${empty docente ? 'crear' : 'actualizar'}">
                            <c:if test="${not empty docente}">
                                <input type="hidden" name="id" value="${docente.id}">
                            </c:if>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="dni" class="form-label">
                                        DNI <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="dni" 
                                           name="dni" 
                                           value="${docente.dni}"
                                           placeholder="8 dígitos"
                                           maxlength="8"
                                           pattern="[0-9]{8}"
                                           required>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="nombre" class="form-label">
                                        Nombre <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="nombre" 
                                           name="nombre" 
                                           value="${docente.nombre}"
                                           placeholder="Nombre del docente"
                                           required>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="apellidoPaterno" class="form-label">
                                        Apellido Paterno <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="apellidoPaterno" 
                                           name="apellidoPaterno" 
                                           value="${docente.apellidoPaterno}"
                                           placeholder="Apellido paterno"
                                           required>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="apellidoMaterno" class="form-label">
                                        Apellido Materno <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="apellidoMaterno" 
                                           name="apellidoMaterno" 
                                           value="${docente.apellidoMaterno}"
                                           placeholder="Apellido materno"
                                           required>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label">
                                        Email
                                    </label>
                                    <input type="email" 
                                           class="form-control" 
                                           id="email" 
                                           name="email" 
                                           value="${docente.email}"
                                           placeholder="correo@ejemplo.com">
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="telefono" class="form-label">
                                        Teléfono
                                    </label>
                                    <input type="tel" 
                                           class="form-control" 
                                           id="telefono" 
                                           name="telefono" 
                                           value="${docente.telefono}"
                                           placeholder="999999999"
                                           maxlength="15">
                                </div>
                            </div>
                            
                            <hr>
                            
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/docentes?action=listar" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
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