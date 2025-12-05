<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Grados - Sistema Horarios</title>
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
                            <i class="bi bi-diagram-3"></i> Gestión de Grados y Secciones
                        </h2>
                        <p class="text-muted">Total de grados: <strong>${totalGrados}</strong></p>
                    </div>
                    <div>
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary me-2">
                            <i class="bi bi-arrow-left"></i> Volver al Dashboard
                        </a>
                        <a href="${pageContext.request.contextPath}/grados?action=nuevo" class="btn btn-primary">
                            <i class="bi bi-plus-circle"></i> Nuevo Grado
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
                <h5 class="mb-0"><i class="bi bi-list-ul"></i> Lista de Grados y Secciones</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty grados}">
                        <div class="alert alert-info">
                            <i class="bi bi-info-circle"></i>
                            No hay grados registrados. Haz clic en "Nuevo Grado" para agregar uno.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-hover table-striped">
                                <thead class="table-primary">
                                    <tr>
                                        <th>ID</th>
                                        <th>Nivel</th>
                                        <th>Grado</th>
                                        <th>Sección</th>
                                        <th>Aula Asignada</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="grado" items="${grados}">
                                        <tr>
                                            <td>${grado.id}</td>
                                            <td>
                                                <span class="badge ${grado.nivel == 'PRIMARIA' ? 'bg-info' : 'bg-warning'}">
                                                    ${grado.nivel.nombre}
                                                </span>
                                            </td>
                                            <td><strong>${grado.numero}°</strong></td>
                                            <td><strong>${grado.seccion}</strong></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty grado.aula}">
                                                        <span class="badge bg-secondary">
                                                            <i class="bi bi-door-open"></i> ${grado.aula.codigo}
                                                        </span>
                                                        <c:if test="${not empty grado.aula.nombre}">
                                                            <br><small class="text-muted">${grado.aula.nombre}</small>
                                                        </c:if>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted fst-italic">Sin asignar</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${grado.estado}">
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
                                                    <a href="${pageContext.request.contextPath}/grados?action=editar&id=${grado.id}" 
                                                       class="btn btn-sm btn-warning" title="Editar">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>
                                                    <button type="button" 
                                                            class="btn btn-sm btn-danger" 
                                                            onclick="confirmarEliminacion(${grado.id}, '${grado.nombreCompleto}')"
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
    <form id="formEliminar" method="post" action="${pageContext.request.contextPath}/grados">
        <input type="hidden" name="action" value="eliminar">
        <input type="hidden" name="id" id="idEliminar">
    </form>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmarEliminacion(id, nombre) {
            if (confirm('¿Está seguro de eliminar el grado "' + nombre + '"?\n\nEsta acción desactivará el grado.')) {
                document.getElementById('idEliminar').value = id;
                document.getElementById('formEliminar').submit();
            }
        }
    </script>
</body>
</html>