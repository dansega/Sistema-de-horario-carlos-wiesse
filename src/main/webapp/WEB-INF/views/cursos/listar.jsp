<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Cursos - Sistema Horarios</title>
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
    
    <div class="container-fluid mt-4">
        <div class="row mb-3">
            <div class="col-md-12">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h2>
                            <i class="bi bi-book"></i> Gestión de Cursos
                        </h2>
                        <p class="text-muted">Total de cursos: <strong>${totalCursos}</strong></p>
                    </div>
                    <div>
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary me-2">
                            <i class="bi bi-arrow-left"></i> Volver al Dashboard
                        </a>
                        <a href="${pageContext.request.contextPath}/cursos?action=exportar" class="btn btn-success me-2">
    <i class="bi bi-file-earmark-excel"></i> Exportar Excel
</a>
<a href="${pageContext.request.contextPath}/cursos?action=nuevo" class="btn btn-primary">
    <i class="bi bi-plus-circle"></i> Nuevo Curso
</a>
                    </div>
                </div>
            </div>
        </div>
        
        <c:if test="${not empty mensaje}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle"></i> ${mensaje}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="mensaje" scope="session"/>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-circle"></i> ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0"><i class="bi bi-list-ul"></i> Lista de Cursos</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty cursos}">
                        <div class="alert alert-info">
                            <i class="bi bi-info-circle"></i>
                            No hay cursos registrados. Haz clic en "Nuevo Curso" para agregar uno.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-hover table-striped">
                                <thead class="table-primary">
                                    <tr>
                                        <th>ID</th>
                                        <th>Nombre</th>
                                        <th>Descripción</th>
                                        <th>Horas Semanales</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="curso" items="${cursos}">
                                        <tr>
                                            <td>${curso.id}</td>
                                            <td>
                                                <strong>${curso.nombre}</strong>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty curso.descripcion}">
                                                        ${curso.descripcion}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted fst-italic">Sin descripción</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge bg-info">
                                                    <i class="bi bi-clock"></i> ${curso.horasSemanales}h
                                                </span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${curso.estado}">
                                                        <span class="badge bg-success">
                                                            <i class="bi bi-check-circle"></i> Activo
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger">
                                                            <i class="bi bi-x-circle"></i> Inactivo
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <a href="${pageContext.request.contextPath}/cursos?action=editar&id=${curso.id}" 
                                                       class="btn btn-sm btn-warning" title="Editar">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>
                                                    <button type="button" 
                                                            class="btn btn-sm btn-danger" 
                                                            onclick="confirmarEliminacion(${curso.id}, '${curso.nombre}')"
                                                            title="Eliminar">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    
    <!-- Formulario oculto para eliminar -->
    <form id="formEliminar" method="post" action="${pageContext.request.contextPath}/cursos">
        <input type="hidden" name="action" value="eliminar">
        <input type="hidden" name="id" id="idEliminar">
    </form>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmarEliminacion(id, nombre) {
            if (confirm('¿Está seguro de eliminar el curso "' + nombre + '"?\n\nEsta acción desactivará el curso.')) {
                document.getElementById('idEliminar').value = id;
                document.getElementById('formEliminar').submit();
            }
        }
    </script>
</body>
</html>