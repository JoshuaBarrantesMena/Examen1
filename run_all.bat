@echo off
setlocal enabledelayedexpansion

echo ===============================
echo Verificando carpetas necesarias
echo ===============================
if not exist out (
    mkdir out
    echo Carpeta out creada.
)
if not exist Java\bin (
    mkdir Java\bin
    echo Carpeta Java\bin creada.
)

echo ===============================
echo        Compilando Fortran
echo ===============================
cd Fortran
gfortran *.f90 -o main.exe
if errorlevel 1 (
    echo Error al compilar Fortran
    exit /b 1
)
echo Compilacion Fortran completada.

echo ===============================
echo       Ejecutando Fortran
echo ===============================
main.exe
if errorlevel 1 (
    echo Error al ejecutar Fortran
    exit /b 1
)
cd ..

echo ===============================
echo         Compilando Java
echo ===============================
cd Java

rmdir /S /Q bin
mkdir bin

(for /R java\src\main\java %%f in (*.java) do @echo %%f) > sources.txt
javac -d bin @sources.txt
del sources.txt

if errorlevel 1 (
    echo Error al compilar Java
    exit /b 1
)
cd ..

echo ===============================
echo         Ejecutando Java
echo ===============================
cd Java
java -cp bin com.mycompany.java.Main ..\out\stats.csv ..\java\rules.json ..\out\reporte.txt ..\out\reporte_resumen.csv
if errorlevel 1 (
    echo Error al ejecutar Java
    exit /b 1
)
cd ..

echo ===============================
echo  Flujo completado exitosamente
echo ===============================

pause