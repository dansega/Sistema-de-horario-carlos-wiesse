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
            <div class="col-md-10 offset-md-1">
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
                        
                        <c:if test="${not empty mensaje}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="bi bi-check-circle"></i> ${mensaje}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/docentes" method="post">
                            <input type="hidden" name="action" value="${empty docente ? 'crear' : 'actualizar'}">
                            <c:if test="${not empty docente}">
                                <input type="hidden" name="id" value="${docente.id}">
                            </c:if>
                            
                            <!-- Datos Personales -->
                            <h5 class="mb-3"><i class="bi bi-person"></i> Datos Personales</h5>
                            
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
                            
                            <!-- Crear usuario SOLO si NO tiene uno -->
                            <c:if test="${empty docente or not tieneUsuario}">
                                <hr class="my-4">
                                
                                <div class="card border-success">
                                    <div class="card-header bg-success text-white">
                                        <h5 class="mb-0">
                                            <i class="bi bi-key"></i> Crear Acceso al Sistema
                                        </h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="form-check mb-3">
                                            <input type="checkbox" class="form-check-input" id="crearUsuario" 
                                                   name="crearUsuario" onchange="toggleUsuarioFields()">
                                            <label class="form-check-label" for="crearUsuario">
                                                <strong>Crear acceso al sistema para este docente</strong>
                                            </label>
                                        </div>
                                        
                                        <div id="usuarioFields" style="display: none;">
                                            <div class="alert alert-info">
                                                <i class="bi bi-info-circle"></i>
                                                El usuario podrá iniciar sesión con estas credenciales.
                                            </div>
                                            
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="username" class="form-label">
                                                            Usuario <span class="text-danger">*</span>
                                                        </label>
                                                        <input type="text" class="form-control" id="username" 
                                                               name="username" placeholder="Ej: jperez"
                                                               value="${username}">
                                                        <small class="text-muted">Sin espacios, solo letras y números</small>
                                                    </div>
                                                </div>
                                                
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="password" class="form-label">
                                                            Contraseña temporal <span class="text-danger">*</span>
                                                        </label>
                                                        <input type="password" class="form-control" id="password" 
                                                               name="password" placeholder="Mínimo 6 caracteres">
                                                        <small class="text-muted">Entregar esta contraseña al usuario</small>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="mb-3">
                                                        <label for="rol" class="form-label">
                                                            Rol en el Sistema <span class="text-danger">*</span>
                                                        </label>
                                                        <select class="form-select" id="rol" name="rol">
                                                            <option value="">-- Seleccione un rol --</option>
                                                            <option value="DOCENTE" selected>Docente (Solo ver su horario)</option>
                                                            <option value="ADMIN">Administrador (Gestionar todo el sistema)</option>
                                                        </select>
                                                        <small class="text-muted">
                                                            <i class="bi bi-info-circle"></i>
                                                            <strong>Docente:</strong> Solo puede ver su horario asignado.
                                                            <strong>Administrador:</strong> Puede gestionar docentes, aulas, horarios y usuarios.
                                                        </small>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            
                            <!-- NUEVA SECCIÓN: Mostrar y cambiar ROL en usuarios existentes -->
                            <c:if test="${not empty docente and tieneUsuario}">
                                <hr class="my-4">
                                
                                <div class="card border-warning">
                                    <div class="card-header bg-warning text-dark">
                                        <h5 class="mb-0">
                                            <i class="bi bi-shield-check"></i> Gestión de Rol del Usuario
                                        </h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="mb-3">
                                            <label class="form-label"><strong>Rol Actual:</strong></label>
                                            <div>
                                                <c:choose>
                                                    <c:when test="${docente.usuario.rol == 'ADMIN'}">
                                                        <span class="badge bg-danger fs-6">
                                                            <i class="bi bi-shield-fill-check"></i> ADMINISTRADOR
                                                        </span>
                                                        <small class="text-muted d-block mt-1">
                                                            <i class="bi bi-check-circle"></i> Tiene acceso completo para gestionar el sistema
                                                        </small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-info fs-6">
                                                            <i class="bi bi-person-circle"></i> DOCENTE
                                                        </span>
                                                        <small class="text-muted d-block mt-1">
                                                            <i class="bi bi-info-circle"></i> Solo puede visualizar sus horarios asignados
                                                        </small>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <label for="nuevoRol" class="form-label">
                                                <strong>Cambiar Rol (Opcional):</strong>
                                            </label>
                                            <select class="form-select" id="nuevoRol" name="nuevoRol">
                                                <option value="">-- Mantener rol actual --</option>
                                                <option value="DOCENTE">DOCENTE - Solo visualiza sus horarios</option>
                                                <option value="ADMIN">ADMINISTRADOR - Gestión completa del sistema</option>
                                            </select>
                                            <small class="text-muted">
                                                <i class="bi bi-exclamation-triangle"></i>
                                                <strong>Importante:</strong> Si cambias el rol, se aplicará inmediatamente.
                                                Deja en "Mantener rol actual" si no deseas hacer cambios.
                                            </small>
                                        </div>
                                        
                                        <div class="alert alert-warning mt-2">
                                            <i class="bi bi-info-circle"></i>
                                            <strong>Nota:</strong> Los cambios de rol se guardan junto con los datos del docente al hacer clic en "Guardar Docente" abajo.
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            
                            <div class="d-flex justify-content-between mt-4">
                                <a href="${pageContext.request.contextPath}/docentes?action=listar" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save"></i> Guardar Docente
                                </button>
                            </div>
                        </form>
                        
                        <!-- SECCIÓN ACTIVAR/DESACTIVAR/RESETEAR - FUERA DEL FORMULARIO -->
                        <c:if test="${not empty docente and tieneUsuario}">
                            <hr class="my-4">
                            
                            <div class="card border-info">
                                <div class="card-header bg-info text-white">
                                    <h5 class="mb-0">
                                        <i class="bi bi-key"></i> Gestión de Acceso al Sistema
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${usuarioActivo}">
                                            <div class="alert alert-success">
                                                <i class="bi bi-check-circle"></i>
                                                <strong>Este docente tiene acceso al sistema (ACTIVO)</strong>
                                            </div>
                                            
                                            <div class="row">
                                                <div class="col-md-6 mb-2">
                                                    <form action="${pageContext.request.contextPath}/docentes" method="post" 
                                                          onsubmit="return confirm('¿Está seguro de desactivar el acceso?');">
                                                        <input type="hidden" name="action" value="desactivarUsuario">
                                                        <input type="hidden" name="id" value="${docente.id}">
                                                        <button type="submit" class="btn btn-warning w-100">
                                                            <i class="bi bi-lock"></i> Desactivar Acceso
                                                        </button>
                                                    </form>
                                                </div>
                                                
                                                <div class="col-md-6 mb-2">
                                                    <button type="button" class="btn btn-primary w-100" 
                                                            data-bs-toggle="modal" data-bs-target="#resetPasswordModal">
                                                        <i class="bi bi-key-fill"></i> Resetear Contraseña
                                                    </button>
                                                </div>
                                            </div>
                                            
                                            <small class="text-muted d-block mt-2">
                                                <i class="bi bi-info-circle"></i>
                                                Puede desactivar el acceso o resetear la contraseña si el docente la olvidó
                                            </small>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="alert alert-warning">
                                                <i class="bi bi-exclamation-triangle"></i>
                                                <strong>Este docente tiene usuario pero está DESACTIVADO</strong>
                                            </div>
                                            
                                            <div class="row">
                                                <div class="col-md-6 mb-2">
                                                    <form action="${pageContext.request.contextPath}/docentes" method="post" 
                                                          onsubmit="return confirm('¿Está seguro de reactivar el acceso?');">
                                                        <input type="hidden" name="action" value="activarUsuario">
                                                        <input type="hidden" name="id" value="${docente.id}">
                                                        <button type="submit" class="btn btn-success w-100">
                                                            <i class="bi bi-unlock"></i> Activar Acceso
                                                        </button>
                                                    </form>
                                                </div>
                                                
                                                <div class="col-md-6 mb-2">
                                                    <button type="button" class="btn btn-primary w-100" 
                                                            data-bs-toggle="modal" data-bs-target="#resetPasswordModal">
                                                        <i class="bi bi-key-fill"></i> Resetear Contraseña
                                                    </button>
                                                </div>
                                            </div>
                                            
                                            <small class="text-muted d-block mt-2">
                                                <i class="bi bi-info-circle"></i>
                                                Puede activar el acceso nuevamente o resetear la contraseña
                                            </small>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Modal para Resetear Contraseña -->
    <div class="modal fade" id="resetPasswordModal" tabindex="-1" aria-labelledby="resetPasswordModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="resetPasswordModalLabel">
                        <i class="bi bi-key-fill"></i> Resetear Contraseña
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="${pageContext.request.contextPath}/docentes" method="post" id="resetPasswordForm">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="resetearPassword">
                        <input type="hidden" name="id" value="${docente.id}">
                        
                        <div class="alert alert-info">
                            <i class="bi bi-info-circle"></i>
                            <strong>Se generará una nueva contraseña temporal para el docente.</strong><br>
                            Debe entregarle esta contraseña para que pueda iniciar sesión.
                        </div>
                        
                        <div class="mb-3">
                            <label for="nuevaPassword" class="form-label">
                                Nueva Contraseña Temporal <span class="text-danger">*</span>
                            </label>
                            <input type="text" class="form-control" id="nuevaPassword" name="nuevaPassword" 
                                   placeholder="Mínimo 6 caracteres" required minlength="6">
                            <small class="text-muted">Entregar esta contraseña al docente</small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check-lg"></i> Resetear Contraseña
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function toggleUsuarioFields() {
            const checkbox = document.getElementById('crearUsuario');
            const fields = document.getElementById('usuarioFields');
            
            if (checkbox && fields) {
                if (checkbox.checked) {
                    fields.style.display = 'block';
                    document.getElementById('username').required = true;
                    document.getElementById('password').required = true;
                    document.getElementById('rol').required = true;
                } else {
                    fields.style.display = 'none';
                    document.getElementById('username').required = false;
                    document.getElementById('password').required = false;
                    document.getElementById('rol').required = false;
                }
            }
        }
        
        // Validación adicional al enviar
        const mainForm = document.querySelector('form[action*="docentes"]');
        if (mainForm && mainForm.querySelector('#crearUsuario')) {
            mainForm.addEventListener('submit', function(e) {
                const crearUsuario = document.getElementById('crearUsuario');
                
                if (crearUsuario && crearUsuario.checked) {
                    const username = document.getElementById('username').value.trim();
                    const password = document.getElementById('password').value;
                    const rol = document.getElementById('rol').value;
                    
                    if (!username || !password || !rol) {
                        e.preventDefault();
                        alert('Debe completar usuario, contraseña y rol');
                        return false;
                    }
                    
                    if (password.length < 6) {
                        e.preventDefault();
                        alert('La contraseña debe tener al menos 6 caracteres');
                        return false;
                    }
                    
                    if (!/^[a-zA-Z0-9]+$/.test(username)) {
                        e.preventDefault();
                        alert('El usuario solo puede contener letras y números, sin espacios');
                        return false;
                    }
                }
            });
        }
    </script>
</body>
</html>