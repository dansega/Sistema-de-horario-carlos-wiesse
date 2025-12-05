<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Sistema Horarios</title>
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
    
    <div class="container mt-5">
        <h1><i class="bi bi-speedometer2"></i> Dashboard</h1>
        <p class="text-muted">Bienvenido al sistema de gestión de horarios</p>
        
        <div class="row mt-4">
            <!-- Docentes -->
            <div class="col-md-3 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <i class="bi bi-person-badge" style="font-size: 3rem; color: #0d6efd;"></i>
                        </div>
                        <h5 class="card-title">Docentes</h5>
                        <p class="card-text text-muted">Gestionar docentes del colegio</p>
                        <a href="${pageContext.request.contextPath}/docentes?action=listar" 
                           class="btn btn-primary">
                            <i class="bi bi-arrow-right-circle"></i> Ver Docentes
                        </a>
                    </div>
                </div>
            </div>
            
            <!-- Aulas -->
            <div class="col-md-3 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <i class="bi bi-door-open" style="font-size: 3rem; color: #198754;"></i>
                        </div>
                        <h5 class="card-title">Aulas</h5>
                        <p class="card-text text-muted">Gestionar aulas y espacios</p>
                        <a href="${pageContext.request.contextPath}/aulas?action=listar" 
                           class="btn btn-success">
                            <i class="bi bi-arrow-right-circle"></i> Ver Aulas
                        </a>
                    </div>
                </div>
            </div>
            
            <!-- Cursos (NUEVO) -->
            <div class="col-md-3 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <i class="bi bi-book" style="font-size: 3rem; color: #fd7e14;"></i>
                        </div>
                        <h5 class="card-title">Cursos</h5>
                        <p class="card-text text-muted">Gestionar cursos del colegio</p>
                        <a href="${pageContext.request.contextPath}/cursos?action=listar" 
                           class="btn btn-warning">
                            <i class="bi bi-arrow-right-circle"></i> Ver Cursos
                        </a>
                    </div>
                </div>
            </div>
            
            <!-- Horarios -->
            <div class="col-md-3 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <i class="bi bi-calendar3" style="font-size: 3rem; color: #dc3545;"></i>
                        </div>
                        <h5 class="card-title">Horarios</h5>
                        <p class="card-text text-muted">Gestionar horarios escolares</p>
                        <a href="${pageContext.request.contextPath}/horarios?action=listar" 
                           class="btn btn-danger">
                            <i class="bi bi-arrow-right-circle"></i> Ver Horarios
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>