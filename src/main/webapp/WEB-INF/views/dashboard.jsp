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
            <div class="col-md-4">
                <div class="card text-center">
                    <div class="card-body">
                        <i class="bi bi-person-badge fs-1 text-primary"></i>
                        <h5 class="card-title mt-3">Docentes</h5>
                        <p class="card-text">Gestionar docentes del colegio</p>
                        <a href="${pageContext.request.contextPath}/docentes?action=listar" class="btn btn-primary">
                            Ir a Docentes
                        </a>
                    </div>
                </div>
            </div>
            
          <!-- creacion de AULAS en el dashboard -->
        <div class="col-md-4">
                 <div class="card text-center">
                        <div class="card-body">
                        <i class="bi bi-door-open fs-1 text-success"></i>
                        <h5 class="card-title mt-3">Aulas</h5>
                        <p class="card-text">Gestionar aulas y espacios</p>
                        <a href="${pageContext.request.contextPath}/aulas?action=listar" class="btn btn-success">
                             Ir a Aulas
                         </a>
                          </div>
                  </div>
         </div>
            
            <div class="col-md-4">
                <div class="card text-center">
                    <div class="card-body">
                        <i class="bi bi-calendar3 fs-1 text-danger"></i>
                        <h5 class="card-title mt-3">Horarios</h5>
                        <p class="card-text">Gestionar horarios escolares</p>
                        <a href="#" class="btn btn-danger disabled">
                            
                           <!--- creacoin de horario -->
                           <a href="${pageContext.request.contextPath}/horarios?action=listar" class="btn btn-danger">
                             Ir a Horarios
                            </a>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>