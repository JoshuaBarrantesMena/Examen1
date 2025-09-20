! Modulo de calculos de estadisticas
MODULE compute_stats
  IMPLICIT NONE
CONTAINS

   ! Calculo de la media
  FUNCTION mean_col(col) RESULT(m)
    REAL, INTENT(IN) :: col(:)
    REAL :: m
    m = SUM(col) / SIZE(col)
  END FUNCTION mean_col

  ! Calculo de la mediana
  FUNCTION median_col(col) RESULT(med)
    REAL, INTENT(IN) :: col(:)
    REAL :: med
    REAL, ALLOCATABLE :: col_copy(:)
    INTEGER :: n

    ! Crear copia de la columna
    n = SIZE(col)
    ALLOCATE(col_copy(n))
    col_copy = col

    ! Ordenar la copia de la columna
    CALL sort(col_copy)

    ! Calcular mediana de la columna ordenada
    IF (MOD(n,2) == 0) THEN
       med = (col_copy(n/2) + col_copy(n/2+1))/2.0
    ELSE
       med = col_copy((n+1)/2)
    END IF
  END FUNCTION median_col

   ! Calculo de la desviacion estandar
  FUNCTION stddev_col(col) RESULT(sd)
    REAL, INTENT(IN) :: col(:)
    REAL :: sd, m
    m = SUM(col)/SIZE(col)
    sd = SQRT(SUM((col-m)**2)/REAL(SIZE(col)))
  END FUNCTION stddev_col

   ! Calculo de outliers (valores con z-score >= 3 o <= -3)
  FUNCTION outliers_col(col) RESULT(count)
    REAL, INTENT(IN) :: col(:)
    INTEGER :: count, i
    REAL :: m, sd, z
    count = 0
    m = mean_col(col)
    sd = stddev_col(col)
    DO i = 1, SIZE(col)
       z = (col(i) - m)/sd
       IF (ABS(z) >= 3.0) count = count + 1
    END DO
  END FUNCTION outliers_col

  ! Ordenar columna (metodo burbuja)
  SUBROUTINE sort(a)
    REAL, INTENT(INOUT) :: a(:)
    INTEGER :: i, j
    REAL :: temp
    DO i = 1, SIZE(a)-1
       DO j = i+1, SIZE(a)
          IF (a(i) > a(j)) THEN
             temp = a(i)
             a(i) = a(j)
             a(j) = temp
          END IF
       END DO
    END DO
  END SUBROUTINE sort

END MODULE compute_stats


! Modulo de lectura y escritora de CSV
MODULE io_csv
  IMPLICIT NONE
CONTAINS

   ! Leer CSV
  SUBROUTINE read_csv(filename, data, nrows, ncols)
    CHARACTER(len=*), INTENT(IN) :: filename
    REAL, ALLOCATABLE, INTENT(OUT) :: data(:,:)
    INTEGER, INTENT(OUT) :: nrows, ncols

    CHARACTER(len=1000) :: line
    INTEGER :: i, j, ios, unit, log_unit
    CHARACTER(len=20), DIMENSION(:), ALLOCATABLE :: tokens

    ! Abrir archivo de log
    OPEN(NEWUNIT=log_unit, FILE="../out/errors.log", STATUS="UNKNOWN", ACTION="WRITE", IOSTAT=ios)

    ! Primero contar filas y columnas
    nrows = 0
    ncols = 0
    OPEN(NEWUNIT=unit, FILE=filename, STATUS='OLD', ACTION='READ')
    DO
       READ(unit,'(A)', IOSTAT=ios) line
       IF (ios /= 0) EXIT
       nrows = nrows + 1
       IF (nrows == 1) THEN
          CALL split_line(line, tokens, ncols)
       END IF
    END DO
    REWIND(unit)

    ! Reservar matriz
    ALLOCATE(data(nrows, ncols))

    ! Leer valores de cada fila
    i = 0
    DO
       READ(unit,'(A)', IOSTAT=ios) line
       IF (ios /= 0) EXIT
       i = i + 1
       CALL split_line(line, tokens, ncols)
       DO j = 1, ncols
          READ(tokens(j), *, IOSTAT=ios) data(i,j)
          IF (ios /= 0) THEN
            data(i,j) = -9999 ! valor inválido por defecto
            WRITE(log_unit,'(A,I0,A,I0,A,A)') "Error en fila=", i, ", columna=", j, ", valor=", TRIM(tokens(j)) ! guardar en errors.log
          END IF
         END DO
    END DO
    CLOSE(unit)
    CLOSE(log_unit)
  END SUBROUTINE read_csv

   ! Escribir CSV
  SUBROUTINE write_stats(filename, means, medians, stddevs, outliers, ncols)
    CHARACTER(len=*), INTENT(IN) :: filename
    REAL, INTENT(IN) :: means(:), medians(:), stddevs(:)
    INTEGER, INTENT(IN) :: outliers(:)
    INTEGER, INTENT(IN) :: ncols
    INTEGER :: j, unit

    OPEN(NEWUNIT=unit, FILE=filename, STATUS='REPLACE', ACTION='WRITE')
    WRITE(unit,'(A)') "variable;mean;median;stddev;outliers"
    DO j = 1, ncols
       WRITE(unit,'(I0, ";", F10.2, ";", F10.2, ";", F10.2, ";", I0)') j, means(j), medians(j), stddevs(j), outliers(j)
    END DO
    CLOSE(unit)
  END SUBROUTINE write_stats


  SUBROUTINE split_line(line, tokens, ncols)
    CHARACTER(len=*), INTENT(IN) :: line
    CHARACTER(len=20), DIMENSION(:), ALLOCATABLE, INTENT(OUT) :: tokens
    INTEGER, INTENT(OUT) :: ncols

    CHARACTER(len=1000) :: temp
    INTEGER :: i, p1, p2

    ! Contar columnas
    temp = TRIM(line)
    ncols = 1
    DO i=1,LEN_TRIM(temp)
       IF (temp(i:i) == ';') ncols = ncols + 1
    END DO

    ALLOCATE(tokens(ncols))
    p1 = 1
    DO i=1,ncols
       p2 = INDEX(temp(p1:), ';')
       IF (p2 == 0) THEN
          tokens(i) = TRIM(ADJUSTL(temp(p1:)))
       ELSE
          tokens(i) = TRIM(ADJUSTL(temp(p1:p1+p2-2)))
          p1 = p1 + p2
       END IF
    END DO
  END SUBROUTINE split_line

END MODULE io_csv



PROGRAM main
  USE io_csv
  USE compute_stats
  IMPLICIT NONE

  REAL, ALLOCATABLE :: data(:,:)
  REAL, ALLOCATABLE :: means(:), medians(:), stddevs(:)
  INTEGER, ALLOCATABLE :: outliers(:)
  INTEGER :: nrows, ncols, j

  CALL read_csv("input.csv", data, nrows, ncols)

  ALLOCATE(means(ncols), medians(ncols), stddevs(ncols), outliers(ncols))

  DO j = 1, ncols
     means(j) = mean_col(data(:,j))
     medians(j) = median_col(data(:,j))
     stddevs(j) = stddev_col(data(:,j))
     outliers(j) = outliers_col(data(:,j))
  END DO

  CALL write_stats("../out/stats.csv", means, medians, stddevs, outliers, ncols)

  PRINT *, "Archivo stats.csv generado con exito."

END PROGRAM main