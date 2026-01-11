# Script para listar y cerrar procesos que escuchan en el puerto 8080
# Ejecutar con PowerShell

$connections = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if (-not $connections) {
    Write-Output 'No process is listening on port 8080.'
    exit 0
}

$pids = $connections | Select-Object -ExpandProperty OwningProcess -Unique
foreach ($pid in $pids) {
    Write-Output "----"
    Write-Output "Found PID: $pid"
    Get-Process -Id $pid -ErrorAction SilentlyContinue | Format-Table Id, ProcessName, Path -AutoSize
    try {
        Stop-Process -Id $pid -Force -ErrorAction Stop
        Start-Sleep -Milliseconds 200
        if (-not (Get-Process -Id $pid -ErrorAction SilentlyContinue)) {
            Write-Output ("Killed PID " + $pid)
        } else {
            Write-Output ("Failed to kill PID " + $pid)
        }
    } catch {
        Write-Output ("Failed to kill PID " + $pid + ": " + $_.Exception.Message)
    }
}

# Verificación
if (-not (Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue)) {
    Write-Output 'Verification: port 8080 is now free.'
} else {
    Write-Output 'Verification: port 8080 still in use.'
}
