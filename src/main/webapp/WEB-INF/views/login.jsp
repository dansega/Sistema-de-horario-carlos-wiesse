<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sistema Horarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-card {
            background: white;
            border-radius: 16px;
            padding: 40px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 420px;
        }
        .login-logo {
            text-align: center;
            margin-bottom: 30px;
        }
        .login-logo i {
            font-size: 60px;
            color: #2563eb;
        }
    </style>
</head>
<body>
    <div class="login-card">
        <div class="login-logo">
            <i class="bi bi-calendar-check"></i>
            <h3 class="mt-3">Sistema de Horarios</h3>
            <p class="text-muted">Colegio Carlos Wiesse - Comas</p>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-circle"></i> ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="mb-3">
                <label for="username" class="form-label">
                    <i class="bi bi-person"></i> Usuario
                </label>
                <input type="text" 
                       class="form-control form-control-lg" 
                       id="username" 
                       name="username" 
                       placeholder="Ingrese su usuario"
                       value="${username}"
                       required 
                       autofocus>
            </div>
            
            <div class="mb-4">
                <label for="password" class="form-label">
                    <i class="bi bi-lock"></i> Contraseña
                </label>
                <input type="password" 
                       class="form-control form-control-lg" 
                       id="password" 
                       name="password" 
                       placeholder="Ingrese su contraseña"
                       required>
            </div>
            
            <button type="submit" class="btn btn-primary btn-lg w-100">
                <i class="bi bi-box-arrow-in-right"></i> Iniciar Sesión
            </button>
        </form>
        
        <div class="text-center mt-3">
            <small class="text-muted">
                Demo: <strong>admin</strong> / <strong>admin123</strong><br>
                o <strong>jperez</strong> / <strong>doc123</strong>
            </small>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>