<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aulas - Sistema Horarios</title>
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
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2><i class="bi bi-door-open"></i> Gestión de Aulas</h2>
                <p class="text-muted">Total: ${totalAulas} aulas</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/aulas?action=nuevo" class="btn btn-success">
                    <i class="bi bi-plus-circle"></i> Nueva Aula
                </a>
                <a href="${pageContext.request.contextPath}/aulas/exportar" 
                   class="btn btn-success ms-2"
                   title="Exportar a Excel">
                    <i class="bi bi-file-earmark-excel"></i> Exportar Excel
                </a>
            </div>
        </div>
        
        <c:if test="${not empty sessionScope.mensaje}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle"></i> ${sessionScope.mensaje}
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
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>Código</th>
                                <th>Nombre</th>
                                <th>Capacidad</th>
                                <th>Piso</th>
                                <th>Edificio</th>
                                <th>Estado</th>
                                <th class="text-center">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${aulas}" var="aula">
                                <tr>
                                    <td><strong>${aula.codigo}</strong></td>
                                    <td>${aula.nombre}</td>
                                    <td><i class="bi bi-people"></i> ${aula.capacidad} alumnos</td>
                                    <td>Piso ${aula.piso}</td>
                                    <td>${aula.edificio}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${aula.estado}">
                                                <span class="badge bg-success">Activa</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Inactiva</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/aulas?action=editar&id=${aula.id}" 
                                           class="btn btn-sm btn-warning" title="Editar">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <button onclick="confirmarEliminacion(${aula.id}, '${aula.codigo}')" 
                                                class="btn btn-sm btn-danger" title="Eliminar">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            
                            <c:if test="${empty aulas}">
                                <tr>
                                    <td colspan="7" class="text-center text-muted">
                                        No hay aulas registradas
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> Volver al Dashboard
            </a>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmarEliminacion(id, codigo) {
            if (confirm('¿Está seguro de eliminar el aula ' + codigo + '?')) {
                var form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/aulas';
                
                var actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'eliminar';
                
                var idInput = document.createElement('input');
                idInput.type = 'hidden';
                idInput.name = 'id';
                idInput.value = id;
                
                form.appendChild(actionInput);
                form.appendChild(idInput);
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>