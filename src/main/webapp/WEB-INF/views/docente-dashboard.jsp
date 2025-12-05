<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Docente - Sistema Horarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-success">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/docente/dashboard">
                <i class="bi bi-calendar-week"></i> Sistema Horarios - Docente
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
        <h1 class="text-center mb-4">
            <i class="bi bi-person-badge"></i> Bienvenido/a, ${docente.nombre}
        </h1>
        
        <!-- Información del Docente -->
        <div class="card mb-4 border-success">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0">
                    <i class="bi bi-person-circle"></i> Información Personal
                </h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong><i class="bi bi-person"></i> Nombre Completo:</strong> ${docente.nombreCompleto}</p>
                        <p><strong><i class="bi bi-card-text"></i> DNI:</strong> ${docente.dni}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong><i class="bi bi-envelope"></i> Email:</strong> 
                            ${not empty docente.email ? docente.email : '<span class="text-muted">No registrado</span>'}
                        </p>
                        <p><strong><i class="bi bi-telephone"></i> Teléfono:</strong> 
                            ${not empty docente.telefono ? docente.telefono : '<span class="text-muted">No registrado</span>'}
                        </p>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Resumen de Cursos -->
        <c:if test="${not empty docente.cursosQueDicta}">
            <div class="card mb-4 border-info">
                <div class="card-header bg-info text-white">
                    <h5 class="mb-0">
                        <i class="bi bi-book"></i> Cursos que Dicto
                    </h5>
                </div>
                <div class="card-body">
                    <c:forEach var="curso" items="${docente.cursosQueDicta}" varStatus="status">
                        <span class="badge bg-info text-dark me-2 mb-2">${curso}</span>
                    </c:forEach>
                </div>
            </div>
        </c:if>
        
        <!-- Mi Horario Semanal -->
        <div class="card">
            <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                <h5 class="mb-0">
                    <i class="bi bi-calendar3"></i> Mi Horario Semanal
                </h5>
                <a href="${pageContext.request.contextPath}/docente/horario/exportar" 
                   class="btn btn-light btn-sm">
                    <i class="bi bi-file-earmark-excel"></i> Exportar
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
                
                <c:choose>
                    <c:when test="${not empty horarios}">
                        <div class="table-responsive">
                            <table class="table table-hover table-bordered">
                                <thead class="table-success">
                                    <tr>
                                        <th>Día</th>
                                        <th>Horario</th>
                                        <th>Curso</th>
                                        <th>Aula</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${horarios}" var="horario">
                                        <tr>
                                            <td>
                                                <span class="badge bg-info text-dark">${horario.dia.nombre}</span>
                                            </td>
                                            <td>
                                                <i class="bi bi-clock"></i>
                                                <strong>${horario.horaInicio}</strong> - <strong>${horario.horaFin}</strong>
                                            </td>
                                            <td>
                                                <strong>${horario.curso.nombre}</strong>
                                            </td>
                                            <td>
                                                <span class="badge bg-secondary">
                                                    <i class="bi bi-door-open"></i> ${horario.aula.codigo}
                                                </span>
                                                <c:if test="${not empty horario.aula.nombre}">
                                                    <br><small class="text-muted">${horario.aula.nombre}</small>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        
                        <div class="alert alert-info mt-3">
                            <i class="bi bi-lightbulb"></i>
                            <strong>Tip:</strong> Puedes exportar tu horario a Excel para imprimirlo o guardarlo.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning text-center">
                            <i class="bi bi-calendar-x"></i>
                            <h5>No tienes clases asignadas</h5>
                            <p class="mb-0">Contacta al administrador para que te asigne horarios.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
