<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Horario - Sistema Horarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-success">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/docente/dashboard">
                <i class="bi bi-calendar-week"></i> Mi Horario
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text text-white me-3">
                    <i class="bi bi-person-circle"></i> ${docente.nombreCompleto}
                </span>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">
                    <i class="bi bi-box-arrow-right"></i> Salir
                </a>
            </div>
        </div>
    </nav>
    
    <div class="container mt-4">
        <!-- Información del Docente -->
        <div class="card mb-4 border-success">
            <div class="card-header bg-success text-white">
                <h4 class="mb-0">
                    <i class="bi bi-person-badge"></i> Información Personal
                </h4>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Nombre:</strong> ${docente.nombreCompleto}</p>
                        <p><strong>DNI:</strong> ${docente.dni}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Email:</strong> ${docente.email != null ? docente.email : 'No registrado'}</p>
                        <p><strong>Teléfono:</strong> ${docente.telefono != null ? docente.telefono : 'No registrado'}</p>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Mi Horario Semanal -->
        <div class="card">
            <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                <h4 class="mb-0">
                    <i class="bi bi-calendar3"></i> Mi Horario Semanal
                </h4>
                <a href="${pageContext.request.contextPath}/docente/horario/exportar" 
                   class="btn btn-light btn-sm">
                    <i class="bi bi-file-earmark-excel"></i> Exportar Mi Horario
                </a>
            </div>
            <div class="card-body">
                <c:if test="${not empty sessionScope.mensaje}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle"></i> ${sessionScope.mensaje}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="mensaje" scope="session"/>
                </c:if>
                
                <p class="text-muted mb-3">
                    <i class="bi bi-info-circle"></i> 
                    Total de clases asignadas: <strong>${totalHorarios}</strong>
                </p>
                
                <div class="table-responsive">
                    <table class="table table-hover table-bordered">
                        <thead class="table-success">
                            <tr>
                                <th>Día</th>
                                <th>Horario</th>
                                <th>Duración</th>
                                <th>Curso</th>
                                <th>Aula</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${horarios}" var="horario">
                                <tr>
                                    <td>
                                        <span class="badge bg-info">${horario.dia.nombre}</span>
                                    </td>
                                    <td>
                                        <i class="bi bi-clock"></i>
                                        <strong>${horario.horaInicio}</strong> - <strong>${horario.horaFin}</strong>
                                    </td>
                                    <td>
                                        <small class="text-muted">${horario.duracionMinutos} minutos</small>
                                    </td>
                                    <td>
                                        <strong>${horario.curso.nombre}</strong><br>
                                        <small class="text-muted">
                                            ${horario.curso.nivel} - ${horario.curso.grado}° grado
                                        </small>
                                    </td>
                                    <td>
                                        <span class="badge bg-secondary">${horario.aula.codigo}</span>
                                        <c:if test="${not empty horario.aula.nombre}">
                                            <br><small>${horario.aula.nombre}</small>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            
                            <c:if test="${empty horarios}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted">
                                        <i class="bi bi-calendar-x"></i>
                                        No tienes clases asignadas en este momento
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                
                <c:if test="${not empty horarios}">
                    <div class="alert alert-info mt-3">
                        <i class="bi bi-lightbulb"></i>
                        <strong>Tip:</strong> Puedes exportar tu horario a Excel para imprimirlo o guardarlo en tu dispositivo.
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>